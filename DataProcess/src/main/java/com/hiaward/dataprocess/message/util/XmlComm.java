package com.hiaward.dataprocess.message.util;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * XML报文处理类
 * @version
 * 2017/8/1 caiweihao
 */
public class XmlComm {

	private final static String xmlInfo = "<?xml version='1.0' encoding='UTF-8'?>";
	// 报文根节点
	private final static String serviceStart = "<service>";
	private final static String serviceEnd = "</service>";
	// 报文头节点
	private final static String serviceHeadStart = "<head>";
	private final static String serviceHeadEnd = "</head>";
	// 报文体节点
	private final static String serviceBodyStart = "<body>";
	private final static String serviceBodyEnd = "</body>";
	// 报文体List节点
	private final static String serviceListStart = "<list>";
	private final static String serviceListEnd = "</list>";
	// 报文体List节点下节点row
	private final static String serviceRowStart = "<row>";
	private final static String serviceRowEnd = "</row>";
	// 报文的字节长度
	private final static int length = 8;
	// xml报文循环的节点名称
	private final static String LISTS = "list";
	// xml报文循环List节点下的节点名称
	private final static String ROW = "row";

	/**
	 * 组装xml报文
	 * @param headMap 存放报文头数据
	 * @param bodyMap 存放报文体数据
	 * @return byte[] 组织完成的返回包
	 */
	public StringBuffer organizeXmlMsg(Map<String, String> headMap,Map<String, String> bodyMap) {
		
		StringBuffer strMesInfo = new StringBuffer();
		// 组装报文
		strMesInfo.append(xmlInfo);
		// 组装报文节点
		strMesInfo.append(serviceStart);
		// 组装报文根节点
		strMesInfo.append(serviceHeadStart);
		for (Map.Entry<String, String> headMapInfo : headMap.entrySet()) {
			// 组装报文头的内容
			strMesInfo.append("<"+headMapInfo.getKey()+">"+headMapInfo.getValue()+"</"+headMapInfo.getKey()+">");
		}
		strMesInfo.append(serviceHeadEnd);
		
		// 组装报文根节点
		strMesInfo.append(serviceBodyStart);
		for (Map.Entry<String, String> bodyMapInfo : bodyMap.entrySet()) {
			// 组装报文头的内容
			strMesInfo.append("<"+bodyMapInfo.getKey()+">"+bodyMapInfo.getValue()+"</"+bodyMapInfo.getKey()+">");
		}

		strMesInfo.append(serviceBodyEnd);
		// 组装报文节点
		strMesInfo.append(serviceEnd);
		// 计算报文的长度
//		String strMessageLength = String.format("%0"+length+"d", strMesInfo.length());
//		strMesInfo.insert(0, strMessageLength);
		
		return strMesInfo;
	}
	
	/**
	 * 组装xml报文
	 * @param headMap 存放报文头数据
	 * @param transBodyInfo 存放报文体数据
	 * @param arrayList 集合数据
	 * @return byte[] 组织完成的返回包
	 */
	public StringBuffer organizeXmlMsg(Map<String, String> headMap,Map<String, String> transBodyInfo,JSONArray arrayList) {
		
		StringBuffer strMesInfo = new StringBuffer();
		// 组装报文
		strMesInfo.append(xmlInfo);
		// 组装报文节点
		strMesInfo.append(serviceStart);
		// 组装报文根节点
		strMesInfo.append(serviceHeadStart);
		for (Map.Entry<String, String> headMapInfo : headMap.entrySet()) {
			// 组装报文头的内容
			strMesInfo.append("<"+headMapInfo.getKey()+">"+headMapInfo.getValue()+"</"+headMapInfo.getKey()+">");
		}
		strMesInfo.append(serviceHeadEnd);
		
		// 组装报文根节点
		strMesInfo.append(serviceBodyStart);
		for (Map.Entry<String, String> bodyMapInfo : transBodyInfo.entrySet()) {
			// 组装报文头的内容
			strMesInfo.append("<"+bodyMapInfo.getKey()+">"+bodyMapInfo.getValue()+"</"+bodyMapInfo.getKey()+">");
		}
		
		// 组装报文体中的list集合数据
		if(null != arrayList && arrayList.size() > 0){
			// 组装报文体中list节点
			strMesInfo.append(serviceListStart);
			for(int i=0,size=arrayList.size(); i<size; i++){
				// 组装报文体中list节点下的row节点
				strMesInfo.append(serviceRowStart);
				JSONObject object = arrayList.getJSONObject(i);
				 //迭代器迭代 map集合所有的keys  
	            Iterator it = object.keys();  
	            while(it.hasNext()){  
	                //获取key  
	                String key = (String) it.next();
	                strMesInfo.append("<"+key+">"+object.get(key).toString()+"</"+key+">");
	            }  
				strMesInfo.append(serviceRowEnd);
			}
			strMesInfo.append(serviceListEnd);
		}
		
		strMesInfo.append(serviceBodyEnd);
		// 组装报文节点
		strMesInfo.append(serviceEnd);
		// 计算报文的长度
//		String strMessageLength = String.format("%0"+length+"d", strMesInfo.length());
//		strMesInfo.insert(0, strMessageLength);
		
		return strMesInfo;
	}

	/**
	 * 分解xml报文
	 * @param byResFiledVal 报文数据
	 * @return Map 报文分析结果
	 */
	public JSONObject analyzeXmlMsg(byte[] byResFiledVal) 
	{
		JSONObject resutl = new JSONObject();
		
		Document document = null;
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(new ByteArrayInputStream(byResFiledVal));
			System.out.println("--------xml的返回报文---------");
			Element root = document.getRootElement();
			// 获取xml中的根节点
			Iterator rootNode = root.elementIterator();
			while (rootNode.hasNext()) {
				Element fieldNode = (Element) rootNode.next();
				// 获取xml中的二级节点
				Iterator secNode = fieldNode.elementIterator();
				while (secNode.hasNext()) {
					Element valNode = (Element) secNode.next();// 结果节点
					// 循环解析xml的集合
					if(LISTS.equals(valNode.getName())){
						Iterator thirdNode = valNode.elementIterator();
						JSONArray arrayList = new JSONArray();
						while (thirdNode.hasNext()) {
							Element listsNode = (Element) thirdNode.next();// 结果节点
							Iterator forthNode = listsNode.elementIterator();
							JSONObject object = new JSONObject();
							while (forthNode.hasNext()) {
								Element listNode = (Element) forthNode.next();// 结果节点
								object.put(listNode.getName(), listNode.getText());
							}
							arrayList.add(object);
						}
						resutl.put(LISTS, arrayList);
					}else{
						resutl.put(valNode.getName(), valNode.getText());
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		System.out.println(resutl.toString());
		return resutl;
	}

}

