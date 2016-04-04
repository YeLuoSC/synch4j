package com.synch4j.execute.web;

import javax.annotation.Resource;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synch4j.execute.service.ISynch2FreeAssignService;
import com.synch4j.po.Synch2FreePatternPO;

@Controller
@RequestMapping(value = "/synch2/freeAssign")
public class Synch2FreeAssignController {
	
	@Resource
	private ISynch2FreeAssignService synch2FreeAssignServiceImpl;
	
	private static String RETURN_ROOT = "synch2/";
	
	@RequestMapping(value = "")
	public String forwardExpMain() {
		return RETURN_ROOT+"freeAssign";
	}

	@ResponseBody
	@RequestMapping(value="/getPatternTree")
	public Object getPatternTree(){
//		return synch2FreeAssignServiceImpl.getPatternTree();
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value="/getPatternById")
	public Object getPatternById(String patternId) throws Exception{
//		return synch2FreeAssignServiceImpl.getPatternById(patternId);
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value="/delPatternById")
	public Object delPatternById(String patternId) throws Exception{
//		synch2FreeAssignServiceImpl.delPatternById(patternId);
		return "1";
	}
	
	@ResponseBody
	@RequestMapping(value="/savePatternForm")
	public Object savePatternForm(String patternPO){
//		ObjectMapper mapper = new ObjectMapper();
//		Synch2FreePatternPO po  = mapper.readJson(patternPO, Synch2FreePatternPO.class);
//
//		System.out.println("ha");
		return null;
	}
}
