package com.synch4j.callback;

import com.synch4j.exception.CallbackException;

/**
 * 该接口用处为导入前及导入完成后，回调该接口的实现类
 * @author XieGuanNan
 * @date 2015-8-12-上午11:10:03
 */
public interface ImportPostProcessor {
	/**
	 * 导入前回调
	 */
	public void postProcessBeforeImport(String logId) throws CallbackException;
	
	/**
	 * 导入完成后回调
	 */
	public void postProcessAfterImport() throws CallbackException;
}
