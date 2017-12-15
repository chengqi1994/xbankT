package com.hiaward.dataprocess.database.pojo.entity;

import java.sql.Timestamp;

/**
 * LogTransHis entity. @author MyEclipse Persistence Tools
 */

public class LogTransHis implements java.io.Serializable {

	// Fields

	private Integer IId;
	private String strSubbranchNum;
	private String strBranchNum;
	private String strLatticeNum;
	private String strTermNum;
	private Integer IAppId;
	private String strTransCode;
	private String strPan;
	private String strScenarioNum;
	private Integer ITransType;
	private Double DAmount;
	private Double DFee;
	private Integer ITransStatus;
	private Integer ITermStatus;
	private Timestamp dtOccur;
	private String strGnettsn;
	private String strGnettsnGeneral;
	private String strTslNum;
	private Integer IVoucherType;
	private String strVoucherNum;
	private String strCurrencyCode;
	private String strAuthoperaterNum;
	private Integer ICardType;
	private Integer ICountType;
	private Integer ITermType;
	private String strExtend1;
	private String strExtend2;
	private Integer IExtend3;
	private Double DExtend4;

	// Constructors

	/** default constructor */
	public LogTransHis() {
	}

	/** minimal constructor */
	public LogTransHis(String strSubbranchNum, String strBranchNum,
			String strLatticeNum, String strTermNum, Integer IAppId,
			Integer ITransType, Integer ICountType, Integer ITermType) {
		this.strSubbranchNum = strSubbranchNum;
		this.strBranchNum = strBranchNum;
		this.strLatticeNum = strLatticeNum;
		this.strTermNum = strTermNum;
		this.IAppId = IAppId;
		this.ITransType = ITransType;
		this.ICountType = ICountType;
		this.ITermType = ITermType;
	}

	/** full constructor */
	public LogTransHis(String strSubbranchNum, String strBranchNum,
			String strLatticeNum, String strTermNum, Integer IAppId,
			String strTransCode, String strPan, String strScenarioNum,
			Integer ITransType, Double DAmount, Double DFee,
			Integer ITransStatus, Integer ITermStatus, Timestamp dtOccur,
			String strGnettsn, String strGnettsnGeneral, String strTslNum,
			Integer IVoucherType, String strVoucherNum,
			String strCurrencyCode, String strAuthoperaterNum,
			Integer ICardType, Integer ICountType, Integer ITermType,
			String strExtend1, String strExtend2, Integer IExtend3,
			Double DExtend4) {
		this.strSubbranchNum = strSubbranchNum;
		this.strBranchNum = strBranchNum;
		this.strLatticeNum = strLatticeNum;
		this.strTermNum = strTermNum;
		this.IAppId = IAppId;
		this.strTransCode = strTransCode;
		this.strPan = strPan;
		this.strScenarioNum = strScenarioNum;
		this.ITransType = ITransType;
		this.DAmount = DAmount;
		this.DFee = DFee;
		this.ITransStatus = ITransStatus;
		this.ITermStatus = ITermStatus;
		this.dtOccur = dtOccur;
		this.strGnettsn = strGnettsn;
		this.strGnettsnGeneral = strGnettsnGeneral;
		this.strTslNum = strTslNum;
		this.IVoucherType = IVoucherType;
		this.strVoucherNum = strVoucherNum;
		this.strCurrencyCode = strCurrencyCode;
		this.strAuthoperaterNum = strAuthoperaterNum;
		this.ICardType = ICardType;
		this.ICountType = ICountType;
		this.ITermType = ITermType;
		this.strExtend1 = strExtend1;
		this.strExtend2 = strExtend2;
		this.IExtend3 = IExtend3;
		this.DExtend4 = DExtend4;
	}

	// Property accessors

	public Integer getIId() {
		return this.IId;
	}

	public void setIId(Integer IId) {
		this.IId = IId;
	}

	public String getStrSubbranchNum() {
		return this.strSubbranchNum;
	}

	public void setStrSubbranchNum(String strSubbranchNum) {
		this.strSubbranchNum = strSubbranchNum;
	}

	public String getStrBranchNum() {
		return this.strBranchNum;
	}

	public void setStrBranchNum(String strBranchNum) {
		this.strBranchNum = strBranchNum;
	}

	public String getStrLatticeNum() {
		return this.strLatticeNum;
	}

	public void setStrLatticeNum(String strLatticeNum) {
		this.strLatticeNum = strLatticeNum;
	}

	public String getStrTermNum() {
		return this.strTermNum;
	}

	public void setStrTermNum(String strTermNum) {
		this.strTermNum = strTermNum;
	}

	public Integer getIAppId() {
		return this.IAppId;
	}

	public void setIAppId(Integer IAppId) {
		this.IAppId = IAppId;
	}

	public String getStrTransCode() {
		return this.strTransCode;
	}

	public void setStrTransCode(String strTransCode) {
		this.strTransCode = strTransCode;
	}

	public String getStrPan() {
		return this.strPan;
	}

	public void setStrPan(String strPan) {
		this.strPan = strPan;
	}

	public String getStrScenarioNum() {
		return this.strScenarioNum;
	}

	public void setStrScenarioNum(String strScenarioNum) {
		this.strScenarioNum = strScenarioNum;
	}

	public Integer getITransType() {
		return this.ITransType;
	}

	public void setITransType(Integer ITransType) {
		this.ITransType = ITransType;
	}

	public Double getDAmount() {
		return this.DAmount;
	}

	public void setDAmount(Double DAmount) {
		this.DAmount = DAmount;
	}

	public Double getDFee() {
		return this.DFee;
	}

	public void setDFee(Double DFee) {
		this.DFee = DFee;
	}

	public Integer getITransStatus() {
		return this.ITransStatus;
	}

	public void setITransStatus(Integer ITransStatus) {
		this.ITransStatus = ITransStatus;
	}

	public Integer getITermStatus() {
		return this.ITermStatus;
	}

	public void setITermStatus(Integer ITermStatus) {
		this.ITermStatus = ITermStatus;
	}

	public Timestamp getDtOccur() {
		return this.dtOccur;
	}

	public void setDtOccur(Timestamp dtOccur) {
		this.dtOccur = dtOccur;
	}

	public String getStrGnettsn() {
		return this.strGnettsn;
	}

	public void setStrGnettsn(String strGnettsn) {
		this.strGnettsn = strGnettsn;
	}

	public String getStrGnettsnGeneral() {
		return this.strGnettsnGeneral;
	}

	public void setStrGnettsnGeneral(String strGnettsnGeneral) {
		this.strGnettsnGeneral = strGnettsnGeneral;
	}

	public String getStrTslNum() {
		return this.strTslNum;
	}

	public void setStrTslNum(String strTslNum) {
		this.strTslNum = strTslNum;
	}

	public Integer getIVoucherType() {
		return this.IVoucherType;
	}

	public void setIVoucherType(Integer IVoucherType) {
		this.IVoucherType = IVoucherType;
	}

	public String getStrVoucherNum() {
		return this.strVoucherNum;
	}

	public void setStrVoucherNum(String strVoucherNum) {
		this.strVoucherNum = strVoucherNum;
	}

	public String getStrCurrencyCode() {
		return this.strCurrencyCode;
	}

	public void setStrCurrencyCode(String strCurrencyCode) {
		this.strCurrencyCode = strCurrencyCode;
	}

	public String getStrAuthoperaterNum() {
		return this.strAuthoperaterNum;
	}

	public void setStrAuthoperaterNum(String strAuthoperaterNum) {
		this.strAuthoperaterNum = strAuthoperaterNum;
	}

	public Integer getICardType() {
		return this.ICardType;
	}

	public void setICardType(Integer ICardType) {
		this.ICardType = ICardType;
	}

	public Integer getICountType() {
		return this.ICountType;
	}

	public void setICountType(Integer ICountType) {
		this.ICountType = ICountType;
	}

	public Integer getITermType() {
		return this.ITermType;
	}

	public void setITermType(Integer ITermType) {
		this.ITermType = ITermType;
	}

	public String getStrExtend1() {
		return this.strExtend1;
	}

	public void setStrExtend1(String strExtend1) {
		this.strExtend1 = strExtend1;
	}

	public String getStrExtend2() {
		return this.strExtend2;
	}

	public void setStrExtend2(String strExtend2) {
		this.strExtend2 = strExtend2;
	}

	public Integer getIExtend3() {
		return this.IExtend3;
	}

	public void setIExtend3(Integer IExtend3) {
		this.IExtend3 = IExtend3;
	}

	public Double getDExtend4() {
		return this.DExtend4;
	}

	public void setDExtend4(Double DExtend4) {
		this.DExtend4 = DExtend4;
	}

}