package com.synch4j.execute.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.execute.dao.Synch2Mapper;
import com.synch4j.execute.service.ISynch2Service;
import com.synch4j.po.SynchExpLogPO;
import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchMainLogPO;

@Service
@Transactional(readOnly = true)
public class Synch2ServiceImpl implements ISynch2Service{

	@Resource
	private Synch2Mapper synch2Mapper;
	
	@Override
	public List<SynchMainLogPO> getSynchMainLogList(String direction) {
		return synch2Mapper.getSynchMainLog(direction);
	}

	@Override
	public List<SynchExpLogPO> getExportDataLogList(String logId) {
		return synch2Mapper.getExportLog(logId);
	}


	@Override
	public List<SynchImpLogPO> getImportDataLogList(String logId) {
		return synch2Mapper.getImportLog(logId);
	}

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void deleteSynchMainLog(String logId) {
		synch2Mapper.delMainLog(logId);
		synch2Mapper.delExpLog(logId);
		synch2Mapper.delImpLog(logId);
	}

	/*@Override
	public Object getProvinceTree(boolean isReport) {
		String provinceCode = user.getProvince();
		String agencyId = user.getAgency();
		String code = synch2Mapper.getCodeByAgencyId(agencyId);
		
		provinceCode = user.getUpagencyCode();
		agencyId = user.getUpagencyid();
		
		if(code != null && code.endsWith("00")){
			code = code.substring(0, code.length() - 2);
			System.out.println("当前用户为本级，提一级");
			agencyId = synch2Mapper.getAgencyIdByCode(code);
		}
		
		//测试，如果省份为空，默认设置为全国
		if(StringUtils.isEmpty(provinceCode)){
			provinceCode="87";
		}
		
		List<Map> tree;
		if(isReport){
			tree = synch2Mapper.getReportProvinceTreeByParentId(provinceCode);
		}else{
			tree = synch2Mapper.getProvinceTreeByParentId(agencyId);
		}
//		Map map = new HashMap();
//		map.put("GUID", "BC152516509A4E8DB9927872406B527B");
//		map.put("NAME", "甘肃省");
//		map.put("CODE", "62");
//		map.put("SUPERGUID","#");
//		tree = new ArrayList<Map>();
//		tree.add(map);
		return tree;
	}*/

	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void reset() {
		synch2Mapper.reset();
	}

	@Override
	public String getDefaultProvince() {
		return synch2Mapper.getDefaultProvince();
	}


}
