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
	 * ��ʼ��xml������Ϣ��common.xml������Ϣ��ͨѶ��ַ
	 * @return
	 */
	
	private boolean IinitOk = true;
	
	@SuppressWarnings({ "rawtypes" })
	public boolean init() {
		if(IinitOk)
		{
			//�趨Ϊ��ǰ�ļ���
			File directory  = new File("");
			List list = new ArrayList<String>();
			//��λ�ļ���������messageĿ¼
	    	ReadFromFile.findDir(list, directory.getAbsolutePath(), "message");
	    	int len = list.size();
	    	List listFile = new ArrayList<String>();
	    	//��λmessage������xml�����ļ�
	    	for(int i=0; i<len; i++)
	    	{
	    		ReadFromFile.isIneedFile(list.get(i).toString(), "xml", listFile);
	    	}
			len = listFile.size();
			String strMessage = null;
			JSONArray jsonArray = null;
			int arrLen = 0;
			JSONObject object = null;
			//����xml�ļ�����jsonobject
			for(int j=0; j<len; j++)
			{
				//��xml�ַ���ת��ΪJSON�ַ���
				strMessage = XmlExercise.xml2json(ReadFromFile.readFileByLines(listFile.get(j).toString()));
		    	jsonArray=JSONArray.fromObject(strMessage);
		    	arrLen = jsonArray.size();
		    	for(int i = 0; i < arrLen; i++){
		    	    object = jsonArray.getJSONObject(i);
		    	    //��������xml�����ļ���Ϣ����ȫ��map����ʼ��initmap���
		    		Constant.initmap.put(object.getString("@commName"), object.toString());
		    	}
			}
			System.out.println(Constant.initmap);
			
			//��λ�ļ���������ISO8583Ŀ¼
			list = new ArrayList<String>();
	    	listFile = new ArrayList<String>();
			ReadFromFile.findDir(list, directory.getAbsolutePath(), "ISO8583");
	    	len = list.size();
	    	//��λISO8583������xml�����ļ�
	    	for(int i=0; i<len; i++)
	    	{
	    		ReadFromFile.isIneedFile(list.get(i).toString(), "xml", listFile);
	    	}
			
			//���ó�ʼ��common.xml����
			//�õ�ISO8583��������
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
					//У��common.xml�Ƿ��Ѿ���ʼ��
					if(null == reqTrans || reqTrans.isEmpty() || reqTrans.size() <= 0){
						//����8583�����ļ�
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
					//У��common.xml�Ƿ��Ѿ���ʼ��
					if(null == resTrans || resTrans.isEmpty() || resTrans.size() <= 0){
						//����8583�����ļ�
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
			//�жϳ�ʼ��initmap�Ƿ����
	    	if (Constant.initmap.isEmpty() || null == Constant.initmap || Constant.initmap.size() == 0 || !flag) {
	    		return false;
			}else {
				//��ʼ��ͨѶ��ַ
				if(Constant.mailAddressmap.isEmpty() || null == Constant.mailAddressmap || Constant.mailAddressmap.size() == 0)
				{
					
					Constant.mailAddressmap.put("transMsgIP", "192.168.13.136");
					Constant.mailAddressmap.put("transMsgPort", "8001");
					Constant.mailAddressmap.put("branchNum", "");
					//���ӵ�ǰ��cur����ʷ��Ϊhis��
////					SysSystemParamDao sysDao = new SysSystemParamDao("cur");
////					SysSystemParam sysinfo = new SysSystemParam();
//					//�ӵ�ǰ��SysSystemParam����ȡ��������Ϣ
////					sysinfo = sysDao.querySystemParamByName(object.getString("@commBackName"));
//					if(null == sysinfo || sysinfo.toString().equals("")){
//						System.out.println("��̨δ��ѯ��"+object.getString("@commBackName")+"��������Ϣ������xml�����ļ�commBackName�ֶΣ���");
//						return false;
//					}else {
//						//��ͨѶ��ַ���˿ںţ����кŻ����ȫ��map
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
	 * ����ǰ̨���󣬽���xml�����ļ�����8583�淶��װ�ɱ���
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public Map<String, Object> commonReqAdapter(Map<String, String> map, String interName) {
		Map<String, String> processMap = new HashMap<String, String>();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		ISO8583Comm iso8583Comm = new ISO8583Comm();
		
		//У���ʼ��initmap�����ļ������Ƿ�����
		String curMap = Constant.initmap.get(interName);
		if(CommonUtil.checkString(curMap) == null)
		{
			//��������
			returnMap.put("code", "TC0001");
			returnMap.put("message", "�Ҳ�����Ϊ["+interName+"]ƥ���xml�ļ�");
			return returnMap;
		}
		JSONObject json=JSONObject.fromObject(Constant.initmap.get(interName));
		
		//����xml�����ļ����̨����������Ϣ����ֵ
		//������̨
		JSONObject backstage=JSONObject.fromObject(json.get("backstage"));
		//������̨����
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
		//������̨�������ӽڵ㣬���ӽڵ��е�ֵ����JSONArray
		//�ӽڵ���û��ֵ��û���ӽڵ�
		if(backstageRequest.toString().equals("[]") || backstageRequest.toString().equals("[[]]")){
			//��������
			returnMap.put("code", "TC0001");
			returnMap.put("message", "��̨�����Ĳ�ȫ������xml�����ļ�");
			return returnMap;
		}else {
			for(int n = 0 ; n < backstageRequest.size() ; n++){
				//����ڵ�
				try {
					//����ڵ���ֵ
					backstageJs = backstageRequest.getJSONObject(n);
					backstageJa = JSONArray.fromObject(backstageJs.get("colum"+json.get("@commType")));
					//����ڵ�û��ֵ
					if (backstageJa.toString().equals("[[]]") || backstageJa.toString().equals("[]")) {
						//��������
						returnMap.put("code", "TC0001");
						returnMap.put("message", "��̨�����Ĳ�ȫ������xml�����ļ�");
						return returnMap;
					}
					//�����ڵ�
				} catch (Exception e) {
					backstageJa = JSONArray.fromObject(backstageRequest.get(n));
				}
			}
			
		}
		
		//����ͨѶ��ʽ��Ӧ����������1��socket��2��webservice��3����
		if(json.get("@commType").toString().equals("1")){
			//������Ӧ�ӽڵ��ֵ
			for(int m = 0; m < backstageJa.size();m++){
				colum1 = backstageJa.getJSONObject(m);
				//ȡ��jsonobject�еļ���ƴ���ַ���
				Iterator<String> iterator = colum1.keys();
				key = iterator.next();
				strValue += key + ",";
				//ȡ��jsonobject�е�ֵ����������
				value = colum1.get(key).toString().replace("[\"", "").replace("\"]", "");
				tempArray = value.split(",");
				System.out.println("���ȣ�" + tempArray.length);
				
				//�ж����鳤�ȶ�Ӧ��ͬ����
				if(tempArray.length > 1){
					for(int t = 0;t < tempArray.length;t++){
						//������̨������ֵ�Ķ�Ӧ�ڵ�
						jsonObjectColum = JSONObject.fromObject(backstageJs.get(tempArray[t]));
						//�ж��Ƿ��иýڵ�
						if(jsonObjectColum.isNullObject() || null==jsonObjectColum){
							if(null == map.get(tempArray[t]) || map.get(tempArray[t]).toString().equals("")){
								result += "";
							}else {
								//��ǰ̨��ֵ��ֵ
								result += map.get(tempArray[t]);
							}
						}else {
							//�ж��Ƿ���value�ڵ㣬�Ƿ���ֵ������ֵ��ֵ����û����ǰ̨��ֵ
							//�ж��Ƿ���rules�ڵ㣬�ӽڵ��Ƿ���ֵ����ֵ����ݹ�����
							result += getRules(getValue(jsonObjectColum,map.get(tempArray[t])),jsonObjectColum);
						}
					}
					//���������ֶζ�Ӧ��ֵ��ӵõ�����ֵ
					finallyResult = result;
				}else{
					//������̨������ֵ��Ӧ�ڵ�
					jsonObjectColum = JSONObject.fromObject(backstageJs.get(value));
					//�ж��Ƿ��иýڵ�
					if(jsonObjectColum.isNullObject() || null==jsonObjectColum){
						if(null == map.get(value) || map.get(value).toString().equals("")){
							finallyResult = "";
						}else {
							//��ǰ̨��ֵ��ֵ
							finallyResult = map.get(value);
						}
					}else {
						//�ж��Ƿ���value�ڵ㣬�Ƿ���ֵ������ֵ��ֵ����û����ǰ̨��ֵ
						//�ж��Ƿ���rules�ڵ㣬�ӽڵ��Ƿ���ֵ����ֵ����ݹ�����
						finallyResult = getRules(getValue(jsonObjectColum,map.get(value)),jsonObjectColum);
					}
				}
				//��������ֵ�����Ӧ�ı�����
				processMap.put(("FIELD" + key),finallyResult);
			}
		}
		//���뱨�����ͱ�ʶ��MACУ���򣬱�����
		processMap.put("MESSAGETYPE", json.get("@messagetype").toString());
		processMap.put("strMacFields", backstageJs.get("mac").toString());
		processMap.put("strValue", strValue.substring(0, strValue.length() - 1));
		//��װ8583����
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
	 * ���պ�̨��Ӧ������xml�����ļ�����8583�淶������Ӧ����
	 * ��ȡxmlConfCache.xml�е�response������Ϣ
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public Map<String, String> commonResAdapter(byte[] response, String initName) {
		//������̨��Ӧ
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
		//������̨��Ӧ���ӽڵ㣬���ӽڵ��е�ֵ����JSONArray
		//�ӽڵ���û��ֵ��û���ӽڵ�
		if(backstageResponse.toString().equals("[]")||backstageResponse.toString().equals("[[]]")){
			//��������
			returnMap.put("code", "TC0001");
			returnMap.put("message", "��̨��Ӧ���Ĳ�ȫ������xml�����ļ�");
			return returnMap;
		}else {
			for(int n = 0 ; n < backstageResponse.size() ; n++){
				//����ڵ�
				try {
					//����ڵ���ֵ
					backstageResponseObj = backstageResponse.getJSONObject(n);
					backstageResponseArr = JSONArray.fromObject(backstageResponseObj.get("colum"+initObject.get("@commType")));
					//����ڵ�û��ֵ
					if (backstageResponseArr.toString().equals("[[]]") || backstageResponseArr.toString().equals("[]")) {
						//��������
						returnMap.put("code", "TC0001");
						returnMap.put("message", "��̨��Ӧ���Ĳ�ȫ������xml�����ļ�");
						return returnMap;
					}
					//�����ڵ���ֵ
				} catch (Exception e) {
					backstageResponseArr = JSONArray.fromObject(backstageResponse.get(n));
				}
			}
		}
		
		//����ͨѶ��ʽ��Ӧ��������
		if(initObject.get("@commType").toString().equals("1")){
			JSONObject colum = new JSONObject();
			for(int m = 0; m < backstageResponseArr.size();m++){
				colum = backstageResponseArr.getJSONObject(m);
				//���jsonobject��Ӧ�ļ���ֵ����map
				Iterator<String> iterator = colum.keys();
				key = iterator.next();
				value = colum.get(key).toString().replace("[\"", "").replace("\"]", "");
				processMap.put(key, value);
			}
		}
		//����MACУ����
		processMap.put("strMacFields", backstageResponseObj.get("mac").toString());
		//����8585����
		responseMap = iso8583Comm.analyzeISO8583MsgRes(processMap, response);
		
		//�����������Ӧ����
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
			returnMap.put("message", "��̨����ʧ��");
		}
		return returnMap;
	}
	
	/**
	 * ����xml�е�value��ǩ
	 */
	public String getValue(JSONObject jsonObjectColum, String value) {

		String returnValue = "";
		//У���Ƿ���value�ڵ㣬�ڵ��Ƿ���ֵ
		if(null == jsonObjectColum.get("value") || jsonObjectColum.get("value").toString().equals("[]")){
			//У��ǰ̨��ζ�Ӧ�ֶ��Ƿ���ֵ
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
	 * ����xml�е�rules��ǩ
	 * @return
	 */
	@Override
	public String getRules(String value, JSONObject jsonObjectColum) {
		
		JSONArray rulesArr = new JSONArray();
		JSONObject rulesObj = new JSONObject();
		String ruletype = "";
		String ruleProcessing = "";
		String rulevalue = "";
		//У���Ƿ���rules�ڵ㣬�ڵ��Ƿ���ֵ
		if(null == jsonObjectColum.get("rules") || jsonObjectColum.get("rules").toString().equals("[]")){
			rulevalue = value;
		}else {
			rulesArr = JSONArray.fromObject(jsonObjectColum.get("rules"));
			//����rules�ӽڵ�
			for (int i = 0; i < rulesArr.size(); i++) {
				rulesObj = JSONObject.fromObject(rulesArr.getJSONObject(i));
				ruletype = rulesObj.getString("ruletype");
				ruleProcessing = rulesObj.getString("ruleProcessing");
				//У���Ƿ���ruletype��ruleProcessing�ڵ㣬�ڵ��Ƿ���ֵ
				if(null == ruletype || null == ruleProcessing || ruletype.equals("[]") || ruleProcessing.equals("[]")){
					rulevalue = value;
				}else {
					//���ݹ�����
					rulevalue = RuleDealUtil.getInstance().check(ruletype, ruleProcessing, value);
					value = rulevalue;
				}
			}
		}
		return rulevalue;
	}
	
	/**
	 * ��¶���������õķ���
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
        	//У���ʼ��
        	if(iSocket.init()){
        		objmap = iSocket.commonReqAdapter(requestMsg, initName);
        		//У�鱨����װ
        		if (objmap.get("code").toString().equals("TT000")) {
        			//����socketͨ��
        			mapMsg = client.transMsg(objmap);
        			responsemap = iSocket.commonResAdapter((byte[])mapMsg.get("response"), initName);
        			//У�鱨�Ľ���
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
        		returnMap.put("message", "��ʼ��ʧ��");
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
