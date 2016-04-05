package com.synch4j.execute.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.execute.dao.Synch4jConfigMapper;
import com.synch4j.execute.service.ISynch4jConfigService;
import com.synch4j.po.SynchMainLogPO;
import com.synch4j.po.SynchPO;
import com.synch4j.util.SynchToolUtil;

@Service
@Transactional(readOnly=true)
public class Synch4jConfigServiceImpl implements ISynch4jConfigService{
	
	@Resource
	private Synch4jConfigMapper synch4jMapper;
	
	public List<SynchPO> getSynchConfigList(String physDBName, String tableName){
		if (physDBName == null || physDBName.length() == 0) {
			physDBName = null;
		} else {
			physDBName = physDBName.toUpperCase();
		}
		if (tableName == null || tableName.length() == 0) {
			tableName = null;
		}
		List<Map<String, Object>> dataList = synch4jMapper.getSynchSettingList(physDBName, tableName);
		return SynchToolUtil.convertSynchPOList(dataList);
	}
}
