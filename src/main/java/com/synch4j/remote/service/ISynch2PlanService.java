package com.synch4j.remote.service;

import java.util.List;

import com.synch4j.po.Synch2PlanPO;

public interface ISynch2PlanService {
	public void start(Synch2PlanPO planPO);
	
	public void success(Synch2PlanPO planPO);
	
	public void fail(Synch2PlanPO planPO,String errorMessage);
	
	public  List<Synch2PlanPO>  getActivePlanPO(String appId);
}
