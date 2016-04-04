package com.synch4j.resolver.imp.dao;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface IImportResolverMapper extends SuperMapper{

	public void insertDecryptData(@Param("logId")String logId, @Param("physDBName")String physDBName, @Param("synchData")String synchData);

	public void insertBlobOrClob(@Param("logId")String logId, @Param("physDBName")String physDBName, @Param("colName")String colName, @Param("condition")String condition, @Param("guid")String guid);
}
