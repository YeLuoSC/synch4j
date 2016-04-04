package com.synch4j.callback;

import com.synch4j.exception.CallbackException;

/**
 * @author XieGuanNan
 * @date 2015-8-24-下午3:48:28
 * 该接口用于导入解析时开始回调
 * 该接口应该只有一个实现，过多的实现回调时候，容易造成混乱。
 */
public interface ImportResolveProcessor {

	/**
	 * 解析业务数据后，将业务数据插入中间表synch_t_decrydata中，这张表中保存着业务表名称和要插入到该业务表的解密数据
	 * 如果有以下需求，如导出时，从A表导出，但是在导入时，你不想导入A表，
	 * 而是导入到其他表中（前提是这张表中至少有一部分列和A表相同，否则会导入不进去，但是也不会提示什么，因为后续导入时候，会剔除掉没有的列）
	 * 那么实现这个接口，返回修改后的表名，可以将解密数据等信息保存到中间表中。
	 * 如果不需要修改，将原先的physDBName返回
	 * @return 
	 * @throws CallbackException
	 */
	public String changePhysDBNameBeforeResolveData(String physDBName) throws CallbackException;
}
