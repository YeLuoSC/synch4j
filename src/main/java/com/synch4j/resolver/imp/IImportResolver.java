package com.synch4j.resolver.imp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.ImportResolverException;
import com.synch4j.po.SynchPO;

/**
 * 导入解析器接口
 * 导入解析器职责：通过压缩包，解析数据，将数据转存中间表,最后返回需要同步的对象集合，这个对象集合应该从流中解析出来
 * @author XieGuanNan
 * @date 2015-8-24-上午11:22:36
 */
public interface IImportResolver {
	/**
	 * @param is
	 * @param logId
	 * @param context TODO
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws CallbackException
	 * @throws SQLException
	 * @throws ImportResolverException
	 * @author XieGuanNan
	 * @date 2015-8-27
	 * 该方法通过流解析数据，最后返回排序好的List<SynchPO>按照synchOrder，synchPO已经实现了Comparable接口
	 */
	public List<SynchPO> resolve(InputStream is, String logId, Synch2Context context) 
			throws UnsupportedEncodingException, IOException, CallbackException, SQLException,ImportResolverException;
}
