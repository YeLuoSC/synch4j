package com.synch4j.datapicker;

import java.util.Map;

import com.synch4j.Synch2Context;
import com.synch4j.callback.CallbackManager;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.DataPickerException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.util.SynchConstants;
import com.synch4j.zip.CZipOutputStream;
import org.apache.log4j.Logger;

/**
 * @author XieGuanNan
 * @date 2015-8-19-上午9:41:38
 * 类似解析器管理器，该类为数据提取器的管理器，用于集中管理数据提取器
 * 根据需要，返回给相应的提取器中返回的数据
 */
public class DataPickerManager {
	
	static Logger logger = Logger.getLogger(DataPickerManager.class);
	
	private DataPickerManager(){
		
	}
	/**
	 * 数据提取器只能传一个beanId，不能传多个，循环调用，问题是通过一个同步对象，应该只能获得一个该对象的数据
	 * @param mode 导出模式
	 * @param context  导出主日志id
	 * @param synchPO 同步对象
	 * @param zos 压缩流
	 * @return
	 * @throws DataPickerException
	 * @throws ErrorConfigureException
	 * @throws CallbackException 
	 */
	public static Map<String, String> getData(ExportMode mode,Synch2Context context,
			SynchPO synchPO, CZipOutputStream zos) throws DataPickerException, ErrorConfigureException, CallbackException{
		IDataPicker dataPicker = null;
		switch(mode){
		default ://提供一个默认的提取器，否则每次都要配置这个东西，太麻烦
			dataPicker = (IDataPicker)CallbackManager.getBeansByPropertiesKey(SynchConstants.EXPORT_DEFAULT_DB_DATAPICKER_KEY, true);
			if(dataPicker == null){
				logger.error("未配置默认的数据提取器项在synch2.properties中！");
				throw new ErrorConfigureException("未配置默认的数据提取器项在synch2.properties中！");
			}
			break;
		}
		Map<String, String> data = dataPicker.getData(mode,context, synchPO, zos);
		return data;
	}
	
}
