package com.synch4j.execute.service;

import java.util.List;

import com.synch4j.po.ProcedureDefinitionPO;

public interface ISynch2ProcedureService {

	public List<ProcedureDefinitionPO> getRemoteProcedure() throws Exception ;
	
	public void delProcedureById(List<String> list) throws Exception;
	
	public void saveProcedure(List<ProcedureDefinitionPO> list) throws Exception;
}
