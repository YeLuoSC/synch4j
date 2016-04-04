package com.synch4j.execute.web;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.synch4j.execute.service.ISynch2Service;
import com.synch4j.fascade.ISynchBegin;
import com.synch4j.po.SynchExpLogPO;
import com.synch4j.po.SynchImpLogPO;
import com.synch4j.po.SynchMainLogPO;

@Controller
@Scope("prototype")
@RequestMapping(value = "/execute")
public class Synch2Controller {
	
	Logger logger = Logger.getLogger(Synch2Controller.class);
	
	@Resource
	private ISynchBegin synchBeginImpl;
	
	@Resource
	private ISynch2Service synch2ServiceImpl;
	
	private static String RETURN_ROOT = "/synch4j/";

	@RequestMapping(value = "/expMain")
	public String forwardExpMain() {
		return RETURN_ROOT+"exe/ExportMain";
	}

	@RequestMapping(value = "/impMain")
	public String forwardImpMain() {
		return RETURN_ROOT+"exe/ImportMain";
	}
	
	@RequestMapping(value = "/getExportMainLogList")
	@ResponseBody
	public Object getExportMainLogList() {
		Map<String, List<SynchMainLogPO>> map = new HashMap<String, List<SynchMainLogPO>>();
		List<SynchMainLogPO> list = synch2ServiceImpl.getSynchMainLogList("O");
		map.put("result", list);
		return map;
	}

	@RequestMapping(value = "/getExportDetailLogMain")
	@ResponseBody
	public Object getExportDetailLogMain(String logId) {
		Map<String, List<SynchExpLogPO>> map = new HashMap<String, List<SynchExpLogPO>>();
		List<SynchExpLogPO> list = synch2ServiceImpl.getExportDataLogList(logId);
		map.put("result", list);
		return map;
	}

	@RequestMapping(value = "/getImportMainLogList")
	@ResponseBody
	public Object getImportMainLogList() {
		Map<String, List<SynchMainLogPO>> map = new HashMap<String, List<SynchMainLogPO>>();
		List<SynchMainLogPO> list = synch2ServiceImpl.getSynchMainLogList("I");
		map.put("result", list);
		return map;
	}

	@RequestMapping(value = "/getImportDetailLogList")
	@ResponseBody
	public Object getImportDetailLogList(String logId) throws Exception{
		List<SynchImpLogPO> list = synch2ServiceImpl.getImportDataLogList(logId);
		return list;
	}

	@RequestMapping(value = "/deleteExportMainLog")
	@ResponseBody
	public String deleteExportMainLog(String logId) {

		try {
			String[] logIds = logId.split(",");
			for (int i = 0; i < logIds.length; i++) {
				synch2ServiceImpl.deleteSynchMainLog(logIds[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "failure";
		}
		return "success";
	}

	@RequestMapping(value = "/deleteImportMainLog")
	@ResponseBody
	public String deleteImportMainLog(String logID) {
		try {
			String[] logIds = logID.split(",");
			for (int i = 0; i < logIds.length; i++) {
				synch2ServiceImpl.deleteSynchMainLog(logIds[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "failure";
		}
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getProvinceTree")
	public Object getProvinceTree() throws Exception{
//		UserDTO user = SecureUtil.getCurrentUser();	 	
//		return synch2ServiceImpl.getProvinceTree(user,false);
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getReportProvinceTree")
	public Object getReportProvinceTree() throws Exception{
//		UserDTO user = SecureUtil.getCurrentUser();	 	
//		return synch2ServiceImpl.getProvinceTree(user,true);
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/assign")
	public Object assign(HttpServletRequest request,
			HttpServletResponse response, String provinceList,String taskTypeId)
			throws Exception {
		boolean successFlag = true;
		Map<String,String> resultMap = new HashMap<String,String>();
		synchBeginImpl.CommonExport();
		if(successFlag){
			resultMap.put("success", "下发成功");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/import")
	@ResponseBody
	public Object executeImp(HttpServletRequest request,
			HttpServletResponse response,MultipartFile fileName) throws Exception {
		JSONObject resultObj = new JSONObject();
		if (!ServletFileUpload.isMultipartContent(request)) {
			resultObj.put("success", false);
			resultObj.put("erroInfo", "不支持的导入协议！");
			return resultObj;
		}

		if(fileName == null){
			resultObj.put("success", false);
			resultObj.put("erroInfo", "系统没有解析到数据文件！");
			return resultObj;
		}

		if (!"ZIP".equalsIgnoreCase(fileName.getOriginalFilename().substring(
				fileName.getOriginalFilename().length() - 3))) {
			resultObj.put("success", false);
			resultObj.put("erroInfo", "系统无法解析的文件【" + fileName.getName() + "】");
			return resultObj;
		}
		InputStream is = null;
		try{
			is = fileName.getInputStream();
			synchBeginImpl.SingleThreadImport(is);
			resultObj.put("success", true);
		}catch(Exception e){
			logger.info("数据导入失败:" + e.getMessage());
			e.printStackTrace();
		}
		return resultObj;
	}
	
	@ResponseBody
	@RequestMapping(value = "/reset")
	public String reset(){
		synch2ServiceImpl.reset();
		return "1";
	}
}
