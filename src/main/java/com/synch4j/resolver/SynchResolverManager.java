package com.synch4j.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.synch4j.Synch2Context;
import com.synch4j.callback.CallbackManager;
import com.synch4j.exception.CallbackClassNotExistIoCException;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.exception.ImportResolverException;
import com.synch4j.exception.NotSupportAppIdException;
import com.synch4j.po.SynchPO;
import com.synch4j.resolver.exp.IExportSynchPOResolver;
import com.synch4j.resolver.imp.IImportResolver;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.synchenum.ImportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;

/**
 * 导入导出解析器的管理器，用于集中管理各个解析器，在其他类中，应通过调用该类
 * 导出解析器：通过某种途径获取同步对象
 * 导入解析器：通过输入流，解析数据，将数据转存中间表
 * 来获取解析的结果，不应直接调用解析器对象。
 * 该类暂时不作抽象，没有可扩展的必要暂时
 * @author XieGuanNan
 * @date 2015-8-15-下午12:32:36
 */
public class SynchResolverManager {

	private static Logger logger = Logger.getLogger(SynchResolverManager.class);
	/**
	 * 该方法需要调用的解析器途径和appId，根据各类途径解析后，返回同步对象集合
	 * @param context TODO
	 * @return
	 * @throws NotSupportAppIdException 
	 */
	public static List<SynchPO> getExportSynchList(ExportMode mode, Synch2Context context) throws ErrorConfigureException, NotSupportAppIdException{
		//确保该list不会为空，如果需要多个解析器，第一个返回空，第二个能顺利addAll
		String[] beanNames = getExportResolverBeanNamesByProperties(mode);
		List<SynchPO> returnList = getReturnList(beanNames,mode, context);
		return returnList;
	}
	
	/**
	 * 导入解析，取配置的解析器beanId，调用该解析器进行解析
	 * 解析这块，我觉得多线程和单线程暂时不需要区分，因为导入这块主要瓶颈在导入，而不是解析
	 * 返回同步对象，这个同步对象应该是在解析时获取到
	 * @param is
	 * @param logId
	 * @param context TODO
	 * @param destProvince
	 * @param year
	 * @throws ImportResolverException 
	 * @throws SQLException 
	 * @throws CallbackException 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 * @throws ErrorConfigureException 
	 * @throws CallbackClassNotExistIoCException 
	 */
	public static List<SynchPO> resolveFromInputStream(ImportMode mode,InputStream is, String logId, Synch2Context context) 
			throws UnsupportedEncodingException, IOException, CallbackException, SQLException, 
			ImportResolverException, CallbackClassNotExistIoCException, ErrorConfigureException{
		IImportResolver importResolver = (IImportResolver)CallbackManager.getBeansByPropertiesKey(SynchConstants.IMPORT_SINGLE_RESOLVER_KEY, true);
		List<SynchPO> list = importResolver.resolve(is, logId, context);
		return list;
	}
	
	/**
	 * 通过bean名称（首字母小写）和appId，调用解析器的解析方法
	 * @param context TODO
	 * @param beanName
	 * @return
	 */
	private static List<SynchPO> getReturnList(String[] beanIds,ExportMode mode, Synch2Context context){
		List list = CallbackManager.getBeansByBeanIds(beanIds);
		IExportSynchPOResolver resolver;
		List<SynchPO> returnList = new ArrayList<SynchPO>();
		for(Object obj : list){
			resolver = (IExportSynchPOResolver)obj;
			returnList.addAll(resolver.resolve(mode, context));
		}
		
		return returnList;
	}
	
	/**
	 * 通过导出模式获取该模式的配置文件中，所有resovler的配置，beanId
	 * @return
	 * @throws ErrorConfigureException 
	 * @throws NotSupportAppIdException 
	 */
	private static String[] getExportResolverBeanNamesByProperties(ExportMode mode) throws ErrorConfigureException, NotSupportAppIdException{
		String beanNames = null;
		String[] beanNamesArr = null;
		List<String> resolverKeys = new ArrayList<String>();
		switch(mode){
		default://全部用默认的，现在没有特殊需求，否则太麻烦了
			resolverKeys.add(SynchConstants.EXPORT_COMMON_DEFAULT_DB_RESOLVER);
			resolverKeys.add(SynchConstants.EXPORT_COMMON_DEFAULT_PROPERTIES_RESOLVER);
			break;
		}
		beanNames = getTotalBeanNames(resolverKeys);
		if(StringUtils.isEmpty(beanNames)){
			logger.error("未配置任何默认的Resolver值在synch2.properties！请检查");
			throw new ErrorConfigureException("未配置任何默认的Resolver值在synch2.properties！请检查");
		}
		beanNamesArr = beanNames.split(",");
		return beanNamesArr;
	}
	
	/**
	 * 将几类解析器的配置文件中的Key值传过来，取值后，将其拼接成一个beanId1,beanId2,...的字符串
	 * @param dbResolverKey
	 * @param propertiesResolverKey
	 * @param xmlResolverKey
	 * @return
	 */
	private static String getTotalBeanNames(List<String> resolverKeys){
		String totalBeanNames = "";
		for(String resolverKey : resolverKeys){
			String beanName = SynchToolUtil.getValueFromProperties(resolverKey);
			if(!StringUtils.isEmpty(beanName)){
				totalBeanNames += beanName;
			}
			//确保每次字符后都有一个,号
			if(!totalBeanNames.endsWith(",")){
				totalBeanNames += ",";
			}
		}
		//遍历后，将最后一个,截掉
		if(totalBeanNames.endsWith(",")){
			totalBeanNames = totalBeanNames.substring(0,totalBeanNames.length() - 1);
		}
		
		return totalBeanNames;
	}
}
