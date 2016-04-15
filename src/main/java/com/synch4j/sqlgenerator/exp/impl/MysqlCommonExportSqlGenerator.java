package com.synch4j.sqlgenerator.exp.impl;

import java.util.List;
import java.util.Map;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.sqlgenerator.exp.IExportSqlGenerator;
import com.synch4j.synchenum.ExportMode;

public class MysqlCommonExportSqlGenerator implements IExportSqlGenerator{

	@Override
	public Map<String, String> getExportSqlInfo(ExportMode mode,
			SynchPO synchPO, List<Map<String, String>> tableColumns,
			Synch2Context context) throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

}
