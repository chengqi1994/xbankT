package com.hiaward.dataprocess.message.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 配置文件读取类
 * @version
 * 
 */
public class XmlConfCache {
	
   //8583通用报文域详细信息对象List，用来存放common.xml中的域信息
   private List<Map<String,String>> fieldList =new ArrayList<Map<String,String>>();
   
   //阳泉特殊化    8583报文返回报文即系域域上送域规则不一致     用responsecommon.xml存放域信息
   private List<Map<String,String>> resfieldList =new ArrayList<Map<String,String>>();
   
   //8583交易配置对象MAP，用来存放bitmap.xml中的交易配置信息
   private Map<String,Map<String,Map<String,String>>> proCodeMap =new HashMap<String,Map<String,Map<String,String>>>();
   
   private static Map<String,Map<String,String>> tempCodeMap =new HashMap<String,Map<String,String>>();
   //交易配置文件路径 bitmap.xml
   public final static String strBitMapName = "bitmap.xml";
   
   //通用报文域配置文件路径 common.xml
   public final static String strCommonName = "common.xml";
   
   //定义map解析数组
   public final static String[] str = {"ResCommMsg","ReqBitmap","TransConfig","responseField","requestField","field"};
   
   //定义xml文件存放值
   public final static String[] xmlvalue = {"strFieldValue","strFieldDesc","strFieldName","ref","strFieldType","strFieldLength","strBeanName"};
   //类对象，只要一个静态实例
   public static XmlConfCache confCache = null;
   
   private static String transCode;
   
   public static String getTransCode() {
	 return transCode;
   }
   
   public static void setTransCode(String transCode) {
	  XmlConfCache.tempCodeMap = new HashMap<String,Map<String,String>>();
	  XmlConfCache.transCode = transCode;
   }
   
   private XmlConfCache() 
   {

   }
	/*
	 * 实例化一个对象后，进行配置文件对象初始化
	 */
   public static XmlConfCache getInstance() {// 得到静态实例
		if(null == confCache){
			confCache = new XmlConfCache();	
		}
		return confCache;

	}
   
   //该方法测试时用
   /*public  void init()
   {
	    
		String filePath = XmlConfCache.class.getClassLoader().getResource("").getFile();
		filePath = filePath.substring(1,filePath.length());
		filePath = filePath.replace("/", "\\");
		String strBitMapPathName = filePath+"TransXml\\bitmap.xml";
		String strCommonPathName = filePath+"TransXml\\common.xml";

		
		boolean bolLoadCommonFile = load8583ConfFile(strCommonPathName, 1);

   	  	boolean bolLoadBitMapFile =load8583ConfFile(strBitMapPathName, 2);
   }*/
   
   /**
	 * 递归XML节点
	 * 
	 * @param Element element   节点
	 * @param int     iConfType 配置文件类型 1表示common.xml，2表示bitmap.xml
	 * @return boolean 
	 */
   private boolean circleElement(Element element, int iConfType) 
   {
	   boolean bolRet = false;
	   try
	   {
		   Map<String,String> CodeMap =new HashMap<String,String>();
		   Iterator<?> iter = element.elementIterator();

//	       System.out.println(element.getName());
	       while (iter.hasNext()) {
	           Element sub = (Element)iter.next();
	           
	           if(Arrays.asList(str).contains(sub.getName())){
	        	   if("TransConfig".equals(sub.getName())){
	        		   setTransCode(sub.attributeValue("strTransCode"));
	        	   }
	        	   if("field".equals(sub.getName())){
	        		  getFieldValue(sub);
	        		  proCodeMap.put(transCode,tempCodeMap);
	        	   }else{
	        		  circleElement(sub,iConfType);  
	        	   }
	           }else{
	        	   circleField(sub,iConfType);
	           }
	       }
	       bolRet = true;
	   }
	   catch(Exception e)
	   {
		   bolRet = false;
		   //记录异常日志
//	       LoggerUtil.e(XmlConfCache.class, "[" +new  DateEx().getTimeStrFull() + "]"
//					+"递归XML节点发生异常 XmlConfCache.circleElement Exception \r\n", e);
	       

	   }
      
       
       return bolRet;
   }
   
   private Map<String,Map<String,String>> getFieldValue(Element element){
	   try
	   {
		   Map<String,String> fieldMap = new HashMap<String,String>(10,0.9f);
		   
		   for (int j = 0; j < xmlvalue.length; j++) {
			   fieldMap.put(xmlvalue[j], element.attributeValue(xmlvalue[j]));
		   }
		   
		   tempCodeMap.put(element.attributeValue("strFieldName"), fieldMap);
	   }
	   catch(Exception e)
	   {
//		   return null;
		   //记录异常日志
//	       LoggerUtil.e(XmlConfCache.class, "[" +new  DateEx().getTimeStrFull() + "]"
//					+"递归XML节点发生异常 XmlConfCache.circleElement Exception \r\n", e);
	   }
	   return tempCodeMap;
   }
   
   
   /**
	 * 加载8583配置文件
	 * 
	 * @param String strConfFileName  配置文件路径名
	 * @param int    iConfType        配置文件类型 1表示common.xml，2表示bitmap.xml
	 * @return boolean 
	 */
   public boolean load8583ConfFile(String strConfFileName, int iConfType)
   {
	   boolean bolRet = false;
	   try
	   {
		   File file = new File(strConfFileName);
		   SAXReader xmlReader = new SAXReader();    
		   Document doc = xmlReader.read(file); 
	       Element root = doc.getRootElement();
	       circleElement(root,iConfType); 
	       bolRet = true;
	   }
	   catch(Exception e)
	   {
		   bolRet = false;
		   //记录异常日志
//	       LoggerUtil.e(XmlConfCache.class, "[" +new  DateEx().getTimeStrFull() + "]"
//					+"加载8583配置文件发生异常 XmlConfCache.load8583ConfFile Exception \r\n", e);
	   }
	   return bolRet;
   }
   
   /**
	 * 将配置文件中的Field封装到数据对象中
	 * common.xml->map->list
	 * bitmap.xml->map->map
	 * @param Element element    节点(配置文件中的Field节点)
	 * @param int     iConfType  配置文件类型 1表示common.xml，2表示bitmap.xml
	 * @return boolean 
	 */
   private boolean circleField(Element element, int iConfType) 
   {

	   boolean bolRet = false;
	   try
	   {
		   Iterator<?> iter = element.elementIterator();
	       Map<String,String> fieldMap = new HashMap<String,String>(10,0.9f);
//	       System.out.println("--------------circleElement2-------------------");
	       String strFiledName = null;
	       while (iter.hasNext()) 
	       {
	           Element sub = (Element)iter.next();
	           if("strName".equals(sub.getName()))
	           {
	        	   strFiledName = sub.getTextTrim();
	        	   //System.out.println("strFiledName=== "+strFiledName);
	           }
	           fieldMap.put(sub.getName(), sub.getTextTrim());
	       }
	       if(1 == iConfType)
	       {
	    	   fieldList.add(fieldMap);
	       }
	       else if(2 == iConfType)
	       {
	    	   resfieldList.add(fieldMap);
	       }
	       bolRet = true;
	   }
	   catch(Exception e)
	   {
		   bolRet = false;
		   //记录异常日志
//	       LoggerUtil.e(XmlConfCache.class, "[" +new  DateEx().getTimeStrFull() + "]"
//					+"Field封装到数据对象发生异常 XmlConfCache.circleField \r\n", e);
		   
	   }
       
       return bolRet;
   }
	

	public List<Map<String, String>> getReqFieldList() {
		return fieldList;
	}
	public List<Map<String, String>> getResFieldList() {
		return resfieldList;
	}

	public Map<String, Map<String,Map<String, String>>> getProCodeMap() {
		return proCodeMap;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			//XmlConfCache.getInstance().init();
//			XmlConfCache t = new XmlConfCache();
//			t.load8583ConfFile("D:\\common.xml");
//			t.load8583ConfFile("D:\\bitmap.xml",2);
//			List list =new ArrayList();
//			HashMap filedmap = new HashMap();
//			list.add(filedmap);
//			list.add(filedmap);
//			list.add(filedmap);
//			Iterator iter = list.iterator();
//			while(iter.hasNext())
//			{
//				HashMap mm = (HashMap)iter.next();
//			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		

	}

}
