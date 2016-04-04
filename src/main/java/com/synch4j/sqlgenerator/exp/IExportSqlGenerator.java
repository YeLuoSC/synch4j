package com.synch4j.sqlgenerator.exp;

import java.util.List;
import java.util.Map;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;

/**
 * Sql语句生成器，负责生成导出SQL
 * @author XieGuanNan
 * @date 2015-8-19-下午4:43:22
 */
public interface IExportSqlGenerator {
	public Map<String, String> getExportSqlInfo(ExportMode mode,SynchPO synchPO,
			List<Map<String, String>> tableColumns,Synch2Context context) throws CallbackException ;
}
