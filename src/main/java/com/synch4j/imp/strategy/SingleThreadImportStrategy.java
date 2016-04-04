package com.synch4j.imp.strategy;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.synch4j.callback.CallbackManager;
import com.synch4j.callback.ImportPostProcessor;
import com.synch4j.dataimporter.IDataImporter;
import com.synch4j.exception.CallbackException;
import com.synch4j.imp.AbsImportStrategy;
import com.synch4j.log.service.ISynchLogService;
import com.synch4j.po.SynchMainLogPO;
import com.synch4j.po.SynchPO;
import com.synch4j.resolver.SynchResolverManager;
import com.synch4j.synchenum.ImportMode;
import com.synch4j.synchenum.LogType;
import com.synch4j.util.SynchConstants;

@Component
public class SingleThreadImportStrategy extends AbsImportStrategy{

	private String logId = null;
	
	@Resource
	private ISynchLogService synchLogService;

	@Override
	public void prepareImportCallback() throws CallbackException {
		List callbackList = (List)CallbackManager.getBeansByInterfaceName("ImportPostProcessor", false);
		for(Object obj : callbackList){
			ImportPostProcessor call = (ImportPostProcessor)obj;
			call.postProcessBeforeImport(logId);
		}
	}

	@Override
	public void startImport() throws Exception {
		//删除中间表残留数据，synch_t_importsql,synch_t_decryptdata,synch_t_blobclob
		//和一些特殊处理的表，model_s,factor_s,formula_t_formuladef_s,DICT_T_SETREFRELA_S
		//因为这些表数据在同步完表后，在后续的逻辑中还会调取这些数据，导致在当次导入时，没有清除它们，当再次导入时，会有数据残留，必须要清除
		//删除的是上一次导入的残留，开发模式的记录也删除
		importMapper.truncateTempData();
		String fileGuid = context.getFileGuid();
		if(!StringUtils.isEmpty(fileGuid)){
			//下载数据包
//			IFUDSService service = (IFUDSService) ServiceFactory.getBean("fuds.service");
//			FUDSFile file;
//			try {
//				file = service.downloadFiles(context.getAppId(), fileGuid);
//				if(file==null){
//					logger.error("导入的文件为空！");
//				}
//				fileName = file.getFilename();
//				is = file.getInputStream();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}else{
			is = context.getIs();
			fileName = "上传文件包";
		}
		//保存导入主日志
		saveImportMainLog();
		
		//开始解析文件包内容，大体上包括解密数据，将业务数据、CLOB和BLOB插入分别中间表synch_t_decrptdata,synch_t_blobclob表中，
		//为后续的导入创造好条件,List<SynchPO>必须为按照同步顺序排好序的list
		try{
			List<SynchPO> synchList = SynchResolverManager.resolveFromInputStream(ImportMode.SINGLE_THREAD,is, logId, context);
			logger.info("将按照下列顺序，依次进行导入：");
			for(SynchPO synchPO : synchList){
				if(synchPO.getTableId() != null){
					logger.info("同步顺序:" + synchPO.getSynchOrder() + "，表名："+synchPO.getPhysDBName() + ",业务表tableId:" + synchPO.getTableId());
				}else{
					logger.info("同步顺序:" + synchPO.getSynchOrder() + "，表名："+synchPO.getPhysDBName());
				}
			}
			IDataImporter dataImporter = (IDataImporter)CallbackManager.getBeansByPropertiesKey(SynchConstants.IMPORT_SINGLE_DATAIMPORTER_KEY, true);
			dataImporter.importData(mode, synchList, logId, context);
		}catch(Exception e){
			//因为事务的问题，以下语句没有作用；
			//synchLogService.updateMainLogSynStatus(logId, "3");
			throw e;
		}
		
	}

	@Override
	public void endImportCallback() throws CallbackException {
		List callbackList = (List)CallbackManager.getBeansByInterfaceName("ImportPostProcessor", false);
		for(Object obj : callbackList){
			ImportPostProcessor call = (ImportPostProcessor)obj;
			call.postProcessAfterImport();
		}
	}

	@Override
	public void endImport() {
		//更新导入的使用时间
		synchLogService.updateMainLog(context.getLogId(), importMapper.getDbDate(), String.valueOf((System.currentTimeMillis() - startTime) / 1000));
		//更新主日志的状态位：
		synchLogService.updateMainLogSynStatus(logId, "2");
		logger.info("导入成功！");
	}

	private void saveImportMainLog(){
		SynchMainLogPO mainLog = new SynchMainLogPO();
		logId = context.getLogId();
		//UserDTO user = SecureUtil.getCurrentUser();
		String userName = "用户";
		
		mainLog.setLogId(logId);
		mainLog.setUserId(userName);
		mainLog.setStartDate(importMapper.getDbDate());
		mainLog.setFileName(fileName);
		mainLog.setDirection("I");
		//开始导入标识符
		mainLog.setSynStatus("3");
		mainLog.setFileGuid(context.getFileGuid());
		synchLogService.saveLog(mainLog,LogType.MAINLOG);
	}

	@Override
	public ImportMode setImportMode() {
		return ImportMode.SINGLE_THREAD;
	}
}
