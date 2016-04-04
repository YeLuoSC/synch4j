package com.synch4j.execute.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.po.ProcedureDefinitionPO;
import com.synch4j.execute.dao.Synch2ProcedureMapper;
import com.synch4j.execute.service.ISynch2ProcedureService;
@Service
@Transactional(readOnly = true)
public class Synch2ProcedureServiceImpl implements ISynch2ProcedureService{
	
	@Resource
	private Synch2ProcedureMapper synch2ProcedureMapper;
	
	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public List<ProcedureDefinitionPO> getRemoteProcedure() throws Exception{
		return synch2ProcedureMapper.getRemoteProcedure();
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void delProcedureById(List<String> list) throws Exception {
		synch2ProcedureMapper.delProcedure(list);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void saveProcedure(List<ProcedureDefinitionPO> list)
			throws Exception {
		for(ProcedureDefinitionPO po : list){
			if(po.getGuid().equals("") || po.getGuid() == null){
				synch2ProcedureMapper.saveProcedure(po);
			}else{
				synch2ProcedureMapper.updateProcedure(po);
			}
			
		}
		
	}
	
	
}
