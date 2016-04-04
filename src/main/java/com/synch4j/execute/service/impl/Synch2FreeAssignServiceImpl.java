package com.synch4j.execute.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.execute.dao.Synch2FreeAssignMapper;
import com.synch4j.execute.service.ISynch2FreeAssignService;
import com.synch4j.po.Synch2FreePatternPO;

@Service
@Transactional(readOnly = true)
public class Synch2FreeAssignServiceImpl implements ISynch2FreeAssignService{

	@Resource
	private Synch2FreeAssignMapper synch2FreeAssignMapper;
	
/*	@Override
	public List<TreePO> getPatternTree() {
		return synch2FreeAssignMapper.getPatternTree();
	}

	@Override
	public Synch2FreePatternPO getPatternById(String patternId) {
		return synch2FreeAssignMapper.getPatternById(patternId);
	}*/

	/*@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void delPatternById(String patternId) {
		synch2FreeAssignMapper.delPatternById(patternId);
	}

	@Override
	public void savePatternForm(Synch2FreePatternPO po) throws Exception{
		System.out.println("aa");
		
	}*/

}
