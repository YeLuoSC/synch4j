package com.synch4j.execute2.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.synch4j.execute2.dao.Synch4jImportMapper;
import com.synch4j.execute2.service.ISynch4jImportService;
import com.synch4j.po.SynchImpLogPO;

@Service
@Transactional(readOnly=true)
public class Synch4jImportServiceImpl implements ISynch4jImportService{

	@Resource
	private Synch4jImportMapper synch4jImportMapper;
	
	@Override
	public PageInfo getImportMainLog(PageInfo pageInfo) {
		PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
		PageInfo page = new PageInfo(synch4jImportMapper.getImportMainLog());
		return page;
	}

	@Override
	public List<SynchImpLogPO> getImportDetail(String logId) {
		return synch4jImportMapper.getImportDetail(logId);
	}

	@Override
	public void delBatch(List<String> list) {
		synch4jImportMapper.delBatch(list);
	}

}
