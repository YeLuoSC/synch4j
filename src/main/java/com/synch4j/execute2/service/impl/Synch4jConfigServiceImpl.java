package com.synch4j.execute2.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.execute2.dao.Synch4jConfigMapper;
import com.synch4j.execute2.service.ISynch4jConfigService;
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

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public String saveSynchPO(SynchPO synchPO) {
		String result = null;
		if(!StringUtils.isEmpty(synchPO.getIsSynch())){
			if(synchPO.getIsSynch().equalsIgnoreCase("true")){
				//新增或更新
				if(synch4jMapper.getCountByTableName(synchPO.getPhysDBName()) > 0){
					synch4jMapper.updateSynchPO(synchPO);
					result = "更新成功";
				}else{
					synch4jMapper.insertSynchPO(synchPO);
					result = "创建成功";
				}
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public void delSynchPO(String physDBName) {
		synch4jMapper.delSynchPO(physDBName);
	}
}
