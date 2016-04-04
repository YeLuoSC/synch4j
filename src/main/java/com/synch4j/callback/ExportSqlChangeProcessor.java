package com.synch4j.callback;

import java.util.List;
import java.util.Map;

import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;

/**
 * @author XieGuanNan
 * @date 2015-8-19-下午5:02:14
 * 
 * 该接口回调于导出SQL生成器中
 */
public interface ExportSqlChangeProcessor {

	/**
	 * 该接口将在生成导出Sql之前调用，给开发人员一个可以修改导出Sql的机会
	 * @param synchPO 同步对象
	 * @param querySql 该表的导出SQL
	 * @param tableColumns 该表的所有列信息
	 * @return 修改好的导出Sql
	 * @throws CallbackException
	 */
	public String changeExportSql(SynchPO synchPO,String querySql,
			List<Map<String, String>> tableColumns) throws CallbackException;
	
}
