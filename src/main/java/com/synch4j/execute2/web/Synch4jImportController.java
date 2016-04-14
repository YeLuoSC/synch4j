package com.synch4j.execute2.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.synch4j.execute2.service.ISynch4jImportService;
import com.synch4j.po.SynchImpLogPO;

/**
 * 
 * <p>Title: Synch4jImportController</p>
 * <p>Description:  特意加了prototype，防止多线程问题；同时导出时上下文的成员变量会有线程问题</p>
 * @author xie
 * @date 2016-4-14 下午11:51:21
 */
@Controller
@RequestMapping(value = "/import")
@Scope("prototype")
public class Synch4jImportController {

	private static String RETURN_ROOT = "/synch4jnew/";
	
	@Resource
	private ISynch4jImportService synch4jImportServiceImpl;
	
	@RequestMapping("")
	public String forwardConfigMain() {
		return RETURN_ROOT+"import";
	}
	
	@RequestMapping("/getImportMainLog")
	@ResponseBody
	public Object getExportMainLog(@RequestBody PageInfo page){
		page = synch4jImportServiceImpl.getImportMainLog(page);
		return page;
	}
	
	@RequestMapping(value = "/getImportDetail")
	@ResponseBody
	public Object getExportDetail(@RequestBody String logId) throws Exception{
		List<SynchImpLogPO> list = synch4jImportServiceImpl.getImportDetail(logId);
		return list;
	}

	@RequestMapping(value = "/import")
	public void import$(HttpServletRequest request) throws Exception{
		
	}
}
