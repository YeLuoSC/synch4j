package com.synch4j.test;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

public class TestOther {
	
	static Logger logger = Logger.getLogger(TestOther.class);
	
	public static void main(String[] args) throws Exception{
//		String a = new String("123");
//		test1(a);
//		System.out.println(a);
//		
//		String c = "123";
//		String d = "123";
//		//test1(c);
//		System.out.println(c==d);
		
//		String tableId = "123,456,789,";
//		tableId = tableId.substring(0,tableId.length() - 1);
//		System.out.println(tableId);
		
//		String str = "P#SYNCH_T_REMOTEPROCEDURE,GUID#PROVINCE#YEAR,";
//		String[] arr = str.split(",");
//		System.out.println(arr.length);
		
//		String str = "SynchInfo.txt";
//		System.out.println(str.indexOf("/"));
		
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("123", "123");
//		//map.remove("123");
//		System.out.println(map.get("123"));
		
//		String a = "123";
//		String b = "";
//		b = a;
//		a = "456";
//		System.out.println(b);
		
//		String tables ="P#SYNCH_T_REMOTEPROCEDURE|GUID*PROVINCE*YEAR@0,P#DICT_T_MODEL|APPID*TABLEID*PROVINCE*YEAR@1,P#DICT_T_FACTOR|TABLEID*COLUMNID*PROVINCE*YEAR@2,P#DICT_T_MODELCODE|APPID*TABLEID*PROVINCE*YEAR@3,P#DICT_T_FACTORCODE|TABLEID*COLUMNID*PROVINCE*YEAR@4,P#SYNCH_T_SETTING|GUID*DIRECTION*PROVINCE*YEAR@5,P#DICT_T_SUIT|PROVINCE*YEAR*APPID*SUITID@5,DICT_T_DEFAULTCOL|DEALID*GUID@5,P#CDT_T_TASKINFO|TASKID@5,P#CDT_T_TASKTYPE|TASKTYPEID@5,P#CDT_T_TASKTYPEMODEL|GUID@5";
//		String tableInfo = tables.split(",")[0];
//		String [] info = tableInfo.split("\\|");
//		String order = info[1].split("@")[1];
//		
//		String[] arr = info[1].split("@")[0].split("\\*");
//		for(String str : arr){
//			System.out.println(str);
//		}
		
//		String test = "6200";
//		test = test.substring(0,2);
//		System.out.println(test);
		
//		String tempPath = "D:/Oracle/Middleware/user_projects/domains/exp_domain/servers/AdminServer/tmp/_WL_user/_appsdir_bgt_war/4btz9w/war/WEB-INF/lib/_wl_cls_gen.jar!/com/tjhq/synch2/callback";
//		String jarPath =  tempPath.substring(0, tempPath.indexOf(".jar")) + ".jar";
//		JarFile jf = new JarFile(jarPath);
//		System.out.println(jf.getName());
//		String path = SynchConstants.CALLBACK_PACKAGE;
//		path = path.replace(".", "/");
//		Enumeration<JarEntry> entries = jf.entries();
//		while (entries.hasMoreElements()) {
//			JarEntry entry = entries.nextElement(); 
//			String name = entry.getName(); 
//			if (name.startsWith(path) && name.endsWith(".class")) {
//				String className = name.substring(0,name.length() - 6).replace("/", ".");
//				Class cl = Class.forName(className);
//				if(cl.isInterface()){
//					System.out.println(className);
//				}
//				
//			}
//		}
		Collection coll;
		Map map;
		try{
			throw new Exception("xxxxx:");
		}catch(Exception e){
			logger.error("错误", e);
		}
	}
	
	public static void test1(String b){
		b = "bbb";
	}
}
