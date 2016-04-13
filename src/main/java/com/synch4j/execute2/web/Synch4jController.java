package com.synch4j.execute2.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.synch4j.execute2.service.ISynch4jConfigService;
import com.synch4j.po.SynchPO;

@Controller
@RequestMapping(value = "/config2")
public class Synch4jController {
	
	@Resource
	private ISynch4jConfigService synch4jConfigServiceImpl;

	private static String RETURN_ROOT = "/synch4jnew/";
	
	@RequestMapping("")
	public String forwardConfigMain() {
		return RETURN_ROOT+"tableConfig";
	}
	
	@RequestMapping(value = "/getSynchSettingList")
	@ResponseBody
	public Object getSynchConfigList(@RequestBody PageInfo page) { 
		page = synch4jConfigServiceImpl.getSynchConfigList(page);
		return page; 
	}
	
	@RequestMapping(value = "/saveSynchPO")
	@ResponseBody
	public Object saveSynchPO(@RequestBody SynchPO synchPO) throws Exception{
		String result = synch4jConfigServiceImpl.saveSynchPO(synchPO);
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", result);
		return map;
	}
	
	@RequestMapping(value = "/delSynchPO")
	@ResponseBody
	public Object delSynchPO(@RequestBody String tableName) throws Exception{
		synch4jConfigServiceImpl.delSynchPO(tableName);
		Map<String,String> map = new HashMap<String,String>();
		map.put("result", "删除成功");
		return map;
	}
}
