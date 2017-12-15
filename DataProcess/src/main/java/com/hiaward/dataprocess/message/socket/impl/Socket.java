package com.hiaward.dataprocess.message.socket.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xpath.operations.Bool;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hiaward.dataprocess.database.dao.impl.SysSystemParamDao;
import com.hiaward.dataprocess.database.pojo.entity.SysSystemParam;
import com.hiaward.dataprocess.message.socket.inter.ISocket;
import com.hiaward.dataprocess.message.socket.util.Client;
import com.hiaward.dataprocess.message.socket.util.DataConversion;
import com.hiaward.dataprocess.message.util.CommonUtil;
import com.hiaward.dataprocess.message.util.Constant;
import com.hiaward.dataprocess.message.util.ISO8583Comm;
import com.hiaward.dataprocess.message.util.ReadFromFile;
import com.hiaward.dataprocess.message.util.RuleDealUtil;
import com.hiaward.dataprocess.message.util.XmlConfCache;
import com.hiaward.dataprocess.message.util.XmlExercise;

public class Socket implements ISocket {
	
	/**
	 * 初始化xml配置信息，common.xml配置信息，通讯地址
	 * @return
	 */
	
	private boolean IinitOk = true;
	
	@SuppressWarnings({ "rawtypes" })
	public boolean init() {
		if(IinitOk)
		{
			//设定为当前文件夹
			File directory  = new File("");
			List list = new ArrayList<String>();
			//定位文件夹下所有message目录
	    	ReadFromFile.findDir(list, directory.getAbsolutePath(), "message");
	    	int len = list.size();
	    	List listFile = new ArrayList<String>();
	    	//定位message下所有xml配置文件
	    	for(int i=0; i<len; i++)
	    	{
	    		ReadFromFile.isIneedFile(list.get(i).toString(), "xml", listFile);
	    	}
			len = listFile.size();
			String strMessage = null;
			JSONArray jsonArray = null;
			int arrLen = 0;
			JSONObject object = null;
			//解析xml文件放入jsonobject
			for(int j=0; j<len; j++)
			{
				//将xml字符串转换为JSON字符串
				strMessage = XmlExercise.xml2json(ReadFromFile.readFileByLines(listFile.get(j).toString()));
		    	jsonArray=JSONArray.fromObject(strMessage);
		    	arrLen = jsonArray.size();
		    	for(int i = 0; i < arrLen; i++){
		    	    object = jsonArray.getJSONObject(i);
		    	    //将解析后xml配置文件信息放入全局map，初始化initmap完成
		    		Constant.initmap.put(object.getString("@commName"), object.toString());
		    	}
			}
			System.out.println(Constant.initmap);
			
			//定位文件夹下所有ISO8583目录
			list = new ArrayList<String>();
	    	listFile = new ArrayList<String>();
			ReadFromFile.findDir(list, directory.getAbsolutePath(), "ISO8583");
	    	len = list.size();
	    	//定位ISO8583下所有xml配置文件
	    	for(int i=0; i<len; i++)
	    	{
	    		ReadFromFile.isIneedFile(list.get(i).toString(), "xml", listFile);
	    	}
			
			//调用初始化common.xml方法
			//得到ISO8583报文配置
			List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getReqFieldList();
			List<Map<String, String>> resTrans = XmlConfCache.getInstance().getResFieldList();
			len = listFile.size();
			Boolean flag = false;
			Boolean tempflag = false;
			for(int j=0; j<len; j++)
			{
				strMessage = listFile.get(j).toString();
				String a = strMessage.substring(strMessage.lastIndexOf("\\") + 1,strMessage.length());
				if("common.xml".equals(a))
				{
					//校验common.xml是否已经初始化
					if(null == reqTrans || reqTrans.isEmpty() || reqTrans.size() <= 0){
						//加载8583配置文件
						if(XmlConfCache.getInstance().load8583ConfFile(strMessage,1))
						{
							if(tempflag){
								flag = true;
								break;
							}else{
								tempflag = true;
							}
						}
					}else {
						if(tempflag){
							flag = true;
							break;
						}else{
							tempflag = true;
						}
					}
				}
				if("responsecommon.xml".equals(a)){
					//校验common.xml是否已经初始化
					if(null == resTrans || resTrans.isEmpty() || resTrans.size() <= 0){
						//加载8583配置文件
						if(XmlConfCache.getInstance().load8583ConfFile(strMessage,2))
						{
							if(tempflag){
								flag = true;
								break;
							}else{
								tempflag = true;
							}
						}
					}else {
						if(tempflag){
							flag = true;
							break;
						}else{
							tempflag = true;
						}
					}
				}
			}
			//判断初始化initmap是否完成
	    	if (Constant.initmap.isEmpty() || null == Constant.initmap || Constant.initmap.size() == 0 || !flag) {
	    		return false;
			}else {
				//初始化通讯地址
				if(Constant.mailAddressmap.isEmpty() || null == Constant.mailAddressmap || Constant.mailAddressmap.size() == 0)
				{
					
					Constant.mailAddressmap.put("transMsgIP", "192.168.13.136");
					Constant.mailAddressmap.put("transMsgPort", "8001");
					Constant.mailAddressmap.put("branchNum", "");
					//连接当前库cur（历史库为his）
////					SysSystemParamDao sysDao = new SysSystemParamDao("cur");
////					SysSystemParam sysinfo = new SysSystemParam();
//					//从当前库SysSystemParam表中取出所需信息
////					sysinfo = sysDao.querySystemParamByName(object.getString("@commBackName"));
//					if(null == sysinfo || sysinfo.toString().equals("")){
//						System.out.println("后台未查询到"+object.getString("@commBackName")+"的配置信息，请检查xml配置文件commBackName字段！！");
//						return false;
//					}else {
//						//将通讯地址，端口号，分行号缓存进全局map
//						Constant.mailAddressmap.put("transMsgIP", "192.168.13.105");
//						Constant.mailAddressmap.put("transMsgPort", "8001");
//						Constant.mailAddressmap.put("branchNum", "");//sysinfo.getStrOrganNum());
//					}
				}
				IinitOk = false;
				return true;
			}
		}
		else
		{
			return true;
		}
	}

	/**
	 * 接收前台请求，解析xml配置文件，按8583规范组装成报文
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public Map<String, Object> commonReqAdapter(Map<String, String> map, String interName) {
		Map<String, String> processMap = new HashMap<String, String>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		ISO8583Comm iso8583Comm = new ISO8583Comm();
		
		//校验初始化initmap配置文件内容是否完整
		String curMap = Constant.initmap.get(interName);
		if(CommonUtil.checkString(curMap) == null)
		{
			//报错，返回
			returnMap.put("code", "TC0001");
			returnMap.put("message", "找不到名为["+interName+"]匹配的xml文件");
			return returnMap;
		}
		JSONObject json=JSONObject.fromObject(Constant.initmap.get(interName));
		
		//解析xml配置文件向后台请求配置信息并赋值
		//解析后台
		JSONObject backstage=JSONObject.fromObject(json.get("backstage"));
		//解析后台请求
		JSONArray backstageRequest=JSONArray.fromObject(backstage.get("request"));
		JSONObject backstageJs = new JSONObject();
		JSONArray backstageJa = new JSONArray();
		JSONObject colum1 = new JSONObject();
		JSONObject jsonObjectColum = new JSONObject();
		String tempArray[] = {};
		String result = "";
		String finallyResult = "";
		String key = "";
		String value = "";
		String strValue = "";
		List<String> keyList = new ArrayList<String>();
		//遍历后台请求下子节点，将子节点中的值放入JSONArray
		//子节点下没有值或没有子节点
		if(backstageRequest.toString().equals("[]") || backstageRequest.toString().equals("[[]]")){
			//报错，返回
			returnMap.put("code", "TC0001");
			returnMap.put("message", "后台请求报文不全，请检查xml配置文件");
			return returnMap;
		}else {
			for(int n = 0 ; n < backstageRequest.size() ; n++){
				//多个节点
				try {
					//多个节点有值
					backstageJs = backstageRequest.getJSONObject(n);
					backstageJa = JSONArray.fromObject(backstageJs.get("colum"+json.get("@commType")));
					//多个节点没有值
					if (backstageJa.toString().equals("[[]]") || backstageJa.toString().equals("[]")) {
						//报错，返回
						returnMap.put("code", "TC0001");
						returnMap.put("message", "后台请求报文不全，请检查xml配置文件");
						return returnMap;
					}
					//单个节点
				} catch (Exception e) {
					backstageJa = JSONArray.fromObject(backstageRequest.get(n));
				}
			}
			
		}
		
		//根据通讯方式对应解析方法（1：socket，2：webservice，3：）
		if(json.get("@commType").toString().equals("1")){
			//遍历对应子节点的值
			for(int m = 0; m < backstageJa.size();m++){
				colum1 = backstageJa.getJSONObject(m);
				//取得jsonobject中的键，拼接字符串
				Iterator<String> iterator = colum1.keys();
				key = iterator.next();
				strValue += key + ",";
				//取得jsonobject中的值，放入数组
				value = colum1.get(key).toString().replace("[\"", "").replace("\"]", "");
				tempArray = value.split(",");
				System.out.println("长度：" + tempArray.length);
				
				//判断数组长度对应不同解析
				if(tempArray.length > 1){
					for(int t = 0;t < tempArray.length;t++){
						//解析后台请求中值的对应节点
						jsonObjectColum = JSONObject.fromObject(backstageJs.get(tempArray[t]));
						//判断是否有该节点
						if(jsonObjectColum.isNullObject() || null==jsonObjectColum){
							if(null == map.get(tempArray[t]) || map.get(tempArray[t]).toString().equals("")){
								result += "";
							}else {
								//将前台的值赋值
								result += map.get(tempArray[t]);
							}
						}else {
							//判断是否有value节点，是否有值，若有值则赋值，若没有则赋前台的值
							//判断是否有rules节点，子节点是否有值，有值则根据规则处理
							result += getRules(getValue(jsonObjectColum,map.get(tempArray[t])),jsonObjectColum);
						}
					}
					//将数组中字段对应的值相加得到最终值
					finallyResult = result;
				}else{
					//解析后台请求中值对应节点
					jsonObjectColum = JSONObject.fromObject(backstageJs.get(value));
					//判断是否有该节点
					if(jsonObjectColum.isNullObject() || null==jsonObjectColum){
						if(null == map.get(value) || map.get(value).toString().equals("")){
							finallyResult = "";
						}else {
							//将前台的值赋值
							finallyResult = map.get(value);
						}
					}else {
						//判断是否有value节点，是否有值，若有值则赋值，若没有则赋前台的值
						//判断是否有rules节点，子节点是否有值，有值则根据规则处理
						finallyResult = getRules(getValue(jsonObjectColum,map.get(value)),jsonObjectColum);
					}
				}
				//将处理后的值放入对应的报文域
				processMap.put(("FIELD" + key),finallyResult);
			}
		}
		//放入报文类型标识，MAC校验域，报文域
		processMap.put("MESSAGETYPE", json.get("@messagetype").toString());
		processMap.put("strMacFields", backstageJs.get("mac").toString());
		processMap.put("strValue", strValue.substring(0, strValue.length() - 1));
		//组装8583报文
		byte [] bateArray = iso8583Comm.organizeISO8583Msg(processMap);
		
		String a = DataConversion.byte2Hex(bateArray);
		System.out.println(a);
		
		returnMap.put("transMsgIP", Constant.mailAddressmap.get("transMsgIP"));
		returnMap.put("transMsgPort", Integer.parseInt(Constant.mailAddressmap.get("transMsgPort")));
		returnMap.put("transMsg", bateArray);
		returnMap.put("branchNum", Constant.mailAddressmap.get("branchNum"));
		returnMap.put("transInfo", map);
		
		returnMap.put("code", "TT000");
		returnMap.put("message", "success");
		return returnMap;
	}
	
	/**
	 * 接收后台响应，解析xml配置文件，按8583规范解析响应报文
	 * 获取xmlConfCache.xml中的response报文信息
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, String> commonResAdapter(byte[] response, String initName) {
		//解析后台响应
		ISO8583Comm iso8583Comm = new ISO8583Comm();
		JSONObject initObject = JSONObject.fromObject(Constant.initmap.get(initName));
		JSONObject backstage = JSONObject.fromObject(initObject.get("backstage"));
		JSONArray backstageResponse = JSONArray.fromObject(backstage.get("response"));
		JSONObject backstageResponseObj = new JSONObject();
		JSONArray  backstageResponseArr = new JSONArray();
		Map<String, String> processMap = new HashMap<String,String>();
		Map<String, String> responseMap = new HashMap<String,String>();
		Map<String, String> returnMap = new HashMap<String,String>();
		String key = "";
		String value = "";
		//遍历后台响应下子节点，将子节点中的值放入JSONArray
		//子节点下没有值或没有子节点
		if(backstageResponse.toString().equals("[]")||backstageResponse.toString().equals("[[]]")){
			//报错，返回
			returnMap.put("code", "TC0001");
			returnMap.put("message", "后台响应报文不全，请检查xml配置文件");
			return returnMap;
		}else {
			for(int n = 0 ; n < backstageResponse.size() ; n++){
				//多个节点
				try {
					//多个节点有值
					backstageResponseObj = backstageResponse.getJSONObject(n);
					backstageResponseArr = JSONArray.fromObject(backstageResponseObj.get("colum"+initObject.get("@commType")));
					//多个节点没有值
					if (backstageResponseArr.toString().equals("[[]]") || backstageResponseArr.toString().equals("[]")) {
						//报错，返回
						returnMap.put("code", "TC0001");
						returnMap.put("message", "后台响应报文不全，请检查xml配置文件");
						return returnMap;
					}
					//单个节点有值
				} catch (Exception e) {
					backstageResponseArr = JSONArray.fromObject(backstageResponse.get(n));
				}
			}
		}
		
		//根据通讯方式对应解析方法
		if(initObject.get("@commType").toString().equals("1")){
			JSONObject colum = new JSONObject();
			for(int m = 0; m < backstageResponseArr.size();m++){
				colum = backstageResponseArr.getJSONObject(m);
				//获得jsonobject对应的键和值放入map
				Iterator<String> iterator = colum.keys();
				key = iterator.next();
				value = colum.get(key).toString().replace("[\"", "").replace("\"]", "");
				processMap.put(key, value);
			}
		}
		//放入MAC校验域
		processMap.put("strMacFields", backstageResponseObj.get("mac").toString());
		//解析8585报文
		responseMap = iso8583Comm.analyzeISO8583MsgRes(processMap, response);
		
		//解析服务层响应报文
		if(responseMap != null || responseMap.size() > 0 || !responseMap.isEmpty()){
			for(Entry<String, String> entry : processMap.entrySet()){
				String entrykey = entry.getKey();
				String entryvalue = entry.getValue();
				if(entryvalue != null){
					returnMap.put(entrykey,responseMap.get("FIELD" + entryvalue));
				}
			}
			returnMap.put("code", "TT000");
			returnMap.put("message", "success");
		}else{
			returnMap.put("code", "TC0001");
			returnMap.put("message", "后台交易失败");
		}
		return returnMap;
	}
	
	/**
	 * 解析xml中的value标签
	 */
	public String getValue(JSONObject jsonObjectColum, String value) {

		String returnValue = "";
		//校验是否有value节点，节点是否有值
		if(null == jsonObjectColum.get("value") || jsonObjectColum.get("value").toString().equals("[]")){
			//校验前台入参对应字段是否有值
			if(null == value || value.equals("")){
				returnValue = "";
			}else {
				returnValue = value;
			}
		}else {
			returnValue = jsonObjectColum.get("value").toString();
		}

		return returnValue;
	}
	
	/**
	 * 解析xml中的rules标签
	 * @return
	 */
	@Override
	public String getRules(String value, JSONObject jsonObjectColum) {
		
		JSONArray rulesArr = new JSONArray();
		JSONObject rulesObj = new JSONObject();
		String ruletype = "";
		String ruleProcessing = "";
		String rulevalue = "";
		//校验是否有rules节点，节点是否有值
		if(null == jsonObjectColum.get("rules") || jsonObjectColum.get("rules").toString().equals("[]")){
			rulevalue = value;
		}else {
			rulesArr = JSONArray.fromObject(jsonObjectColum.get("rules"));
			//遍历rules子节点
			for (int i = 0; i < rulesArr.size(); i++) {
				rulesObj = JSONObject.fromObject(rulesArr.getJSONObject(i));
				ruletype = rulesObj.getString("ruletype");
				ruleProcessing = rulesObj.getString("ruleProcessing");
				//校验是否有ruletype和ruleProcessing节点，节点是否有值
				if(null == ruletype || null == ruleProcessing || ruletype.equals("[]") || ruleProcessing.equals("[]")){
					rulevalue = value;
				}else {
					//根据规则处理
					rulevalue = RuleDealUtil.getInstance().check(ruletype, ruleProcessing, value);
					value = rulevalue;
				}
			}
		}
		return rulevalue;
	}
	
	/**
	 * 暴露给服务层调用的方法
	 * @return
	 */
	@Override
	public Map<String, String> responseMsg(Map<String, String> requestMsg, String initName) {
		ISocket iSocket = new Socket();
        Client client = new Client();
        Map<String, String> returnMap = new HashMap<String, String>();
        Map<String, String> responsemap = new HashMap<String, String>();
        Map<String, Object> objmap = new HashMap<String, Object>();
        Map<String, Object> mapMsg = new HashMap<String, Object>();
        try {
        	//校验初始化
        	if(iSocket.init()){
        		objmap = iSocket.commonReqAdapter(requestMsg, initName);
        		//校验报文组装
        		if (objmap.get("code").toString().equals("TT000")) {
        			//调用socket通信
        			mapMsg = client.transMsg(objmap);
        			responsemap = iSocket.commonResAdapter((byte[])mapMsg.get("response"), initName);
        			//校验报文解析
        			if(responsemap.get("code").toString().equals("TT000")){
        				returnMap.put("code", "TT000");
            			returnMap.put("message", "success");
            			returnMap = responsemap;
        			}else {
        				returnMap.put("code", responsemap.get("code").toString());
    	        		returnMap.put("message", responsemap.get("message").toString());
					}
				}else {
					returnMap.put("code", objmap.get("code").toString());
	        		returnMap.put("message", objmap.get("message").toString());
				}
        	}else {
        		returnMap.put("code", "TC0001");
        		returnMap.put("message", "初始化失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return returnMap;
	}
	
	public static void main(String[] args) {
		ISocket iSocket = new Socket();
        Map<String, String> requestmap = new HashMap<String, String>();
        Map<String, String> responsemap = new HashMap<String, String>();
        
        requestmap.put("C_Pan", "6217380000001521");
        requestmap.put("C_PROCESSINGCODE", "000000");
        requestmap.put("aaa", "1245");
        requestmap.put("C_TransDate", "21102730");
        
        requestmap.put("C_TermTsn", "101010");
        requestmap.put("C_LocalTime", "112233");
        requestmap.put("C_LocalDate", "332211");
        requestmap.put("C_EntryModeCode", "021");
        requestmap.put("C_ConditionCode", "00");
        requestmap.put("C_Track2", "6217380000001521=00001200059310000");
        requestmap.put("C_Track3", "");
        requestmap.put("C_APTLID", "00000000");
        requestmap.put("C_PerReqMsg", "00000000");
        
        requestmap.put("C_PinBlock", "");
        responsemap = iSocket.responseMsg(requestmap,"T001");
        System.out.println(responsemap);
        /*
        requestmap.put("C_Pan", "6224325486473436");
        requestmap.put("C_PROCESSINGCODE", "020000");
        requestmap.put("C_PinBlock", "");
        responsemap = iSocket.responseMsg(requestmap,"T001");
        System.out.println(responsemap);
//        responsemap = iSocket.responseMsg(requestmap,"T002");
//        System.out.println(responsemap);
//        responsemap = iSocket.responseMsg(requestmap,"T003");
//        System.out.println(responsemap);
 * */
    }

}
