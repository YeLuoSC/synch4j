package com.synch4j.callback.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.Synch2Context;
import com.synch4j.callback.StandardExportPostProcessor;
import com.synch4j.callback.dao.ICallbackMapper;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;

import org.apache.log4j.Logger;

/**
 * 
 * @author XieGuanNan
 * @date 2015-9-9-下午3:11:43
 * 该回调于规范下发，文件打包走XCH
 */
@Component
public class StandardXchProcessorImpl implements StandardExportPostProcessor {

	Logger logger = Logger.getLogger(StandardXchProcessorImpl.class);
	
	@Resource
	private ICallbackMapper callbackMapper;
	
	@Override
	public void postProcessBeforeExport(ExportMode mode, List<SynchPO> list,
			String logId, Synch2Context context)
			throws CallbackException {
		// TODO Auto-generated method stub

	}

	@Override
	public void postProcessAfterExport(ExportMode mode, String zipPath,
			String logId, Synch2Context context) throws CallbackException {
		/*//开发模式跳过上传
		if(!SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY).equalsIgnoreCase("true")){
			logger.info("回调StandardXchProcessorImpl！即将发送文件包至XCH中");
			FileInputStream io = null;
	        try {
	        	IFUDSService service = (IFUDSService) ServiceFactory.getBean("fuds.service");
	    		File file = new File(zipPath);
	    		io = new FileInputStream(file);
	    		String fileGuid = null;
	        	fileGuid = service.uploadFile(io, file.getName(), appId);
	            logger.debug("文件：" + file.getPath() + "上传文件服务器成功,GUID=" + fileGuid);
	            XCHTaskFileDTO taskDTO = new XCHTaskFileDTO();
	            taskDTO.setAppId(appId);
	            List<String> list = new ArrayList<String>();
	            list.add(fileGuid);
	            List<String> fileIdList = callbackMapper.getFileIds();
				if(fileIdList != null){
					list.addAll(fileIdList);
					if(logger.isInfoEnabled()){
						logger.info("附件文件ID："+fileIdList.toString());
					}
				}
	            taskDTO.setMessageInfo(list);
	            taskDTO.setProvinceCode(sourceProvince);//发送端的码
	            List<String> provinceList = new ArrayList<String>();
	            provinceList.add(destProvince);
	            taskDTO.setProvinceInfo(provinceList);//接收端的码
	            taskDTO.setTaskType("test");
	            taskDTO.setBeanId("synch2BgtXchReceiveHandler");
	            Map<String,Object> tempMap = new HashMap<String,Object>();
	            tempMap.put("year", year);
	            taskDTO.putAdditionalInfo("infoMap",tempMap);
	            //记录日志
	            ISendTaskFileService sts = (ISendTaskFileService) ServiceFactory.getBean("xch.localsendtaskfileservice");
	            String taskguid = sts.sendDataService(taskDTO); 
	        } catch (Exception e) {
	        	e.printStackTrace();
	            logger.error("发送给XCH时，出现异常！",e);
	            throw new CallbackException("发送给XCH时，出现异常！");
	        } finally {
	            if (io != null) {
	                try {
	                    io.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		}else{
			logger.info("开发模式跳过上传文件至XCH！");
		}
*/
	}

}
