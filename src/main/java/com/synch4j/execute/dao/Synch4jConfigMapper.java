package com.synch4j.execute.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;
import com.synch4j.po.SynchMainLogPO;

public interface Synch4jConfigMapper extends SuperMapper{

	public List<Map<String, Object>> getSynchSettingList(@Param("physDBName")String physDBName, @Param("tableName")String tableName);
}
