package com.synch4j.execute2.web;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synch4j.execute2.service.ISynch4jProcedureService;
import com.synch4j.po.ProcedureDefinitionPO;

@Controller
@RequestMapping(value = "/procedure")
public class Synch4jProcedureController {
	
	private static String RETURN_ROOT = "/synch4jnew/";
	
	@Resource
	private ISynch4jProcedureService synch4jProcedureServiceImpl;
	
	@RequestMapping("")
	public String assignConfigMain(HttpServletRequest request,
			HttpServletResponse response) {
		return RETURN_ROOT+"procedure";
	}
	
	@ResponseBody
	@RequestMapping("getRemoteProcedure")
	public Object getRemoteProcedure(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,List> map = new HashMap<String, List>(); 
		map.put("result", synch4jProcedureServiceImpl.getRemoteProcedure());
		return map;
	}
	
	@ResponseBody
	@RequestMapping("delRemoteProcedure")
	public Object delRemoteProcedure(@RequestBody List<String> procedureIdList) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		synch4jProcedureServiceImpl.delProcedureById(procedureIdList);
		map.put("success", "删除成功");
		return map;
	}
	
	@ResponseBody
	@RequestMapping("saveRemoteProcedure")
	public Object saveRemoteProcedure(@RequestBody ProcedureDefinitionPO proPO) throws Exception {
		synch4jProcedureServiceImpl.saveProcedure(proPO);
		return "success";
	}
}
