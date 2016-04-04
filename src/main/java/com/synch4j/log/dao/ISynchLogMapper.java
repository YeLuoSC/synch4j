package com.synch4j.log.dao;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;
import com.synch4j.po.SynchExpLogPO;
import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchMainLogPO;

public interface ISynchLogMapper extends SuperMapper{
	
	public void saveMainLog(SynchMainLogPO logPO);
	
	public void saveExpLog(SynchExpLogPO logPO);
	
	public void saveImpLog(SynchImpLogPO logPO);
	
	public SynchMainLogPO getMainLog(@Param("logId")String logId);
	
	public SynchExpLogPO getExpLogPO(@Param("logId")String logId);
	
	public SynchImpLogPO getImpLogPO(@Param("logId")String logId);
	
	public void delMainLog(@Param("logId")String logId);
	
	public void delExpLog(@Param("logId")String logId);
	
	public void delImpLog(@Param("logId")String logId);
	
	public void updateMainLog(@Param("logId")String logId,@Param("endTime")String endTime,@Param("useTime")String useTime);
	
	public void updateImportLog(SynchImpLogPO logPO);
	
	public void updateMainLogSynStatus(@Param("logId")String logId,@Param("synStatus")String synStatus);

	public void updateMainLogSynStatusByFileGuid(@Param("fileGuid")String fileGuId,@Param("synStatus")String synStatus);

}
