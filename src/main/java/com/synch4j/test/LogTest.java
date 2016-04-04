package com.synch4j.test;

import com.synch4j.po.SynchExpLogPO;
import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchLogPO;
import com.synch4j.po.SynchMainLogPO;
import com.synch4j.synchenum.LogType;

public class LogTest {
	public static SynchMainLogPO getMainPO(){
		SynchMainLogPO po = new SynchMainLogPO();
		po.setFileName("main");
		po.setSynStatus("1");
		return po;
	}
	
	public static SynchExpLogPO getExpPO(){
		SynchExpLogPO po = new SynchExpLogPO();
		po.setExpDatas("exp");
		po.setExpFileName("2");
		return po;
	}
	
	public static <T extends SynchLogPO> T getSynchLog(String logId, LogType type) {
		T logPO;
		switch(type){
		case MAINLOG:
			logPO = (T)getMainPO();
			return logPO;
		case IMPLOG:
			logPO = (T)new SynchImpLogPO();
			return logPO;
		case EXPLOG:
			logPO = (T)getExpPO();
			return logPO;
				
		}
		return null;
	}
	public static void main(String[] args){
		SynchMainLogPO po1 = getSynchLog("1",LogType.MAINLOG);
		SynchImpLogPO po2 = getSynchLog("2",LogType.IMPLOG);
		SynchExpLogPO po3 = getSynchLog("3",LogType.EXPLOG);
//		System.out.println(po1.getFileName()+"|"+po1.getSynStatus());
//		System.out.println(po3.getExpDatas()+"|"+po3.getExpFileName());
//		System.out.println(po3.getClass().getSimpleName());
		Object obj = po1;
		System.out.println(obj instanceof SynchMainLogPO);
		SynchMainLogPO po4 = (SynchMainLogPO)obj;
		System.out.println(po4.getFileName());
		
	}
}
