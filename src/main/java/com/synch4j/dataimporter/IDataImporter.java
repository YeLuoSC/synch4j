package com.synch4j.dataimporter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ImportMode;

/**
 * @author XieGuanNan
 * @date 2015-8-27-下午5:49:18
 * 数据导入器，职责：导入方调用，解析后导入数据。只写一个导入到db的实现，也许以后会有导入到excel的需求？
 */
public interface IDataImporter {
	
	public void importData(ImportMode mode,List<SynchPO> synchList,
			Synch2Context context) throws IOException,CallbackException ;
}
