package com.synch4j.log.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.log.dao.ISynchLogMapper;
import com.synch4j.log.service.ISynchLogService;
import com.synch4j.po.SynchExpLogPO;
import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchLogPO;
import com.synch4j.po.SynchMainLogPO;
import com.synch4j.synchenum.LogType;

import org.apache.log4j.Logger;

@Service
@Transactional(readOnly = true)
public class SynchLogServiceImpl implements ISynchLogService{
	
	Logger logger = Logger.getLogger(SynchLogServiceImpl.class);
	
	@Resource
	private ISynchLogMapper synchLogMapper;
	
	@Override
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void saveLog(SynchLogPO logPO, LogType type){
		switch(type){
		case MAINLOG:
			logger.info("即将保存同步主日志");
			SynchMainLogPO mainLogPO = (SynchMainLogPO)logPO;
			synchLogMapper.saveMainLog(mainLogPO);
			logger.info("保存同步主日志完成");
			break;
		case IMPLOG:
			logger.info("即将保存导入日志");
			SynchImpLogPO impLogPO = (SynchImpLogPO)logPO;
			synchLogMapper.saveImpLog(impLogPO);
			logger.info("保存导入完成");
			break;
		case EXPLOG:
			logger.info("即将保存导出日志");
			SynchExpLogPO expLogPO = (SynchExpLogPO)logPO;
			synchLogMapper.saveExpLog(expLogPO);
			logger.info("保存导出日志完成");
			break;
		default:
			logger.error("错误的日志类型！请检查");
			//throw new WrongLogTypeException("");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends SynchLogPO> T getSynchLog(String logId, LogType type) {
		T logPO;
		switch(type){
		case MAINLOG:
			logPO = (T)synchLogMapper.getMainLog(logId);
			return logPO;
		case IMPLOG:
			logPO = (T)synchLogMapper.getImpLogPO(logId);
			return logPO;
		case EXPLOG:
			logPO = (T)synchLogMapper.getExpLogPO(logId);
			return logPO;
		default:
			logger.error("错误的日志类型！请检查");
				
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void delImpLog(String logId) {
		synchLogMapper.delMainLog(logId);
		synchLogMapper.delImpLog(logId);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void delExpLog(String logId) {
		synchLogMapper.delMainLog(logId);
		synchLogMapper.delExpLog(logId);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void updateMainLog(String logId, String endTime, String useTime) {
		synchLogMapper.updateMainLog(logId, endTime, useTime);
	}

	@Override
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public void updateImportLog(SynchImpLogPO logPO) {
		synchLogMapper.updateImportLog(logPO);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateMainLogSynStatus(String logId, String synStatus) {
		synchLogMapper.updateMainLogSynStatus(logId, synStatus);
	}

	@Override
	public void updateMainLogSynStatusByFileGuid(String fileGuid,
			String synStatus) {
		synchLogMapper.updateMainLogSynStatus(fileGuid, synStatus);
	}
}
