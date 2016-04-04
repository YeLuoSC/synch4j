package com.synch4j.callback.impl;

import org.springframework.stereotype.Component;

import com.synch4j.callback.ImportDbValueChangeProcessor;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ImportMode;

@Component
public class ImportDbValueChangeProcessorImpl implements ImportDbValueChangeProcessor{

	@Override
	public String changeDbValue(ImportMode mode, SynchPO synchPO,
			String dbValue, String colName)
			throws CallbackException {
		if(colName.equalsIgnoreCase("DBVERSION")){
			dbValue = "SYSDATE";
		}
		return dbValue;
	}

}
