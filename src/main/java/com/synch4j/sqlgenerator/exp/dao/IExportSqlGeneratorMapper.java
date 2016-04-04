package com.synch4j.sqlgenerator.exp.dao;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface IExportSqlGeneratorMapper extends SuperMapper{
	public void insertExportSQL(@Param("logId")String logId,@Param("sql")String sql,
			@Param("physDBName")String physDBName);

	public String getLastExpDate(@Param("expPhysDBName")String expPhysDBName);
}
