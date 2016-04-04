package com.synch4j.callback.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.Synch2Context;
import com.synch4j.callback.ImportDbProcessor;
import com.synch4j.callback.dao.ICallbackMapper;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ImportMode;
import com.synch4j.util.SynchConstants;
@Component
public class ImportDbProcessorImpl implements ImportDbProcessor{

	Logger logger = Logger.getLogger(ImportDbProcessorImpl.class);
	
	@Resource
	private ICallbackMapper callbackMapper;

	
	@Override
	public void processBeforeBusiData(ImportMode mode, SynchPO synchPO,
			Synch2Context context) throws CallbackException {
		String physDBName = synchPO.getPhysDBName();
		String originalPhysDBName = synchPO.getOriginalPhysDBName();
		String tableId = synchPO.getTableId();
		if(originalPhysDBName != null){
			//说明该表格，在导入时候被改过名字。
			logger.info("创建临时表："+physDBName);
			callbackMapper.createTempTable(physDBName, originalPhysDBName);
		}
	}

	@Override
	public void processAfterBusiData(ImportMode mode, SynchPO synchPO,
			Synch2Context context) throws CallbackException {

	}

	@Override
	public void processAfterAllData(ImportMode mode, SynchPO synchPO,
			Synch2Context context) throws CallbackException {
		String physDBName = synchPO.getPhysDBName();
		String originalPhysDBName = synchPO.getOriginalPhysDBName();
		if(originalPhysDBName != null){
			//说明该表格，在导入时候被改过名字。
			
			// 删除临时表,只能此时删除，如果在同步数据的存储过程中删除，会导致BLOB,CLOB插入失败
			logger.info("同步表数据中："+physDBName +"表至"+originalPhysDBName);
			callbackMapper.synchTableData(physDBName, originalPhysDBName);
			
			logger.info("删除临时表："+physDBName);
			callbackMapper.delTempTable(physDBName);
		}
	}

}
