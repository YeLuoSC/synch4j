package com.synch4j.fascade;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import com.synch4j.exception.CallbackException;
import com.synch4j.exception.DataPickerException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.exception.ImportResolverException;
import com.synch4j.exception.NotSupportAppIdException;

/**
 * 同步开始的地方，外观模式的接口
 * @author XieGuanNan
 * @date 2015-9-7-下午6:58:30
 *
 */
public interface ISynchBegin {
	
	/**
	 * 单线程导入
	 * @param fileGuid TODO
	 * @throws CallbackException
	 * @throws IOException
	 * @throws AppException
	 * @throws ImportResolverException
	 * @throws ErrorConfigureException
	 * @throws SQLException
	 */
	public void SingleThreadImport(String fileGuid)throws 
			Exception;
	
	
	/**
	 * 单线程导入,通过流进行导入，而非通过fileGuid
	 * @param is
	 * @throws CallbackException
	 * @throws IOException
	 * @throws AppException
	 * @throws ImportResolverException
	 * @throws ErrorConfigureException
	 * @throws SQLException
	 */
	public void SingleThreadImport(InputStream is)throws 
	 Exception;
	
	/**
	 * SPF下发导出
	 * @param sourceProvince
	 * @param destProvince
	 * @param year
	 * @throws ErrorConfigureException
	 * @throws CallbackException
	 * @throws NotSupportAppIdException
	 * @throws DataPickerException
	 * @throws IOException
	 */
	public void CommonExport() throws ErrorConfigureException, CallbackException, NotSupportAppIdException,
			DataPickerException, IOException;
}
