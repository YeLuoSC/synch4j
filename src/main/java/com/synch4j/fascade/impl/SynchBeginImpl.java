package com.synch4j.fascade.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.DataPickerException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.exception.NotSupportAppIdException;
import com.synch4j.exp.IBaseExportStrategy;
import com.synch4j.fascade.ISynchBegin;
import com.synch4j.fascade.dao.FascadeMapper;
import com.synch4j.imp.IBaseImportStrategy;
import com.synch4j.util.SpringContextHolder;

/**
 * @date 2016-3-11
 * @author 谢冠男
 * 去掉了原型模式的标签，改为单例模式，并在各个导出方法签名中加入了synchronized关键字，在导出时加锁，保证
 * 同一时间只能有一个人在导出
 * 
 * @date 2016-3-25
 * 改为了原型模式，因为单例会有并发问题，将synchronized去掉了，由数据库锁控制
 */
@Component
@Scope("prototype")
public class SynchBeginImpl implements ISynchBegin{

	Logger logger = Logger.getLogger(SynchBeginImpl.class);
	
	@Resource
	private Synch2Context context;
	
	@Resource
	private FascadeMapper fascadeMapper;

	@Override
	public void SingleThreadImport(String fileGuid)
			throws Exception{
		IBaseImportStrategy importStrategy = (IBaseImportStrategy)SpringContextHolder.getContext().getBean("singleThreadImportStrategy");
		context.setImportStrategy(importStrategy);
		context.import$(fileGuid, null);
	}

	@Override
	public void SingleThreadImport(InputStream is) throws Exception {
		IBaseImportStrategy importStrategy = (IBaseImportStrategy)SpringContextHolder.getContext().getBean("singleThreadImportStrategy");
		context.setImportStrategy(importStrategy);
		context.import$(null, is);
	}

	@Override
	public void CommonExport() throws ErrorConfigureException,
			CallbackException, NotSupportAppIdException, DataPickerException,
			IOException {
		IBaseExportStrategy exportStrategy = (IBaseExportStrategy)SpringContextHolder.getContext().getBean("commonExportStrategy");
		context.setExportStrategy(exportStrategy);
		context.export();
	}

}
