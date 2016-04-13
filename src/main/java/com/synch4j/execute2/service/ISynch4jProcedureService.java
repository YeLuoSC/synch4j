package com.synch4j.execute2.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.synch4j.po.ProcedureDefinitionPO;

public interface ISynch4jProcedureService {
	
	public PageInfo getRemoteProcedure(PageInfo pageInfo) throws Exception ;
	
	public void delProcedureById(List<String> list) throws Exception;
	
	public void saveProcedure(ProcedureDefinitionPO proPO) throws Exception;
}
