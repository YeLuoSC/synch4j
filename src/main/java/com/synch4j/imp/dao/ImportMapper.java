package com.synch4j.imp.dao;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface ImportMapper extends SuperMapper {
	
	public String getDbDate();
	
	public void truncateTempData();
}
