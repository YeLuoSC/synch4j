package com.synch4j.execute2.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;
import com.synch4j.po.SynchPO;

public interface Synch4jConfigMapper extends SuperMapper{

	public List<SynchPO> getSynchSettingList(@Param("physDBName")String physDBName, @Param("tableName")String tableName);
	
	public int getCountByTableName(@Param("tableName")String tableName);
	
	public void insertSynchPO(@Param("synchPO")SynchPO synchPO);
	
	public void updateSynchPO(@Param("synchPO")SynchPO synchPO);
	
	public void delSynchPO(@Param("physDBName")String physDBName);
}
