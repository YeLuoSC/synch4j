package com.synch4j.sqlgenerator.imp.dao;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface IImportSqlGeneratorMapper extends SuperMapper{
	public void insertImportSQL(@Param("logId")String logId,@Param("sql")String sql,
			@Param("physDBName")String physDBName);
}
