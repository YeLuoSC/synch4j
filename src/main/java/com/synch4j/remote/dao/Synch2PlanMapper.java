package com.synch4j.remote.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;
import com.synch4j.po.Synch2PlanPO;

public interface Synch2PlanMapper extends SuperMapper{
	// 插入下发计划的记录，status默认为1，自动开启
	public void insertAssignPlan(
				@Param("docId") String docId,
				@Param("fileGuid") String fileGuid,
				@Param("provinceCode") String provinceCode,
				@Param("year") String year, 
				@Param("userId") String userId,
				@Param("appId") String appId);

	public void insertReportPlan(
				@Param("docId") String docId,
				@Param("fileGuid") String fileGuid,
				@Param("provinceCode") String provinceCode,
				@Param("year") String year, 
				@Param("userId") String userId,
				@Param("appId") String appId);
	public void insertImportPlan(
				@Param("docId") String docId,
				@Param("fileGuid") String fileGuid,
				@Param("provinceCode") String provinceCode,
				@Param("year") String year, 
				@Param("userId") String userId,
				@Param("direction") String direction,
				@Param("appId") String appId);

	public String countPlanByDocIdAndFileGuid(@Param("docId") String docId,
				@Param("fileGuid") String fileGuid);

	// 将计划改为正在执行
	public void runningPlanByDocIdAndFileGuid(@Param("docId") String docId,
				@Param("fileGuid") String fileGuid);

	// 将计划改为执行成功
	public void successPlanByDocIdAndFileGuid(@Param("docId") String docId,
				@Param("fileGuid") String fileGuid);

	// 将计划改为执行失败
	public void failPlanByDocIdAndFileGuid(@Param("docId") String docId,
				@Param("fileGuid") String fileGuid);

	// 将计划改为未激活，用于有异常时改变状态，下次执行任务时，重新执行。目前失败后，不重新执行，所以暂时没用
	public void activePlanByDocIdAndFileGuid(@Param("docId") String docId,
				@Param("fileGuid") String fileGuid);

	//取自己APPID下的任务
	public List<Synch2PlanPO> getActivePlan(@Param("appId")String appId);

	public void updateRemarkByDocIdAndFileGuid(@Param("docId") String docId,
				@Param("fileGuid") String fileGuid, @Param("remark") String remark);
	
	public String getFileGuidByDocId(@Param("docId")String docId);
	
	public void delFileGuidDocId(@Param("docId")String docId,@Param("fileGuid")String fileGuid);
	
	public String getCodeByAgencyId(@Param("province")String province);
	
	public String getTaskTypeIdByDocId(@Param("docId")String docId);
	
	public String getDefaultProvince();
	
	public List<String> getFileIds();
}
