package com.synch4j.callback.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;


public interface ICallbackMapper extends SuperMapper{
	
	
	/**
	 * 根据物理表名查询该表是否有大数据列
	 * @param tableName
	 * @return
	 */
	public String getIsBigDataByPhysDbName(@Param("tableName")String tableName);
	
	/**
	 * 创建临时表
	 * @param physDBName 临时表名称
	 * @param originalPhysDBName 原表名称
	 */
	public void createTempTable(@Param("physDBName")String physDBName,
			@Param("originalPhysDBName")String originalPhysDBName);

	/**
	 * 同步表数据
	 * @param physDBName 临时表名称
	 * @param originalPhysDBName 原表名称
	 */
	public void synchTableData(@Param("physDBName")String physDBName,
			@Param("originalPhysDBName")String originalPhysDBName);
	
	/**
	 * 删除临时表
	 * @param physDBName
	 */
	public void delTempTable(@Param("physDBName")String physDBName);
	
	
	/**
	 * 重新编译
	 */
	public void updateCompileInvalid();
}
