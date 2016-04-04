package com.synch4j.datapicker.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface IDataPickerMapper extends SuperMapper{

	public List<Map<String, String>> getTableColumns(@Param("physDBName")String physDBName);
	
	public String getLastExpDate(@Param("expPhysDBName")String expPhysDBName);

	public void updateExportData(@Param("sql")String sql);
}
