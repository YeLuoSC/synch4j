package com.synch4j.remote.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.synch4j.fascade.ISynchBegin;
import com.synch4j.po.Synch2PlanPO;
import com.synch4j.remote.service.ISynch2PlanService;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;
import org.apache.log4j.Logger;

@Component
@Scope("prototype")
public class Synch2TaskPlan {
	
	Logger logger = Logger.getLogger(Synch2TaskPlan.class);
	
	@Resource
	private ISynch2PlanService synch2PlanServiceImpl;
	
	@Resource
	private ISynchBegin synchBeginImpl;
	
	public void execute(){
//		List<Synch2PlanPO> plans = synch2PlanServiceImpl.getActivePlanPO
//				(SynchToolUtil.getSystemAppID().toLowerCase());
		List<Synch2PlanPO> plans = null;
		if(plans==null || plans.size()==0){
			return;
		}
		for(Synch2PlanPO planPO : plans){
			//揽下所有任务，标志位改为1
			synch2PlanServiceImpl.start(planPO);
		}
		for(Synch2PlanPO planPO : plans){
			try{
				//任务执行中
				String direction = planPO.getDirection();
				
				synchBeginImpl.SingleThreadImport(planPO.getFileGuid());	
				synch2PlanServiceImpl.success(planPO);
			}catch(Exception e){
				e.printStackTrace();
				logger.error("执行时发生异常：", e);
				synch2PlanServiceImpl.fail(planPO, e.getMessage());
				//synchLogServiceImpl.updateMainLogSynStatusByFileGuid(planPO.getFileGuid(), "3");
			}
			
		}
	}
}
