package com.synch4j.callback.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.Synch2Context;
import com.synch4j.callback.NotStandardExportPostProcessor;
import com.synch4j.callback.StandardExportPostProcessor;
import com.synch4j.callback.dao.ICallbackMapper;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;

/**
 * @author XieGuanNan
 * @date 2015-8-18-上午11:48:26
 * 该回调将根据参数，对同步对象进行修改
 * 该类继承了标准和非标准模式的回调接口，即不论标准和非标准，都将回调该接口
 * 在标准中，我会回调StandardExportPostProcessor，
 * 其他模式中，我会回调NotStandardExportPostProcessor，但是是一个实现类，所以都会回调到该类
 * 目前业务上，固定行列表和浮动表的模板数据是需要下发的，所以没必要写2个完全相同的算法，整合成一个，更好维护
 * 
 * @date 2015-8-30-下午19:28:26
 * 这里忘记考虑了上报的业务情景，上报时这些数据是不导出的，所以不应该增加上报的switch分支
 */
@Component
public class AllExportPostProcessorA1A2 implements StandardExportPostProcessor,NotStandardExportPostProcessor{
	
	Logger logger = Logger.getLogger(AllExportPostProcessorA1A2.class);

	@Resource
	private ICallbackMapper callbackMapper;
	
	@Override
	public void postProcessBeforeExport(ExportMode mode, List<SynchPO> list, Synch2Context context) throws CallbackException {
		logger.info("导出前回调！AllExportPostProcessorA1A2");
		try{
			switch(mode){
			case COMMON_EXPORT:
			}
		}catch(Exception e){
			logger.error("回调时，AllExportPostProcessorA1A2时出错！",e);
			e.printStackTrace();
			throw new CallbackException("回调时，AllExportPostProcessorA1A2时出错！");
		}
	}

	@Override
	public void postProcessAfterExport(ExportMode mode,String zipPath, String logId, Synch2Context context) throws CallbackException {
		// TODO Auto-generated method stub
		
	}
}
