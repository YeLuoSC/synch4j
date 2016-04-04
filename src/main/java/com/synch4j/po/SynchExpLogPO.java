package com.synch4j.po;

/**
 * 同步导出日志PO
 * @author XieGuanNan
 * @date 2015-8-11-下午4:07:46
 */
public class SynchExpLogPO extends SynchLogPO{
	/**
	 * 导出文件名
	 */
	private String expFileName;
	/**
	 * 对应数据库表名
	 */
	private String expPhysDBName;
	/**
	 * 导出数据条数
	 */
	private String expDatas;
	/**
	 * 导出的最后时间
	 */
	private String lastDate;
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 省份
	 */
	private String districtId;

	public String getExpFileName() {
		return expFileName;
	}

	public void setExpFileName(String expFileName) {
		this.expFileName = expFileName;
	}

	public String getExpPhysDBName() {
		return expPhysDBName;
	}

	public void setExpPhysDBName(String expPhysDBName) {
		this.expPhysDBName = expPhysDBName;
	}

	public String getExpDatas() {
		return expDatas;
	}

	public void setExpDatas(String expDatas) {
		this.expDatas = expDatas;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	
}
