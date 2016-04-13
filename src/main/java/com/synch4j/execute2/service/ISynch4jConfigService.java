package com.synch4j.execute2.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.synch4j.po.SynchPO;

public interface ISynch4jConfigService {
	
	public PageInfo getSynchConfigList(PageInfo page);
	
	public String saveSynchPO(SynchPO synchPO);
	
	public void delSynchPO(String physDBName);
}
