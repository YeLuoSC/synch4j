package com.synch4j.execute.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.synch4j.common.dao.SuperMapper;
import com.synch4j.po.ProcedureDefinitionPO;

public interface Synch2ProcedureMapper extends SuperMapper{
	public List<ProcedureDefinitionPO> getRemoteProcedure();
	
	public void delProcedure(@Param("list")List<String> list);
	
	public void saveProcedure(ProcedureDefinitionPO po);
	
	public void updateProcedure(ProcedureDefinitionPO po);
}
