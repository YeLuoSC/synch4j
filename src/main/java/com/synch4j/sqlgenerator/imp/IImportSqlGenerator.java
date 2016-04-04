package com.synch4j.sqlgenerator.imp;

import java.util.List;
import java.util.Map;

import com.synch4j.exception.CallbackClassNotExistIoCException;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ImportMode;

/**
 * 
 * @author XieGuanNan
 * @date 2015-8-31-上午10:22:57
 * 导入SQL生成器，负责生成导入SQL语句
 */
public interface IImportSqlGenerator {
	public String getImportSQL(ImportMode mode,SynchPO synchPO,
			List<Map<String, String>> importColNameList,String[] colValues,String logId) throws CallbackClassNotExistIoCException, CallbackException ;
}
