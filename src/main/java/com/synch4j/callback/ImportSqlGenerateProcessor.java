package com.synch4j.callback;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ImportMode;

/**
 * @author XieGuanNan
 * @date 2015-8-31-上午10:39:46
 * 这个接口暂时不用实现，已经将导入器中的代码注释掉了，感觉这里不能提供这样的接口。导出是什么导入就应该是什么，为何不在导出时就搞好了？如果出现了问题，
 * 查起来都会非常繁琐。
 * 该接口将在生成导入Sql之前调用，给开发人员一个可以修改导入Sql的机会
 * 该接口回调于导入SQL生成器中
 */
public interface ImportSqlGenerateProcessor {

	/**
	 * @param synchPO
	 * @param importSql
	 * @param context TODO
	 * @return 修改后的SQL字符串
	 * @throws CallbackException
	 * 可以通过所给的参数对sql进行修改，返回修改后的SQL字符串
	 */
	public String changeImportSql(SynchPO synchPO,String importSql,Synch2Context context) throws CallbackException;
}
