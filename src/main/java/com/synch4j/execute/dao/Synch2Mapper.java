package com.synch4j.execute.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;
import com.synch4j.po.SynchExpLogPO;
import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchMainLogPO;

public interface Synch2Mapper extends SuperMapper{

	public List<SynchMainLogPO> getSynchMainLog(@Param("direction")String direction);
	
	public List<SynchExpLogPO> getExportLog(@Param("logId")String logId);

	public void delMainLog(@Param("logId")String logId);
	
	public void delImpLog(@Param("logId")String logId);
	
	public void delExpLog(@Param("logId")String logId);

	public List<SynchImpLogPO> getImportLog(@Param("logId")String logId);
	
	public String getCodeByAgencyId(@Param("agencyId")String agencyId);

	public String getAgencyIdByCode(String code);

	public List<Map> getReportProvinceTreeByParentId(String provinceCode);

	public List<Map> getProvinceTreeByParentId(String agencyId);
	
	public void reset();
	
	public String getDefaultProvince();
}
