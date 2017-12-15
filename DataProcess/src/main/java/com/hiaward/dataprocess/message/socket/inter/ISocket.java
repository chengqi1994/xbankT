package com.hiaward.dataprocess.message.socket.inter;

import java.util.Map;

import net.sf.json.JSONObject;

public interface ISocket {
	
	boolean init();
	
	Map<String, Object> commonReqAdapter(Map<String, String> map, String interName);

	/**
	 * 获取backCommunication.xml中的response报文信息
	 * @return
	 */
	Map<String, String> commonResAdapter(byte[] response, String initName);

	String getValue(JSONObject jsonObjectColum, String value);
	/**
	 * 解析backCommunication.xml中的rules标签
	 * @return
	 */
	String getRules(String value, JSONObject jsonObjectColum);

	Map<String, String> responseMsg(Map<String, String> requestMsg, String initName);
	
}
