package com.synch4j.datapicker.impl;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.Synch2Context;
import com.synch4j.callback.CallbackManager;
import com.synch4j.callback.ExportDbValueChangeProcessor;
import com.synch4j.common.dao.IJdbcDao;
import com.synch4j.datapicker.IDataPicker;
import com.synch4j.datapicker.dao.IDataPickerMapper;
import com.synch4j.exception.CallbackClassNotExistIoCException;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.DataPickerException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.log.service.ISynchLogService;
import com.synch4j.po.SynchExpLogPO;
import com.synch4j.po.SynchPO;
import com.synch4j.secret.SecretManager;
import com.synch4j.sqlgenerator.exp.IExportSqlGenerator;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.synchenum.LogType;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;
import com.synch4j.zip.CZipOutputStream;
import com.synch4j.zip.ZipEntry;

/**
 * @author XieGuanNan
 * @date 2015-8-19-上午9:26:41
 * 数据提取器的数据库实现
 */
@Component
public class DbDataPicker implements IDataPicker{

	Logger logger = Logger.getLogger(DbDataPicker.class);
	
	@Resource
	private ISynchLogService synchLogService;

	@Resource
	private IDataPickerMapper dataPickerMapper;
	
	/**
	 * 默认注入通用sql生成器
	 */
	private IExportSqlGenerator expSqlGenerator;
	
	@Resource(name = "jdbcDao")
	private IJdbcDao jdbcDao;
	
	@Override
	public Map<String, String> getData(ExportMode mode,Synch2Context context, SynchPO synchPO,
			CZipOutputStream zos) throws DataPickerException, CallbackException, CallbackClassNotExistIoCException, ErrorConfigureException {
		logger.info(synchPO.getPhysDBName() + "开始导出数据");
		// 得到当前表所有列tableColumns中的Key为COLUMN_NAME和DATA_TYPE
		expSqlGenerator = getSqlGeneratorByProperties();
		
		List<Map<String, String>> tableColumns = dataPickerMapper.getTableColumns(synchPO.getPhysDBName());
		if (tableColumns == null || tableColumns.size() == 0) {
			logger.error("表[" + synchPO.getPhysDBName() + "]没有找到对应的列，可能数据库中表被删除，请检查定义！");
			throw new DataPickerException("表[" + synchPO.getPhysDBName()
					+ "]没有找到对应的列，可能数据库中表被删除，请检查定义！");
		}
		
		// 根据定义和数据信息组织导出表相关信息
		Map<String, String> infoMap = expSqlGenerator.getExportSqlInfo(mode, synchPO, tableColumns, context);
		try{
			Map<String, String> colsValueMap = null;
			//开发模式跳过导出数据
			if(!SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY).equalsIgnoreCase("true")){
				// 得到当前表的导出值
				logger.info(synchPO.getPhysDBName() + " 开始抽取数据......");
				colsValueMap = queryExpData(mode,synchPO, zos, context, infoMap.get("querySQL"), infoMap.get("colsName"), tableColumns);
				logger.info(synchPO.getPhysDBName() + " 导出记录数：" + colsValueMap.get("outRow") + " 导出最后的一条数据的dbversion时间：" + colsValueMap.get("lastDate"));
				logger.info(synchPO.getPhysDBName() + "数据导出结束");logger.info(synchPO.getPhysDBName() + " 导出记录数：" + colsValueMap.get("outRow") + " 导出最后的一条数据的dbversion时间：" + colsValueMap.get("lastDate"));
				logger.info(synchPO.getPhysDBName() + "数据导出结束");
			}
			return colsValueMap;
		}catch(SQLException e){
			e.printStackTrace();
			logger.error("SQLEXCEPTION:"+e.getMessage(),e);
			throw new DataPickerException("SQLEXCEPTION:"+e.getMessage());
		}
	}
	
	
	private Map<String, String> queryExpData(ExportMode mode,SynchPO synchPO, CZipOutputStream zos, Synch2Context context,
			String querySQL, String colsName, List<Map<String, String>> tableColumns) throws DataPickerException, SQLException {
		String logId = context.getLogId();
		//得到数据库连接
		Statement stmt = getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(querySQL);
		
		
		StringBuffer colsValueBuff = new StringBuffer("");
		StringBuffer readBuffer = new StringBuffer("");
		//记录附件信息
		StringBuffer attachBuffer = new StringBuffer("");
		//记录导出条数
		int datas = 0;
		String lastExpDate = "";
		//数据导入更新条件
		StringBuffer whereBuffer = null;
		//记录主键个数
		int talePks = 0;
		try {
			while (rs.next()) {
				String colsValueStr = "";
				whereBuffer = new StringBuffer();
				talePks = 0;
				boolean isBreak = false;
				for (int j = 0; j < tableColumns.size(); j++) {
					Map<String, String> map = tableColumns.get(j);
					String colName = map.get("COLUMN_NAME");
					
					//判断是否有和配置文件中导出的列相同的列名；
					//SqlGenerator中也有此方法，虽然在结果集中已经过滤了这些列（没有这些数据），但是在tableColumns中还是存在
					//该列的信息，如果不把他再次排除，会导致导出时，列和值得数量对应不上
					if(decideIgnoreColNameFromProperties(colName)){
						continue;
					}
					String dataType = (String) map.get("DATA_TYPE");

					if (!dataType.startsWith("BLOB")
							&& !dataType.startsWith("CLOB")) // 非BLOB、CLOB字段
					{
						//2015.4.13谢冠男加
						String colDBValue =  rs.getString(colName) == null ? ""
								: rs.getString(colName);
						//回调接口，如果需要改变值
						colDBValue = callbackDbValueChange(mode, synchPO,colDBValue,colName);
						
						colDBValue = colDBValue.replaceAll("\r|\n", "");
					
						colsValueStr = colsValueStr + colDBValue
								+ SynchConstants.DATA_SPLIT;
						
						//记录主键信息 大数据导入时需要根据主键更新数据库
						if (synchPO.getPkList().contains(colName)) {
							talePks ++;
							whereBuffer.append(" " + colName
									+ "="
									+ SynchToolUtil.getOracleValue(dataType, colDBValue).replaceAll("'", "\\$") + " AND");
						}
						
						//2016.3.15加入了附件的支持，当前只支持一张cdt_T_tasktypefile表的导出
						//未来如果需要导出业务表的附件，此处就需要进行修改，判断是否为附件表，然后对比是否为附件列，在把
						//fileid-docid保存到表中
						if (synchPO.getTableType().equals(
								SynchConstants.TABLE_TYPE_ATTACH)){
							if(colName.equals(SynchConstants.ATTACHMENT_COL_ATTACHID)) {
								//2016.3.17附件表中，status为2的记录的文件guid不记录，但是这行数据还会导出，比如上级删除了表中的
								//文件id级联关系，下级也要同步更新为2，但我告诉XCH或OA需要上传的附件时，不会告诉他们这个附件，因为有可能附件已经在服务器被删除了
								//走XCH时候，会报错
								String status = rs.getString("STATUS") == null ? null : rs.getString("STATUS");
								if(!StringUtils.isEmpty(colDBValue) && (status != null && status.equals("1"))){
									if(logger.isInfoEnabled()){
										logger.info("发现需要下发的附件，ID为："+colDBValue);
									}
									attachBuffer.append(colDBValue + ",");
								}
							}
						}
						
					} else {
						if(whereBuffer.toString().endsWith("AND")){
							whereBuffer = new StringBuffer(whereBuffer.substring(0,
									whereBuffer.length() - 3));
						}
						try{
							zos.putNextEntry(new ZipEntry(SynchConstants.SYNCH_ZIP_BIGDATA
									+ "/" + synchPO.getPhysDBName() + "/" + dataType
									+ "/" + colName + "/" + whereBuffer.toString()));
						}catch(Exception e){
							//2016.1.7谢冠男加：
							//只有一种可能性出现异常，即这条CLOB，BLOB的数据之前导出过一次，这次出现了重复；
							//出现此情况时，直接继续导出；有重复跳出内层循环，当前条数据的所有其余数据全部舍掉
							//e.printStackTrace();
							logger.error("当前表:"+synchPO.getPhysDBName()+",列："+colName+",条件："+whereBuffer.toString()+"可能出现了重复导出数据，程序将跳过此条数据的第二次导出！",e);
							isBreak = true;
							break;
						}

						if (dataType.startsWith("CLOB")) {
							Object obj = rs.getClob(colName);
							writeClob(obj, zos);
							colsValueStr = colsValueStr + "EMPTY_CLOB()"
									+ SynchConstants.DATA_SPLIT;

						} else if (dataType.startsWith("BLOB")) {
							Object obj = rs.getBlob(colName);
							writeBlob(obj, zos);
							colsValueStr = colsValueStr + "EMPTY_BLOB()"
									+ SynchConstants.DATA_SPLIT;
						}
						continue;
					}
				}
				if(!isBreak){
					//如果导出blob,clob时发现有重复导出数据，直接break，并且不记录导出语句
					colsValueBuff.append(colsValueStr.substring(0,
							colsValueStr.length() - SynchConstants.DATA_SPLIT.length())
							+ "\r\n");
					//经过排序 最后一条数据为最新记录
					if (!StringUtils.isEmpty(synchPO.getSynchRecogCol())) {
						lastExpDate = (String) rs.getString(synchPO.getSynchRecogCol());
					}
					
					datas++;
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw new DataPickerException(e.getMessage());
		} finally {
			rs.close();
			stmt.close();
		}
		
		//如果数据时间相同 把后续数据时间更新
		if (!StringUtils.isEmpty(synchPO.getSynchRecogCol()) && Integer.valueOf(synchPO.getMaxRow()) != null && datas > 0 && datas == synchPO.getMaxRow()) {
			updateDataDbversion(synchPO, lastExpDate);
		}
		
		if (attachBuffer.length() > 0) {
			attachBuffer = new StringBuffer(attachBuffer.toString().substring(
					0, attachBuffer.toString().length() - 1));
		}
		
		readBuffer.append(colsName).append(colsValueBuff.toString());
		saveExportLog(logId, synchPO.getPhysDBName(), lastExpDate, datas);
		
		String outInfo = readBuffer.toString();
		
		try {
			outInfo = SecretManager.encode(mode,
					outInfo.getBytes("GBK"), SynchConstants.SECURE_KEY.getBytes());
		} catch (UnsupportedEncodingException e) {
			logger.error("不支持的编码方式GBK！请检查！是否有该编码存在",e);
			e.printStackTrace();
		} 

		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("outRow", datas+"");
		resultMap.put("outInfo", outInfo);
		resultMap.put("lastDate", lastExpDate);
		resultMap.put("attach", attachBuffer.toString());
		
		return resultMap;
		
	}

	/**
	 * 判断传来的列名，是否为在配置文件中配置的忽略列,该匹配会匹配所有表格！非某一张单表
	 * @param colName
	 * @return false为不匹配，true为匹配，将忽略该列
	 */
	private boolean decideIgnoreColNameFromProperties(String colName){
		boolean flag = false;
		String colNames = SynchToolUtil.getValueFromProperties(SynchConstants.EXPORT_COMMON_SQLGEN_IGNORE_COL_KEY);
		if(colNames != null){
			String[] colNameArr = colNames.split(",");
			if(colNameArr == null){
				return false;
			}
			for(String matchCol : colNameArr){
				if(matchCol.equalsIgnoreCase(colName)){
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 写入CLOB
	 * @param obj
	 * @param zos
	 * @throws Exception
	 */
	private void writeClob(Object obj, CZipOutputStream zos) throws Exception {
		try {
			Method method = null;
			if (obj == null) {
				return;
			}
			method = obj.getClass().getMethod(
					"getCharacterStream",
					new Class[] {});
			Reader inStream = (Reader) method.invoke(
					obj, new Object[] {});
			char[] c = new char[1024];
			int len = 0;
			while ((len = inStream.read(c)) != -1) {
				String str = new String(c, 0, len);
				zos.write(str.getBytes("GBK"));
			}
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出CLOB时发生异常",e);
			throw new Exception("导出CLOB时发生异常");
		}
	}
	
	/**
	 * 写入BLOB
	 * @param obj
	 * @param zos
	 * @throws Exception
	 */
	private void writeBlob(Object obj, CZipOutputStream zos) throws Exception {
		try {
			Method method = null;
			if (obj == null) {
				return;
			}
			method = obj.getClass().getMethod(
					"getBinaryStream", new Class[] {});
			InputStream inStream = (InputStream) method
					.invoke(obj, new Object[] {});
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = inStream.read(b)) != -1) {
				zos.write(b, 0, len);
			}
			inStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出BLOB时发生异常",e);
			throw new Exception("导出BLOB时发生异常");
		}
	}
	
	private void saveExportLog(String logId, String expPhysDBName, String lastDate, int expDatas) {
		SynchExpLogPO explog = new SynchExpLogPO();
		explog.setLogId(logId);
		explog.setExpFileName(expPhysDBName.concat(".txt"));
		explog.setExpPhysDBName(expPhysDBName);
		if (lastDate.equals("")) {
			explog.setLastDate("SYSTIMESTAMP");
		} else {
			explog.setLastDate("TO_TIMESTAMP('" + lastDate
					+ "'," + SynchConstants.SYSTIMESTAMP_FORMAT + ")");
		}
		explog.setExpDatas(expDatas+"");
		explog.setDistrictId("");
		synchLogService.saveLog(explog,LogType.EXPLOG);
	}
	/**
	 * 更新后续相同的DBVERSION
	 * @param synchPO
	 * @param newLastExpDate
	 */
	private void updateDataDbversion(SynchPO synchPO,
			String newLastExpDate) {
		
		// 得到当前表的最新导出时间
		String oldLastExpDate = dataPickerMapper.getLastExpDate(synchPO.getPhysDBName());
		
		StringBuffer updateSQL = new StringBuffer();
		String updatePK = "";
		String PK = "";
		
		updateSQL.append("UPDATE ").append(synchPO.getPhysDBName());
		updateSQL.append(" SET ");
		for (String str : synchPO.getPkList()) {
			updatePK += str + " = "+ str + ",";
			PK += str + ",";
		}
		updatePK = updatePK.substring(0, updatePK.length() - 1);
		PK = PK.substring(0, PK.length() - 1);
		
		updateSQL.append(updatePK);
		updateSQL.append(" WHERE (").append(PK).append(") IN ");
		updateSQL.append("( SELECT ").append(PK).append(" FROM ");
		updateSQL.append("( SELECT ").append(" * ").append(" FROM ");
		updateSQL.append("( SELECT ROWNUM RN, TB.* FROM (SELECT ").append(PK).append(", "+ synchPO.getSynchRecogCol()).append(" FROM ");
		updateSQL.append(synchPO.getPhysDBName());
	
		if (synchPO.getSynchCondition() != null && synchPO.getSynchCondition().length() > 0) {
			updateSQL.append(synchPO.getSynchCondition());
		} else {
			updateSQL.append(" WHERE 1 = 1 ");
		}
		
		if (oldLastExpDate != null && oldLastExpDate.length() > 0) {
			updateSQL.append(" AND ").append(synchPO.getSynchRecogCol()
					+ " > TO_TIMESTAMP('" + oldLastExpDate + "', "
					+ SynchConstants.SYSTIMESTAMP_FORMAT + ") ");
		}
		
		updateSQL.append(" ORDER BY ").append(synchPO.getSynchRecogCol()).append(",").append(PK).append(" ) TB) ");
		
		updateSQL.append(" WHERE RN > ").append(synchPO.getMaxRow()).append(" AND ");
		updateSQL.append(synchPO.getSynchRecogCol()).append(" = ").append("TO_TIMESTAMP('" + newLastExpDate + "', "
				+ SynchConstants.SYSTIMESTAMP_FORMAT + ") ) )");
		//改updateSQL
		dataPickerMapper.updateExportData(updateSQL.toString());
		
	}
	
	private IExportSqlGenerator getSqlGeneratorByProperties() throws CallbackClassNotExistIoCException, ErrorConfigureException{
		IExportSqlGenerator sqlGenerator = (IExportSqlGenerator)CallbackManager.getBeansByPropertiesKey(SynchConstants.EXPORT_DEFAULT_SQLGENERATOR_INC_KEY, true);
		return sqlGenerator;
		
	}
	/**
	 * 回调开发者实现的接口，允许在接口中改变提取出来的值，但是请注意dbValue只返回一个值，如果你有多个
	 * 该接口的实现，而且又对同一张表同一列进行值的修改，那么依次调用后，只会保留最后一个值，最好只有一个回调，但是此处没有限制死
	 * @param mode
	 * @param synchPO
	 * @param dbValue
	 * @param colName
	 * @return
	 * @throws CallbackException
	 */
	private String callbackDbValueChange(ExportMode mode,SynchPO synchPO, String dbValue,String colName) throws CallbackException{
		List callbackList = (List)CallbackManager.getBeansByInterfaceName("ExportDbValueChangeProcessor", false);
		for(Object obj : callbackList){
			ExportDbValueChangeProcessor call = (ExportDbValueChangeProcessor)obj;
			dbValue = call.changeDbValue(mode,synchPO, dbValue, colName);
		}
		return dbValue;
	}
	
	private Connection getConnection() {
		Connection conn = jdbcDao.getCurrentConnection();
		return conn;
	}
}
