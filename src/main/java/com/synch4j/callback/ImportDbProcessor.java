package com.synch4j.callback;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ImportMode;

/**
 * 
 * @author XieGuanNan
 * @date 2015-9-1-上午10:22:46
 * 该接口回调于数据库导入器中。导入数据库时，几个时间点分别回调
 * 1.业务数据导入之前；
 * 2.业务数据导入完成，clob,blob大文本数据导入之前；
 * 3.该表所有数据导入完成（包括业务数据和大文本数据）。
 */
public interface ImportDbProcessor {
	public void processBeforeBusiData(ImportMode mode,SynchPO synchPO, Synch2Context context)
			throws CallbackException;

	public void processAfterBusiData(ImportMode mode,SynchPO synchPO, Synch2Context context)
			throws CallbackException;
	
	public void processAfterAllData(ImportMode mode,SynchPO synchPO, Synch2Context context)
			throws CallbackException;
}
