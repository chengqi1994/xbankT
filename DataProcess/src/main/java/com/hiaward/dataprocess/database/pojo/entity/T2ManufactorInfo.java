package com.hiaward.dataprocess.database.pojo.entity;

import java.sql.Timestamp;

/**
 * T2ManufactorInfo entity. @author MyEclipse Persistence Tools
 */

public class T2ManufactorInfo implements java.io.Serializable {

	// Fields

	private Integer IId;
	private String strSupplierName;
	private String strSupplierShortName;
	private String strBussinessType;
	private String strSupplierAddr;
	private String strSupplierPostcode;
	private String strSupplierTel;
	private String strSupplierFax;
	private String strSupplierUrl;
	private Long DRegistrationMoney;
	private Timestamp dtRegistrationDate;
	private Integer IRegistrationMoneyType;
	private String strCorporateName;
	private String strRegisteredAddress;

	// Constructors

	/** default constructor */
	public T2ManufactorInfo() {
	}

	/** full constructor */
	public T2ManufactorInfo(String strSupplierName,
			String strSupplierShortName, String strBussinessType,
			String strSupplierAddr, String strSupplierPostcode,
			String strSupplierTel, String strSupplierFax,
			String strSupplierUrl, Long DRegistrationMoney,
			Timestamp dtRegistrationDate, Integer IRegistrationMoneyType,
			String strCorporateName, String strRegisteredAddress) {
		this.strSupplierName = strSupplierName;
		this.strSupplierShortName = strSupplierShortName;
		this.strBussinessType = strBussinessType;
		this.strSupplierAddr = strSupplierAddr;
		this.strSupplierPostcode = strSupplierPostcode;
		this.strSupplierTel = strSupplierTel;
		this.strSupplierFax = strSupplierFax;
		this.strSupplierUrl = strSupplierUrl;
		this.DRegistrationMoney = DRegistrationMoney;
		this.dtRegistrationDate = dtRegistrationDate;
		this.IRegistrationMoneyType = IRegistrationMoneyType;
		this.strCorporateName = strCorporateName;
		this.strRegisteredAddress = strRegisteredAddress;
	}

	// Property accessors

	public Integer getIId() {
		return this.IId;
	}

	public void setIId(Integer IId) {
		this.IId = IId;
	}

	public String getStrSupplierName() {
		return this.strSupplierName;
	}

	public void setStrSupplierName(String strSupplierName) {
		this.strSupplierName = strSupplierName;
	}

	public String getStrSupplierShortName() {
		return this.strSupplierShortName;
	}

	public void setStrSupplierShortName(String strSupplierShortName) {
		this.strSupplierShortName = strSupplierShortName;
	}

	public String getStrBussinessType() {
		return this.strBussinessType;
	}

	public void setStrBussinessType(String strBussinessType) {
		this.strBussinessType = strBussinessType;
	}

	public String getStrSupplierAddr() {
		return this.strSupplierAddr;
	}

	public void setStrSupplierAddr(String strSupplierAddr) {
		this.strSupplierAddr = strSupplierAddr;
	}

	public String getStrSupplierPostcode() {
		return this.strSupplierPostcode;
	}

	public void setStrSupplierPostcode(String strSupplierPostcode) {
		this.strSupplierPostcode = strSupplierPostcode;
	}

	public String getStrSupplierTel() {
		return this.strSupplierTel;
	}

	public void setStrSupplierTel(String strSupplierTel) {
		this.strSupplierTel = strSupplierTel;
	}

	public String getStrSupplierFax() {
		return this.strSupplierFax;
	}

	public void setStrSupplierFax(String strSupplierFax) {
		this.strSupplierFax = strSupplierFax;
	}

	public String getStrSupplierUrl() {
		return this.strSupplierUrl;
	}

	public void setStrSupplierUrl(String strSupplierUrl) {
		this.strSupplierUrl = strSupplierUrl;
	}

	public Long getDRegistrationMoney() {
		return this.DRegistrationMoney;
	}

	public void setDRegistrationMoney(Long DRegistrationMoney) {
		this.DRegistrationMoney = DRegistrationMoney;
	}

	public Timestamp getDtRegistrationDate() {
		return this.dtRegistrationDate;
	}

	public void setDtRegistrationDate(Timestamp dtRegistrationDate) {
		this.dtRegistrationDate = dtRegistrationDate;
	}

	public Integer getIRegistrationMoneyType() {
		return this.IRegistrationMoneyType;
	}

	public void setIRegistrationMoneyType(Integer IRegistrationMoneyType) {
		this.IRegistrationMoneyType = IRegistrationMoneyType;
	}

	public String getStrCorporateName() {
		return this.strCorporateName;
	}

	public void setStrCorporateName(String strCorporateName) {
		this.strCorporateName = strCorporateName;
	}

	public String getStrRegisteredAddress() {
		return this.strRegisteredAddress;
	}

	public void setStrRegisteredAddress(String strRegisteredAddress) {
		this.strRegisteredAddress = strRegisteredAddress;
	}

}