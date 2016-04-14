package com.synch4j.execute2.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.synch4j.po.SynchExpLogPO;

public interface ISynch4jExportService {

	public PageInfo getExportMainLog(PageInfo pageInfo);

	public List<SynchExpLogPO> getExportDetail(String logId);
}
