package com.synch4j.dataimporter.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.Synch2Context;
import com.synch4j.callback.CallbackManager;
import com.synch4j.callback.ImportDbProcessor;
import com.synch4j.common.dao.IJdbcDao;
import com.synch4j.dataimporter.IDataImporter;
import com.synch4j.dataimporter.dao.IDataImporterMapper;
import com.synch4j.exception.CallbackClassNotExistIoCException;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.DataImporterException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.log.service.ISynchLogService;
import com.synch4j.po.ImportColInfoVO;
import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchPO;
import com.synch4j.sqlgenerator.imp.IImportSqlGenerator;
import com.synch4j.synchenum.ImportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;
/**
 * @author XieGuanNan
 * @date 2015-8-30-下午6:57:21
 * 数据导入器的数据库版本实现，职责：将解析好的数据插入到数据库中
 */
@Component
public class DbDataImporter implements IDataImporter{

	Logger logger = Logger.getLogger(DbDataImporter.class);
	
	@Resource
	private ISynchLogService synchLogService;
	
	@Resource
	private IDataImporterMapper dataImporterMapper;
	
	@Resource(name = "jdbcDao")
	private IJdbcDao jdbcDao;
	
	@Override
	public void importData(ImportMode mode, List<SynchPO> synchList, Synch2Context context) throws IOException, CallbackException {
		switch(mode){
		case SINGLE_THREAD:
			importDataSingleThread(mode,synchList,context);
			break;
		case MULTI_THREAD:
			break;
		}
	}
	
	private void importDataSingleThread(ImportMode mode, List<SynchPO> synchList,Synch2Context context) throws IOException, CallbackException{
		String logId = context.getLogId();
		BufferedReader bufferReader = null;
		ByteArrayInputStream dataStream = null;
		for(SynchPO synchPO : synchList){
			String physDBName = synchPO.getPhysDBName();
			logger.info("开始同步 " + physDBName);
			//2016.1.8因为支持了同一张表的多次导出，所以导入时，可能存在多个中间表数据
			//解密数据表中取到该表的数据，key为:physDBName、synchdata
			List<Map<String,String>> dataMapList = dataImporterMapper.getTableData(logId, physDBName);
			for(Map<String,String> dataMap : dataMapList){
				String synchData = dataMap.get("SYNCHDATA");
				try{
					dataStream = new ByteArrayInputStream(synchData.getBytes("GBK"));
					bufferReader = new BufferedReader(new InputStreamReader(dataStream,"GBK"));
					tableImportData(mode,synchPO,bufferReader,logId,context);
				}finally{
					bufferReader.close();
					dataStream.close();
				}
			}
			
			logger.info("同步完成 " + physDBName);
		}
	}

	
	private void tableImportData(ImportMode mode, SynchPO synchPO,BufferedReader bufferReader, 
			String logId,Synch2Context context) throws IOException, CallbackException{
		String physDBName = synchPO.getPhysDBName();
		String dataSrc = null;
		int insRow = 0;
		int failRow = 0;
		boolean isFirstRow = true;
		
		List<Map<String, String>> importColNameList = null;
		List<Integer> noDataIndexList = null;
		ImportColInfoVO vo = null;
		
		long startDate = System.currentTimeMillis();
		String remark = "";
		Map<String, String> tableImpInfo = new HashMap<String, String>();
		//回调接口：
		callbackBeforeBusiData(mode, synchPO, context);
		//先插业务数据
		while ((dataSrc = bufferReader.readLine()) != null) {
			//第一行的话，因为是列信息，解析列信息
			if (isFirstRow) {
				isFirstRow = false;
				vo = readFileExpCols(dataSrc, physDBName);
				importColNameList = vo.getImportColNameList();
				//2015.8.6谢冠男加，同步时如果有不同的列，则取数据时也调过该列，只插入有的列
				noDataIndexList = vo.getNoDataIndex();
				continue;
			}

			String[] colValues = SynchToolUtil.split(dataSrc, SynchConstants.DATA_SPLIT);
			List<String> colValueList = new ArrayList<String>(colValues.length);
			//以下为处理导出表的列信息和导入方的表列对不上的情况，比如导出时5列，导入方只有3列相同列名的，那么只会导入3列的数据
			if(noDataIndexList != null && noDataIndexList.size() > 0){
				for(int index = 0;index < colValues.length;index++){
					boolean flag = false;
					String colval = colValues[index];
					for(int noDataIndex : noDataIndexList){
						if(index == noDataIndex){
							flag = true;
							break;
						}
					}
					if(!flag){
						colValueList.add(colval);
					}
				}
				colValues = new String[colValueList.size()];
				for (int i = 0; i < colValues.length; i++) {
					colValues[i] = colValueList.get(i);
				}
			}
			try {
					if (importColNameList.size() != colValues.length) {
						logger.error("列个数" + importColNameList.size() + "值个数"
								+ colValues.length);
						throw new DataImporterException(synchPO + "表导入字段与取值个数不匹配！");
					}
					//执行导入sql
					executeImportData(mode,synchPO, importColNameList,
							colValues, logId);
					insRow++;
				} catch (Exception e) {
					e.printStackTrace();
					failRow++;
					remark += e.getMessage();
				}
		}
		//回调
		callbackAfterBusiData(mode,synchPO,context);
		
		//插入后统一全部讲clob，blob更新进去
		//当该表的所有业务数据全部插入完成后，插入blob,clob的数据到表中
		//如果是开发模式，不会执行更新大文本数据：
		if(!(SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY) != null && 
				SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY).equalsIgnoreCase("true"))){
			Map<String, String> bigDataUpdateInfoMap = updateBigDataCol(synchPO,
					logId, tableImpInfo);

			//2016.1.18下面这段代码现在2.0不会起作用了，因为1.0同步机制是在程序中通过主键判断是插入还是更新
			//现在2.0机制同步数据已经挪到存储过程中去了。
			if (bigDataUpdateInfoMap.containsKey("I")
					&& Integer.parseInt(bigDataUpdateInfoMap.get("I")) > 0) {
				insRow -= Integer.parseInt(bigDataUpdateInfoMap.get("I"));
				failRow += Integer.parseInt(bigDataUpdateInfoMap.get("I"));

				if (insRow < 0)
					insRow = 0;
			}

			remark += bigDataUpdateInfoMap.get("ERROR");

			// 记录日志
			SynchImpLogPO logPO = new SynchImpLogPO();
			logPO.setExpPhysDBName(physDBName);
			logPO.setLogId(logId);
			logPO.setImpUsedDate(String.valueOf((System.currentTimeMillis() - startDate) / 1000));
			logPO.setInsertDatas(insRow);
			logPO.setFailDatas(failRow);
			synchLogService.updateImportLog(logPO);

			if (!(StringUtils.isEmpty(remark) || remark.equals("null"))) {
				logger.error("logId:"+logId+",物理表："+physDBName+",错误信息："+remark);
				// 记录错误日志
				SynchImpLogPO errlogPO = new SynchImpLogPO();
				errlogPO.setLogId(logId);
				remark = remark.substring(0, 400);
				errlogPO.setRemark(remark);
				errlogPO.setExpPhysDBName(physDBName);
				synchLogService.updateImportLog(errlogPO);
			}
		}
		//该表全部导出完成后，回调：
		callbackAfterAllData(mode, synchPO, context);
	}
	/**
	 * 得到导出时记录的列
	 * 
	 * @param data
	 * @param physDBName
	 * @return
	 * @throws Exception
	 */
	private ImportColInfoVO readFileExpCols(String data,
			String physDBName){
		// 要保证顺序
		String[] cols = SynchToolUtil.split(data, SynchConstants.DATA_SPLIT);
		List<Map<String, String>> tableColumns = dataImporterMapper.getTableColumns(physDBName);

		Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
		for (Map<String, String> map : tableColumns) {
			String colName = map.get("COLUMN_NAME");
			tempMap.put(colName, map);
		}

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		List<Integer> noDataIndex = new ArrayList<Integer>();
		for (int i = 0; i < cols.length; i++) {
			String colName = cols[i];
			if (!tempMap.containsKey(colName)) {
				//2015.8.6谢冠男改，如果有不一样的列，只插入有列的值
				logger.info(physDBName + "数据库缺少列" + colName + "!导入时将跳过该列的值");
				Integer index = new Integer(i);
				noDataIndex.add(index);
			}else{
				resultList.add(tempMap.get(colName));
			}
		}
		
		ImportColInfoVO vo = new ImportColInfoVO();
		vo.setImportColNameList(resultList);
		vo.setNoDataIndex(noDataIndex);
		
		return vo;
	}
	
	
	/**
	 * 
	 * @param mode TODO
	 * @param synchPO
	 * @param importColNameList
	 * @param colValues
	 * @param logId
	 * @return
	 * @throws ErrorConfigureException 
	 * @throws CallbackClassNotExistIoCException 
	 * @throws CallbackException 
	 * @throws SQLException 
	 * @throws Exception
	 * 执行数据导入
	 */
	private void executeImportData(ImportMode mode,
			SynchPO synchPO, List<Map<String, String>> importColNameList,
			String[] colValues, String logId) throws CallbackClassNotExistIoCException, ErrorConfigureException, CallbackException, SQLException{
		
		IImportSqlGenerator sqlGenerator = (IImportSqlGenerator)CallbackManager.getBeansByPropertiesKey(SynchConstants.IMPORT_SINGLE_SQLGENERATOR_KEY, true);
		String sql = sqlGenerator.getImportSQL(mode, synchPO, importColNameList, colValues, logId);
		
		//如果是开发模式，不会执行导入SQL：
		if(!(SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY) != null && 
				SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY).equalsIgnoreCase("true"))){
			dataImporterMapper.insertExportData(sql);
		}
		
	}
	
	
	private Map<String, String> updateBigDataCol(SynchPO synchPO,
			String logId, Map<String, String> tableImpInfo) {
		//得到需要更新的大数据列
		//因为表名可能已经被修改了
		//返回的key:PHYSDBNAME, COLUNAME, DATA_TYPE, CONDITION, GUID
		List<Map<String, String>> bigDataCols = dataImporterMapper.getNeedUpdateBlobOrClob(logId, 
				synchPO.getPhysDBName(), synchPO.getOriginalPhysDBName());
		
		Map<String, String> updateInfoMap = new HashMap<String, String>();
		if (bigDataCols == null || bigDataCols.size() == 0) {
			return updateInfoMap;
		}

		String coluName = "";
		String dataType = "";
		String condition = "";
		String guid = "";
		Map<String, String> bigDataCol = null;

		String errorInfo = "";
		String tempErrorInfo = "";
		int insertFails = 0;
		int updateFails = 0;

		for (int i = 0; i < bigDataCols.size(); i++) {
			bigDataCol = bigDataCols.get(i);
			coluName = bigDataCol.get("COLUNAME");
			dataType = bigDataCol.get("DATA_TYPE");
			condition = bigDataCol.get("CONDITION");
			guid = bigDataCol.get("GUID");
			try {
				if (dataType.equals("BLOB")) {
					updateBlobCol(synchPO, coluName, condition, logId, guid);
				} else if (dataType.equals("CLOB")) {
					updateClobCol(synchPO, coluName, condition, logId, guid);
				}

			} catch (Exception e) {
				e.printStackTrace();
				tempErrorInfo = "大数据更新失败 " + synchPO.getPhysDBName() + " " + dataType + " "
						+ coluName + " " + condition + " 更新错误  ";
				errorInfo += tempErrorInfo;
				logger.error(tempErrorInfo,e);
				insertFails++;

			}
		}
		updateInfoMap.put("I", insertFails + "");
		updateInfoMap.put("ERROR", errorInfo + "");

		return updateInfoMap;
	}
	
	/**
	 * 更新BLOB
	 * @param coluName
	 * @param condition
	 * @param physDBName
	 * @param province
	 *            TODO
	 * 
	 * @throws Exception
	 */
	private void updateBlobCol(SynchPO synchPO, String coluName,
			String condition, String logId, String guid) throws Exception {
		//得到数据库连接
		Connection conn  = jdbcDao.getCurrentConnection();
		//conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		
		//如果原始导入表不为空，说明回调时候，表名被修改了，把数据插到原表去，而不是修改名字后的表，
		//这样做，是因为修改名字的表会在导入blob,clob之前，调用存储过程，将业务数据提前插回原表去，完成后再将clob和blob直接插到原表
		String physDBName = synchPO.getPhysDBName();
				
		String busiDataSQL = "SELECT " + coluName + " FROM " + physDBName + " WHERE " + condition + " FOR UPDATE";
		ResultSet busiData = stmt.executeQuery(busiDataSQL);
		ResultSet impData = null;
		try {
			if (busiData.next()) {
				Method blobMethod = null;
				Object blob = busiData.getBlob(coluName);
				blobMethod = blob.getClass()
						.getMethod("getBinaryOutputStream",
								new Class[] {});
				OutputStream outStream = (OutputStream) blobMethod
						.invoke(blob, new Object[] {});
			
				String impDataSQL = "SELECT BDATA FROM SYNCH_T_BLOBCLOB WHERE GUID = '" + guid + "'";
			
				impData = stmt.executeQuery(impDataSQL);
				if (impData.next()) {
					Method method = null;
					Object impObj = impData.getBlob("BDATA");
					method = impObj.getClass().getMethod(
							"getBinaryStream", new Class[] {});
					InputStream inStream = (InputStream) method
							.invoke(impObj, new Object[] {});
					byte[] b = new byte[1024];
					int len = 0;
					while ((len = inStream.read(b)) != -1) {
						outStream.write(b, 0, len);
					}
					inStream.close();
				}
				outStream.close();
				
			}
			//conn.commit();
			//conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入BLOB列出错！",e);
			throw e;
		} finally {
			if (!busiData.isClosed()) {
				busiData.close();
			}
			if (impData != null && !impData.isClosed()) {
				impData.close();
			}
			if (!stmt.isClosed()) {
				stmt.close();
			}
		}
	}

	/**
	 * 更新CLOB
	 * @param coluName
	 * @param condition
	 * @param physDBName
	 * @param province
	 *            TODO
	 * 
	 * @throws Exception
	 */
	private void updateClobCol(SynchPO synchPO, String coluName,
			String condition, String logId, String guid) throws Exception {
		//得到数据库连接
		Connection conn = jdbcDao.getCurrentConnection();
		Statement stmt = conn.createStatement();
		
		String impDataSQL = "SELECT CDATA FROM P#SYNCH_T_BLOBCLOB WHERE GUID = '" + guid + "'";
		
		//如果原始导入表不为空，说明回调时候，表名被修改了，把数据插到原表去，而不是修改名字后的表，
		//这样做，是因为修改名字的表会在导入blob,clob之前，调用存储过程，将业务数据提前插回原表去，完成后再将clob和blob直接插到原表
		//String physDBName = synchPO.getOriginalPhysDBName() == null ? synchPO.getPhysDBName() : synchPO.getOriginalPhysDBName();
		
		//15.9.8日，这里被坑了，FORMULADEF表的触发器无法完成这种机制，CLOB需要直插临时表，不再“先插临时表业务数据，后插原表大文本了”，全部直插临时表；
		//同样同步数据的存储过程调用顺序也要改变为更新完成大文本数据后，调用。
		String physDBName = synchPO.getPhysDBName();
		String busiDataSQL = "SELECT " + coluName + " FROM " + physDBName + " WHERE " + condition + " FOR UPDATE";

		ResultSet busiData = stmt.executeQuery(busiDataSQL);
		ResultSet impData = null;
		try {
			if (busiData.next()) {
				Method method = null;
				Object clob = busiData.getClob(coluName);
				method = clob.getClass().getMethod(
						"getCharacterOutputStream",
						new Class[] {});
				Writer outStream = (Writer) method.invoke(
						clob, new Object[] {});
				impData = stmt.executeQuery(impDataSQL);
				if (impData.next()) {
					Method method2 = null;
					Object impObj = impData.getClob("CDATA");

					method2 = impObj.getClass().getMethod(
							"getCharacterStream",
							new Class[] {});
					Reader inStream = (Reader) method2
							.invoke(impObj, new Object[] {});
					char[] c = new char[1024];
					int len = 0;
					//远程脚本表特殊处理，导入后，再执行代码
					//2016.3.3新增一张特殊处理的表格p#synch_t_viewcode，创建视图
					if(synchPO.getPhysDBName().equalsIgnoreCase(SynchConstants.TABLE_REMOTEPROCEDURE_NAME+SynchConstants.CHANGE_DBNAME_SUFFIX)
							|| synchPO.getPhysDBName().equalsIgnoreCase(SynchConstants.VIEWCODE_TABLE+SynchConstants.CHANGE_DBNAME_SUFFIX)){
						StringBuffer execStr = new StringBuffer();
						while ((len = inStream.read(c)) != -1) {
							outStream.write(c, 0, len);
							execStr.append(c, 0, len);
						}
						outStream.flush();
						if (execStr.toString().trim().length() > 0) {//开始执行脚本代码：
							logger.info("即将执行脚本的代码为："+execStr.toString());
							//2016.3.11有可能出现PLSQL中编译错误，直接忽略
							try{
								dataImporterMapper.callProcedure(execStr.toString());
							}catch(Exception e){
								logger.error("执行远程脚本出错，已经忽略该错误，继续执行.",e);
								e.printStackTrace();
							}
						}
					}else{
						while ((len = inStream.read(c)) != -1) {
							outStream.write(c, 0, len);
						}
					}
					inStream.close();
				}
				outStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入CLOB列出错！",e);
			throw e;
		} finally {
			if (!busiData.isClosed()) {
				busiData.close();
			}
			if (impData != null && !impData.isClosed()) {
				impData.close();
			}
			if (!stmt.isClosed()) {
				stmt.close();
			}
		}
	}
	
	private void callbackBeforeBusiData(ImportMode mode,SynchPO synchPO, Synch2Context context) throws CallbackException{
		List callbackList = (List)CallbackManager.getBeansByInterfaceName("ImportDbProcessor", false);
		for(Object obj : callbackList){
			ImportDbProcessor call = (ImportDbProcessor)obj;
			call.processBeforeBusiData(mode, synchPO, context);
		}
	}
	
	private void callbackAfterBusiData(ImportMode mode,SynchPO synchPO, Synch2Context context) throws CallbackException{
		List callbackList = (List)CallbackManager.getBeansByInterfaceName("ImportDbProcessor", false);
		for(Object obj : callbackList){
			ImportDbProcessor call = (ImportDbProcessor)obj;
			call.processAfterBusiData(mode, synchPO, context);
		}
	}
	
	private void callbackAfterAllData(ImportMode mode,SynchPO synchPO, Synch2Context context) throws CallbackException{
		List callbackList = (List)CallbackManager.getBeansByInterfaceName("ImportDbProcessor", false);
		for(Object obj : callbackList){
			ImportDbProcessor call = (ImportDbProcessor)obj;
			call.processAfterAllData(mode, synchPO, context);
		}
	}
}
