package com.synch4j.execute2.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;
import com.synch4j.po.SynchImpLogPO;

public interface Synch4jImportMapper extends SuperMapper{

	public List getImportMainLog();

	public List<SynchImpLogPO> getImportDetail(@Param("logId")String logId);
	
	public void delBatch(@Param("list")List<String> list);
}
