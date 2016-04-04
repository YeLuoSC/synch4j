package com.synch4j.sqlgenerator.imp.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.callback.CallbackManager;
import com.synch4j.callback.ImportDbValueChangeProcessor;
import com.synch4j.callback.ImportSqlGenerateProcessor;
import com.synch4j.exception.CallbackClassNotExistIoCException;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.sqlgenerator.imp.IImportSqlGenerator;
import com.synch4j.sqlgenerator.imp.dao.IImportSqlGeneratorMapper;
import com.synch4j.synchenum.ImportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;

@Component
public class CommonImportSqlGenerator implements IImportSqlGenerator{

	Logger logger = Logger.getLogger(CommonImportSqlGenerator.class);
	
	@Resource
	private IImportSqlGeneratorMapper importSqlGeneratorMapper;
	
	/**
	 * 得到导入数据的SQL
	 * 参数importColNameList包含key:COLUMN_NAME,DATA_TYPE两个值
	 * @throws CallbackException 
	 * @throws CallbackClassNotExistIoCException 
	 */
	@Override
	public String getImportSQL(ImportMode mode,
			SynchPO synchPO, List<Map<String, String>> importColNameList,
			String[] colValues, String logId) throws CallbackClassNotExistIoCException, CallbackException {
		String[] info;
		// 插入信息
		StringBuffer factorBuffer = new StringBuffer();
		StringBuffer factorValueBuffer = new StringBuffer();

		// 返回值信息 回传给handler类
		StringBuffer dataBuffer = new StringBuffer();

		Map<String, String> colInfo = null;

		for (int i = 0; i < importColNameList.size(); i++) {
			colInfo = importColNameList.get(i);
			String colName = colInfo.get("COLUMN_NAME");
			String dataType = colInfo.get("DATA_TYPE");

			String colValue = SynchToolUtil.getOracleValue(dataType,colValues[i]);
			
			//回调开发者的接口，允许在导入时，修改某些列的值
			colValue = callbackDbValueChangeProcessor(mode, synchPO, colValue, colName);
			
			dataBuffer.append(colName + ":" + colValues[i] + SynchConstants.DATA_SPLIT);
		
			factorBuffer.append(colName + ",");
			factorValueBuffer.append(colValue + ",");

		}

		//如果有CLOB、BLOB字段，拼到factorBuffer,factorValueBuffer中，列名和值(empty_clob())
		setRecordImportClobOrBlob(synchPO.getPhysDBName(), importColNameList,
				factorBuffer, factorValueBuffer);

		dataBuffer = new StringBuffer(dataBuffer.substring(0,
				dataBuffer.length() - 4));

		factorBuffer = new StringBuffer(factorBuffer.substring(0,
				factorBuffer.length() - 1));

		factorValueBuffer = new StringBuffer(factorValueBuffer.substring(0,
				factorValueBuffer.length() - 1));
		
		String sql = "INSERT INTO " + synchPO.getPhysDBName() + "( "+factorBuffer.toString()+" )"
					  + " VALUES ( "+factorValueBuffer.toString()+" )";
		//回调接口
		//暂时关掉这些
		//sql = callbackSqlGenerateProcessor(synchPO,sql,destProvince,year);
		
		logger.info("导入SQL：" + sql);
		//如果是开发模式，保存导入SQL：
		if(SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY) != null && 
				SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY).equalsIgnoreCase("true")){
			//保存导入SQL
			importSqlGeneratorMapper.insertImportSQL(logId, sql, synchPO.getPhysDBName());
		}
		
		return sql;
	}

	/**
	 * 写入表中大文本字段
	 * 
	 * @param physDBName
	 * @param importColNameList TODO
	 * @param factorBuffer
	 * @param factorValueBuffer
	 */
	private void setRecordImportClobOrBlob(String physDBName,
			List<Map<String, String>> importColNameList, StringBuffer factorBuffer, StringBuffer factorValueBuffer) {
		//下边的方法是错误的，1.0没有发现，实际上导出时，没有导出BLOB,CLOB列信息和值信息在BUSIDATA文件夹下的所有文件中
		//所以这个方法永远不会触发IF条件，导致CLOB,BLOB值压根没插EMPTY_CLOB(),在后续导入CLOB时，取值就会为空！
//		for (Map<String, String> map : importColNameList) {
//			String colName = map.get("COLUMN_NAME");
//			String dataType = map.get("DATA_TYPE");
//			if (dataType.equals("BLOB")) {
//				factorBuffer.append(colName + ",");
//				factorValueBuffer.append("EMPTY_BLOB(),");
//			} else if (dataType.equals("CLOB")) {
//				factorBuffer.append(colName + ",");
//				factorValueBuffer.append("EMPTY_CLOB(),");
//			}
//		}
	}
	
	private String callbackSqlGenerateProcessor(SynchPO synchPO,String sql,String destProvince,String year) throws CallbackClassNotExistIoCException, CallbackException{
		List list = (List) CallbackManager.getBeansByInterfaceName("ImportSqlGenerateProcessor", false);
		for(Object obj : list){
			logger.info(synchPO.getPhysDBName() + "表回调前导入SQL：" + sql);
			ImportSqlGenerateProcessor callback = (ImportSqlGenerateProcessor)obj;
			String changeSql = callback.changeImportSql(synchPO, sql, destProvince, year);
			//防止回调时候，返回了空
			if(!StringUtils.isEmpty(changeSql)){
				sql = changeSql;
			}
			logger.info(synchPO.getPhysDBName() + "表回调后导入SQL：" + sql);
		}
		return sql;
	}
	
	private String callbackDbValueChangeProcessor(ImportMode mode,SynchPO synchPO,String dbValue,String colName) throws CallbackClassNotExistIoCException, CallbackException{
		List list = (List) CallbackManager.getBeansByInterfaceName("ImportDbValueChangeProcessor", false);
		for(Object obj : list){
			ImportDbValueChangeProcessor callback = (ImportDbValueChangeProcessor)obj;
			//这里无法“如果为空就返回原值”的判断，因为有可能就是要改成空值
			dbValue = callback.changeDbValue(mode, synchPO, dbValue, colName);
		}
		return dbValue;
	}
}
