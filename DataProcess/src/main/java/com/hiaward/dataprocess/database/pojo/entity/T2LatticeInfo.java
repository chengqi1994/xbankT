package com.hiaward.dataprocess.database.pojo.entity;

import java.sql.Timestamp;

/**
 * T2LatticeInfo entity. @author MyEclipse Persistence Tools
 */

public class T2LatticeInfo implements java.io.Serializable {

	// Fields

	private Integer IId;
	private String strOrgNum;
	private Integer IPointType;
	private Integer IMoneyExechange;
	private Integer IBrokenExechange;
	private Integer IFloorArea;
	private String strCompanyStart;
	private String strCompanyEnd;
	private String strPersonalStart;
	private String strPersonalEnd;
	private Timestamp dtFound;
	private Timestamp dtOpen;
	private String strAddr;
	private String strTel;
	private String strFax;
	private String strPostalcode;
	private String strContacts;
	private String strContactsPost;
	private String strContactsTel;
	private Integer IFlag;

	// Constructors

	/** default constructor */
	public T2LatticeInfo() {
	}

	/** full constructor */
	public T2LatticeInfo(String strOrgNum, Integer IPointType,
			Integer IMoneyExechange, Integer IBrokenExechange,
			Integer IFloorArea, String strCompanyStart,
			String strCompanyEnd, String strPersonalStart,
			String strPersonalEnd, Timestamp dtFound, Timestamp dtOpen,
			String strAddr, String strTel, String strFax, String strPostalcode,
			String strContacts, String strContactsPost, String strContactsTel,
			Integer IFlag) {
		this.strOrgNum = strOrgNum;
		this.IPointType = IPointType;
		this.IMoneyExechange = IMoneyExechange;
		this.IBrokenExechange = IBrokenExechange;
		this.IFloorArea = IFloorArea;
		this.strCompanyStart = strCompanyStart;
		this.strCompanyEnd = strCompanyEnd;
		this.strPersonalStart = strPersonalStart;
		this.strPersonalEnd = strPersonalEnd;
		this.dtFound = dtFound;
		this.dtOpen = dtOpen;
		this.strAddr = strAddr;
		this.strTel = strTel;
		this.strFax = strFax;
		this.strPostalcode = strPostalcode;
		this.strContacts = strContacts;
		this.strContactsPost = strContactsPost;
		this.strContactsTel = strContactsTel;
		this.IFlag = IFlag;
	}

	// Property accessors

	public Integer getIId() {
		return this.IId;
	}

	public void setIId(Integer IId) {
		this.IId = IId;
	}

	public String getStrOrgNum() {
		return this.strOrgNum;
	}

	public void setStrOrgNum(String strOrgNum) {
		this.strOrgNum = strOrgNum;
	}

	public Integer getIPointType() {
		return this.IPointType;
	}

	public void setIPointType(Integer IPointType) {
		this.IPointType = IPointType;
	}

	public Integer getIMoneyExechange() {
		return this.IMoneyExechange;
	}

	public void setIMoneyExechange(Integer IMoneyExechange) {
		this.IMoneyExechange = IMoneyExechange;
	}

	public Integer getIBrokenExechange() {
		return this.IBrokenExechange;
	}

	public void setIBrokenExechange(Integer IBrokenExechange) {
		this.IBrokenExechange = IBrokenExechange;
	}

	public Integer getIFloorArea() {
		return this.IFloorArea;
	}

	public void setIFloorArea(Integer IFloorArea) {
		this.IFloorArea = IFloorArea;
	}

	public String getStrCompanyStart() {
		return this.strCompanyStart;
	}

	public void setStrCompanyStart(String strCompanyStart) {
		this.strCompanyStart = strCompanyStart;
	}

	public String getStrCompanyEnd() {
		return this.strCompanyEnd;
	}

	public void setStrCompanyEnd(String strCompanyEnd) {
		this.strCompanyEnd = strCompanyEnd;
	}

	public String getStrPersonalStart() {
		return this.strPersonalStart;
	}

	public void setStrPersonalStart(String strPersonalStart) {
		this.strPersonalStart = strPersonalStart;
	}

	public String getStrPersonalEnd() {
		return this.strPersonalEnd;
	}

	public void setStrPersonalEnd(String strPersonalEnd) {
		this.strPersonalEnd = strPersonalEnd;
	}

	public Timestamp getDtFound() {
		return this.dtFound;
	}

	public void setDtFound(Timestamp dtFound) {
		this.dtFound = dtFound;
	}

	public Timestamp getDtOpen() {
		return this.dtOpen;
	}

	public void setDtOpen(Timestamp dtOpen) {
		this.dtOpen = dtOpen;
	}

	public String getStrAddr() {
		return this.strAddr;
	}

	public void setStrAddr(String strAddr) {
		this.strAddr = strAddr;
	}

	public String getStrTel() {
		return this.strTel;
	}

	public void setStrTel(String strTel) {
		this.strTel = strTel;
	}

	public String getStrFax() {
		return this.strFax;
	}

	public void setStrFax(String strFax) {
		this.strFax = strFax;
	}

	public String getStrPostalcode() {
		return this.strPostalcode;
	}

	public void setStrPostalcode(String strPostalcode) {
		this.strPostalcode = strPostalcode;
	}

	public String getStrContacts() {
		return this.strContacts;
	}

	public void setStrContacts(String strContacts) {
		this.strContacts = strContacts;
	}

	public String getStrContactsPost() {
		return this.strContactsPost;
	}

	public void setStrContactsPost(String strContactsPost) {
		this.strContactsPost = strContactsPost;
	}

	public String getStrContactsTel() {
		return this.strContactsTel;
	}

	public void setStrContactsTel(String strContactsTel) {
		this.strContactsTel = strContactsTel;
	}

	public Integer getIFlag() {
		return this.IFlag;
	}

	public void setIFlag(Integer IFlag) {
		this.IFlag = IFlag;
	}

}