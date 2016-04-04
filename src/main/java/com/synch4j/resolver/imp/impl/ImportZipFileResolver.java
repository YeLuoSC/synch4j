package com.synch4j.resolver.imp.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.Synch2Context;
import com.synch4j.callback.CallbackManager;
import com.synch4j.callback.ImportResolveProcessor;
import com.synch4j.common.dao.IJdbcDao;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.ImportResolverException;
import com.synch4j.log.service.ISynchLogService;
import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchPO;
import com.synch4j.resolver.imp.IImportResolver;
import com.synch4j.resolver.imp.dao.IImportResolverMapper;
import com.synch4j.secret.SecretManager;
import com.synch4j.synchenum.LogType;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;
import com.synch4j.zip.CZipInputStream;
import com.synch4j.zip.ZipEntry;

@Component
public class ImportZipFileResolver implements IImportResolver{

	Logger logger = Logger.getLogger(ImportZipFileResolver.class);
	
	@Resource
	private ISynchLogService synchLogService;
	
	@Resource
	private IImportResolverMapper importResolverMapper;

	@Resource(name = "jdbcDao")
	private IJdbcDao jdbcDao;
	
	@Override
	public List<SynchPO> resolve(InputStream is, String logId, Synch2Context context) 
			throws UnsupportedEncodingException, IOException, CallbackException, SQLException, ImportResolverException{
		logger.info("开始解析ZIP文件！");
		
		Map<String,SynchPO> synchMap = null;
		
		CZipInputStream zis = new CZipInputStream(new BufferedInputStream(is), "GBK");
		ZipEntry zipEntry = null;
		String zipPrvName = "";
		String zipChildName = "";
		while ((zipEntry = zis.getNextEntry()) != null) {
			zipChildName = zipEntry.getName();
			logger.info("开始解析文件:" + zipChildName);

			int indx = zipChildName.indexOf("/");
			if (indx > 0) {
				zipPrvName = zipChildName.substring(0, indx);
				zipChildName = zipChildName.substring(indx + 1);
			}

			if(zipChildName.equalsIgnoreCase(SynchToolUtil.getFileName(SynchConstants.SYNCH_INFO_TXT))){
				BufferedReader bufferReader = new BufferedReader(
						new InputStreamReader(zis, "GBK"));
				// 解析SynchInfo文件
				synchMap = parseSynchInfoFile(bufferReader, logId, context);
				continue;
			}
			
			if (zipPrvName.equals(SynchConstants.SYNCH_ZIP_EXPLAIN)) {// 说明文件
				BufferedReader bufferReader = new BufferedReader(
						new InputStreamReader(zis, "GBK"));
				// 解析说明文件
				parseExplainFile(bufferReader, logId);
				continue;
			}

			// 解析数据
			byte[] dataBytes = null;
			try {
				byte[] buffer = new byte[5 * 1024 * 1024];
				int len;
				while ((len = zis.read(buffer)) > 0) {
					//ck说此处好像是解决特殊字符串问题的，具体他也记不清了。
					byte[] notNullByte = SynchToolUtil.removeNull(buffer, len);
					if (dataBytes == null) {
						dataBytes = notNullByte;
						continue;
					}
					dataBytes = SynchToolUtil.join(dataBytes, notNullByte, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("解zip失败！",e);
				throw new ImportResolverException("解zip失败！");
			}

			logger.info("解析文件:" + zipChildName + "结束！");
			if (zipPrvName.equals(SynchConstants.SYNCH_ZIP_BUSIDATA)) {// 业务数据
				parseBusiDataFile(dataBytes,zipChildName, logId, synchMap);
			} else if (zipPrvName.equals(SynchConstants.SYNCH_ZIP_BIGDATA)) {// 大文本数据
				parseBigDataFile(dataBytes, zipChildName, logId);
			} else if (zipPrvName.equals(SynchConstants.SYNCH_ZIP_ATTACHMENT)) {// 附件表
				parseAttachmentFile(dataBytes, zipChildName);
				logger.info("附件:" + zipChildName + "已上传到文件服务器上！");
			}
		}
		logger.info("ZIP文件解析完成！");
		
		return convertListFromMap(synchMap);
	}
	
	/**
	 * 解析SynchInfo.txt文件
	 * 
	 * @param bufferReader
	 * @param context TODO
	 * @param logID
	 * @throws IOException 
	 * @throws Exception
	 */
	private Map<String,SynchPO> parseSynchInfoFile(BufferedReader bufferReader, String logId, Synch2Context context) throws ImportResolverException, IOException {
		String dataSrc = null;
		boolean isFirstRow = true;
		Map<String,SynchPO> synchMap = new HashMap<String,SynchPO>();
		// 日志信息
		SynchImpLogPO impLog = null;
		while ((dataSrc = bufferReader.readLine()) != null) {
			dataSrc = new String(dataSrc.getBytes()).trim();
			if (isFirstRow) {// 说明信息
				try{
					String[] tmpStrArr = dataSrc.split(",");
					String exportMode = tmpStrArr[0].split(":")[1];
					String sourceProvince = tmpStrArr[1].split(":")[1];
					String sourceYear = tmpStrArr[2].split(":")[1];
					String appId = tmpStrArr[3].split(":")[1];
					String destProvince = tmpStrArr[4].split(":")[1];
					
					//context.setSourceProvince(sourceProvince);
					//这里是否需要回调？
				}catch(Exception e){
					e.printStackTrace();
					logger.error("未按照标准格式导出文件，SynchInfo.txt文件头信息解析出现错误！不影响后续导入，继续导入...",e);
				}
				isFirstRow = false;
			} else {// 解析synchInfo！每一行都是一张表的信息，表名,主键,tableId(业务表才有)
				try {
					String[] tmpStr = dataSrc.split(",");
					SynchPO synchPO = new SynchPO();
					String physDBName = tmpStr[0];
					synchPO.setPhysDBName(physDBName);
					String[] pkArr = tmpStr[1].split("#");
					//导出方已经进行了主键验证，导入方原则上永远不会出现下面的问题
					if(pkArr.length == 0){
						logger.error("导出方的压缩包存在问题！该"+physDBName+"表在导出方未设置任何主键！联系导出方管理人员");
						throw new ImportResolverException("导出方的压缩包存在问题！该"+physDBName+"表在导出方未设置任何主键！联系导出方管理人员");
					}
					for(String str : pkArr){
						if(str.equalsIgnoreCase("STATUS")){
							//不允许STATUS为主键，如果检测到，自动跳过！
							continue;
						}
						//不会为空，重载了默认构造函数
						List<String> pkList = synchPO.getPkList();
						pkList.add(str);
					}
					String synchOrder = tmpStr[2];
					synchPO.setSynchOrder(Integer.parseInt(synchOrder));
					
					if(tmpStr.length == 4){
						String tableId = tmpStr[3];
						synchPO.setTableId(tableId);
					}
					synchMap.put(physDBName, synchPO);
				} catch (Exception e) {
					e.printStackTrace();
					throw new ImportResolverException("解析SynchInfo.txt信息时出错！");
				}
			}
		}
		return synchMap;
	}
	/**
	 * 解析说明文件
	 * 
	 * @param bufferReader
	 * @param logID
	 * @throws IOException 
	 * @throws Exception
	 */
	private void parseExplainFile(BufferedReader bufferReader, String logId) throws ImportResolverException, IOException {
		String dataSrc = null;
		boolean isFirstRow = true;
		// 日志信息
		SynchImpLogPO impLog = null;
		while ((dataSrc = bufferReader.readLine()) != null) {
			dataSrc = new String(dataSrc.getBytes()).trim();
			if (isFirstRow) {// 说明信息
				try{
					String[] tmpStrArr = dataSrc.split(",");
					String exportMode = tmpStrArr[0].split(":")[1];
					//这里是否需要回调？
				}catch(Exception e){
					e.printStackTrace();
					logger.error("未按照标准格式导出文件，导出清单文件头信息解析出现错误！不影响后续导入，继续导入...");
				}
				isFirstRow = false;
			} else {// 解析导出清单信息
				try {
					impLog = new SynchImpLogPO();
					String[] tmpStr = dataSrc.split(",");
					impLog.setLogId(logId);
					impLog.setExpDatas(tmpStr[2]);
					impLog.setExpFileName(SynchToolUtil.getFileName(tmpStr[0]));
					impLog.setExpPhysDBName(tmpStr[0]);
					synchLogService.saveLog(impLog,LogType.IMPLOG);
				} catch (Exception e) {
					e.printStackTrace();
					throw new ImportResolverException("解析导出清单信息时出错！");
				}
			}
		}
	}
	
	/**
	 * 解析业务数据文件
	 * 
	 * @param dataBytes
	 * @param zipChildName
	 * @param logId
	 * @param synchMap TODO
	 * @param decoder
	 * @throws UnsupportedEncodingException 
	 * @throws CallbackException 
	 * @throws Exception
	 */
	private void parseBusiDataFile(byte[] dataBytes,String zipChildName, String logId, Map<String, SynchPO> synchMap) throws ImportResolverException, UnsupportedEncodingException, CallbackException{
		int index = zipChildName.indexOf(SynchConstants.FILE_TYPE);
		if (index < 0) {
			throw new ImportResolverException("ZIP包中文件[" + zipChildName + "]不为TXT类型！");
		}
		//String physDBName = zipChildName.substring(0, index);
		String temp = zipChildName.substring(0,index);
		String physDBName = temp.substring(0,temp.indexOf("-"));
		byte[] data = null;

		try {
			data = SecretManager.decode(dataBytes);
			logger.info(physDBName + "数据解密完成！");
		} catch (Exception e) {
			throw new ImportResolverException(zipChildName + "解密失败");
		}
		
		String dat = new String(data, "GBK");
		//因为回调后，有可能会改变表名，但是synchMap里存的还是原始的表名，所以这里必须处理！要保持synchMap和插到中间表的物理表名一致
		//算法比较简单，先用原始表名取值，回调后，再取一次，如果这次为空，说明表名被改了
		SynchPO synchPO = synchMap.get(physDBName);
		String orginalPhysDBName = physDBName;
		//回调，可以修改插入到中间表的物理表名称！
		physDBName = changePhysDBNameCallback(physDBName);
		
		SynchPO tempSynchPO = synchMap.get(physDBName);
		if(tempSynchPO == null){
			logger.info("导出表为："+orginalPhysDBName+",导入到表:"+physDBName+"中！看到此条信息说明你在回调时候，将导入表修改了！");
			//说明回调时，表名被改了,用原来的对象处理
			//将同步对象中的表名改成回调后的表名
			synchPO.setPhysDBName(physDBName);
			//将修改前的表名保存下来
			synchPO.setOriginalPhysDBName(orginalPhysDBName);
			//删除原先Map中的synchPO
			synchMap.remove(orginalPhysDBName);
			//将新的表名对应的同步对象放到Map中
			synchMap.put(physDBName, synchPO);
		}
		
		try {
			//将解密数据插入到中间表synch_t_decrydata中
			importResolverMapper.insertDecryptData(logId, physDBName, dat);
			logger.info(physDBName + "数据插入中间表！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(physDBName + "数据插入中间表失败！");
			throw new ImportResolverException(physDBName + "数据插入中间表失败！");
		}
		
	}

	/**
	 * 解析大数据文件
	 * 
	 * @param dataBytes
	 * @param zipChildName
	 * @param logId
	 * @throws CallbackException 
	 * @throws SQLException 
	 * @throws Exception
	 */
	private void parseBigDataFile(byte[] dataBytes, String zipChildName,String logId) throws ImportResolverException, CallbackException, SQLException {

		if (dataBytes == null)
			return;

		String physDBName = zipChildName.split("/")[0];
		String dataType = zipChildName.split("/")[1];
		String colName = zipChildName.split("/")[2];
		String where = zipChildName.split("/")[3];
		where = where.replaceAll("\\$", "'");

		logger.info(zipChildName + "解析大数据字段");

		String guid = SynchToolUtil.GUID();
		//这里应该不需要再对Map进行修改了，运行到这里，业务数据已经解析完成，Map已经改好了
		//回调接口，修改物理表名称
		physDBName = changePhysDBNameCallback(physDBName);
		
		// 把数据存入大文本中间表
		importResolverMapper.insertBlobOrClob(logId, physDBName, colName, where, guid);

		String sql = getBigDataSQL(logId, physDBName, dataType, colName, guid);
		logger.info("获取大文件数据:" + sql);

		parseBigDataFile(sql, dataBytes, dataType);

	}

	/**
	 * 解析附件文件
	 * 
	 * @param dataBytes
	 * @param zipChildName
	 * @throws Exception
	 */
	private FutureTask<String> parseAttachmentFile(byte[] dataBytes,
			String zipChildName) throws ImportResolverException{
		// 上传附件
		//String uploadURL = getFudsUrl("SYNCH_UPLOADURL");
		String guid = zipChildName.split("/")[0];
		String fileName = zipChildName.split("/")[1];

//		FileUploadClient fuc = new FileUploadClient();
//		try {
//			fuc.fileUpload(uploadURL, guid, fileName, SynchConstants.BGT_APPID,
//					"", dataBytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("附件下载失败！URL:"+uploadURL+",文件名:"+fileName);
//			throw new ImportResolverException("附件下载失败！URL:"+uploadURL+",文件名:"+fileName);
//		}
		return null;
	}
	
	/**
	 * 将CLOB,BLOB数据保存到中间表中，使用的是jdbc的事务机制，因为未设置手动提交，所以每执行一条SQL语句，就会直接提交。
	 * 未纳入SPRING的事务管理器中进行管理
	 * @param sql
	 * @param dataBytes
	 * @param dataType
	 * @throws SQLException 
	 * @throws Exception
	 */
	private void parseBigDataFile(String sql, byte[] dataBytes, String dataType) throws SQLException,ImportResolverException {
		// 得到数据库连接
		Connection conn = jdbcDao.getCurrentConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		try {
			while (rs.next()) {
				Method method = null;
				if (dataType.equals("CLOB")) {
					Object obj = rs.getClob("CDATA");
					method = obj.getClass().getMethod(
							"getCharacterOutputStream", new Class[] {});
					Writer out = (Writer) method.invoke(obj,
							new Object[] {});
					String clobScript = new String(dataBytes, "GBK");
					out.write(clobScript);
					out.close();
				} else {
					Object obj = rs.getBlob("BDATA");
					method = obj.getClass().getMethod(
							"getBinaryOutputStream", new Class[] {});
					OutputStream out = (OutputStream) method.invoke(obj,
							new Object[] {});
					out.write(dataBytes);
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw new ImportResolverException(e.getMessage());
		} finally {
			rs.close();
			stmt.close();
		}
	}
	
	/**
	 * 获取大文本sql
	 * 
	 * @param logID
	 * @param physDBName
	 * @param dataType
	 * @param colName
	 * @return
	 */
	private String getBigDataSQL(String logID, String physDBName,
			String dataType, String colName, String guid) {
		StringBuffer sql = new StringBuffer(" SELECT ");
		if (dataType.equals("BLOB")) {
			sql.append(" BDATA ");
		} else {
			sql.append(" CDATA ");
		}
		sql.append(" FROM SYNCH_T_BLOBCLOB WHERE ");
		sql.append(" LOGID = '" + logID + "' ");
		sql.append("AND PHYSDBNAME = '" + physDBName + "' ");
		sql.append("AND COLUNAME = '" + colName + "' ");
		sql.append("AND GUID = '" + guid + "' FOR UPDATE ");

		return sql.toString();
	}
	
	/**
	 * @param synchMap
	 * @return
	 * 将Map转为List<SynchPO>,按照synchOrder排序
	 */
	private List<SynchPO> convertListFromMap(Map<String,SynchPO> synchMap){
		Iterator<String> it = synchMap.keySet().iterator();
		List<SynchPO> list = new ArrayList<SynchPO>();
		while(it.hasNext()){
			SynchPO synchPO = synchMap.get(it.next());
			list.add(synchPO);
		}
		Collections.sort(list);
		return list;
	}
	
	/**
	 * @param physDBName
	 * @return
	 * @throws CallbackException
	 * 修改表名最多只能有一个回调，否则容易造成混乱
	 */
	private String changePhysDBNameCallback(String physDBName) throws CallbackException{
		ImportResolveProcessor call  = (ImportResolveProcessor)CallbackManager.getBeansByInterfaceName("ImportResolveProcessor", true);
		if(call == null){
			//如果没有回调则直接返回表名
			return physDBName;
		}else{
			physDBName = call.changePhysDBNameBeforeResolveData(physDBName);
			return physDBName;
		}
		
	}
}
