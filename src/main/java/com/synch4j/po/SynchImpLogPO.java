package com.synch4j.po;

import com.sun.xml.internal.rngom.ast.builder.Include;

/**
 * 同步导入日志PO
 * @author XieGuanNan
 * @date 2015-8-11-下午4:10:17
 */
public class SynchImpLogPO extends SynchLogPO{
	/**
	 * 导出文件名
	 */
	private String expFileName;
	/**
	 * 导出数据条数
	 */
	private String expDatas;
	/**
	 * 对应数据库表名
	 */
	private String expPhysDBName;
	/**
	 * 导入使用时间
	 */
	private String impUsedDate;
	
	/**
	 * 插入数据条数
	 */
	private int insertDatas;
	
	/**
	 * 更新数据条数
	 */
	private int updateDatas;
	
	/**
	 * 失败数据条数
	 */
	private int failDatas;
	
	/**
	 * 备注
	 */
	private String remark;

	public String getExpFileName() {
		return expFileName;
	}

	public void setExpFileName(String expFileName) {
		this.expFileName = expFileName;
	}

	public String getExpDatas() {
		return expDatas;
	}

	public void setExpDatas(String expDatas) {
		this.expDatas = expDatas;
	}

	public String getExpPhysDBName() {
		return expPhysDBName;
	}

	public void setExpPhysDBName(String expPhysDBName) {
		this.expPhysDBName = expPhysDBName;
	}

	public String getImpUsedDate() {
		return impUsedDate;
	}

	public void setImpUsedDate(String impUsedDate) {
		this.impUsedDate = impUsedDate;
	}

	public Integer getInsertDatas() {
		return insertDatas;
	}

	public void setInsertDatas(int insertDatas) {
		this.insertDatas = insertDatas;
	}

	public int getUpdateDatas() {
		return updateDatas;
	}

	public void setUpdateDatas(int updateDatas) {
		this.updateDatas = updateDatas;
	}

	public Integer getFailDatas() {
		return failDatas;
	}

	public void setFailDatas(int failDatas) {
		this.failDatas = failDatas;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
