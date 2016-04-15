package com.synch4j.execute2.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.synch4j.execute2.service.ISynch4jExportService;
import com.synch4j.fascade.ISynchBegin;
import com.synch4j.po.SynchExpLogPO;

/**
 * <p>Title: Synch4jExportController</p>
 * <p>Description: 特意加了prototype，防止多线程问题；同时导出时上下文的成员变量会有线程问题</p>
 * @author xie
 * @date 2016-4-14 下午11:49:18
 */
@Controller
@RequestMapping(value = "/export")
@Scope("prototype")
public class Synch4jExportController {

	private static String RETURN_ROOT = "/synch4jnew/";
	
	@Resource
	private ISynch4jExportService synch4jExportServiceImpl;
	
	@Resource
	private ISynchBegin synchBeginImpl;
	
	@RequestMapping("")
	public String forwardConfigMain() {
		return RETURN_ROOT+"export";
	}
	
	@RequestMapping("/getExportMainLog")
	@ResponseBody
	public Object getExportMainLog(@RequestBody PageInfo page){
		page = synch4jExportServiceImpl.getExportMainLog(page);
		return page;
	}
	
	@RequestMapping(value = "/getExportDetail")
	@ResponseBody
	public Object getExportDetail(@RequestBody String logId) throws Exception{
		List<SynchExpLogPO> list = synch4jExportServiceImpl.getExportDetail(logId);
		return list;
	}
	
	@RequestMapping(value = "/download")
	@ResponseBody
	public void download(@RequestBody String logId,HttpServletResponse response) throws Exception{
		List<SynchExpLogPO> list = synch4jExportServiceImpl.getExportDetail(logId);
	}
	
	@RequestMapping(value = "/export")
	public void export(HttpServletResponse response) throws Exception{
		ServletOutputStream out = null;
		try{
			response.reset();
			response.setContentType("text/html; charset=GBK");
			// 设置下载头
			response.setHeader("Content-Disposition", "attachment; filename=\"export.zip\"");
			out = response.getOutputStream();
			synchBeginImpl.CommonExport(out);
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	@RequestMapping(value="/delBatch")
	@ResponseBody
	public Object delBatch(@RequestBody List<String> list){
		synch4jExportServiceImpl.delBatch(list);
		Map<String,String> map = new HashMap<String,String>();
		map.put("success", "删除成功");
		return map;
	}
}
