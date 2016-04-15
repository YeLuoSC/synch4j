package com.synch4j.execute2.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.synch4j.execute2.dao.Synch4jExportMapper;
import com.synch4j.execute2.service.ISynch4jExportService;
import com.synch4j.po.SynchExpLogPO;

@Service
@Transactional(readOnly=true)
public class Synch4jExportServiceImpl implements ISynch4jExportService{

	@Resource
	private Synch4jExportMapper synch4jExportMapper;

	@Override
	public PageInfo getExportMainLog(PageInfo pageInfo) {
		PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
		PageInfo page = new PageInfo(synch4jExportMapper.getExportMainLog());
		return page;
	}

	@Override
	public List<SynchExpLogPO> getExportDetail(String logId) {
		
		return synch4jExportMapper.getExportDetail(logId);
	}

	@Override
	public void delBatch(List<String> list) {
		synch4jExportMapper.delBatch(list);
	}
	

}
