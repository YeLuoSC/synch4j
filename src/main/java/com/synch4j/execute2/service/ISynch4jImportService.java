package com.synch4j.execute2.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.synch4j.po.SynchImpLogPO;

public interface ISynch4jImportService {
	
	public PageInfo getImportMainLog(PageInfo pageInfo);

	public List<SynchImpLogPO> getImportDetail(String logId);

	public void delBatch(List<String> list);
}
