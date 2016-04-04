package com.synch4j.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

public class SynchClassUtil {
	private static Logger logger = Logger.getLogger(SynchClassUtil.class);
	
	/**
	 * 目前暂时写死了，固定去当前接口所在包下的impl包下去寻找实现类，未来再做具体考虑
	* @Description: 根据一个接口返回该接口的所有类
	* @param c 接口
	* @return List<Class>    实现接口的所有类
	 */
	@SuppressWarnings("unchecked")
	public static List<Class> getAllClassByInterface(Class c){
		List returnClassList = new ArrayList<Class>();
		//判断是不是接口,不是接口不作处理
		if(c.isInterface()){
			String packageName = c.getPackage().getName() + ".impl";	//获得当前包名
			try {
				List<Class> allClass = getClasses(packageName);//获得当前包以及子包下的所有类
				
				//判断是否是一个接口
				for(int i = 0; i < allClass.size(); i++){
					if(c.isAssignableFrom(allClass.get(i))){
						if(!c.equals(allClass.get(i))){
							returnClassList.add(allClass.get(i));
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return returnClassList;
	}
	
	/**
	 * 
	* @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的
	* @param pageName 包名
	* @return List<Class>    包下所有类
	 */
	private static List<Class> getClasses(String packageName) throws ClassNotFoundException,IOException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		List<Class> classes = new ArrayList<Class>();
		String path = packageName.replace(".", "/");
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while(resources.hasMoreElements()){
			URL resource = resources.nextElement();
			
			if(resource.getPath().contains(".jar")){
				//weblogic的处理，不解压无法获取路径
				classes.addAll(processForJar(resource,path,false));
				//logger.info("jar包形式加载完成！");
			}else{
				//其他形式的处理，解压，可以通过文件形式获取
				//文件形式的可以迭代
				String newPath = resource.getFile().replace("%20", " ");
				dirs.add(new File(newPath));
				for(File directory:dirs){
					classes.addAll(findClass(directory, packageName));
				}
				//logger.info("文件形式加载完成！");
			}
		}
		return classes;
	}
	
	private static  List<Class> findClass(File directory, String packageName) 
		throws ClassNotFoundException{
		List<Class> classes = new ArrayList<Class>();
		if(!directory.exists()){
			return classes;
		}
		File[] files = directory.listFiles();
		for(File file:files){
			if(file.isDirectory()){
				assert !file.getName().contains(".");
				classes.addAll(findClass(file, packageName+"."+file.getName()));
			}else if(file.getName().endsWith(".class")){
				classes.add(Class.forName(packageName+"."+file.getName().substring(0,file.getName().length()-6)));
			}
		}
		return classes;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Class> getAllClassByAnnotation(Class annotationClass){
		List returnClassList = new ArrayList<Class>();
		//判断是不是注解
		if(annotationClass.isAnnotation()){
			String packageName = annotationClass.getPackage().getName();	//获得当前包名
			try {
				List<Class> allClass = getClasses(packageName);//获得当前包以及子包下的所有类
				
				//判断是否是一个接口
				for(int i = 0; i < allClass.size(); i++){
					if(allClass.get(i).isAnnotationPresent(annotationClass)){
						returnClassList.add(allClass.get(i));
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return returnClassList;
	}
	
	/**
	 * 通过该方法，获取callback包下所有接口类的class对象
	 * @return
	 */
	public static List<Class> getSynchCallbackInterface() throws Exception{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = SynchConstants.CALLBACK_PACKAGE.replace(".", "/");
		List<Class> classes = new ArrayList<Class>();
		Enumeration<URL> resources;
		resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while(resources.hasMoreElements()){
			URL resource = resources.nextElement();
			//System.out.println("路径："+resource.getPath());
			//System.out.println("协议："+resource.getProtocol());
			if(resource.getPath().contains(".jar")){
				//weblogic的处理:war包会被weblogic搞出一个war包来，要去war包中找到class文件
				classes.addAll(processForJar(resource,path,true));
			}else{
				//其他形式的处理，可以通过文件形式获取，weblogic非war包的形式走这里，其他中间件应该也走此处
				//只有weblogic的war包会有不同
				String newPath = resource.getFile().replace("%20", " ");
				dirs.add(new File(newPath));
				classes.addAll(processForFile(dirs));
			}
		}
		
		return classes;
	}
	
	private static List<Class> processForJar(URL resource,String packagePath,boolean needInterface){
		try{
			List<Class> classes = new ArrayList<Class>();
			String tempPath = resource.getPath();
			String jarPath =  tempPath.substring(0, tempPath.indexOf(".jar")) + ".jar";
			JarFile jf = new JarFile(jarPath);
			Enumeration<JarEntry> entries = jf.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement(); 
				String name = entry.getName(); 
				if (name.startsWith(packagePath) && name.endsWith(".class")) {
					String className = name.substring(0,name.length() - 6).replace("/", ".");
					Class cl = Class.forName(className);
					if(needInterface){
						if(cl.isInterface()){
							classes.add(cl);
						}else{
							logger.warn("初始化加载时，该"+cl.getName()+"类不是一个接口，跳过加载该对象！");
						}
					}else{
						if(!cl.isInterface()){
							classes.add(cl);
						}
					}
				}
			}
			return classes;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static List<Class> processForFile(List<File> dirs){
		List<Class> classes = new ArrayList<Class>();
		if(dirs != null){
			for(File dir:dirs){
				File[] dirfiles = dir.listFiles(new FileFilter(){
					@Override
					public boolean accept(File file) {
						return (file.getName().endsWith(".class"));
					}
				});
				for(File file : dirfiles){
					if(!file.isDirectory()){
						String className = file.getName().substring(0,file.getName().length() - 6);
						try {
							Class cl = Class.forName(SynchConstants.CALLBACK_PACKAGE + "." + className);
							if(cl.isInterface()){
								classes.add(cl);
							}else{
								logger.warn("初始化加载时，该"+cl.getName()+"对象不是一个接口，跳过加载该对象！");
							}
						} catch (ClassNotFoundException e) {
							logger.error("未找到接口的Class对象，接口名:"+className);
							e.printStackTrace();
						}
					}
				}
			}
		}
		return classes;
	}
}