package com.synch4j.imp;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.imp.dao.ImportMapper;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ImportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.util.SynchToolUtil;

/**
 * 策略算法的分离，该类是导入算法策略的一个模板，提供一套默认实现，
 * 根据算法的不同，通过继承该类，获得默认实现，并在此基础上，进行扩展
 * 如果算法变动很大，也可直接实现IBaseImportStrategy接口
 * @author XieGuanNan
 * @date 2015-8-23-上午12:52:13
 */
public abstract class AbsImportStrategy implements IBaseImportStrategy{
	
	protected Logger logger = Logger.getLogger(AbsImportStrategy.class);
	
	protected Synch2Context context;
	
	protected String importFileName;
	
	protected long startTime;
	
	protected String zipPath;

	protected InputStream is = null;
	
	protected List<SynchPO> synchList = null;
	
	protected String fileName;
	
	protected ImportMode mode;
	
	@Resource
	protected ImportMapper importMapper;
	
	public void import$(Synch2Context context) 
			throws Exception{
		
		this.context = context;
		
		mode = setImportMode();
		//导入前，一些处理工作；
		prepareImport(context);
		
		//开始导入
		startImport();
		
		//导入完成后的一些处理；
		endImport();
		
		//导入完成后，回调；
		endImportCallback();
	}

	/**
	 * @throws CallbackException
	 * 导入前，最重要的一件事情就是切分区了，这是所有导入到数据库中必须做的，写到抽象类中，子类不用考虑这件事情了
	 */
	public final void prepareImport(Synch2Context synchContext) throws CallbackException{
		startTime = System.currentTimeMillis();		
		//导入前调用，实现类的接口，如果子类算法不需要回调，可以重写该方法,空实现；回调子类中规定的相应名称的接口
		prepareImportCallback();
		
		if(SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY) != null && 
				SynchToolUtil.getValueFromProperties(SynchConstants.DEVELOPE_MODE_KEY).equalsIgnoreCase("true")){
			logger.warn("当前为开发模式，只会记录导入语句，不会导入任何数据！" +
					"如需修改，在synch2.properties中进行修改!另外，开发模式下需要手动清理p#synch_t_importsql," +
					"p#synch_t_decryptdata,p#synch_t_blobclob表");
		}
		
//		Synch2PartitionSwitch.setSynch2PartitionInfo(null);
	}
	
	public abstract void prepareImportCallback() throws CallbackException ;
	
	public abstract void startImport() throws Exception;

	public abstract void endImportCallback() throws CallbackException ;

	public abstract void endImport();

	public abstract ImportMode setImportMode();
	
}
