package com.synch4j.datapicker;

import java.util.Map;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackClassNotExistIoCException;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.DataPickerException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;
import com.synch4j.zip.CZipOutputStream;

/**
 * 数据提取器，职责为提取数据，做抽象的原因是数据源可能会有扩展，
 * 如也许未来会有excel，txt等文件中，通过某种形式进行提取数据
 * @author XieGuanNan
 * @date 2015-8-18-下午8:29:58
 */
public interface IDataPicker {
	/**
	 * 根据同步对象，源分区进行数据提取，提取后，通过logId记录导出日志
	 * @param synchPO
	 * @param zos
	 * @param logID
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getData(ExportMode mode,Synch2Context context,
			SynchPO synchPO, CZipOutputStream zos) throws DataPickerException, CallbackException,CallbackClassNotExistIoCException, ErrorConfigureException  ;
		
}
