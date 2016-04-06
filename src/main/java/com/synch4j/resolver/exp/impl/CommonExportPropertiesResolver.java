package com.synch4j.resolver.exp.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.synch4j.Synch2Context;
import com.synch4j.po.SynchPO;
import com.synch4j.resolver.exp.IExportSynchPOResolver;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;

/**
 * @author XieGuanNan
 * @date 2015-8-18-上午10:35:01
 * 该解析器通过读取synch2.properties中配置的SYSTEM.TABLE进行解析
 * 该组件只负责解析系统表，如果有额外的解析需求，实现接口，在配置文件中的该配置选项中，增加一个你新实现的类beanId
 */
@Component
public class CommonExportPropertiesResolver implements IExportSynchPOResolver{
	
Logger logger = Logger.getLogger(CommonExportPropertiesResolver.class);
	
	@Override
	public List<SynchPO> resolve(ExportMode mode, Synch2Context context) {
		logger.info("CommonExportPropertiesResolver解析器开始工作");
		String[] tables = SynchToolUtil.getValueFromProperties(SynchConstants.SYSTEM_TABLE).split(",");
		if(tables == null ){
			return new ArrayList<SynchPO>(1);
		}
		List<SynchPO> resultList = new ArrayList<SynchPO>();
		for (int i = 0; i < tables.length; i++) {
			SynchPO bean = getSynchPOBean(tables[i]);
			//对remote_procedure表导出时，不启用的远程脚本不进行同步
			if(bean.getPhysDBName().equalsIgnoreCase(SynchConstants.TABLE_REMOTEPROCEDURE_NAME)){
				bean.setSynchCondition(" WHERE AVAILABLE='1' ");
			}
			resultList.add(bean);
		}
		
		return resultList;
	}

	/**
	 * 根据表名转换Bean
	 * @param SynchPO
	 * @return
	 */
	private SynchPO getSynchPOBean(String tableInfo) {
		SynchPO resultBean = new SynchPO();
		String [] info = tableInfo.split("\\|");
		resultBean.setPhysDBName(info[0]);
		resultBean.setTableName("系统表");
		resultBean.setIsBigDataCol("0");
		String synchOrder = "";
		if(info[1].split("\\[")[1].contains("?")){
			synchOrder = info[1].split("\\[")[1].split("\\?")[0];
			if(info[1].split("\\[")[1].split("\\?")[1].contains(".")){
				String[] filterColArr = info[1].split("\\[")[1].split("\\?")[1].split("\\.");
				String filterColTotal = "";
				for(String filterCol : filterColArr){
					filterColTotal += filterCol + ",";
				}
				if(!StringUtils.isEmpty(filterColTotal)){
					filterColTotal = filterColTotal.substring(0, filterColTotal.length() - 1);
					resultBean.setFilterCol(filterColTotal);
					resultBean.setIsAlwaysExport("1");
				}
			}else{
				String filterCol = info[1].split("\\[")[1].split("\\?")[1];
				resultBean.setFilterCol(filterCol);
				resultBean.setIsAlwaysExport("1");
			}
		}else{
			synchOrder = info[1].split("\\[")[1];
		}
		resultBean.setSynchOrder(Integer.parseInt(synchOrder));
		resultBean.setSynchRecogCol("DBVERSION");
		resultBean.setTableType("0");
		resultBean.setMaxRow(Integer.parseInt(SynchToolUtil.getValueFromProperties(SynchConstants.EXPORT_DEFAULT_MAXROW_KEY)));
		resultBean.setRemark("默认导出，如需修改，检查synch2.properties中的SYSTEM.TABLE。");
		resultBean.setSynchCondition(" WHERE 1=1 ");
		for (String str : info[1].split("\\[")[0].split("\\*")) {
			//判断这些系统表的主键列中是否设置了Province和year，如果设置了这些字段，直接将其拼到where 条件中，加入分区筛选
			if(str.equalsIgnoreCase("PROVINCE")){
				String condition = resultBean.getSynchCondition();
				condition += SynchConstants.PROVINCE_CONDITION;
				resultBean.setSynchCondition(condition);
			}else if(str.equalsIgnoreCase("YEAR")){
				String condition = resultBean.getSynchCondition();
				condition += SynchConstants.YEAR_CONDITION;
				resultBean.setSynchCondition(condition);
			}
			resultBean.getPkList().add(str);
		}
		return resultBean;
	}
}
