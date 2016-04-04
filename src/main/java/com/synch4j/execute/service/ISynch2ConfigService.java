package com.synch4j.execute.service;

import java.util.List;
import java.util.Map;

import com.synch4j.po.SynchPO;

public interface ISynch2ConfigService {
	
	public List<Map> getAppSelect();
	
	public List<Map> getSuitTree(String appId);
	
	public List<Map> getTableInfo(String suitId);

	public void saveSynchPO(List<SynchPO> list, Map<String, String> map) throws Exception;
	
	/**
	 * 获取所有的系统表及定义信息
	 * @return
	 */
	public List<SynchPO> getSynchConfigList(String physDBName, String tableName);
	
	/**
	 * 获取表的所有列
	 * @param physDBName
	 * @return
	 */
	public List<String> getTableColumnList(String physDBName);
	
	/**
	 * 获取表同步列
	 * @param physDBName
	 * @return
	 */
	public List<String> getTableRecogColList(String physDBName);
	
	public void clearCache();
	
	/**
	 * 一键设置所有设置表的主键及filtercol
	 */
	public void oneButtonToSet();
}
