package com.synch4j.callback.impl;

import org.springframework.stereotype.Component;

import com.synch4j.callback.ImportResolveProcessor;
import com.synch4j.exception.CallbackException;
import com.synch4j.util.SynchConstants;
@Component
public class ImportResolveProcessorImpl implements ImportResolveProcessor{

	@Override
	public String changePhysDBNameBeforeResolveData(String physDBName)
			throws CallbackException {
		//MODEL表有一列initSQL，由于model表在导入时，插入的是model_s表，所以在解析数据时，
		//发现是model表，将全部解析数据全部转为model_s表
//		if(physDBName.equalsIgnoreCase(SynchConstants.MODEL_TABLE)){
//			physDBName = SynchConstants.MODEL_TABLE + "_S";
//		}else if(physDBName.equalsIgnoreCase(SynchConstants.FACTOR_TABLE)){
//			physDBName = SynchConstants.FACTOR_TABLE + "_S";
//		}else if(!physDBName.equalsIgnoreCase(SynchConstants.TABLE_REMOTEPROCEDURE_NAME)){
//			physDBName = physDBName + "_S";
//		}
		//最好不要做太多修改在此处，特别是model_s,factor_s，会涉及到存储过程，其他表应该没问题
		physDBName = physDBName + SynchConstants.CHANGE_DBNAME_SUFFIX;
		return physDBName;
	}

}
