package com.synch4j.execute.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface Synch2ConfigMapper extends SuperMapper{
	
	public List<Map<String, Object>> getSynchSettingList(@Param("physDBName")String physDBName, @Param("tableName")String tableName);
	
	public List<String> getTableColumnList(@Param("physDBName")String physDBName);
	
	public List<String> getTableRecogColList(@Param("physDBName")String physDBName);
	
	public void insertSynchPO(Map<String, Object> dataMap);
	
	public void updateSynchPO(Map<String, Object> dataMap);
	
	public void deleteSynchPO(@Param("physDBName")String physDBName);
	
	public List<Map> getAppSelect();
	
	public List<Map> getSuitTree(@Param("appId")String appId);
	
	public List<Map> getModelTableInfo(@Param("suitId")String suitId);
	
	public String getGuidByPhysDBName(@Param("dbTableName")String dbTableName);
	
	/**
	 * 一键设置主键及检查错误
	 */
	public void oneButtonToSet();
}
