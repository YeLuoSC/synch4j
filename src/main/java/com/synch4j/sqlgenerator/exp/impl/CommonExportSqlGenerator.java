package com.synch4j.sqlgenerator.exp.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.Synch2Context;
import com.synch4j.callback.CallbackManager;
import com.synch4j.callback.ExportDecideIncreaseTableProcessor;
import com.synch4j.callback.ExportSqlChangeProcessor;
import com.synch4j.datapicker.dao.IDataPickerMapper;
import com.synch4j.exception.CallbackClassNotExistIoCException;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.sqlgenerator.exp.IExportSqlGenerator;
import com.synch4j.sqlgenerator.exp.dao.IExportSqlGeneratorMapper;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;

/**
 * @author XieGuanNan
 * @date 2015-8-19-下午1:33:29
 * 该组件职责为生成SQL语句，为数据库数据提取器提供Sql语句生成的功能,这个组件还会把model,factor，modelcode,factorcode永久导出
 * 该生成器按照增量导出生成SQL语句；
 * 如果未来需要非增量，重新实现一个接口，并在配置文件中修改为新的实现类beanId即可
 */
@Component
public class CommonExportSqlGenerator implements IExportSqlGenerator{

	Logger logger = Logger.getLogger(CommonExportSqlGenerator.class);
	
	@Resource
	private IExportSqlGeneratorMapper exportSqlGeneratorMapper;
	
	/**
	 * 生成一张表的导出sql语句,该生成器按照增量导出
	 * 通用的SQL生成器算法中，model,factor一定是每次都下发，
	 * key:colsName,value:COL1TjhQCOL2..
	 * key:querySQL,value:导出SQL字符串
	 * @throws CallbackException 
	 */
	public Map<String, String> getExportSqlInfo(ExportMode mode,SynchPO synchPO,
			List<Map<String, String>> tableColumns,Synch2Context context) throws CallbackException {
		Map<String, String> resultMap = new HashMap<String, String>();
		StringBuffer colSql = new StringBuffer("");// 记录数据库查询列
		StringBuffer colsName = new StringBuffer("");// 记录导出的列信息
		String querySQL = "";
		
		//获取下发目标分区的最新导出时间
		String lastExpDate = exportSqlGeneratorMapper.getLastExpDate(synchPO.getPhysDBName());
		
		logger.info(synchPO.getPhysDBName()+"该表的上一次导出时间："+lastExpDate);
		for (int i = 0; i < tableColumns.size(); i++) {
			//拼导出SQL的列，SELECT xxxx
			
			Map<String, String> map = tableColumns.get(i);
			String colName = (String) map.get("COLUMN_NAME");
		
			
			//判断配置文件中，需要忽略的列
			if(decideIgnoreColNameFromProperties(mode,colName)){
				continue;
			}
			
			String dataType = map.get("DATA_TYPE");
			//TODO 此处是否应该抽象出去？如抽象出DataTypeHandler,针对每个类型选择调用某个接口。暂时没想好，15.8.19，谢冠男
			if (dataType.equals("DATE")) {
				colSql.append(" TO_CHAR(" + colName
						+ ",'yyyy-mm-dd hh24:mi:ss') " + colName + ",");
			} else if (dataType.contains("TIMESTAMP")) {
				//为什么是contains，因为DBVERSION字段类型是TIMESTAMP(6)，只能这样做了
				colSql.append(" TO_CHAR(" + colName
						+ ",'yyyy-mm-dd hh24:mi:ss.ff9') " + colName + ",");
			} else {
				colSql.append(colName + ",");
			}

			//如果是CLOB,BLOB，同样要将其拼到业务数据文件的第一行列信息中，同时将其对应的值，设置为empty_clob()或empty_blob()
			colsName.append(colName + SynchConstants.DATA_SPLIT);
		}

		String colSqlStr = colSql.toString();
		colSqlStr = colSqlStr.substring(0, colSqlStr.length() -1);
		querySQL = "SELECT " + colSqlStr + " FROM "
				+ synchPO.getPhysDBName() + " " + synchPO.getSynchCondition() ;

		//如果该表没设置过时间戳列，直接按照非增量导出
		if(!StringUtils.isEmpty(synchPO.getSynchRecogCol())){
			//这些忽略的表将被永久性导出，而不按照增量的形式！
			//需要注意此处为或者的关系，配置文件中如果配置了某张表，或者符合了回调的条件，该表就会被以非增量形式导出
			if(decideIgnoreTableNameFromCallback(mode,synchPO) || decideIgnoreTableNameFromProperties(mode,synchPO) 
					|| ((!StringUtils.isEmpty(synchPO.getIsAlwaysExport())) && synchPO.getIsAlwaysExport().equals("1"))){
				//MODEL、MODELCODE、FACTOR、FACTORCODE表特殊处理，不按照最后导出时间进行导出，
				//否则在下次导出数据时，如果有导入时间之前的表，不会被导出，但是你又需要导出它们
				//15.7.30谢冠男，还有问题，model,factor等必须恒下发，A,B两人对应A.B两个任务类型，分别对应4和6张业务表，
				//那么当A下发完后，保存了当时的导出时间，当B再次下发到同一个省的时候，会读取到A人的导出时间，导致B人的6张表的MODEL，FACTOR数据没下发下去！
				
				//2015.9.7 之前有个地方疏漏了，其实是只要某一张表有下发部分数据的需求，就必须每次全下发，永久导出！否则就会有上述问题
			}else{
				if (!StringUtils.isEmpty(lastExpDate)) {
					querySQL =  querySQL + " AND " + synchPO.getSynchRecogCol()
							+ " > TO_TIMESTAMP('" + lastExpDate + "', "
							+ SynchConstants.SYSTIMESTAMP_FORMAT + ") ";
				}
			}
		}
		
		//TODO 待测试

		colsName = new StringBuffer(colsName.substring(0, colsName.length()
				- SynchConstants.DATA_SPLIT.length()));
		colsName.append("\r\n");

		querySQL = callbackSqlGenerateProcessor(mode,synchPO,querySQL,tableColumns);
		
		resultMap.put("colsName", colsName.toString());
		resultMap.put("querySQL", querySQL);
		
		//输出导出sql日志
		logger.info(synchPO.getPhysDBName() + "表导出SQL：" + querySQL);

		if(SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY).equalsIgnoreCase("true")){
			// 保存导出SQL
			exportSqlGeneratorMapper.insertExportSQL(context.getLogId(), querySQL, synchPO.getPhysDBName());
		}
		return resultMap;
	}
	
	private boolean decideIgnoreTableNameFromCallback(ExportMode mode,SynchPO synchPO) throws CallbackClassNotExistIoCException, CallbackException{
		Object obj = CallbackManager.getBeansByInterfaceName("ExportDecideIncreaseTableProcessor", true);
		ExportDecideIncreaseTableProcessor callback = (ExportDecideIncreaseTableProcessor)obj;
		if(callback == null){
			return false;
		}else{
			return callback.needIncreaseSynch(mode, synchPO);
		}
	}
	
	/**
	 * 判断传来的表名，是否为在配置文件中配置的忽略表格，这些忽略的表将被永久性导出，而不按照增量的形式！
	 * @param tableName
	 * @return false为不匹配，true为匹配，将忽略该列
	 * @throws CallbackException 
	 * @throws CallbackClassNotExistIoCException 
	 */
	private boolean decideIgnoreTableNameFromProperties(ExportMode mode,SynchPO synchPO){
		//读取配置文件：
		String tableName = synchPO.getPhysDBName();
		//可以根据导出模式进行分别判断，这里暂时没有需求
		String tableNames =SynchToolUtil.getValueFromProperties(SynchConstants.EXPORT_COMMON_SQLGEN_NOINC_TABLE_KEY);
		String[] tableNameArr = tableNames.split(",");
		if(tableNameArr == null){
			return false;
		}
		boolean flag = false;
		for(String matchTable : tableNameArr){
			if(matchTable.equalsIgnoreCase(tableName)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 判断传来的列名，是否为在配置文件中配置的忽略列,该匹配会匹配所有表格！非某一张单表
	 * @param colName
	 * @return false为不匹配，true为匹配，将忽略该列
	 */
	private boolean decideIgnoreColNameFromProperties(ExportMode mode,String colName){
		boolean flag = false;
		String colNames =SynchToolUtil.getValueFromProperties(SynchConstants.EXPORT_COMMON_SQLGEN_IGNORE_COL_KEY);
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
	 * 回调接口，给开发人员一个可以修改导出Sql的机会,默认回调实现了SqlGenerateProcessor接口的类，
	 * 注意该回调，目前影响所有导出模式。并没有根据MODE不同，调用不同的接口
	 * @param synchPO 同步对象
	 * @param tableColumns 该表的所有列信息
	 * @param querySql 该表的导出SQL
	 * @return 修改好的导出Sql
	 * @throws CallbackException
	 */
	private String callbackSqlGenerateProcessor(ExportMode mode,SynchPO synchPO,String querySQL,
			List<Map<String, String>> tableColumns) throws CallbackException{
		List callbackList = (List)CallbackManager.getBeansByInterfaceName("ExportSqlChangeProcessor", false);
		if(callbackList != null){
			for(Object obj : callbackList){
				logger.debug(synchPO.getPhysDBName() + "表回调前导出SQL：" + querySQL);
				ExportSqlChangeProcessor call = (ExportSqlChangeProcessor)obj;
				//querySQL = call.changeExportSql(synchPO, querySQL, tableColumns, destProvince, year);
				String changeSql = call.changeExportSql(synchPO, querySQL, tableColumns);
				//防止回调中，返回了空，比如开发人员忘记返回了SQL
				if(!StringUtils.isEmpty(changeSql)){
					querySQL = changeSql;
				}
				logger.debug(synchPO.getPhysDBName() + "表回调后导出SQL：" + querySQL);
			}
		}
		return querySQL;
	}
}
