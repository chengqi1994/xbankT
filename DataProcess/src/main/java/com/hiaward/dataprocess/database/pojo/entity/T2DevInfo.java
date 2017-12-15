package com.hiaward.dataprocess.database.pojo.entity;

import java.sql.Timestamp;

/**
 * T2DevInfo entity. @author MyEclipse Persistence Tools
 */

public class T2DevInfo implements java.io.Serializable {

	// Fields

	private Integer IId;
	private Integer IModelId;
	private String strDevSid;
	private Integer IDevType;
	private Integer IDevStatus;
	private Timestamp dtInstall;
	private Timestamp dtBack;
	private Timestamp dtDiscarde;
	private String strOrgNum;
	private String strContractName;
	private Integer IRepairLife;
	private Integer IUsefulLife;
	private Integer IFlag;

	// Constructors

	/** default constructor */
	public T2DevInfo() {
	}

	/** full constructor */
	public T2DevInfo(Integer IModelId, String strDevSid,
			Integer IDevType, Integer IDevStatus, Timestamp dtInstall,
			Timestamp dtBack, Timestamp dtDiscarde, String strOrgNum,
			String strContractName, Integer IRepairLife,
			Integer IUsefulLife, Integer IFlag) {
		this.IModelId = IModelId;
		this.strDevSid = strDevSid;
		this.IDevType = IDevType;
		this.IDevStatus = IDevStatus;
		this.dtInstall = dtInstall;
		this.dtBack = dtBack;
		this.dtDiscarde = dtDiscarde;
		this.strOrgNum = strOrgNum;
		this.strContractName = strContractName;
		this.IRepairLife = IRepairLife;
		this.IUsefulLife = IUsefulLife;
		this.IFlag = IFlag;
	}

	// Property accessors

	public Integer getIId() {
		return this.IId;
	}

	public void setIId(Integer IId) {
		this.IId = IId;
	}

	public Integer getIModelId() {
		return this.IModelId;
	}

	public void setIModelId(Integer IModelId) {
		this.IModelId = IModelId;
	}

	public String getStrDevSid() {
		return this.strDevSid;
	}

	public void setStrDevSid(String strDevSid) {
		this.strDevSid = strDevSid;
	}

	public Integer getIDevType() {
		return this.IDevType;
	}

	public void setIDevType(Integer IDevType) {
		this.IDevType = IDevType;
	}

	public Integer getIDevStatus() {
		return this.IDevStatus;
	}

	public void setIDevStatus(Integer IDevStatus) {
		this.IDevStatus = IDevStatus;
	}

	public Timestamp getDtInstall() {
		return this.dtInstall;
	}

	public void setDtInstall(Timestamp dtInstall) {
		this.dtInstall = dtInstall;
	}

	public Timestamp getDtBack() {
		return this.dtBack;
	}

	public void setDtBack(Timestamp dtBack) {
		this.dtBack = dtBack;
	}

	public Timestamp getDtDiscarde() {
		return this.dtDiscarde;
	}

	public void setDtDiscarde(Timestamp dtDiscarde) {
		this.dtDiscarde = dtDiscarde;
	}

	public String getStrOrgNum() {
		return this.strOrgNum;
	}

	public void setStrOrgNum(String strOrgNum) {
		this.strOrgNum = strOrgNum;
	}

	public String getStrContractName() {
		return this.strContractName;
	}

	public void setStrContractName(String strContractName) {
		this.strContractName = strContractName;
	}

	public Integer getIRepairLife() {
		return this.IRepairLife;
	}

	public void setIRepairLife(Integer IRepairLife) {
		this.IRepairLife = IRepairLife;
	}

	public Integer getIUsefulLife() {
		return this.IUsefulLife;
	}

	public void setIUsefulLife(Integer IUsefulLife) {
		this.IUsefulLife = IUsefulLife;
	}

	public Integer getIFlag() {
		return this.IFlag;
	}

	public void setIFlag(Integer IFlag) {
		this.IFlag = IFlag;
	}

}