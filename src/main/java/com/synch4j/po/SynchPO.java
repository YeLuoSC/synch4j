package com.synch4j.po;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步对象
 * @author XieGuanNan
 * @date 2015-8-12-上午11:00:22
 */
public class SynchPO implements Comparable<SynchPO>{
	/**
	 * 显示名称
	 */
	private String tableName;
	
	/**
	 * 数据库物理名称
	 */
	private String physDBName;
	
	/**
	 * 原始数据库物理名称，导入时有可能会改变表名，所以需要一个属性记录，这张表在导出时是从哪张表导出的
	 */
	private String originalPhysDBName;
	
	/**
	 * 同步顺序
	 */
	private int synchOrder;
	
	/**
	 * 同步条件
	 */
	private String synchCondition;
	
	/**
	 * 是否大数据列BOLOB COLOB
	 */
	private String isBigDataCol;
	
	/**
	 * 主键集合
	 */
	private List<String> pkList;
	
	/**
	 * 同步完成后处理方法 spring Bean ID
	 */
	private String synchedHandler;
	
	/**
	 * 同步标识列 (时间戳)
	 */
	private String synchRecogCol;
	
	/**
	 * 表类型0系统表 1普通录入表 2附件表
	 */
	private String tableType;
	
	/**
	 * 导出最大记录数
	 */
	private int maxRow;
	
	/**
	 * 网络类型0内网  1外网
	 */
	private String netType;
	
	/**
	 * 导入或导出
	 */
	private String direction;
	
	/**
	 * 备注信息
	 */
	private String remark;
	
	/**
	 * 是否同步0否 1为是
	 */
	private String isSynch;
	
	/**
	 * 过滤列
	 * 如果当前设置表，需要根据tableId等值进行筛选，需要这里设置，格式：列名1=tableId，列名2=XXXX,现在只支持tableid
	 */
	private String filterCol;
	
	/**
	 * 当前表的tableId，前提是业务表。
	 */
	private String tableId;
	
	/**
	 * 是否永久下发（0为增量，1为永久导出）
	 */
	private String isAlwaysExport;
	
	/**
	 * 防止在创建同步对象后，没有设置主键的list，会有空指针错误。
	 */
	public SynchPO(){
		List<String> pkList = new ArrayList<String>();
		this.pkList = pkList;
	}

	/**
	 * 按照同步顺序排序
	 */
	@Override
	public int compareTo(SynchPO target) {
		return this.getSynchOrder() - target.getSynchOrder();
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPhysDBName() {
		return physDBName;
	}

	public void setPhysDBName(String physDBName) {
		this.physDBName = physDBName;
	}

	public int getSynchOrder() {
		return synchOrder;
	}

	public void setSynchOrder(int synchOrder) {
		this.synchOrder = synchOrder;
	}

	public String getSynchCondition() {
		return synchCondition;
	}

	public void setSynchCondition(String synchCondition) {
		this.synchCondition = synchCondition;
	}

	public String getIsBigDataCol() {
		return isBigDataCol;
	}

	public void setIsBigDataCol(String isBigDataCol) {
		this.isBigDataCol = isBigDataCol;
	}

	public List<String> getPkList() {
		return pkList;
	}

	public void setPkList(List<String> pkList) {
		this.pkList = pkList;
	}

	public String getSynchedHandler() {
		return synchedHandler;
	}

	public void setSynchedHandler(String synchedHandler) {
		this.synchedHandler = synchedHandler;
	}

	public String getSynchRecogCol() {
		return synchRecogCol;
	}

	public void setSynchRecogCol(String synchRecogCol) {
		this.synchRecogCol = synchRecogCol;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public int getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsSynch() {
		return isSynch;
	}

	public void setIsSynch(String isSynch) {
		this.isSynch = isSynch;
	}

	public String getFilterCol() {
		return filterCol;
	}

	public void setFilterCol(String filterCol) {
		this.filterCol = filterCol;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getOriginalPhysDBName() {
		return originalPhysDBName;
	}

	public void setOriginalPhysDBName(String orignalPhysDBName) {
		this.originalPhysDBName = orignalPhysDBName;
	}

	public String getIsAlwaysExport() {
		return isAlwaysExport;
	}

	public void setIsAlwaysExport(String isAlwaysExport) {
		this.isAlwaysExport = isAlwaysExport;
	}
	
	
}
