package com.hiaward.dataprocess.message.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.hiaward.dataprocess.message.socket.impl.Socket;
import com.hiaward.dataprocess.message.socket.inter.ISocket;
import com.hiaward.dataprocess.message.socket.util.DataConversion;

/*
 * 阳泉商业银行
 * 接口调试工具适配器
 * @author czf 
 * @version 2017-11-24
 */

public class adapter {
	
	private static final Logger logger = Logger.getLogger("adapter");
	
	/**
	 * 对上送报文进行解析
	 * public 
	 * @param map 前台传入参数
	 * @return Map<String, Object>
	 */
	
	public static Map<String, Object> parseRequestMsg (Map<String, String> map) {
		Map<String, Object> processMap = new HashMap<String, Object>();
		String transcode = map.get("transcode");
		
		
		return processMap;
	}
	
	public static void main(String[] args) {
		ISocket iSocket = new Socket();
		File directory  = new File("");
		List list = new ArrayList<String>();
		//定位文件夹下所有message目录
    	ReadFromFile.findDir(list, directory.getAbsolutePath(), "test");
    	int len = list.size();
    	List listFile = new ArrayList<String>();
    	//定位message下所有xml配置文件
    	for(int i=0; i<len; i++)
    	{
    		ReadFromFile.isIneedFile(list.get(i).toString(), "xml", listFile);
    	}
    	
    	for(int nloop = 0;nloop<listFile.size();nloop++){
    		XmlConfCache.getInstance().load8583ConfFile(listFile.get(nloop).toString(),3);
    	}
    	iSocket.init();
    	Map<String,Map<String,Map<String,String>>> proCodeMap = XmlConfCache.getInstance().getProCodeMap();
    	
//    	List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getReqFieldList();
//    	StringBuffer a = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>\n<QuickTrans descripe='余额查询'>\n<TransConfig businessId='0100'  strTransCode='T001'>\n<requestField input_entity=''>\n");
//    	if (null != reqTrans) 
//		{
//			for (int m = 0; m < reqTrans.size(); m++) 
//			{
//				// 取出报文域
//				Map<String, String> fieldMap = (Map<String, String>) reqTrans.get(m);
//				a.append("<field strFieldValue=''");
//				a.append(" strFieldDesc='" + fieldMap.get("strCNDescr") + "'");
//				a.append(" strFieldName='" + fieldMap.get("strName") + "'");
//				if(null == fieldMap.get("strRefValue")){
//					a.append(" ref='" + fieldMap.get("strRefValue") + "'");
//				}else{
//					a.append(" ref='" + fieldMap.get("strRefValue") + "'");
//				}
//				
//				a.append(" strFieldType='string' strFieldLength='' strBeanName=''/>\n");
//			}
//			a.append("</requestField>\n");
//			a.append("<responseField output_entity=''>\n");
//			a.append("</responseField>\n</TransConfig>\n</QuickTrans>");
//			System.out.println(a);
//				
//    	}
    	
    	Map<String, String> requestmap = new HashMap<String, String>();
    	Map<String, String> responsemap = new HashMap<String, String>();
    	String transcode = "T000";
    	
    	for (Entry<String, Map<String, Map<String, String>>> entry : proCodeMap.entrySet()) {
    		if(entry.getKey().toString().equals(transcode)){
    			for(Entry<String, Map<String, String>> jloop : entry.getValue().entrySet()){
    				if(null == requestmap.get(jloop.getValue().get("ref"))){
    					requestmap.put(jloop.getValue().get("ref"),jloop.getValue().get("strFieldValue"));
    				}
    			}
    		}
    	}
    	System.out.println(requestmap);
//    	String temp = new String("30313638E3B8048020810000600054000000000031363632313733383030303030303135323130303030303431323131313032373330323031343131323132303135303133303030303030323039343533303131323730323130303334363231373338303030303030313532313D30303030313230303035393331303030303030303030303030353036303130363037313132303135303132383130323132323739353433363137");
//    	byte[] bt = DataConversion.hex2Byte(temp);
//    	
//    	responsemap = iSocket.commonResAdapter(bt, transcode);
//    	
    	responsemap = iSocket.responseMsg(requestmap,transcode);
        System.out.println(responsemap);
	}
}
