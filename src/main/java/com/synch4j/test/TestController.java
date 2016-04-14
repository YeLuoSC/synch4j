package com.synch4j.test;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synch4j.Synch2Context;
import com.synch4j.exp.IBaseExportStrategy;
import com.synch4j.imp.IBaseImportStrategy;
import com.synch4j.remote.dao.Synch2PlanMapper;
import com.synch4j.util.SpringContextHolder;

@Controller
@RequestMapping(value="/synch2/test2")
public class TestController {
	
	Logger logger = Logger.getLogger(TestController.class);
	
	@Resource
	private Synch2Context context;
	
	@Resource
	private Synch2PlanMapper synch2PlanMapper;
	
	//@Resource
	//private ImportResolverMapper importMapper;
	
	//@Resource
	//private IExportResolverMapper exportMapper;
	
	@RequestMapping("")
	public String test(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("hahaa");
		return "synch2/test2";
	}
	
	/**
	 * 测试连接：http://127.0.0.1:8001/bgt/synch2/test.do?tokenid=
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/click")
	public void click(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
//		List list = SynchContext.getBeansByInterfaceName("ExportPostProcessor");
//		for(Object obj : list){
//			ExportPostProcessor callback = (ExportPostProcessor)obj;
//			try {
//				callback.postProcessBeforeExport();
//			} catch (CallbackException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		System.out.println(list.size());
//		System.out.println("you clicked");
		
		//测试规范导出：
//		IBaseExportStrategy exportStrategy = (IBaseExportStrategy)SpringContextHolder.getContext().getBean("standardExportStrategy");
//		context.setExportStrategy(exportStrategy);
//		context.export("15", "150100", "2016", null, null, "BGT");
		
		//测试oa-bgt导出
		IBaseExportStrategy exportStrategy = (IBaseExportStrategy)SpringContextHolder.getContext().getBean("oaExportStrategyBgt");
		context.setExportStrategy(exportStrategy);
		//该任务类型相关联的表：
//		context.export();
//		context.export();
		String fileGuid=synch2PlanMapper.getFileGuidByDocId("F7D80583Z81ABZ4F1DZ8146Z405524D7");
		System.out.println(fileGuid);
//		synch2PlanMapper.delFileGuidDocId("F7D80583Z81ABZ4F1DZ8146Z405524D7", fileGuid);
//		docAssignBgtServiceImpl.sendData("40E803CFZ8CCEZ48B3ZB445ZF3115F6F", "6E9E07AAD55A4E6CAE7FD05323C7E49E", "2016");
		
		//测试oa-bas导出
//		IBaseExportStrategy exportStrategy = (IBaseExportStrategy)SpringContextHolder.getContext().getBean("oaExportStrategyBas");
//		context.setExportStrategy(exportStrategy);
//		//该任务类型相关联的表：PHCZCLZJTJHZB，PHYBGGYSJZJYTJB
//		context.export("1500", "150100", "2016", "270FCDB4110B138EE050A8C021050310", "40E803CFZ8CCEZ48B3ZB445ZF3115F6F", "BAS");
//		String fileGuid=synch2PlanMapper.getFileGuidByDocId("F7D80583Z81ABZ4F1DZ8146Z405524D7");
//		System.out.println(fileGuid);
//		synch2PlanMapper.delFileGuidDocId("F7D80583Z81ABZ4F1DZ8146Z405524D7", fileGuid);
		
		//测试SPF导出
//		IBaseExportStrategy exportStrategy = (IBaseExportStrategy)SpringContextHolder.getContext().getBean("spfExportStrategy");
//		context.setExportStrategy(exportStrategy);
//		//该任务类型相关联的表：PHCZCLZJTJHZB，PHYBGGYSJZJYTJB
//		context.export("1500", "150100", "2016", null, null, "SPF");
		
//		//测试BGT上报导出
//		IBaseExportStrategy exportStrategy = (IBaseExportStrategy)SpringContextHolder.getContext().getBean("bgtReportExportStrategy");
//		context.setExportStrategy(exportStrategy);
//		//该任务类型相关联的表：
//		context.export("1500", "8700", "2016", "270FCDB4110B138EE050A8C021050310", "F7D80583Z81ABZ4F1DZ8146Z405524D7", "BGT");
		
		//测试BAS上报导出
//		IBaseExportStrategy exportStrategy = (IBaseExportStrategy)SpringContextHolder.getContext().getBean("basReportExportStrategy");
//		context.setExportStrategy(exportStrategy);
//		//该任务类型相关联的表：PHCZCLZJTJHZB，PHYBGGYSJZJYTJB
//		context.export("8700", "10", "2016", "270FCDB4110B138EE050A8C021050310", "F7D80583Z81ABZ4F1DZ8146Z405524D7", "BAS");

		
		//测试导入：
		IBaseImportStrategy importStrategy = (IBaseImportStrategy)SpringContextHolder.getContext().getBean("singleThreadImportStrategy");
		context.setImportStrategy(importStrategy);
		//context.import$(null, "87", "2016", "BGT");
//		try{
//			throw new Exception("出现错误异常");
//		}catch(Exception e){
//			logger.error("错误：",e);
//		}
		
	}
}
