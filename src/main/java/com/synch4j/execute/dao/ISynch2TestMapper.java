package com.synch4j.execute.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;

public interface ISynch2TestMapper extends SuperMapper{

	public List<Map<String, Object>> getData();

	public void reset(@Param("list")List<String> filelist);
}
