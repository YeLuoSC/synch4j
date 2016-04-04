package com.synch4j.remote.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.remote.dao.Synch2PlanMapper;
import com.synch4j.remote.service.ISynch2PlanService;
import com.synch4j.po.Synch2PlanPO;
@Component
@Transactional(readOnly=true)
public class Synch2PlanServiceImpl implements ISynch2PlanService{

	@Resource
	private Synch2PlanMapper synch2PlanMapper;
	
	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void start(Synch2PlanPO planPO){
		//任务执行中
		synch2PlanMapper.runningPlanByDocIdAndFileGuid(planPO.getDocId(), planPO.getFileGuid());
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void success(Synch2PlanPO planPO){
		synch2PlanMapper.successPlanByDocIdAndFileGuid(planPO.getDocId(), planPO.getFileGuid());
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void fail(Synch2PlanPO planPO,String errorMessage) {
		synch2PlanMapper.updateRemarkByDocIdAndFileGuid(planPO.getDocId(), planPO.getFileGuid(), errorMessage);
		synch2PlanMapper.failPlanByDocIdAndFileGuid(planPO.getDocId(), planPO.getFileGuid());
	}

	@Override
	public List<Synch2PlanPO> getActivePlanPO(String appId) {
		return synch2PlanMapper.getActivePlan(appId);
	}


}
