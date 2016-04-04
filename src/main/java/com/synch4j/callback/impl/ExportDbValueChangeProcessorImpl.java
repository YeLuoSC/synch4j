package com.synch4j.callback.impl;

import org.springframework.stereotype.Component;

import com.synch4j.callback.ExportDbValueChangeProcessor;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.util.SynchConstants;
import org.apache.log4j.Logger;

@Component
public class ExportDbValueChangeProcessorImpl implements ExportDbValueChangeProcessor{

	Logger logger = Logger.getLogger(ExportDbValueChangeProcessorImpl.class);
	
	@Override
	public String changeDbValue(ExportMode mode, SynchPO synchPO,
			String dbValue, String colName) throws CallbackException {
		//如果是model,factor表，将isReserver字段置为1。不许接收方修改表格
//		if(synchPO.getPhysDBName().equalsIgnoreCase(SynchConstants.MODEL_TABLE) || 
//				synchPO.getPhysDBName().equals(SynchConstants.FACTOR_TABLE)){
//			//model表的预留列和factor表的预留列差一个字母....
//			if(colName.equals("ISRESERVE") || colName.equals("ISRESERVED")){
//				dbValue = "1";
//				//logger.info("已将"+synchPO.getPhysDBName()+"表的"+colName+"列值:改为"+dbValue);
//			}
//		}
		return dbValue;
	}

}
