package com.synch4j.resolver.exp;

import java.util.List;

import com.synch4j.po.SynchPO;
import com.synch4j.synchenum.ExportMode;

/**
 * 该接口用于从某种资源（DB,配置文件,XML等）中，获取同步对象信息
 * 解析器应有多个实现，对应不同的获取路径
 * @author XieGuanNan
 * @date 2015-8-15-下午12:24:39
 */
public interface IExportSynchPOResolver {

	public List<SynchPO> resolve(ExportMode mode);
}
