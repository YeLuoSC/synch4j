package com.synch4j.resolver.exp.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface IExportResolverMapper extends SuperMapper{

	/**
	 * 获取synch_t_setting表的设置
	 * @param direction
	 * @return
	 */
	public List<Map<String, Object>> getSettingTableSynchObjectList(@Param("direction")String direction);
	
	/**
	 * 根据物理表名查询该表是否有大数据列
	 * @param tableName
	 * @return
	 */
	public String getIsBigDataByPhysDbName(@Param("tableName")String tableName);
	
	public List<String> getPkListByPhysDbName(@Param("tableName")String tableName);
}
