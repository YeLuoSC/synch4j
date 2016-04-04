package com.synch4j.exp;

import java.io.IOException;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.exception.DataPickerException;
import com.synch4j.exception.ErrorConfigureException;
import com.synch4j.exception.NotSupportAppIdException;
/**
 * @author XieGuanNan
 * @date 2015-8-23-下午12:53:19
 * 导出策略算法的接口，传递上下文，在算法中通过获取上下文中的通用变量，获取需要的信息
 */
public interface IBaseExportStrategy {
	
	public void export(Synch2Context context) 
			throws CallbackException, NotSupportAppIdException,
			ErrorConfigureException, DataPickerException, IOException ;
}
