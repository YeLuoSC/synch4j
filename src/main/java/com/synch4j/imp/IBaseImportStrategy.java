package com.synch4j.imp;

import com.synch4j.Synch2Context;
/**
 * 导入策略的算法接口
 * @author XieGuanNan
 * @date 2015-8-23-下午12:54:20
 */
public interface IBaseImportStrategy {

	public void import$(Synch2Context context) throws Exception ;
}
