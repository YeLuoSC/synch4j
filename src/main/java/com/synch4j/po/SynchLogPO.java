package com.synch4j.po;

/**
 * 同步日志的父类，只包含一个属性，贯穿所有类型的Log，存储Log时，统一一个方法
 * @author XieGuanNan
 * @date 2015-8-11-下午3:59:23
 */
public class SynchLogPO {
	/**
	 * 日志ID
	 */
	private String logId;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}
}
