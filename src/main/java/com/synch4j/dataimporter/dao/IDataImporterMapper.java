package com.synch4j.dataimporter.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface IDataImporterMapper extends SuperMapper{
	
	public List<Map<String,String>> getTableData(@Param("logId")String logId,@Param("physDBName")String physDBName);

	public List<Map<String, String>> getTableColumns(@Param("physDBName")String physDBName);
	
	public void insertExportData(@Param("sql")String sql);
	
	public List<Map<String, String>> getNeedUpdateBlobOrClob(@Param("logId")String logId, @Param("physDBName")String physDBName,
			@Param("originalPhysDBName")String originalPhysDBName);
	
	public void callProcedure(@Param("content")String content);
}
