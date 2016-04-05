package com.synch4j.execute.service;

import java.util.List;

import com.synch4j.po.SynchMainLogPO;
import com.synch4j.po.SynchPO;

public interface ISynch4jConfigService {
	
	public List<SynchPO> getSynchConfigList(String physDBName, String tableName);
	
}
