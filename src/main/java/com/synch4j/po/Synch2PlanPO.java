package com.synch4j.po;

import java.sql.Timestamp;

public class Synch2PlanPO {
	private String fileGuid;
	private String provinceCode;
	private String year;
	private String docId;
	private String userId;
	private String appId;
	private String remark;
	private String status;//0为未执行，1为执行中，2为执行成功，3为执行失败
	private String direction;//A为下发，R为上报
	private Timestamp runTime;
	
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Timestamp getRunTime() {
		return runTime;
	}
	public void setRunTime(Timestamp runTime) {
		this.runTime = runTime;
	}
	public String getFileGuid() {
		return fileGuid;
	}
	public void setFileGuid(String fileGuid) {
		this.fileGuid = fileGuid;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
