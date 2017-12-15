package com.hiaward.dataprocess.database.pojo.entity;

import java.sql.Timestamp;

/**
 * SysSystemParam entity. @author MyEclipse Persistence Tools
 */

public class SysSystemParam implements java.io.Serializable {

	// Fields

	private Integer IId;
	private String strName;
	private String strValue;
	private String strRemark;
	private String strDesc;
	private String strOrganNum;
	private Integer IShow;
	private Integer IRange;
	private String strUsernum;
	private Timestamp tsTime;

	// Constructors

	/** default constructor */
	public SysSystemParam() {
	}

	/** minimal constructor */
	public SysSystemParam(String strName, String strValue, String strOrganNum,
			Integer IShow, Integer IRange, Timestamp tsTime) {
		this.strName = strName;
		this.strValue = strValue;
		this.strOrganNum = strOrganNum;
		this.IShow = IShow;
		this.IRange = IRange;
		this.tsTime = tsTime;
	}

	/** full constructor */
	public SysSystemParam(String strName, String strValue, String strRemark,
			String strDesc, String strOrganNum, Integer IShow,
			Integer IRange, String strUsernum, Timestamp tsTime) {
		this.strName = strName;
		this.strValue = strValue;
		this.strRemark = strRemark;
		this.strDesc = strDesc;
		this.strOrganNum = strOrganNum;
		this.IShow = IShow;
		this.IRange = IRange;
		this.strUsernum = strUsernum;
		this.tsTime = tsTime;
	}

	// Property accessors

	public Integer getIId() {
		return this.IId;
	}

	public void setIId(Integer IId) {
		this.IId = IId;
	}

	public String getStrName() {
		return this.strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrValue() {
		return this.strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public String getStrRemark() {
		return this.strRemark;
	}

	public void setStrRemark(String strRemark) {
		this.strRemark = strRemark;
	}

	public String getStrDesc() {
		return this.strDesc;
	}

	public void setStrDesc(String strDesc) {
		this.strDesc = strDesc;
	}

	public String getStrOrganNum() {
		return this.strOrganNum;
	}

	public void setStrOrganNum(String strOrganNum) {
		this.strOrganNum = strOrganNum;
	}

	public Integer getIShow() {
		return this.IShow;
	}

	public void setIShow(Integer IShow) {
		this.IShow = IShow;
	}

	public Integer getIRange() {
		return this.IRange;
	}

	public void setIRange(Integer IRange) {
		this.IRange = IRange;
	}

	public String getStrUsernum() {
		return this.strUsernum;
	}

	public void setStrUsernum(String strUsernum) {
		this.strUsernum = strUsernum;
	}

	public Timestamp getTsTime() {
		return this.tsTime;
	}

	public void setTsTime(Timestamp tsTime) {
		this.tsTime = tsTime;
	}

}