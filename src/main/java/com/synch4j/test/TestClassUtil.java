package com.synch4j.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.synch4j.po.SynchMainLogPO;

public class TestClassUtil {

	private static Logger logger = Logger.getLogger(TestClassUtil.class);
	
	private static Map<String,List<Class>> callBackClassMap = new HashMap<String,List<Class>>();
	
	/**
	 * @param args
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
//		List<Class> list = ClassUtil.getAllClassByInterface(SignInterface.class);
//		for(Class c : list){
//			String name = c.getAnnotation(SignAnnotation.class).getClass().
//			System.out.println(name);
//			SignInterface sign = (SignInterface)c.newInstance();
//			System.out.println(sign.returnStr());
//		}
		
//		List<Class> list = ClassUtil.getAllClassByAnnotation(SignAnnotation.class);
//		for(Class c : list){
//			//SignInterface sign = (SignInterface)c.newInstance();
//			System.out.println(c.getSimpleName());
//			SignAnnotation annot = (SignAnnotation) c.getAnnotation(SignAnnotation.class);
//			System.out.println(annot.TypeName());
//			//System.out.println(sign.returnStr());
//		}
		
//		List<Class> interfaceClass = SynchClassUtil.getSynchCallbackInterface();
//		System.out.println(interfaceClass.size());
		
//		List<Class> interfaceList = SynchClassUtil.getSynchCallbackInterface();
//		for(Class iClass : interfaceList){
//			List<Class> implClass = SynchClassUtil.getAllClassByInterface(iClass);
//			callBackClassMap.put(iClass.getSimpleName(), implClass);
//			logger.info(iClass.getSimpleName()+"：该接口已经成功加载"+implClass.size()+"个实现类对象！");
//		}
//		
//		List<Class> implClass = callBackClassMap.get("ExportPostProcessor");
//		System.out.println(implClass.get(0));
		
//		String zipPath = SynchToolUtil.getValueFromProperties("export.zipPath");
//		File f = new File(zipPath);
//		if(!f.exists()){
//			f.mkdir();
//		}
//		System.out.println();
		SynchMainLogPO mainLog = new SynchMainLogPO();
		System.out.println(mainLog.getDirection());
	}

}
