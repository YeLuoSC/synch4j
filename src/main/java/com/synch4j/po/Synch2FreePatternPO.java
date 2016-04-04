package com.synch4j.po;

public class Synch2FreePatternPO {

	private String patternId;
	
	/**
	 * 自定义模式名称
	 */
	private String patternName;
	
	private String status;
	
	/**
	 * 是否下发A1,A2模板数据
	 */
	private String templateData;
	
	/**
	 * 是否下发业务表数据
	 */
	private String busiData;
	
	/**
	 * 是否下发设置表数据（synch_t_setting表中定义的表）
	 */
	private String settingData;
	
	/**
	 * 是否下发系统表数据（synch2.properties中system.table定义的表）
	 */
	private String systemData;
	
	/**
	 * 导入时，分区获取方式（0：代表直接按照下发时所选目标省份码分区；1：代表取导入方的application.properties的设置）默认0
	 */
	private String importPartitionMode = "0";
	
	/**
	 * 导出时，省份分区信息
	 */
	private String sourceProvince;

	/**
	 * 导出时，年份分区信息
	 */
	private String sourceYear;
	/**
	 * 是否一直下发（恒下发）
	 */
	private String isAlwaysAssign;

	public String getPatternId() {
		return patternId;
	}

	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}

	public String getPatternName() {
		return patternName;
	}

	public void setPatternName(String patternName) {
		this.patternName = patternName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTemplateData() {
		return templateData;
	}

	public void setTemplateData(String templateData) {
		this.templateData = templateData;
	}

	public String getBusiData() {
		return busiData;
	}

	public void setBusiData(String busiData) {
		this.busiData = busiData;
	}

	public String getSettingData() {
		return settingData;
	}

	public void setSettingData(String settingData) {
		this.settingData = settingData;
	}

	public String getSystemData() {
		return systemData;
	}

	public void setSystemData(String systemData) {
		this.systemData = systemData;
	}

	public String getImportPartitionMode() {
		return importPartitionMode;
	}

	public void setImportPartitionMode(String importPartitionMode) {
		this.importPartitionMode = importPartitionMode;
	}

	public String getSourceProvince() {
		return sourceProvince;
	}

	public void setSourceProvince(String sourceProvince) {
		this.sourceProvince = sourceProvince;
	}

	public String getSourceYear() {
		return sourceYear;
	}

	public void setSourceYear(String sourceYear) {
		this.sourceYear = sourceYear;
	}

	public String getIsAlwaysAssign() {
		return isAlwaysAssign;
	}

	public void setIsAlwaysAssign(String isAlwaysAssign) {
		this.isAlwaysAssign = isAlwaysAssign;
	}
	
	
}
