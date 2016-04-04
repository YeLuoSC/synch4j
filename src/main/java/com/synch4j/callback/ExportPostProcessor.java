package com.synch4j.callback;

import java.util.List;

import com.synch4j.Synch2Context;
import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;

/**
 * 任何导出模式导出前和导出后，都应该回调该接口的子接口，该接口为各个导出模式的父接口，子接口继承该接口，空接口即可，
 * 相应的实现类应该实现对应的子接口。
 * 在你的导出模式中，回调子接口，不要回调该父接口。该接口的存在意义，仅仅是约束子接口的方法
 * @author XieGuanNan
 * @date 2015-8-23-下午3:08:44
 */
public interface ExportPostProcessor {
	/**
	 * 导出前回调,该接口的主要目的是让开发者可以根据即将开始的同步对象，做出修改，可以根据业务
	 * 添加至list中一些新的同步对象，这样在后续导出时，会将业务新增的表格一起同步
	 * @param mode TODO
	 * @param list 同步对象，该集合中的对象，包含了即将被同步的表格信息
	 * @param logId TODO
	 * @param context TODO
	 */
	public void postProcessBeforeExport(ExportMode mode, List<SynchPO> list, String logId, Synch2Context context) throws CallbackException;
	
	/**
	 * 导出完成后回调
	 * @param mode TODO
	 * @param zipPath TODO
	 * @param logId TODO
	 * @param context TODO
	 */
	public void postProcessAfterExport(ExportMode mode,String zipPath, String logId, Synch2Context context) throws CallbackException;
}
