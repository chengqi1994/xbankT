package com.hiaward.dataprocess.message.socket.util;

public class JournalObjectBank {
	//时间
	private String transTime;
	//渠道名
	private String chnlNo;
	//返回码
	private String retCode;
	//交易码
	private String transCode;
	//交易用时
	private String costTime;
	//自定义1
	private String retainfield1;
	//自定义2
	private String retainfield2;
	
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	public String getChnlNo() {
		return chnlNo;
	}
	public void setChnlNo(String chnlNo) {
		this.chnlNo = chnlNo;
	}
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getCostTime() {
		return costTime;
	}
	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}
	public String getRetainfield1() {
		return retainfield1;
	}
	public void setRetainfield1(String retainfield1) {
		this.retainfield1 = retainfield1;
	}
	public String getRetainfield2() {
		return retainfield2;
	}
	public void setRetainfield2(String retainfield2) {
		this.retainfield2 = retainfield2;
	}
	
	
}
