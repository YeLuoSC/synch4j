package com.synch4j.execute.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synch4j.execute.service.ISynch2ProcedureService;
import com.synch4j.po.ProcedureDefinitionPO;

@Controller
@RequestMapping(value = "/procedureConfig")
public class Synch2ProcedureConfigController {
	
	@Resource
	private ISynch2ProcedureService synch2ProcedureService;
	
	private static String RETURN_ROOT = "/synch4j/";
	
	@RequestMapping("")
	public String assignConfigMain(HttpServletRequest request,
			HttpServletResponse response) {
		return RETURN_ROOT+"config/ProcedureConfig";
	}
	
	@ResponseBody
	@RequestMapping("getRemoteProcedure")
	public Object getRemoteProcedure(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return synch2ProcedureService.getRemoteProcedure();
	}
	
	@ResponseBody
	@RequestMapping("delRemoteProcedure")
	public Object delRemoteProcedure(String procedureIdList) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		List<String> modelList = (List<String>)(new ObjectMapper()).readValue(procedureIdList,List.class);
		synch2ProcedureService.delProcedureById(modelList);
		map.put("success", "删除成功");
		return map;
	}
	
	@ResponseBody
	@RequestMapping("saveRemoteProcedure")
	public Object saveRemoteProcedure(String objList) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		ObjectMapper mapper = new ObjectMapper();
		JavaType type = mapper.getTypeFactory().constructParametricType(ArrayList.class, ProcedureDefinitionPO.class);
		List<ProcedureDefinitionPO> list = (List<ProcedureDefinitionPO>)(new ObjectMapper()).readValue(objList,type);
		synch2ProcedureService.saveProcedure(list);
		map.put("success", "保存成功");
		return map;
	}

}
