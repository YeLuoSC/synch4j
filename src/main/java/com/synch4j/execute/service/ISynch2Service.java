package com.synch4j.execute.service;

import java.util.List;

import com.synch4j.po.SynchExpLogPO;
import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchMainLogPO;

public interface ISynch2Service {

	public List<SynchMainLogPO> getSynchMainLogList(String direction);

	public List<SynchExpLogPO> getExportDataLogList(String logId);

	public List<SynchImpLogPO> getImportDataLogList(String logId);

	public void deleteSynchMainLog(String logId);

	/*public Object getProvinceTree(UserDTO user, boolean isReport);*/
	
	public void reset();

	public String getDefaultProvince();
}
