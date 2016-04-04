package com.synch4j.callback.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.synch4j.callback.ExportSqlChangeProcessor;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;

@Component
public class ExportSqlChangeProcessorImpl implements ExportSqlChangeProcessor{

	@Override
	public String changeExportSql(SynchPO synchPO, String querySql,
			List<Map<String, String>> tableColumns) throws CallbackException {
		// TODO Auto-generated method stub
		return null;
	}

}
