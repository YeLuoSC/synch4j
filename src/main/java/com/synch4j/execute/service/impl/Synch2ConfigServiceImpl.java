package com.synch4j.execute.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.execute.dao.Synch2ConfigMapper;
import com.synch4j.execute.service.ISynch2ConfigService;
import com.synch4j.po.SynchPO;
import com.synch4j.util.SynchToolUtil;

@Service
@Transactional(readOnly = true)
public class Synch2ConfigServiceImpl implements ISynch2ConfigService{

	private Map<String, List<String>> tableColumnCache = new HashMap<String, List<String>>();
	
	@Resource
	private Synch2ConfigMapper synch2ConfigMapper;
	
	
	@Override
	public List<Map> getAppSelect() {
		return synch2ConfigMapper.getAppSelect();
	}

	@Override
	public List<Map> getSuitTree(String appId) {
		return synch2ConfigMapper.getSuitTree(appId);
	}

	@Override
	public List<Map> getTableInfo(String suitId) {
		return synch2ConfigMapper.getModelTableInfo(suitId);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void saveSynchPO(List<SynchPO> list, Map<String, String> map) throws Exception {
		//I新增  M修改 D删除
		for (SynchPO synchPO : list) {
			if (!map.containsKey(synchPO.getPhysDBName())) continue;
			if ("I".equals(map.get(synchPO.getPhysDBName()))) {
				//该表必须在同步记录中没有记录才可以同步，否则不插入同步的表格，否则导出报错
				String guid = synch2ConfigMapper.getGuidByPhysDBName(synchPO.getPhysDBName());
				if(guid == null){
					synch2ConfigMapper.insertSynchPO(SynchToolUtil.getSynchPOMap(synchPO));
				}
			} else if ("M".equals(map.get(synchPO.getPhysDBName()))) {
				synch2ConfigMapper.updateSynchPO(SynchToolUtil.getSynchPOMap(synchPO));
			} else if ("D".equals(map.get(synchPO.getPhysDBName()))) {
				synch2ConfigMapper.deleteSynchPO(synchPO.getPhysDBName());
			}
		}
	}

	@Override
	public List<SynchPO> getSynchConfigList(String physDBName, String tableName) {
		if (physDBName == null || physDBName.length() == 0) {
			physDBName = null;
		} else {
			physDBName = physDBName.toUpperCase();
		}
		if (tableName == null || tableName.length() == 0) {
			tableName = null;
		}
		List<Map<String, Object>> dataList = synch2ConfigMapper.getSynchSettingList(physDBName, tableName);

		return SynchToolUtil.convertSynchPOList(dataList);
	}

	@Override
	public List<String> getTableColumnList(String physDBName) {
		String end = "_ALL";
		if (!tableColumnCache.containsKey(physDBName+end)) {
			//列信息变化不大 放入缓存
			tableColumnCache.put(physDBName+end, synch2ConfigMapper.getTableColumnList(physDBName));
		}
		return tableColumnCache.get(physDBName+end);
	}

	@Override
	public List<String> getTableRecogColList(String physDBName) {
		
		return synch2ConfigMapper.getTableRecogColList(physDBName);
	}

	@Override
	public void clearCache() {
		tableColumnCache = new HashMap<String, List<String>>();
		
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void oneButtonToSet() {
		synch2ConfigMapper.oneButtonToSet();
	}
	
}
