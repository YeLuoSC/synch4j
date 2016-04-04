package com.synch4j.callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.synch4j.exception.CallbackClassNotExistIoCException;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.util.SpringContextHolder;
import com.synch4j.util.SynchClassUtil;
import com.synch4j.util.SynchToolUtil;

/**
 * @author XieGuanNan
 * @date 2015-8-27-上午10:16:54
 * 回调管理器，管理器负责回调
 *
 */
@Component
@Lazy(value=false)
public class CallbackManager {

	static Logger logger = Logger.getLogger(CallbackManager.class);
	
	private CallbackManager(){
		
	}
	/**
	 * 该map中保存的Key是接口名称，class为所有该接口下的所有实现类的Class对象
	 */
	private static Map<String,List<Class>> callBackClassMap = new HashMap<String,List<Class>>();
	
	/**
	 * 在系统启动时加载，扫描callback包下所有的接口，再通过接口获得其所有实现类的Class对象保存至该Map中；
	 */
	@PostConstruct
	public void initClassMap(){
		List<Class> interfaceList;
		try {
			interfaceList = SynchClassUtil.getSynchCallbackInterface();
			for(Class iClass : interfaceList){
				List<Class> implClass = SynchClassUtil.getAllClassByInterface(iClass);
				callBackClassMap.put(iClass.getSimpleName(), implClass);
				logger.info(iClass.getName()+"：该接口已经成功加载"+implClass.size()+"个实现类对象！");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("同步2.0初始化加载完成！");
	}
	
	/**
	 * @param interfaceName 接口的名称
	 * @param isSingle  该值意为当前这个接口，是否允许有多个实现类。如果为true，将直接返回这个bean，如果为false返回这些bean的List集合
	 * @return 返回Object对象，根据isSingle的设置由调用者自行强转为接口类型或List类型
	 * @throws CallbackException 
	 * @throws ClassNotFoundException
	 * 通过接口的字符串名称，获取该接口的所有实现类，目前返回List，没有进行强制转换，
	 * 需要由调用者强转。
	 */
	public static Object getBeansByInterfaceName(String interfaceName,boolean isSingle) throws CallbackClassNotExistIoCException, CallbackException{
		ApplicationContext ac = SpringContextHolder.getContext();
		List<Class> cls = callBackClassMap.get(interfaceName);
		if(cls != null){
			if(isSingle){
				if(cls.size() > 1){
					String errorInfo = "";
					for(Class clazz : cls){
						errorInfo += clazz.getName() + "|";
					}
					throw new CallbackException("接口：" + interfaceName + "只应有一个实现类！实际却有多个！分别如下：" + errorInfo + "请根据需要合并或者删除为一个！");
				}else if(cls.size() == 0){
					logger.info("未找到接口名称为：‘"+interfaceName+"’的实现类！没有可以回调的类！如果无需回调，忽略该信息！");
				}else if(cls.size() == 1){
					Class clz = cls.get(0);
					try{
						return ac.getBean(clz);
					}catch(NoSuchBeanDefinitionException e){
						logger.error("在Spring IoC中未找到接口为："+ interfaceName +"的实现类，请检查是否已经将该类加载至IoC容器中！" +
								"如@Component或者XML是否配置该bean！");
						throw new CallbackClassNotExistIoCException("在Spring IoC中未找到beanId为："+ interfaceName +"的实现类，请检查是否已经将该类加载至IoC容器中！" +
								"如@Component或者XML是否配置该bean！");
					}
				}
			}else{
					List results = new ArrayList();
					for(Class clazz : cls){
						try{
							Object obj = ac.getBean(clazz);
							results.add(obj);
						}catch(NoSuchBeanDefinitionException e){
							logger.error("在Spring IoC中未找到"+clazz.getName()+"类，请检查是否已经将该类加载至IoC容器中！" +
									"如@Component或者XML是否配置该bean！");
							throw new CallbackClassNotExistIoCException("在Spring IoC中未找到"+clazz.getName()+"类，请检查是否已经将该类加载至IoC容器中！" +
									"如@Component或者XML是否配置该bean！");
						}
						
					}
					return results;
				}
		}
		else{
			logger.info("未找到接口名称为：‘"+interfaceName+"’的实现类！没有可以回调的类！如果无需回调，忽略该信息！");
			return null;
		}
		return null;
	}
	
	/**
	 * @param properitesKey synch2.properties配置文件中的key名称
	 * @param isSingle  该值意为当前这个key下，是否允许配置多个beanId。如果为true，将直接返回这个bean，如果为false返回这些bean的List集合
	 * @return synch2.properties配置文件中，配置的beanId对应的bean！返回Object，需要自行强转为List或相应的bean类型
	 * @throws ClassNotFoundException
	 * 通过配置文件中key，获取该key下的所有beanId，目前返回List，没有进行强制转换，
	 * 需要由调用者强转。
	 * PS：本想在这里强制转换，直接返回传入的接口类型，但是不会写。
	 */
	public static Object getBeansByPropertiesKey(String properitesKey,boolean isSingle) throws CallbackClassNotExistIoCException,ErrorConfigureException{
		ApplicationContext ac = SpringContextHolder.getContext();
		String value = SynchToolUtil.getValueFromProperties(properitesKey);
		String[] arr = value.split(",");
		if(isSingle){
			//如果只允许配置一个beanId
			if(arr.length == 1){
				try{
					return ac.getBean(value);
				}catch(NoSuchBeanDefinitionException e){
					logger.error("在Spring IoC中未找到beanId为："+ value +"的类，请检查是否已经将该类加载至IoC容器中！" +
							"如@Component或者XML是否配置该bean！");
					throw new CallbackClassNotExistIoCException("在Spring IoC中未找到beanId为："+ value +"的类，请检查是否已经将该类加载至IoC容器中！" +
							"如@Component或者XML是否配置该bean！");
				}
			}else{
				logger.error("配置文件中" + properitesKey + "项配置了多个beanId!请检查是否正确！最多且至少应设置1个beanId！");
				throw new ErrorConfigureException("配置文件中" + properitesKey + "项配置了多个beanId!请检查是否正确！最多且至少应设置1个beanId！");
			}
		}else{
			List beanList = new ArrayList(arr.length);
			for(String beanId : arr){
				try{
					Object obj = ac.getBean(beanId);
					beanList.add(obj);
				}catch(NoSuchBeanDefinitionException e){
					logger.error("在Spring IoC中未找到beanId为："+ value +"的类，请检查是否已经将该类加载至IoC容器中！" +
							"如@Component或者XML是否配置该bean！");
					throw new CallbackClassNotExistIoCException("在Spring IoC中未找到beanId为："+ value +"的类，请检查是否已经将该类加载至IoC容器中！" +
							"如@Component或者XML是否配置该bean！");
				}
			}
			return beanList;
		}
	}
	
	public static List getBeansByBeanIds(String[] beanIds) throws CallbackClassNotExistIoCException{
		ApplicationContext ac = SpringContextHolder.getContext();
		List beanList = new ArrayList(beanIds.length);
		for(String beanId : beanIds){
			try{
				Object obj = ac.getBean(beanId);
				beanList.add(obj);
			}catch(NoSuchBeanDefinitionException e){
				logger.error("在Spring IoC中未找到beanId为："+ beanId +"的类，请检查是否已经将该类加载至IoC容器中！" +
						"如@Component或者XML是否配置该bean！");
				throw new CallbackClassNotExistIoCException("在Spring IoC中未找到beanId为："+ beanId +"的类，请检查是否已经将该类加载至IoC容器中！" +
						"如@Component或者XML是否配置该bean！");
			}
		}
		return beanList;
	}
}
