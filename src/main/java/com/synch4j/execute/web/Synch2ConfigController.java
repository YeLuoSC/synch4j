package com.synch4j.execute.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synch4j.execute.service.ISynch2ConfigService;
import com.synch4j.po.SynchPO;

@Controller
@RequestMapping(value = "/config")
public class Synch2ConfigController {
	
	Logger logger = Logger.getLogger(Synch2ConfigController.class);
	@Resource
	private ISynch2ConfigService synch2ConfigService;
	
	private static String RETURN_ROOT = "/synch4j/";
	
	@RequestMapping("")
	public String forwardConfigMain() {
		return RETURN_ROOT+"config/main";
	}
	
	@RequestMapping("synchConfigMain")
	public String synchConfigMain() {
		return RETURN_ROOT+"config/SynchConfigMain";
	}
	
	
	//前台做定义时 注意：把系统表放在最前面 序号为0 其它表序号从1开始
	@RequestMapping(value = "/getSynchConfigList") 
	@ResponseBody
	public Object getSynchConfigList(String physDBName, String tableName) { 
		Map<String,List<SynchPO>> map = new HashMap<String, List<SynchPO>>(); 
		List<SynchPO> dataList = synch2ConfigService.getSynchConfigList(physDBName, tableName);
		for(int i=0;i<dataList.size();i++){
			SynchPO synchPO = (SynchPO)dataList.get(i);
			List<String> recogColList = synch2ConfigService.getTableRecogColList(synchPO.getPhysDBName());
			for(int t=0;t<recogColList.size();t++){
				if(recogColList.get(t).equals("DBVERSION")){
					synchPO.setSynchRecogCol(recogColList.get(t));   
				}
			}
		} 
		map.put("result", dataList);
		return map; 
	}
	
	@RequestMapping(value = "/getTableColumnList")
	@ResponseBody
	public Object getTableColumnList(String physDBName) {
		Map<String,List<Map<String, Object>>> map = new HashMap<String,List<Map<String, Object>>>(); 
		List<String> columnList = synch2ConfigService.getTableColumnList(physDBName);
		List<Map<String, Object>> columnMapList = new ArrayList<Map<String, Object>>();
		Iterator<String> iter = columnList.iterator();
		while(iter.hasNext()){
			String columnname = (String)iter.next();
			Map<String, Object> columnMap = new HashMap<String, Object>(); 
			columnMap.put("columnName", columnname);
			columnMap.put("isKey", 0);  
			columnMapList.add(columnMap); 
		}
		map.put("result", columnMapList); 
		return map;  
	}
	
	@RequestMapping(value = "/getSynchRecogCol")
	@ResponseBody
	public Object getSynchRecogCol(String physDBName) {
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		List<String> columnList = synch2ConfigService.getTableRecogColList(physDBName);
		List<Map<String, Object>> columnMapList = new ArrayList<Map<String,Object>>();
		Iterator<String> iter = columnList.iterator();
		while(iter.hasNext()){
			String columnname = iter.next();
			Map<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("columnName", columnname);
			columnMapList.add(columnMap);  
		}
		map.put("result", columnMapList); 
		return map;   
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveSynchPO") 
	@ResponseBody
	public String saveSynchPO(@RequestParam(value = "datas", required=false) String datas,String map) {
		try{
			JSONArray jsonArray = JSONArray.fromObject(datas); 
			List<Object> dataList = (List<Object>)jsonArray; 
			List<SynchPO> synchList = new ArrayList<SynchPO>();
			for(int i=0;i<dataList.size();i++){
				JSONObject object = (JSONObject) dataList.get(i);
				SynchPO synchPO = (SynchPO)JSONObject.toBean(object, SynchPO.class);
				synchList.add(synchPO);
			}
			JSONObject mapObject = JSONObject.fromObject(map); 
			synch2ConfigService.saveSynchPO(synchList, (Map<String, String>)JSONObject.toBean(mapObject, Map.class));  
		}catch(Exception e){
			e.printStackTrace();
			return "'failure'";
		}
		return "'success'";
	}
	
	@RequestMapping(value = "/clearCache")
	public void clearCache() {
		synch2ConfigService.clearCache();
	}
	
	@RequestMapping(value = "/oneButtonToSet")
	public void oneButtonToSet() {
		synch2ConfigService.oneButtonToSet();
	}
}
