package com.synch4j.execute2.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;
import com.synch4j.po.SynchExpLogPO;

public interface Synch4jExportMapper extends SuperMapper{

	public List getExportMainLog();

	public List<SynchExpLogPO> getExportDetail(@Param("logId")String logId);

}
