package com.synch4j.execute2.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.execute2.dao.Synch4jProcedureMapper;
import com.synch4j.execute2.service.ISynch4jProcedureService;
import com.synch4j.po.ProcedureDefinitionPO;
@Service
@Transactional(readOnly=true)
public class Synch4jProcedureServiceImpl implements ISynch4jProcedureService{

	@Resource
	private Synch4jProcedureMapper synch4jProcedureMapper;
	
	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public List<ProcedureDefinitionPO> getRemoteProcedure() throws Exception{
		return synch4jProcedureMapper.getRemoteProcedure();
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void delProcedureById(List<String> list) throws Exception {
		synch4jProcedureMapper.delProcedure(list);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void saveProcedure(ProcedureDefinitionPO proPO)
			throws Exception {
		if(!StringUtils.isEmpty(proPO.getGuid())){
			synch4jProcedureMapper.updateProcedure(proPO);
		}else{
			synch4jProcedureMapper.saveProcedure(proPO);
		}
	}
}
