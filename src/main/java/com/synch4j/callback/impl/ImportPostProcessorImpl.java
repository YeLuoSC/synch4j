package com.synch4j.callback.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.synch4j.callback.ImportPostProcessor;
import com.synch4j.callback.dao.ICallbackMapper;
import com.synch4j.exception.CallbackException;
import org.apache.log4j.Logger;
@Component
public class ImportPostProcessorImpl implements ImportPostProcessor{
	
	Logger logger = Logger.getLogger(ImportPostProcessorImpl.class);
	
	@Resource
	private ICallbackMapper callbackMapper;
	
	@Override
	public void postProcessBeforeImport() throws CallbackException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcessAfterImport() throws CallbackException {
		logger.info("即将重新编译失效对象");
		callbackMapper.updateCompileInvalid();
		logger.info("重新编译失效对象完成");
	}

}
