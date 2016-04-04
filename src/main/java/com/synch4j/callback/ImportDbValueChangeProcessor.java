package com.synch4j.callback;

import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ImportMode;

/**
 * @author XieGuanNan
 * @date 2015-8-19-下午7:56:46
 * 该接口用于导入某张表时，改变导入数据库中的数据，实现该接口后，可以在自己的方法中判断表名、列名
 * 如果为需要的，将值改变后返回。该接口在数据库提取器中回调。如果不需要修改值，返回dbValue值
 * 这里只是非CLOB,BLOB数据
 */
public interface ImportDbValueChangeProcessor {

	public String changeDbValue(ImportMode mode,SynchPO synchPO,String dbValue,String colName) throws CallbackException;
}
