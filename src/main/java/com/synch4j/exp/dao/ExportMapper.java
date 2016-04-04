package com.synch4j.exp.dao;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface ExportMapper extends SuperMapper {
	
	public String getDbDate();
	
	public void truncateTempData();
	
	public void insertFileId(@Param("fileIdArr")String[] fileIdArr);
}
