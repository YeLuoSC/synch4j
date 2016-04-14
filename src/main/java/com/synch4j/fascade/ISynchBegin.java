package com.synch4j.fascade;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	 * 标准模式导出,参数可以传值null，当为null时，压缩包会放在synch4j配置文件中设置的路径下
	 * 如果传流，将压缩流输出到该流中，比如你如果需要前台下载该压缩包，将servletoutputstream传进来
	 * @throws ErrorConfigureException
	 * @throws CallbackException
	 * @throws NotSupportAppIdException
	 * @throws DataPickerException
	 * @throws IOException
	 */
	public void CommonExport(OutputStream out) throws ErrorConfigureException, CallbackException, NotSupportAppIdException,
			DataPickerException, IOException;
}
