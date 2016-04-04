package com.synch4j.callback;

import com.synch4j.exception.CallbackException;
import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;

/**
 * 该接口用于判断给出的表名是否需要永久导出，只允许有一个实现类，否则会出现混乱，多个实现时，运行会抛出异常
 * @author XieGuanNan
 * @date 2015-9-7-上午10:59:56
 *
 */
public interface ExportDecideIncreaseTableProcessor {
	/**
	 * 用于判断传来的表，是否需要永久下发，如果需要永久下发，返回true，否则返回false，另外如果该物理表和
	 * 你需要对比的不匹配，必须返回false!
	 * @param mode
	 * @param synchPO
	 * @return
	 * @throws CallbackException
	 */
	public boolean needIncreaseSynch(ExportMode mode,SynchPO synchPO) throws CallbackException;
}
