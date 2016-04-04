package com.synch4j.po;

import com.synch4j.synchenum.ExportMode;

/**
 * 同步主日志PO
 * @author XieGuanNan
 * @date 2015-8-11-下午4:05:13
 */
public class SynchMainLogPO extends SynchLogPO{
	/**
	 * 操作人
	 */
	private String userId;
	/**
	 * 操作IP
	 */
	private String ipAddress;
	/**
	 * 导入或导出文件名称
	 */
	private String fileName;
	/**
	 * 导入或导出开始时间
	 */
	private String startDate;
	/**
	 * 导入或导出结束时间
	 */
	private String endDate;
	/**
	 * 导入或导出使用时间
	 */
	private String usedDate;
	/**
	 * 备注信息
	 */
	private String remark;
	/**
	 * 导入或导出方向
	 */
	private String direction;
	
	/**
	 * 导入与导出些ID一样,没用了2.0
	 */
	private String batchId;
	/*
	 * 导入导出状态
	 */
	private String synStatus;
	
	/**
	 * 导出的地区码
	 */
	private String districtId;
	
	/**
	 * 导入时的文件GUID
	 */
	private String fileGuid;

	public String getFileGuid() {
		return fileGuid;
	}

	public void setFileGuid(String fileGuid) {
		this.fileGuid = fileGuid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getUsedDate() {
		return usedDate;
	}

	public void setUsedDate(String usedDate) {
		this.usedDate = usedDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getSynStatus() {
		return synStatus;
	}

	public void setSynStatus(String synStatus) {
		this.synStatus = synStatus;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
}
