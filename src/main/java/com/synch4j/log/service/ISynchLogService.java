package com.synch4j.log.service;

import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchLogPO;
import com.synch4j.synchenum.LogType;

/**
 * @author XieGuanNan
 * @date 2015-8-11-下午6:28:25
 * LogType为枚举，根据枚举类型不同，做不同处理
 */
public interface ISynchLogService {
	
	/**
	 * 保存该日志类型的日志
	 * @param logPO
	 */
	public void saveLog(SynchLogPO logPO, LogType type);
	
	/**
	 * 获取该日志类型及LogId的日志对象
	 * @param logId
	 * @param type
	 * @return
	 */
	public <T extends SynchLogPO>T getSynchLog(String logId,LogType type);
	
	/**
	 * 删除主日志及导入日志
	 * @param logId
	 */
	public void delImpLog(String logId);
	
	/**
	 * 删除主日志及导出日志
	 * @param logId
	 */
	public void delExpLog(String logId);
	
	/**
	 * 更新主日志，只更新该LogId的结束时间和使用时间字段
	 * @param logId
	 * @param endTime
	 * @param useTime
	 */
	public void updateMainLog(String logId,String endTime,String useTime);
	
	/**
	 * 更新导入日志
	 * @param logId
	 * @param physDBName
	 * @param usedTime
	 * @param insertRows
	 * @param failRows
	 */
	public void updateImportLog(SynchImpLogPO impLogPO);
	
	/**
	 * 更新主日志同步状态位
	 * @param logId
	 * @param endTime
	 * @param useTime
	 */
	public void updateMainLogSynStatus(String logId,String synStatus);
	
	/**
	 * 更新主日志同步状态位
	 * @param logId
	 * @param endTime
	 * @param useTime
	 */
	public void updateMainLogSynStatusByFileGuid(String fileGuid,String synStatus);
	
}
