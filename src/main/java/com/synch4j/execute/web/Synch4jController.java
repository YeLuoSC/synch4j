package com.synch4j.execute.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synch4j.execute.service.ISynch4jConfigService;
import com.synch4j.po.SynchPO;

@Controller
@RequestMapping(value = "/config2")
public class Synch4jController {
	
	@Resource
	private ISynch4jConfigService synch4jConfigServiceImpl;

	private static String RETURN_ROOT = "/synch4j/";
	
	@RequestMapping("")
	public String forwardConfigMain() {
		return RETURN_ROOT+"tables";
	}
	
	@RequestMapping(value = "/getSynchSettingList")
	@ResponseBody
	public Object getSynchConfigList(String physDBName, String tableName) { 
		Map<String,List<SynchPO>> map = new HashMap<String, List<SynchPO>>(); 
		List<SynchPO> dataList = synch4jConfigServiceImpl.getSynchConfigList(physDBName, tableName);
		map.put("result", dataList);
		return map; 
	}
}
