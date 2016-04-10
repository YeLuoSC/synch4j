package com.synch4j.execute2.service;

import java.util.List;

import com.synch4j.po.SynchPO;

public interface ISynch4jConfigService {
	
	public List<SynchPO> getSynchConfigList(String physDBName, String tableName);
	
	public String saveSynchPO(SynchPO synchPO);
	
	public void delSynchPO(String physDBName);
}
