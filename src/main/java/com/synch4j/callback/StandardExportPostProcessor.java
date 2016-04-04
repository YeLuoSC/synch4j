package com.synch4j.callback;

import java.util.List;

import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;

/**
 * 该接口用于标准导出模式的导出前及导出完成后的回调，回调所有该接口的实现类
 * 程序中如果可能抛出异常，请抛出CallbackException类型的异常
 * @author XieGuanNan
 * @date 2015-8-12-上午11:11:14
 */
public interface StandardExportPostProcessor extends ExportPostProcessor{
	
}
