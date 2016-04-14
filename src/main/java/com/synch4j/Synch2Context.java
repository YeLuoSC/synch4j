package com.synch4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synch4j.exception.CallbackException;
import com.synch4j.exception.DataPickerException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.exception.NotSupportAppIdException;
import com.synch4j.exp.IBaseExportStrategy;
import com.synch4j.imp.IBaseImportStrategy;
import com.synch4j.util.SynchToolUtil;

/**
 * 同步上下文，
 * 关于两个注解：非延迟加载，因为需要在启动时，扫描开发人员的实现类
 * 范围：原型模式，原因是当有多个请求，这个类中又不得不使用了大量类级变量，如果按照默认单例，会产生严重的并发问题，所以必须是原型模式
 * 这里还需要后续验证一下
 * @author XieGuanNan
 * @date 2015-8-24-下午6:00:36
 * 
 * 非延迟加载已经删除了，这个功能已经被转移到CallbackManager类上了。由专门的类统一负责回调，而不是上下文
 * @author XieGuanNan
 * @date 2015-9-8-上午10:00:36
 */
@Service
@Scope("prototype")
public class Synch2Context {
	
	Logger logger = Logger.getLogger(Synch2Context.class);
	
	
	
	/**
	 * 导出的算法策略
	 */
	private IBaseExportStrategy exportStrategy;
	
	/**
	 * 导入的算法策略
	 */
	private IBaseImportStrategy importStrategy;
	
	/**
	 * 导出时，记录的日志id
	 */
	private String logId;
	
	/**
	 * 导入时，数据包guid
	 */
	private String fileGuid;
	
	/**
	 * 当通过文件包进行导入时，需要一个输入流；
	 */
	private InputStream is;
	
	/**
	 * 导出时若需要返回一个流，将外部传来的流设置到这里,如果不设置该属性，将压缩包导出至配置文件设置的路径中
	 */
	private OutputStream out;
	
	
	/**
	 * 导出时，调用该方法，需要确定导出调用哪个算法，可以通过导出策略工厂获取
	 * @throws IOException 
	 * @throws DataPickerException 
	 * @throws NotSupportAppIdException 
	 * @throws CallbackException 
	 * @throws Exception 
	 */
	@Transactional(readOnly=false,timeout=3600, rollbackFor=Exception.class)
	public void export(OutputStream out) throws ErrorConfigureException, CallbackException, NotSupportAppIdException, DataPickerException, IOException{
		if(out != null){
			this.out = out;
		}
		this.logId = SynchToolUtil.GUID();
		if(exportStrategy != null){
			exportStrategy.export(this);
		}else{
			logger.error("未设置导出策略！无法完成导出！请检查是否已经设置了导出策略！");
			throw new ErrorConfigureException("未设置导出策略！无法完成导出！请检查是否已经设置了导出策略！");
		}
	}
	
	/**
	 * 导出时，调用该方法，需要确定导出调用哪个算法，可以通过导出策略工厂获取,可以传入输入流，也可以传入文件guid，两种形式
	 * 定位到上传的压缩文件包。必选之一
	 * @param fileGuid 当通过fuds下载压缩包形式进行导入时，传入guid，当fileGuid为空时，is不允许为空，否则报错!
	 * @param is 当通过流的形式进行导入时，传入输入流，当is为空时，fileGuid不允许为空，否则报错!
	 * @param sourceProvince
	 * @param taskTypeId
	 * @throws DataPickerException 
	 * @throws NotSupportAppIdException 
	 * @throws Exception 
	 */
	@Transactional(readOnly=false,timeout=3600, noRollbackFor=Exception.class)
	public void import$(String fileGuid,InputStream is) throws Exception{
		this.logId = SynchToolUtil.GUID();
		if(!StringUtils.isEmpty(fileGuid)){
			this.fileGuid = fileGuid;
		}
		if(is != null){
			this.is = is;
		}
		if(is == null && StringUtils.isEmpty(fileGuid)){
			logger.error("未获取到文件fileGuid或输入流！通过页面上传文件包OR使用FUDS上传文件？");
			throw new ErrorConfigureException("未获取到文件fileGuid或输入流！通过页面上传文件包OR使用FUDS上传文件？");
		}
		if(importStrategy != null){
			importStrategy.import$(this);
		}else{
			logger.error("未设置导入策略！无法完成导入！请检查是否已经设置了导入策略！");
			throw new ErrorConfigureException("未设置导入策略！无法完成导入！请检查是否已经设置了导入策略！");
		}
	}

	public IBaseExportStrategy getExportStrategy() {
		return exportStrategy;
	}

	public void setExportStrategy(IBaseExportStrategy exportStrategy) {
		this.exportStrategy = exportStrategy;
	}

	
	public IBaseImportStrategy getImportStrategy() {
		return importStrategy;
	}

	public void setImportStrategy(IBaseImportStrategy importStrategy) {
		this.importStrategy = importStrategy;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getFileGuid() {
		return fileGuid;
	}

	public void setFileGuid(String fileGuid) {
		this.fileGuid = fileGuid;
	}
	
	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		this.out = out;
	}
	
	
}
