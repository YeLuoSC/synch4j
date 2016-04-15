package com.synch4j.execute2.web;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.synch4j.execute2.service.ISynch4jImportService;
import com.synch4j.fascade.ISynchBegin;
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
	
	@Resource
	private ISynchBegin synchBeginImpl;
	
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
	@ResponseBody
	public Object import$(HttpServletRequest request,MultipartFile fileName) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		if (!ServletFileUpload.isMultipartContent(request)) {
			map.put("success", "false");
			map.put("erroInfo", "不支持的导入协议！");
			return map;
		}

		if(fileName == null){
			map.put("success", "false");
			map.put("erroInfo", "系统没有解析到数据文件！");
			return map;
		}

		if (!"ZIP".equalsIgnoreCase(fileName.getOriginalFilename().substring(
				fileName.getOriginalFilename().length() - 3))) {
			map.put("success", "false");
			map.put("erroInfo", "系统无法解析的文件【" + fileName.getName() + "】");
			return map;
		}
		InputStream is = null;
		try{
			is = fileName.getInputStream();
			synchBeginImpl.SingleThreadImport(is);
			map.put("success", "true");
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value="/delBatch")
	@ResponseBody
	public Object delBatch(@RequestBody List<String> list){
		synch4jImportServiceImpl.delBatch(list);
		Map<String,String> map = new HashMap<String,String>();
		map.put("success", "删除成功");
		return map;
	}
}
