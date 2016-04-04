package com.synch4j.callback.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.callback.ExportDecideIncreaseTableProcessor;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;

/**
 * 为了解决filtercol存在二义性的问题，即永久下发和部分下发的问题，不用这个回调了，在导出解析器中，如果filtercol存在，则设置isAlwaysAssign
 * 属性为1，使其永久下发，Filtercol只负责部分下发筛选表格的作用
 * @author XieGuanNan
 *
 * @Date 2016.3.16
 * 今日又将这个类恢复了，先前根据filtercol是否为空返回true,false的逻辑已经删除，目前该类的作用是读取配置文件中的key，
 * 根据设置的值来判断是否按照增量导出
 */
@Component
public class ExportDecideIncreaseTableProcessorImpl implements ExportDecideIncreaseTableProcessor{

	Logger logger = Logger.getLogger(ExportDecideIncreaseTableProcessorImpl.class);
	
	@Override
	public boolean needIncreaseSynch(ExportMode mode, SynchPO synchPO)
			throws CallbackException {
		switch(mode){
		default:
			if(logger.isInfoEnabled()){
				logger.info("当前模式："+mode.toString()+",未匹配到对应的模式，是否是新增的模式？将默认按照增量导出");
			}
			return false;
		}
	}

}
