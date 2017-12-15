package com.hiaward.dataprocess.message.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hiaward.dataprocess.message.socket.util.DataConversion;


/**
 * ISO8583报文处理类
 * 
 * @version
 * <p>
 * 2010/12/8 王毅 First release
 * </p>
 * 
 */

public class ISO8583Comm {

	private final int CONSTRUCT_BUF_LEN = 1536;// 缓冲区数组大小

	// 8583成功交易返回码
	public final static String ISO8583RETCODE_SUCCEED = "00";
	// 8583成功交易返回码
	public final static String ISO8583RETDESC_SUCCEED = "成功";

	// 8583安全处理失败返回码
	public final static String ISO8583RETCODE_SECURITY_ERROR = "A7";

	// 8583MAC鉴别失败返回码
	public final static String ISO8583RETCODE_MAC_ERROR = "A0";
	// 8583MAC鉴别失败返回码
	public final static String ISO8583RETDESC_MAC_ERROR = "MAC鉴别失败";

	// 8583PIN 格式错
	public final static String ISO8583RETCODE_PIN_BLOCK_ERROR = "99";
	// 8583PIN 格式错
	public final static String ISO8583RETDESC_PIN_BLOCK_ERROR = "PIN 格式错";

	// 8583发卡方或交换中心不能操作返回码Issuer or switch is inoperative
	public final static String ISO8583RETCODE_INOPERATIVE_ERROR = "91";
	// 8583发卡方或交换中心不能操作返回码Issuer or switch is inoperative
	public final static String ISO8583RETDESC_INOPERATIVE_ERROR = "发卡方或交换中心不能操作";

	public final static String ISO8583RETCODE_INVOIDTRAN_ERROR = "57";
	// 8583发卡方或交换中心不能操作返回码Issuer or switch is inoperative
	public final static String ISO8583RETDESC_INVOIDTRAN_ERROR = "交换中心不支持该卡的此种交易";

	// 8583交换中心转发了原交易请求，但未收到发卡方应答时，交换中心直接向受理方应答为有缺陷的成功交易
	public final static String ISO8583RETCODE_COMM_ERROR = "A2";

	// 8583不予承兑，1、CVN验证失败 2、网上交易的交易信息超期送达
	public final static String ISO8583RETCODE_DONOTHONOR_ERROR = "05";
	// 8583不予承兑，1、CVN验证失败 2、网上交易的交易信息超期送达
	public final static String ISO8583RETDESC_DONOTHONOR_ERROR = "不予承兑";

	// 8583交换中心系统异常、失效
	public final static String ISO8583RETCODE_SWITCH_SYSTEM_MALFUNCTION = "96";
	// 8583交换中心系统异常、失效
	public final static String ISO8583RETDESC_SWITCH_SYSTEM_MALFUNCTION = "交换中心系统异常、失效";

	/**
	 * 组织ISO8583报文
	 * 
	 * @param strWay
	 *            渠道方向
	 * @param strChannelName
	 *            渠道名称
	 * @param map
	 *            存放待处理的数据
	 * @param oXmlCache
	 *            XML配置文件读取对象
	 * @return byte[] 组织完成的返回包
	 */
	public byte[] organizeISO8583Msg(Map<String, String> map) {
		// /////////////////////////////////////////////
		// 用来记录详细交易报文，调试是用
		StringBuffer strLogData = new StringBuffer(128);
		// /////////////////////////////////////////////

		// 分行号
		String strBranchNum = map.get("STRBRANCHNUM");
		// 终端编号
		String strTermNum = map.get("STRTERMNUM");
		// 交易码用来取配置文件交易配置信息
		StringBuffer bufTRCD = new StringBuffer(16);
		// 缓冲区数组大小
		int CONSTRUCT_BUF_LEN = 1536;
		// 初始化整个报文的长度
		int iOffset = 0;
		// 存放组包报文
		byte[] byteArrBuf = new byte[CONSTRUCT_BUF_LEN];

//		//保留4位报文域长度   阳泉非标准化8583报文特殊处理
//		System.arraycopy("0000".getBytes(), 0, byteArrBuf, iOffset, 4);
		
		// 初始化位图
		String strBitMap = "10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		// 该交易配置的报文域
		String strField = null;
		// 配置计算MAC的域
		String strMACField = null;
		// 该请求报文对应的响应报文的交易类型
		String strMsgTypeID = null;
		// 用于存放需要MAC校验的数据
		StringBuffer strMACData = new StringBuffer(128);
		// 是否需要MAC校验
		boolean bMACFlag = false;
		// 存放需要组的域
		Map<String, String> keyMap = new HashMap<String, String>(60, 0.8f);
		// 存放配置计算MAC的域
		Map<String, String> keyMACMap = new HashMap<String, String>(60, 0.8f);

		// debug
		// XmlConfCache.getInstance().init();

		// 得到ISO8583报文配置
		List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getReqFieldList();

		if (null != map.get("strValue") && null != map.get("strMacFields")) 
		{
			// 该交易所需要的报文域
			strField = map.get("strValue");
			// 该交易的MAC校验域
			strMACField = map.get("strMacFields");
			
		}
		else 
		{
			String strJrn = "organizeISO8583Msg beanMap is null 组请求报文时出错！找不到:" + map.get("strValue") + "对应的交易配置\r\n";
			System.out.println(strJrn);
//			strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
			return null;
		}

		// 初始化位图转换成char数组
		char[] cBitMap = strBitMap.toCharArray();
		// 把交易所需要的报文域拆分成数组
		String[] strFieldBuf = strField.split(",");
		// 用交易所需要的报文域生产位图MAP，里面该交易所用到的键名称，同时修改初始的位图
		for (int j = 0; j < strFieldBuf.length; j++) 
		{
			int iField = DataConversionBack.str2Int(strFieldBuf[j], -1);
			// 如果配置里有该域，但是map中没有此域的值，那么组包不组该域,128除外
			if (iField == 128 || (map.get("FIELD" + iField) != null && !"".equals(map.get("FIELD" + iField)))) 
			{
				keyMap.put("FIELD" + iField, "FIELD" + iField);
				cBitMap[iField - 1] = '1';
			}
		}

		// 生成该交易的位图
		StringBuffer BitMapBuf = new StringBuffer(128);
		BitMapBuf.append(cBitMap);
		strBitMap = BitMapBuf.toString();
		BitMapBuf.delete(0, BitMapBuf.length());

		// 把需校验的报文域形成MAP键值对
		if (keyMap.containsKey("FIELD128") && !(null == strMACField) && !("".equals(strMACField))) 
		{
			// 取出该类型最多的MAC校验域
			bMACFlag = true;
			String[] strMACFieldBuf = strMACField.split(",");
			for (int d = 0; d < strMACFieldBuf.length; d++) 
			{
				int iField = DataConversionBack.str2Int(strMACFieldBuf[d], -1);
				keyMACMap.put("FIELD" + iField, "FIELD" + iField);
			}
			// 消息标识必须放入校验
			keyMACMap.put("MESSAGETYPE", "MESSAGETYPE");
		}

		// 数据报文头3个域
//		int times = 1;
//		
//		//阳泉农商行报文头为长度，组装时不组装报文头
//			// 把报文头形成MAP键值对
//		for (int m = 0; m < times; m++) 
//		{
//			keyMap.put("HDFIELD" + (m + 1), "HDFIELD" + (m + 1));
//		}
		
		//保留4位报文域长度   阳泉非标准化8583报文特殊处理
		System.arraycopy("0000".getBytes(), 0, byteArrBuf, iOffset, 4);
		iOffset = iOffset + 4;
		
		
		// 把报文标识形成键值对
		keyMap.put("MESSAGETYPE", "MESSAGETYPE");

		// 把位图形成键值对
		keyMap.put("BITMAP", "BITMAP");
		// 放入初始的FIELD128域
		map.remove("FIELD128");
		map.put("FIELD128", "0000000000000000000000000000000000000000000000000000000000000000");
		map.remove("BITMAP");
		// 交易位图
		map.put("BITMAP", strBitMap);
		
//		iOffset += strBitMap.length();
		
		if (null != reqTrans) 
		{
			// 循环ISO8583公共报文配置对象
			for (int n = 0; n < reqTrans.size(); n++) 
			{
				// 取出报文域
				Map<String, String> fieldMap = (Map<String, String>) reqTrans.get(n);
				// 取出报文域名称
				String strFieldName = fieldMap.get("strName");
			
				// 该交易报文里是否包含此域
				if (keyMap.containsKey(strFieldName)) 
				{
					// 对报文域数据进行预处理，看看是否配置了默认值
					String strValue = DataConversionBack.getFieldDefVal(map.get(strFieldName), fieldMap.get("strValue"));
					// 对报文域值转为byte
					byte[] bValue = ValueToByte(fieldMap, strValue, map);
					
					
					if (null == bValue) 
					{
						String strJrn = "organizeISO8583Msg bValue is null 组请求报文时,转换报文域失败！\r\n";
//						strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//						LoggerUtil.w(ISO8583Comm.class, strJrn);
						return null;
					}
					// 配置了该域为MAC校验域,将用该域来组装MACDATA
					if (bMACFlag && keyMACMap.containsKey(strFieldName)) 
					{
						String strMACValue = new String(bValue).trim();
						if ("FIELD90".equals(strFieldName)) 
						{
							strMACValue = strMACValue.substring(0, 20); // 90域如果作为MAC校验域，只取前20位
						}
						strMACData.append(strMACValue).append(" ");
					}

					// 下面是和ESB测试过程中加上的详细日志，生产正式使用时必须关闭，以免出现问题
					// ///////////////////////////////////////////////////////////////////////////////
					try 
					{
						String strTempValue = null;
						int iType = -1;
						if (fieldMap.get("strType") != null && fieldMap.get("strType").length() != 0) 
						{
							iType = Integer.parseInt(fieldMap.get("strType"));
						}
						if (iType == 3) 
						{
							if (Integer.parseInt(fieldMap.get("strLength")) == 1) 
							{
								char[] cTemp = new String(bValue).toCharArray();
								int iTemp = cTemp[0];
								strTempValue = String.valueOf(iTemp);
								strTempValue = DataConversionBack.getZeroPrefixStr(strTempValue, 2);
							} 
							else if ("BITMAP".equals(fieldMap.get("strName"))) 
							{
								strTempValue = strValue;
							}
							else 
							{
								strTempValue = DataConversionBack.byte2Hex(bValue);
							}

						} 
						else if (iType == 4)//bcd压缩码
						{
							
							strTempValue = strValue;
						}
						else 
						{
							
							if(null != fieldMap.get("strName") && "FIELD55".equals(fieldMap.get("strName")))
							{
								strTempValue = DataConversionBack.cbcdTostr(bValue);
							}
							else
							{
								strTempValue = new String(bValue);
							}
							
						}

						strLogData.append(DataConversionBack.getSuffixStr(fieldMap.get("strName"), ' ', 13)).append('.').append(DataConversionBack.getSufCNfixStr(fieldMap.get("strCNDescr"), ' ', 50)).append("=  [ ").append(strTempValue).append(" ]|");
						System.out.println("域"+strFieldName+"长度"+strValue.length()+"["+strTempValue+"]");
					} 
					catch (Exception e) 
					{
						StringWriter sw = new StringWriter(1024 * 4);
						e.printStackTrace(new PrintWriter(sw));
						String strJrn = "ESB测试过程中组装详细日志出错！抛出异常:" + "\r\n";
//						strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + sw + "\r\n";
//						LoggerUtil.w(ISO8583Comm.class, strJrn);
					}

					// ////////////////////////////////////////////////////////////////////////////////////////
					// 将转换后的域数据放入byteArrBuf
					System.arraycopy(bValue, 0, byteArrBuf, iOffset, bValue.length);
					// 累加报文长度
					iOffset += bValue.length;

				}
			}
		}

		// MAC就是128域也就是FIELD128
		String strMac = "";
		// 如果配置了128域并且strMACData不为空
		if (bMACFlag)
		{
			// 计算MAC
			byte[] bMac = getMac(strMACData.toString(), map);
			strMACData.delete(0, strMACData.length());
			if (null == bMac) 
			{
				String strJrn = "organizeISO8583Msg bMac is null 组请求报文时,计算MAC失败！\r\n";
//				strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
				return null;
			}
			// 将MAC放入byteArrBuf
			System.arraycopy(bMac, 0, byteArrBuf, iOffset - 8, 8);
			strMac = new String(bMac);
		}
		
		String msglength = String.valueOf(iOffset - 4);
        while(msglength.length() < 4)
        {
            msglength = "0" + msglength;
        }
        System.arraycopy(msglength.getBytes(), 0, byteArrBuf, 0, 4);
		
		// 按照实际组装好的报文长度生成1个新的byte[]
		byte[] byteSendBuf = new byte[iOffset];
		// 拷贝实际发送报文数据
		System.arraycopy(byteArrBuf, 0, byteSendBuf, 0, iOffset);

		// 下面是和ESB测试过程中加上的详细日志
		// ///////////////////////////////////////////////////////////////////////////////////
		try {
			Object[] obj = strLogData.toString().split("\\|");
			for (int length = 0; length < obj.length; length++) 
			{
				if (length == 0) 
				{
					String strJrn = "上送ISO8583报文域：";
//					strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//					LoggerUtil.w(ISO8583Comm.class, strJrn);
				}
				if (obj[length].toString().indexOf("FIELD128") != -1)
				{
					String strJrn = "Debug：" + obj[length].toString().substring(0, obj[length].toString().indexOf('[')) + "[ " + strMac + " ]";
//					strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//					LoggerUtil.w(ISO8583Comm.class, strJrn);
					continue;
				}

				String strJrn = "Debug：" + obj[length].toString();
//				strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
////				JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
			}
		} 
		catch (Exception e) 
		{
			StringWriter sw = new StringWriter(1024 * 4);
			e.printStackTrace(new PrintWriter(sw));
			String strJrn = "ESB测试过程中加上的详细日志出错！抛出异常:" + "\r\n";
//			strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + sw + "\r\n";
////			JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
		}

		// /////////////////////////////////////////////////////////////////////////////////////

		if (keyMap != null) 
		{
			keyMap.clear();
			keyMap = null;
		}
		if (keyMACMap != null)
		{
			keyMACMap.clear();
			keyMACMap = null;
		}

		return byteSendBuf;

	}
	
	
	/**
	 * 解析8583报文
	 */
	public Map<String, String> analyzeISO8583MsgRes(Map<String, String> map, byte[] byResFiledVal)
	{

		boolean bolRet = false;
		// 测试打详细日志
		StringBuffer strLogData = new StringBuffer(128);
		// 用于存放需要MAC校验的数据
		StringBuffer strMACData = new StringBuffer(128);
		// 是否需要MAC校验
		boolean bMACFlag = false;
		// 报文特殊域拆解方法
		String strFomatFuncs = null;
		// 配置计算MAC的域
		String strMACField = null;
		// 存放需要解的域
		Map<String, String> keyMap = new HashMap<String, String>(60, 0.8f);
		// 存放返回的域和值
		Map<String, String> returnMap = new HashMap<String, String>();
		// 存放计算MAC的域
		Map<String, String> keyMACMap = new HashMap<String, String>(60, 0.8f);
		// 存放需要域特殊处理方法
		Map<String, String> FomatFuncMap = null;
		//取位图临时数组
		byte[] btemp = new byte[8];
		//取报文长度临时数组
		byte[] totalLength = new byte[4];
		//取报文交易类型临时数组
		byte[] bytearrMessageID = new byte[4];
		
		StringWriter sw = null;
		PrintWriter psw = null;
		try 
		{
			// ESB8583数据报文头长度3
			int iPagHeadLength = 6;
			// 初始化整个报文的长度
			int iOffset = 0;
			// 位图在报文的起始位置
			int iBitmap = iPagHeadLength + 4;
			
			//去除返回报文头
			//报文长度
//	        System.arraycopy(byResFiledVal, iOffset, totalLength, 0, 4);
//	        iOffset += 4;
//	        System.out.println(new String(totalLength));
	        
	        //取交易类型
	        System.arraycopy(byResFiledVal, iOffset, bytearrMessageID, 0, 4);
			iOffset = iOffset + 4;
			System.out.println(new String(bytearrMessageID));
			
			//取位图
			System.arraycopy(byResFiledVal, iOffset, btemp, 0, 8);
			iOffset += 8;
			//把64位主位图的转化二进制字符串
			String strBitMap = DataConversion.atob(btemp);
			
			  //Map 第一位是 1 时，代表还有扩展位图
		    if ("1".equals(strBitMap.substring(0, 1)))
		    {
		        System.arraycopy(byResFiledVal, iOffset, btemp, 0, 8);
		        strBitMap += DataConversion.atob(btemp);
		        iOffset += 8;
		    }
		    System.out.println("返回报文位图信息："+strBitMap);
			
			// 得到ISO8583报文配置
			List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getResFieldList();

			// 把位图转换成MAP键值对
			char[] cBitMap = strBitMap.toCharArray(); // 转换成char数组
			for (int i = 0; i < cBitMap.length; i++)
			{
				if (cBitMap[i] == '1') 
				{
					keyMap.put("FIELD" + (i + 1), "FIELD" + (i + 1));
				}
			}
			// 通过交易码查找bitmap配置文件中对应的配置信息
//			Map<String, String> beanMap = reqBitmap.get(strTRCD);
//			if (null != beanMap) 
			if(null != map.get("strFomatFuncs") || null != map.get("strMacFields"))
			{
				// 特殊处理域对应处理方法名
				strFomatFuncs = map.get("strFomatFuncs");
				// 该交易的MAC校验域
				strMACField = map.get("strMacFields");
			} 
			else 
			{
				String strJrn = "analyzeISO8583Msg beanMap is null 解响应报文时出错！找不到:" + "对应的交易配置\r\n";
				return null;
			}
			// 把需校验的报文域形成MAP键值对
			if (strMACField != null && strMACField.length() != 0) 
			{
				// 取出该类型最多的MAC校验域
				String[] strMACFieldBuf = strMACField.split(",");
				for (int d = 0; d < strMACFieldBuf.length; d++) 
				{
					// 把需MAC校验域放入MAP
					int iField = DataConversionBack.str2Int(strMACFieldBuf[d], -1);
					keyMACMap.put("FIELD" + iField, "FIELD" + iField);
				}
				// 消息标识必须放入校验
				keyMACMap.put("MESSAGETYPE", "MESSAGETYPE");
				bMACFlag = true;

			}
			// 把需要执行域转换方法形成MAP键值对
			if (strFomatFuncs != null && strFomatFuncs.length() != 0) 
			{
				FomatFuncMap = new HashMap<String, String>(20, 0.8f);
				// 把需过滤方法报文域形成Map键值对
				String[] strFomatFuncsBuf = strFomatFuncs.split(",");
				for (int j = 0; j < strFomatFuncsBuf.length; j++)
				{
					try 
					{
						// 取出前3位域号码
						int iField = DataConversionBack.str2Int(strFomatFuncsBuf[j].substring(0, 3), -1);
						// 从第4位数取方法名称
						String strFuncName = strFomatFuncsBuf[j].substring(3);
						FomatFuncMap.put("FIELD" + iField, strFuncName);
					} 
					catch (Exception e) 
					{
						sw = new StringWriter(1024 * 4);
						psw = new PrintWriter(sw);
						e.printStackTrace(psw);
						String strJrn = "analyzeISO8583Msg strFomatFuncs Exception 分解域转换方法时出错\r\n";
						return null;
					}
					finally
					{
						releaseRes(sw, psw);
					}
				}
			}
			// 报文头3个域
//			int times = 1;
//			// 组报文头
//			for (int j = 0; j < times; j++) 
//			{
//				keyMap.put("HDFIELD" + (j + 1), "HDFIELD" + (j + 1));
//			}
			// 把报文标识形成键值对
//			keyMap.put("MESSAGETYPE", "MESSAGETYPE");
//			// 把位图形成键值对
//			keyMap.put("BITMAP", "BITMAP");
			if (null != reqTrans) 
			{
				for (int m = 0; m < reqTrans.size(); m++) 
				{
					// 取出报文域
					Map<String, String> fieldMap = (Map<String, String>) reqTrans.get(m);
					// 取出报文域名称
					String strFieldName = fieldMap.get("strName");
					// 该交易报文里是否包含此域
					if (keyMap.containsKey(strFieldName))
					{
						// 解报文域出来的值
						String strFieldValue = null;
						// 该域是否需要进行特殊方法处理
						if (FomatFuncMap != null && FomatFuncMap.containsKey(strFieldName)) 
						{
							strFieldValue = fieldFomatFuncs(FomatFuncMap.get(strFieldName), byResFiledVal, iOffset, map);
						} 
						else 
						{
							// 将报文域解析成字符串
							strFieldValue = ByteToValue(fieldMap, byResFiledVal, iOffset);
						}
						// 当前解到的报文位置
						iOffset = Integer.valueOf(strFieldValue.substring(0, 4), 10).intValue();

						// 取出后报文域的值
						strFieldValue = strFieldValue.substring(4);
						
						// 下面是和ESB测试过程中加上的详细日志，生产正式使用时必须关闭，以免出现问题
						// ////////////////////////////////////////////////////////////////////////

						String strTempValue = "";
						int itype = -1;
						if (fieldMap.get("strType") != null && fieldMap.get("strType").length() != 0) 
						{
							itype = Integer.parseInt(fieldMap.get("strType"));
						}
						if (itype == 3 && null != strFieldValue && !("".equals(strFieldValue)))
						{
							if (Integer.parseInt(fieldMap.get("strLength")) == 1) 
							{
								strTempValue = DataConversionBack.byte2Hex(DataConversionBack.atoc(strFieldValue));
							} 
							else if ("FIELD128".equals(strFieldName) || "BITMAP".equals(strFieldName)) 
							{
								strTempValue = strFieldValue;
							} 
							else 
							{
								strTempValue = DataConversionBack.byte2Hex(DataConversionBack.atoc(strFieldValue));
							}

						} 
						else 
						{
							strTempValue = strFieldValue;
						}

						strLogData.append(DataConversionBack.getSufCNfixStr(strFieldName, ' ', 13)).append(DataConversionBack.getSufCNfixStr(fieldMap.get("strCNDescr"), ' ', 50)).append("=  [ ").append(strTempValue).append(" ]|");
						// ////////////////////////////////////////////////////////////////////////

						// 配置了该域为MAC校验域
						if (bMACFlag && keyMACMap.containsKey(strFieldName)) 
						{
							String strMACValue = strFieldValue;
							if ("FIELD90".equals(strFieldName)) 
							{
								// 90域如果作为MAC校验域，只取前20位
								strMACValue = strMACValue.substring(0, 20);
							}
							strMACData.append(strMACValue).append(" ");
						}
						// 根据配置的数据长度类型来取出解析后的报文域中的数据
						if (strFieldValue.length() != 0) 
						{
							int iElementType = Integer.parseInt(fieldMap.get("strElementType"));
							switch (iElementType)
							{
							case 2:
								strFieldValue = strFieldValue.substring(2);// 2位数变长
								break;
							case 3:
								strFieldValue = strFieldValue.substring(3);// 3位数变长
								break;
							case 4:
								strFieldValue = strFieldValue.substring(4);// 3位数变长
								break;
							}
							// map中重新放入解包后的数据
							map.remove(strFieldName);
							returnMap.put(strFieldName, strFieldValue);
							System.out.println("域名称：" + strFieldName + "==域值：[" + strFieldValue + "]");
						}
					}
				}
			}

			// 下面是和ESB测试过程中加上的详细日志，生产正式使用时必须关闭，以免出现问题
			// ///////////////////////////////////////////////////////////
			Object[] obj = strLogData.toString().split("\\|");
			for (int length = 0; length < obj.length; length++) 
			{
				if (length == 0) 
				{
					String strJrn = "返回ISO8583报文域：";
				}
			}

			map.put("MACDATA", strMACData.toString().trim());
			// 如果交易成功就开始验证MAC 这个还没有测试
			if (map.containsKey("FIELD39")) 
			{
				if (bMACFlag) 
				{ 
					// 如果需要校验MAC,就需要计算MAC和收到的128域比较，相等则成功
					byte[] bMac = getMac(strMACData.toString(), map);
					strMACData.delete(0, strMACData.length());
					if (null == bMac) 
					{
						map.put("XBANKMTBS_ERRO_CODE", "-21");
					}
					if (!new String(bMac).equals(map.get("FIELD128"))) 
					{
						map.remove("XBANKMTBS_RETCODE");
						map.put("XBANKMTBS_RETCODE", "57");
					}
				}
			}
			bolRet = true;
		} 
		catch (Exception e)
		{

			// 下面是和ESB测试过程中加上的详细日志，生产正式使用时必须关闭，以免出现问题
			// ///////////////////////////////////////////////////////////
//			Object[] obj = strLogData.toString().split("\\|");
//			for (int length = 0; length < obj.length; length++) 
//			{
//				if (length == 0) 
//				{
//					String strJrn = "返回ISO8583报文域：";
//				}
//
//			}
//			if (strMACData != null) 
//			{
//			}

			sw = new StringWriter(1024 * 4);
			psw = new PrintWriter(sw);
			e.printStackTrace(psw);
			bolRet = false;
			e.printStackTrace();
		} 
		finally
		{
			if (keyMap != null)
			{
				keyMap.clear();
				keyMap = null;
			}
			if (keyMACMap != null) 
			{
				keyMACMap.clear();
				keyMACMap = null;
			}
			if (FomatFuncMap != null) 
			{
				FomatFuncMap.clear();
				FomatFuncMap = null;
			}
			releaseRes(sw, psw);
		}
		return returnMap;
	
	};
	

	/**
	 * 分解ISO8583报文
	 * 
	 * @param strWay
	 *            渠道方向
	 * @param strChannelName
	 *            渠道名称
	 * @param map
	 *            存放处理后的数据
	 * @param byResFiledVal
	 *            报文数据
	 * @param oXmlCache
	 *            XML配置文件读取对象
	 * @return Map 报文分析结果
	 */
	public Map<String, String> analyzeISO8583Msg(Map<String, String> map, byte[] byResFiledVal) 
	{
		boolean bolRet = false;
		// //////////////////////////////////////////////////
		// 测试打详细日志
		StringBuffer strLogData = new StringBuffer(128);
		// ///////////////////////////////////////////////////
		// 分行号
//		String strBranchNum = map.get("STRBRANCHNUM");
		// 终端编号
//		String strTermNum = map.get("STRTERMNUM");
		// 用于存放需要MAC校验的数据
		StringBuffer strMACData = new StringBuffer(128);
		// 是否需要MAC校验
		boolean bMACFlag = false;
		// 报文特殊域拆解方法
		String strFomatFuncs = null;
		// 配置计算MAC的域
		String strMACField = null;
		// 存放需要解的域
		Map<String, String> keyMap = new HashMap<String, String>(60, 0.8f);
		// 存放返回的域和值
		Map<String, String> returnMap = new HashMap<String, String>();
		// 存放计算MAC的域
		Map<String, String> keyMACMap = new HashMap<String, String>(60, 0.8f);
		// 存放需要域特殊处理方法
		Map<String, String> FomatFuncMap = null;
		StringWriter sw = null;
		PrintWriter psw = null;
		try 
		{
			// ESB8583数据报文头长度3
			int iPagHeadLength = 6;
			// 初始化整个报文的长度
			int iOffset = 0;
			// 位图在报文的起始位置
			int iBitmap = iPagHeadLength + 4;
			// 取出这个交易的交易代码
			String strTRCD = null;
			if (map.get("PROCESSINGCODE") != null && map.get("RESMESSAGETYPE") != null)
			{
				strTRCD = map.get("RESMESSAGETYPE") + "_" + map.get("PROCESSINGCODE");
			}
			// 得到ISO8583报文配置
//			List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getFieldList();
			List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getReqFieldList();
			// 得到交易位图配置
			//Map<String, Map<String, String>> reqBitmap = XmlConfCache.getInstance().getProCodeMap();
			// 解析后得到的位图
			byte[] btemp = new byte[16];
			System.arraycopy(byResFiledVal, iBitmap, btemp, 0, 16);
			String strBitMap = DataConversionBack.atob(btemp);
//			System.out.println("位图：" + strBitMap);

			// 把位图转换成MAP键值对
			char[] cBitMap = strBitMap.toCharArray(); // 转换成char数组
			for (int i = 0; i < cBitMap.length; i++)
			{
				if (cBitMap[i] == '1') 
				{
					keyMap.put("FIELD" + (i + 1), "FIELD" + (i + 1));
				}
			}
			// 通过交易码查找bitmap配置文件中对应的配置信息
//			Map<String, String> beanMap = reqBitmap.get(strTRCD);
//			if (null != beanMap) 
			if(null != map.get("strFomatFuncs") || null != map.get("strMacFields"))
			{
				// 特殊处理域对应处理方法名
				strFomatFuncs = map.get("strFomatFuncs");
				// 该交易的MAC校验域
				strMACField = map.get("strMacFields");
			} 
			else 
			{
				String strJrn = "analyzeISO8583Msg beanMap is null 解响应报文时出错！找不到:" + strTRCD + "对应的交易配置\r\n";
//				strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//				JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
				return null;
			}
			// 把需校验的报文域形成MAP键值对
			if (strMACField != null && strMACField.length() != 0) 
			{
				// 取出该类型最多的MAC校验域
				String[] strMACFieldBuf = strMACField.split(",");
				for (int d = 0; d < strMACFieldBuf.length; d++) 
				{
					// 把需MAC校验域放入MAP
					int iField = DataConversionBack.str2Int(strMACFieldBuf[d], -1);
					keyMACMap.put("FIELD" + iField, "FIELD" + iField);
				}
				// 消息标识必须放入校验
				keyMACMap.put("MESSAGETYPE", "MESSAGETYPE");
				bMACFlag = true;

			}
			// 把需要执行域转换方法形成MAP键值对
			if (strFomatFuncs != null && strFomatFuncs.length() != 0) 
			{
				FomatFuncMap = new HashMap<String, String>(20, 0.8f);
				// 把需过滤方法报文域形成Map键值对
				String[] strFomatFuncsBuf = strFomatFuncs.split(",");
				for (int j = 0; j < strFomatFuncsBuf.length; j++)
				{
					try 
					{
						// 取出前3位域号码
						int iField = DataConversionBack.str2Int(strFomatFuncsBuf[j].substring(0, 3), -1);
						// 从第4位数取方法名称
						String strFuncName = strFomatFuncsBuf[j].substring(3);
						FomatFuncMap.put("FIELD" + iField, strFuncName);
					} 
					catch (Exception e) 
					{
						sw = new StringWriter(1024 * 4);
						psw = new PrintWriter(sw);
						e.printStackTrace(psw);
						String strJrn = "analyzeISO8583Msg strFomatFuncs Exception 分解域转换方法时出错\r\n";
//						strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + sw + "\r\n";
//						JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//						LoggerUtil.w(ISO8583Comm.class, strJrn);
						return null;
					}
					finally
					{
						releaseRes(sw, psw);
					}
				}
			}
			// 报文头3个域
			int times = 1;
			// 组报文头
			for (int j = 0; j < times; j++) 
			{
				keyMap.put("HDFIELD" + (j + 1), "HDFIELD" + (j + 1));
			}
			// 把报文标识形成键值对
			keyMap.put("MESSAGETYPE", "MESSAGETYPE");
			// 把位图形成键值对
			keyMap.put("BITMAP", "BITMAP");
			if (null != reqTrans) 
			{
				for (int m = 0; m < reqTrans.size(); m++) 
				{
					// 取出报文域
					Map<String, String> fieldMap = (Map<String, String>) reqTrans.get(m);
					// 取出报文域名称
					String strFieldName = fieldMap.get("strName");
					// 该交易报文里是否包含此域
					if (keyMap.containsKey(strFieldName))
					{
						// 解报文域出来的值
						String strFieldValue = null;
						// 该域是否需要进行特殊方法处理
						if (FomatFuncMap != null && FomatFuncMap.containsKey(strFieldName)) 
						{
							strFieldValue = fieldFomatFuncs(FomatFuncMap.get(strFieldName), byResFiledVal, iOffset, map);
						} 
						else 
						{
							// 将报文域解析成字符串
							strFieldValue = ByteToValue(fieldMap, byResFiledVal, iOffset);
						}
						// 当前解到的报文位置
						iOffset = Integer.valueOf(strFieldValue.substring(0, 4), 10).intValue();

						// 取出后报文域的值
						strFieldValue = strFieldValue.substring(4);
						
						// 这个还没有测试到
						if (!"0800".equals(map.get("MESSAGETYPE")) && "FIELD128".equals(strFieldName)) 
						{
							strFieldValue = new String(DataConversionBack.hex2Byte(strFieldValue));
						}

						// 下面是和ESB测试过程中加上的详细日志，生产正式使用时必须关闭，以免出现问题
						// ////////////////////////////////////////////////////////////////////////

						String strTempValue = "";
						int itype = -1;
						if (fieldMap.get("strType") != null && fieldMap.get("strType").length() != 0) 
						{
							itype = Integer.parseInt(fieldMap.get("strType"));
						}
						if (itype == 3 && null != strFieldValue && !("".equals(strFieldValue)))
						{
							if (Integer.parseInt(fieldMap.get("strLength")) == 1) 
							{
								strTempValue = DataConversionBack.byte2Hex(DataConversionBack.atoc(strFieldValue));
							} 
							else if ("FIELD128".equals(strFieldName) || "BITMAP".equals(strFieldName)) 
							{
								strTempValue = strFieldValue;
							} 
							else 
							{
								strTempValue = DataConversionBack.byte2Hex(DataConversionBack.atoc(strFieldValue));
							}

						} 
						else 
						{
							strTempValue = strFieldValue;
						}

						strLogData.append(DataConversionBack.getSufCNfixStr(strFieldName, ' ', 13)).append(DataConversionBack.getSufCNfixStr(fieldMap.get("strCNDescr"), ' ', 50)).append("=  [ ").append(strTempValue).append(" ]|");
						// ////////////////////////////////////////////////////////////////////////

						// 配置了该域为MAC校验域
						if (bMACFlag && keyMACMap.containsKey(strFieldName)) 
						{
							String strMACValue = strFieldValue;
							if ("FIELD90".equals(strFieldName)) 
							{
								// 90域如果作为MAC校验域，只取前20位
								strMACValue = strMACValue.substring(0, 20);
							}
							strMACData.append(strMACValue).append(" ");
						}
						// 根据配置的数据长度类型来取出解析后的报文域中的数据
						if (strFieldValue.length() != 0) 
						{
							int iElementType = Integer.parseInt(fieldMap.get("strElementType"));
							switch (iElementType)
							{
							case 2:
								strFieldValue = strFieldValue.substring(2).trim();// 2位数变长
								break;
							case 3:
								strFieldValue = strFieldValue.substring(3).trim();// 3位数变长
								break;
							case 4:
								strFieldValue = strFieldValue.substring(4).trim();// 3位数变长
								break;
							}
							// map中重新放入解包后的数据
							map.remove(strFieldName);
							returnMap.put(strFieldName, strFieldValue);
							System.out.println("域名称：" + strFieldName + "=-=域值：" + strFieldValue);
						}
					}
				}
			}

			// 下面是和ESB测试过程中加上的详细日志，生产正式使用时必须关闭，以免出现问题
			// ///////////////////////////////////////////////////////////
			Object[] obj = strLogData.toString().split("\\|");
			for (int length = 0; length < obj.length; length++) 
			{
				if (length == 0) 
				{
					String strJrn = "返回ISO8583报文域：";
//					strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
////					JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//					LoggerUtil.w(ISO8583Comm.class, strJrn);
				}
//				String strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + obj[length].toString() + "\r\n";
////				JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
			}
			// /////////////////////////////////////////////////////////////

			map.put("MACDATA", strMACData.toString().trim());
//			String strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "From ESB macDataBuff Map SUCCESS 分解ESB响应报文成功 macDataBuff=" + strMACData.toString().trim() + "\r\n";
////			JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
			// 如果交易成功就开始验证MAC 这个还没有测试
			if (map.containsKey("FIELD39")) 
			{
				if (bMACFlag) 
				{ 
					// 如果需要校验MAC,就需要计算MAC和收到的128域比较，相等则成功
					byte[] bMac = getMac(strMACData.toString(), map);
					strMACData.delete(0, strMACData.length());
					if (null == bMac) 
					{
						map.put("XBANKMTBS_ERRO_CODE", "-21");
					}
					if (!new String(bMac).equals(map.get("FIELD128"))) 
					{
						map.remove("XBANKMTBS_RETCODE");
						map.put("XBANKMTBS_RETCODE", "57");
					}
				}
			}
			bolRet = true;
//			strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "success map:" + map + "\r\n";
////			JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
		} 
		catch (Exception e)
		{

			// 下面是和ESB测试过程中加上的详细日志，生产正式使用时必须关闭，以免出现问题
			// ///////////////////////////////////////////////////////////
			Object[] obj = strLogData.toString().split("\\|");
			for (int length = 0; length < obj.length; length++) 
			{
				if (length == 0) 
				{
					String strJrn = "返回ISO8583报文域：";
//					strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
////					JournalThread.getInstance().append(strBranchNum,JournalThread.LEVEL_3, strJrn);
//					LoggerUtil.w(ISO8583Comm.class, strJrn);
				}

//				String strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + obj[length].toString() + "\r\n";
////				JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
			}
			// /////////////////////////////////////////////

//			String strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "From ESB analyzeISO8583Msg Map Exception 分解ESB响应报文出现异常 MAP:" + map + "\r\n";
////			JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
			if (strMACData != null) 
			{
//				strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "From ESB macDataBuff Map Exception 分解ESB响应报文出现异常 macDataBuff:" + strMACData.toString().trim() + "\r\n";
////				JournalThread.getInstance().append(strBranchNum,JournalThread.LEVEL_3, strJrn);
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
			}

			sw = new StringWriter(1024 * 4);
			psw = new PrintWriter(sw);
			e.printStackTrace(psw);
//			strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "ISO8583Comm.analyzeISO8583Msg Exception: 分解ESB响应报文出现异常 :" + sw + "\r\n";
////			JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
			bolRet = false;
		} 
		finally
		{
			if (keyMap != null)
			{
				keyMap.clear();
				keyMap = null;
			}
			if (keyMACMap != null) 
			{
				keyMACMap.clear();
				keyMACMap = null;
			}
			if (FomatFuncMap != null) 
			{
				FomatFuncMap.clear();
				FomatFuncMap = null;
			}
			releaseRes(sw, psw);
		}
		return returnMap;
	}

	public String fieldFomatFuncs(String strFuncName, byte[] reqmsg, int curLenAll, Map<String, String> map) 
	{
		String strVal = null;
		StringWriter sw = null;
		PrintWriter psw = null;
		try
		{
			Class<?> defClass = Class.forName("com.hiaward.exchange.ISO8583Comm");
			Method defMed = defClass.getMethod(strFuncName, new Class[] {byte[].class, int.class, Map.class });
			strVal = (String) defMed.invoke(defClass.newInstance(), new Object[] { reqmsg, curLenAll, map });
		} 
		catch (Exception e) 
		{
			sw = new StringWriter(1024 * 4);
			psw = new PrintWriter(sw);
			e.printStackTrace(psw);
			// 分行号
			String strBranchNum = map.get("STRBRANCHNUM");
			// 终端编号
			String strTermNum = map.get("STRTERMNUM");
//			String strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "ISO8583Comm.fieldFomatFuncs Exception:" + sw + "\r\n";
////			JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
		}
		finally
		{
			releaseRes(sw, psw);
		}
		return strVal;
	}

	public byte[] getUnionPayMacData(String macDataBuff) 
	{

		byte[] macData64 = null;
		StringWriter sw = null;
		PrintWriter psw = null;
		try
		{
			String MACData = macDataBuff.toUpperCase(); // 转换为大写
			MACData = MACData.replaceAll("[^A-Z0-9,.' ']", "");// 除了字母(A-Z)，数字(0-9)，空格，逗号(，)和点号(.)以外的字符都删去
			MACData = MACData.replaceAll("[ ]+", " ");// 多于一个的连续空格，由一个空格代替
			String strMacData = MACData.trim();
			byte[] macDataBt = strMacData.getBytes();
			int macLen = macDataBt.length;

			if ((macLen % 8) != 0)
				macData64 = new byte[macLen + (8 - macLen % 8)];
			else
				macData64 = new byte[macLen];
			System.arraycopy(macDataBt, 0, macData64, 0, macLen);
			if (macLen % 8 != 0) 
			{
				for (int j = 0; j < 8 - macLen % 8; j++) 
				{
					macData64[macLen + j] = 0x00;
				}
			}

		} 
		catch (Exception e) 
		{
			sw = new StringWriter(1024 * 4);
			psw = new PrintWriter(sw);
			e.printStackTrace(psw);
		}
		finally
		{
			releaseRes(sw, psw);
		}
		return macData64;
	}

	/**
	 * 根据MAC数据计算MAC
	 * 
	 * @param strMac
	 *            计算MAC的数据
	 * @return byte[] 计算出来的MAC
	 */
	/*private byte[] getMac(String strMac, Map<String, String> map) 
	{
		byte[] bMac64 = null;
		// 转换为大写
		String MACData = strMac.toString().toUpperCase();

		// 除了字母(A-Z)，数字(0-9)，空格，逗号(，)和点号(.)以外的字符都删去
		MACData = MACData.replaceAll("[^A-Z0-9,.' ']", "");

		// 多于一个的连续空格，由一个空格代替
		MACData = MACData.replaceAll("[ ]+", " ");

		String strMacData = MACData.trim();
		//System.out.println("strMacData========================"+strMacData);
		byte[] macDataBt = strMacData.getBytes();
		int macLen = macDataBt.length;

		if ((macLen % 8) != 0) 
		{
			bMac64 = new byte[macLen + (8 - macLen % 8)];
		} 
		else 
		{
			bMac64 = new byte[macLen];
		}
		System.arraycopy(macDataBt, 0, bMac64, 0, macLen);
		if (macLen % 8 != 0) 
		{
			for (int j = 0; j < 8 - macLen % 8; j++)
			{
				bMac64[macLen + j] = 0x00;
			}
		}
		String	strMac64 = new String(bMac64);
		String strCryptorIP = InitData.getInstance().getStrCryptorIP();
		int iCryptorPort = InitData.getInstance().getICryptorPort();
		String strMACValue = null;
		try 
		{
			UnionAPI unionAPI = new UnionAPI(strCryptorIP, iCryptorPort, 60, "at");
			strMACValue = unionAPI.UnionGenerateChinaPayMac("atmp.esb.zak", strMac64.length(), strMac64);
			//System.out.println("strMACValue==============================="+strMACValue);
		} 
		catch (Exception e)
		{
			StringWriter sw = new StringWriter(1024 * 4);
			e.printStackTrace(new PrintWriter(sw));
			String strJrn = "计算MAC请求报文出错！抛出异常:" + "\r\n";
			strJrn = "["+ new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull()+ "]" + strJrn + sw + "\r\n";
			JournalThreadNX.getInstance().append(map.get("STRBRANCHNUM"), map.get("STRTERMNUM"), JournalThreadNX.LEVEL_3, strJrn);
		}

		if (null == strMACValue) 
		{
			return null;
		} 
		else 
		{
			return strMACValue.getBytes();
		}
	}*/

	/**
	 * 根据MAC数据计算MAC
	 * @param strMac           计算MAC的数据
	 * @return byte[]          计算出来的MAC
	 */
	private byte[]  getMac(String strMac, Map<String, String> map) 
	{
	      byte[]	bMac64 = null;
	      //转换为大写
	      String  MACData = strMac.toString().toUpperCase();
	      
	      //除了字母(A-Z)，数字(0-9)，空格，逗号(，)和点号(.)以外的字符都删去
		  MACData =MACData.replaceAll("[^A-Z0-9,.' ']", "");
		  
		  //多于一个的连续空格，由一个空格代替
		  MACData =MACData.replaceAll("[ ]+", " ");
		  
		  String strMacData = MACData.trim();
		  byte[] macDataBt=strMacData.getBytes();
		  int macLen=macDataBt.length;
		
		  if((macLen % 8)!=0)
		  {
			  bMac64=new byte[macLen+(8 - macLen % 8)];
		  }
		  else
		  {
			  bMac64=new byte[macLen];
		  }
		  System.arraycopy(macDataBt, 0, bMac64, 0, macLen);
		  if (macLen % 8 != 0)
		  {
			  for (int  j = 0; j < 8 - macLen % 8; j++)
			  {
			     bMac64[macLen + j] = 0x00;
			  }
		  }	
		  String strMacKey = "";
		  String strMACValue = "";
		  //String strMacKey = getWorkingKey.getInstance().getMacKey();	
		     
	      //String strMACValue = new Sjl06t(true).generateMAC(strMacKey, bMac64);
		 
		  if(null == strMACValue)
		  {
			 return null;	
		  }
		  else
		  {
			 return strMACValue.getBytes();
		  }
	}

	/**
	 * 将byte[]转为域值
	 * 
	 * @param entity
	 *            报文域配置实体bean
	 * @param byResFiledVal
	 *            报文数据
	 * @param iOffset
	 *            当前分解偏移量
	 * @return String 域值
	 */
	private String ByteToValue(Map<String, String> fieldMap, byte[] byResFiledVal, int iOffset) 
	{
		String strFieldValue = null;
		// 域数据长度
		int iFieldLength = Integer.parseInt(fieldMap.get("strLength").trim());
		// 域类型
		int iElementType = Integer.parseInt(fieldMap.get("strElementType").trim());

		// 域名称
		String strFieldName = fieldMap.get("strName");
		if (iElementType == 1) // FIX
		{
			// 域数据类型
			int iStrType = Integer.parseInt(fieldMap.get("strType"));
			if (iStrType == 1 || iStrType == 2) // 字符串
			{

				byte[] bFieldValue = new byte[iFieldLength];
				System.arraycopy(byResFiledVal, iOffset, bFieldValue, 0, iFieldLength);
				try {
					strFieldValue = new String(bFieldValue,"GBK");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iOffset += iFieldLength;

			} 
			else if (iStrType == 3) // 二进制
			{
				if ("FIELD52".equals(strFieldName) || "FIELD128".equals(strFieldName) || "FIELD96".equals(strFieldName)) 
				{
					byte[] bFieldValue = new byte[iFieldLength];
					System.arraycopy(byResFiledVal, iOffset, bFieldValue, 0, iFieldLength);
					strFieldValue = DataConversionBack.byte2Hex(bFieldValue);
					iOffset = iOffset + iFieldLength;
				} 
				else 
				{
					byte[] bFieldValue = new byte[iFieldLength];
					System.arraycopy(byResFiledVal, iOffset, bFieldValue, 0, iFieldLength);
					strFieldValue = DataConversionBack.atob(bFieldValue);
					iOffset += iFieldLength;
				}
			}
		} 
		else if (iElementType == 2) // LLVAR
		{
			byte[] bLen = new byte[2];
			System.arraycopy(byResFiledVal, iOffset, bLen, 0, 2);
			iOffset += 2;
			String strLen = new String(bLen);
			int iLen = Integer.parseInt(strLen, 10);
			byte[] bFieldValue = new byte[iLen];
			System.arraycopy(byResFiledVal, iOffset, bFieldValue, 0, iLen);
			iOffset += iLen;
			try {
				strFieldValue = new String(bLen) + new String(bFieldValue,"GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		else if (iElementType == 3) // LLLVAR
		{
			byte[] bLen = new byte[3];
			System.arraycopy(byResFiledVal, iOffset, bLen, 0, 3);
			iOffset += 3;
			String strLen = new String(bLen);
			int iLen = Integer.parseInt(strLen, 10);
			if (0 != iLen)
			{
				byte[] bFieldValue = new byte[iLen];
				System.arraycopy(byResFiledVal, iOffset, bFieldValue, 0, iLen);
				iOffset += iLen;
				try {
					strFieldValue = new String(bLen) + new String(bFieldValue,"GBK");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} 
			else 
			{
				strFieldValue = new String(bLen);
			}

		}
		else if (iElementType == 4) // LLLVAR
		{
			byte[] bLen = new byte[4];
			System.arraycopy(byResFiledVal, iOffset, bLen, 0, 4);
			iOffset += 4;
			String strLen = new String(bLen);
			int iLen = Integer.parseInt(strLen, 10);
			if (0 != iLen)
			{
				byte[] bFieldValue = new byte[iLen];
				System.arraycopy(byResFiledVal, iOffset, bFieldValue, 0, iLen);
				iOffset += iLen;
				try {
					strFieldValue = new String(bLen) + new String(bFieldValue,"GBK");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else 
			{
				strFieldValue = new String(bLen);
			}
		}

		String strOffset = DataConversionBack.getZeroPrefixStr(String.valueOf(iOffset), 4);
		return strOffset + strFieldValue;
	}

	/**
	 * 将域值转为byte[]
	 * 
	 * @param entity
	 *            报文域配置实体bean
	 * @param strValue
	 *            域值
	 * @return byte[] 域byte[]格式
	 */
	private byte[] ValueToByte(Map<String, String> fieldMap, String strValue, Map<String, String> map) {

		byte[] bFieldValue = null;
		StringWriter sw = null;
		PrintWriter psw = null;
		try 
		{
			// 域数据长度
			int iFieldLength = Integer.parseInt(fieldMap.get("strLength"));
			// 域类型
			int iElementType = Integer.parseInt(fieldMap.get("strElementType"));

			bFieldValue = new byte[iFieldLength];
			// FIX
			if (iElementType == 1) 
			{
				// 域数据类型
				int iStrType = Integer.parseInt(fieldMap.get("strType"));
				if (iStrType == 1) 
				{ // 字符串
					// 字符串类型，左对齐，右补空格
					strValue = DataConversionBack.getSufCNfixStr(strValue, ' ',
							iFieldLength);
					bFieldValue = strValue.getBytes();
				}
				else if (iStrType == 2)
				{// 数字
					// 纯数字类型，右对齐，左补'0'
					strValue = DataConversionBack.getZeroPrefixStr(strValue, iFieldLength);
					// 组装MacData
					bFieldValue = strValue.getBytes();
				} 
				else if (iStrType == 3)
				{
					if ("FIELD52".equals(fieldMap.get("strName")))
					{
						// 二进制
						byte[] bTemp = DataConversionBack.hex2Byte(strValue);
						// 二进制类型，将二进制字符串转byte
						System.arraycopy(bTemp, 0, bFieldValue, 0, iFieldLength);
					} 
					else
					{
						// 二进制
						byte[] bTemp = DataConversionBack.atoc(strValue);
						// 二进制类型，将二进制字符串转byte
						System.arraycopy(bTemp, 0, bFieldValue, 0, iFieldLength);
					}

				}

			} 
			else if (iElementType == 2) // LLVAR
			{
				if (0 == strValue.length())
					strValue = " ";
				String strLen = String.valueOf(strValue.getBytes().length);
				strLen = DataConversionBack.getZeroPrefixStr(strLen, 2);
				strValue = strLen + strValue;
				bFieldValue = strValue.getBytes();

			} 
			else if (iElementType == 3) // LLLVAR
			{
				if (0 == strValue.length()) 
				{
					strValue = " ";
				}

				// 48域，用法一组包特殊处理
				if ("FIELD48".equals(fieldMap.get("strName")) && map.containsKey("FIELD48_1")) 
				{
					String strLen = String.valueOf(DataConversionBack.hex2Byte(strValue).length);
					strLen = DataConversionBack.getZeroPrefixStr(strLen, 3);
					int ilen = Integer.parseInt(strLen) + 3;
					byte[] byte48_3 = new byte[ilen];
					// 先放入长度
					System.arraycopy(strLen.getBytes(), 0, byte48_3, 0, strLen.getBytes().length);
					byte[] bVal = DataConversionBack.hex2Byte(strValue);
					// 再放入值
					System.arraycopy(bVal, 0, byte48_3, 3, bVal.length);
					bFieldValue = byte48_3;
				}
				else 
				{
					String strLen = String.valueOf(strValue.getBytes().length);
					strLen = DataConversionBack.getZeroPrefixStr(strLen, 3);
					strValue = strLen + strValue;
					bFieldValue = strValue.getBytes();
				}

			}
			else if(iElementType == 5)
			{
				// LLLVAR
				int ilength = 0;
				byte[] temp = DataConversionBack.hex2Byte(strValue);
				ilength = temp.length;
				bFieldValue = new byte[temp.length+3];
				String strLength = DataConversionBack.getPrefixStr(Integer.toString(ilength),'0',3);
				System.arraycopy(strLength.getBytes(), 0, bFieldValue, 0, strLength.getBytes().length);
				System.arraycopy(temp, 0, bFieldValue, strLength.getBytes().length, temp.length);
			}
		} 
		catch (Exception e) 
		{
			bFieldValue = null;
			sw = new StringWriter(1024 * 4);
			psw = new PrintWriter(sw);
			e.printStackTrace(psw);
			String strJrn = "组单个请求报文数据域时出错！抛出异常  ISO8583Comm.ValueToByte Exception:" + "\r\n";
//			strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + sw + "\r\n";
////			JournalThread.getInstance().append(map.get("STRBRANCHNUM"), JournalThread.LEVEL_3, strJrn);
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
		}
		finally
		{
			releaseRes(sw, psw);
		}
		return bFieldValue;
	}

	// 释放资源
	public static void releaseRes(StringWriter sw, PrintWriter psw) 
	{
		if (sw != null) 
		{
			try 
			{
				sw.close();
			}
			catch (Exception e) 
			{
			}
		}
		if (psw != null)
		{
			try
			{
				psw.close();
			} 
			catch (Exception e) 
			{
			}

		}

	}

	public static void main(String args[]) {
//		String strFieldVal = "46B8AD8170154F68C8EE956FCCA9B3C12E36E4AF0D9791990E15267D2B7B277A6BAD0FC2069120101209105651";
//		String strPinKey = strFieldVal.substring(0, 32);
//		String strPinKeyCheckData = strFieldVal.substring(32, 32 + 6);
//		String strMacKey = strFieldVal.substring(32 + 6, 32 + 6 + 32);
//		String strMacKeyCheckData = strFieldVal.substring(32 + 6 + 32, 32 + 6 + 32 + 6);
	}
}

