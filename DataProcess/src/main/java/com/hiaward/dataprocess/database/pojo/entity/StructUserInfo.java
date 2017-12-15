package com.hiaward.dataprocess.database.pojo.entity;

import java.sql.Timestamp;

/**
 * StructUserInfo entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("serial")
public class StructUserInfo implements java.io.Serializable {

	// Fields

	private Integer IId;
	private String strUserNum;
	private String strName;
	private String strAccount;
	private String strPassword;
	private String strEmail;
	private Short IDelstatus;
	private Timestamp dtActiveEndtime;
	private String strExtend1;
	private Integer IExtend2;
	private String strOrgNum;
	private Integer ICurStatus;
	private Integer IRoleId;
	private String strMobilePhone;
	private String strTelPhone;
	private String strDepartment;
	private String strMemo;
	private Integer IUserType;
	private String strRoleSet;
	// Constructors

	public Integer getIUserType() {
		return IUserType;
	}

	public void setIUserType(Integer iUserType) {
		IUserType = iUserType;
	}

	/** default constructor */
	public StructUserInfo() {
	}

	/** minimal constructor */
	public StructUserInfo(Integer IId, String strUserNum) {
		this.IId = IId;
		this.strUserNum = strUserNum;
	}

	/** full constructor */
	public StructUserInfo(Integer IId, String strUserNum, String strName,
			String strAccount, String strPassword, String strEmail,
			Short IDelstatus, Timestamp dtActiveEndtime, String strExtend1,
			Integer IExtend2, String strOrgNum, Integer ICurStatus,
			Integer IRoleId, String strMobilePhone, String strTelPhone,
			String strDepartment, String strMemo, Integer IUserType,
			String strRoleSet) {
		this.IId = IId;
		this.strUserNum = strUserNum;
		this.strName = strName;
		this.strAccount = strAccount;
		this.strPassword = strPassword;
		this.strEmail = strEmail;
		this.IDelstatus = IDelstatus;
		this.dtActiveEndtime = dtActiveEndtime;
		this.strExtend1 = strExtend1;
		this.IExtend2 = IExtend2;
		this.strOrgNum = strOrgNum;
		this.ICurStatus = ICurStatus;
		this.IRoleId = IRoleId;
		this.strMobilePhone = strMobilePhone;
		this.strTelPhone = strTelPhone;
		this.strDepartment = strDepartment;
		this.strMemo = strMemo;
		this.IUserType = IUserType;
		this.strRoleSet = strRoleSet;
	}

	// Property accessors

	public Integer getIId() {
		return this.IId;
	}

	public void setIId(Integer IId) {
		this.IId = IId;
	}

	public String getStrUserNum() {
		return this.strUserNum;
	}

	public void setStrUserNum(String strUserNum) {
		this.strUserNum = strUserNum;
	}

	public String getStrName() {
		return this.strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrAccount() {
		return this.strAccount;
	}

	public void setStrAccount(String strAccount) {
		this.strAccount = strAccount;
	}

	public String getStrPassword() {
		return this.strPassword;
	}

	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}

	public String getStrEmail() {
		return this.strEmail;
	}

	public void setStrEmail(String strEmail) {
		this.strEmail = strEmail;
	}

	public Short getIDelstatus() {
		return this.IDelstatus;
	}

	public void setIDelstatus(Short IDelstatus) {
		this.IDelstatus = IDelstatus;
	}

	public Timestamp getDtActiveEndtime() {
		return this.dtActiveEndtime;
	}

	public void setDtActiveEndtime(Timestamp dtActiveEndtime) {
		this.dtActiveEndtime = dtActiveEndtime;
	}

	public String getStrExtend1() {
		return this.strExtend1;
	}

	public void setStrExtend1(String strExtend1) {
		this.strExtend1 = strExtend1;
	}

	public Integer getIExtend2() {
		return this.IExtend2;
	}

	public void setIExtend2(Integer IExtend2) {
		this.IExtend2 = IExtend2;
	}

	public String getStrOrgNum() {
		return this.strOrgNum;
	}

	public void setStrOrgNum(String strOrgNum) {
		this.strOrgNum = strOrgNum;
	}

	public Integer getICurStatus() {
		return this.ICurStatus;
	}

	public void setICurStatus(Integer ICurStatus) {
		this.ICurStatus = ICurStatus;
	}

	public Integer getIRoleId() {
		return this.IRoleId;
	}

	public void setIRoleId(Integer IRoleId) {
		this.IRoleId = IRoleId;
	}

	public String getStrMobilePhone() {
		return this.strMobilePhone;
	}

	public void setStrMobilePhone(String strMobilePhone) {
		this.strMobilePhone = strMobilePhone;
	}

	public String getStrTelPhone() {
		return this.strTelPhone;
	}

	public void setStrTelPhone(String strTelPhone) {
		this.strTelPhone = strTelPhone;
	}

	public String getStrDepartment() {
		return this.strDepartment;
	}

	public void setStrDepartment(String strDepartment) {
		this.strDepartment = strDepartment;
	}

	public String getStrMemo() {
		return this.strMemo;
	}

	public void setStrMemo(String strMemo) {
		this.strMemo = strMemo;
	}

	public String getStrRoleSet() {
		return strRoleSet;
	}

	public void setStrRoleSet(String strRoleSet) {
		this.strRoleSet = strRoleSet;
	}

}