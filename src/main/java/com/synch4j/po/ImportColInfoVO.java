package com.synch4j.po;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author XieGuanNan
 * @date 2015-8-27-下午6:33:02
 * 导入时，保存列信息及缺少列对应的数组角标，导入数据时，绕过该角标的数据
 */
public class ImportColInfoVO {
	/**
	 * ImportColNameList中包含Key:COLUMN_NAME和DATA_TYPE
	 */
	private List<Map<String, String>> importColNameList;
	private List<Integer> noDataIndex;
	public List<Map<String, String>> getImportColNameList() {
		return importColNameList;
	}
	public void setImportColNameList(List<Map<String, String>> importColNameList) {
		this.importColNameList = importColNameList;
	}
	public List<Integer> getNoDataIndex() {
		return noDataIndex;
	}
	public void setNoDataIndex(List<Integer> noDataIndex) {
		this.noDataIndex = noDataIndex;
	}
	
}
