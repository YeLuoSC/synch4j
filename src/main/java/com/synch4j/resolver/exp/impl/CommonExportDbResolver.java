package com.synch4j.resolver.exp.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.po.SynchPO;
import com.synch4j.resolver.exp.IExportSynchPOResolver;
import com.synch4j.resolver.exp.dao.IExportResolverMapper;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.util.SynchToolUtil;

/**
 * @author XieGuanNan
 * @date 2015-8-15-下午1:13:52
 * 解析器的数据库形式实现，只读取synch_t_setting表的设置
 */
@Component
public class CommonExportDbResolver implements IExportSynchPOResolver {

	Logger logger = Logger.getLogger(CommonExportDbResolver.class);
	
	@Resource
	private IExportResolverMapper exportResolverMapper;
	
	@Override
	public List<SynchPO> resolve(ExportMode mode) {
		logger.info("CommonExportDbResolver解析器开始工作");
		//这个参数暂时没用，以后可以考虑通过模式的标识符去取同步对象，或者实现接口，按照一个参数取
		List<Map<String, Object>> list = exportResolverMapper.getSettingTableSynchObjectList("1");
		List<SynchPO> returnList = getDbSynchPOList(list);
		for(SynchPO synchPO : returnList){
			if(!StringUtils.isEmpty(synchPO.getFilterCol())){
				synchPO.setIsAlwaysExport("1");
			}
		}
		return returnList;
	}

	
	private List<SynchPO> getDbSynchPOList(List<Map<String, Object>> dataList){
			List<SynchPO> resultList = new ArrayList<SynchPO>();
			if (dataList == null) return resultList;
			Map<String, Object> dataMap = null;
			SynchPO synchPO = null;
			for (int i = 0; i < dataList.size(); i++) {
				dataMap = dataList.get(i);
				String tableName = (String)dataMap.get("PHYSDBNAME").toString().trim();
				synchPO = new SynchPO();
				synchPO.setPhysDBName(tableName);
				synchPO.setTableName((String)dataMap.get("TABLENAME"));
				synchPO.setIsBigDataCol(exportResolverMapper.getIsBigDataByPhysDbName(tableName));
				if(dataMap.get("FILTERCOL") != null){
					synchPO.setFilterCol(dataMap.get("FILTERCOL").toString());
				}
				synchPO.setSynchOrder(dataMap.get("SYNCHORDER") == null?0: Integer.valueOf(String.valueOf(dataMap.get("SYNCHORDER"))));
				if(dataMap.get("SYNCHCONDITION") != null && !"".equals(((String)dataMap.get("SYNCHCONDITION")).trim())){
					synchPO.setSynchCondition((String)dataMap.get("SYNCHCONDITION"));
				}
				if(dataMap.get("SYNCHCONDITION") == null){
					synchPO.setSynchCondition(" WHERE 1=1 ");
				}
				synchPO.setSynchRecogCol(dataMap.get("SYNCHRECOGCOL") == null?"":(String)dataMap.get("SYNCHRECOGCOL"));
				synchPO.setTableType(dataMap.get("TABLETYPE") == null?"":(String)dataMap.get("TABLETYPE"));
				synchPO.setRemark(dataMap.get("REMARK") == null?"":(String)dataMap.get("REMARK"));
				synchPO.setMaxRow(dataMap.get("MAXROW") == null?0: Integer.valueOf(String.valueOf(dataMap.get("MAXROW"))));
				synchPO.setIsAlwaysExport(dataMap.get("ISALWAYSEXPORT") == null?"":(String)dataMap.get("ISALWAYSEXPORT"));
				synchPO.setPkList(exportResolverMapper.getPkListByPhysDbName(tableName));
				resultList.add(synchPO);
			}
			return resultList;
	}
}
