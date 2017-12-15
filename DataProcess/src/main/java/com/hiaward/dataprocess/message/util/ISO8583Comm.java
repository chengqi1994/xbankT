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
 * ISO8583���Ĵ�����
 * 
 * @version
 * <p>
 * 2010/12/8 ���� First release
 * </p>
 * 
 */

public class ISO8583Comm {

	private final int CONSTRUCT_BUF_LEN = 1536;// �����������С

	// 8583�ɹ����׷�����
	public final static String ISO8583RETCODE_SUCCEED = "00";
	// 8583�ɹ����׷�����
	public final static String ISO8583RETDESC_SUCCEED = "�ɹ�";

	// 8583��ȫ����ʧ�ܷ�����
	public final static String ISO8583RETCODE_SECURITY_ERROR = "A7";

	// 8583MAC����ʧ�ܷ�����
	public final static String ISO8583RETCODE_MAC_ERROR = "A0";
	// 8583MAC����ʧ�ܷ�����
	public final static String ISO8583RETDESC_MAC_ERROR = "MAC����ʧ��";

	// 8583PIN ��ʽ��
	public final static String ISO8583RETCODE_PIN_BLOCK_ERROR = "99";
	// 8583PIN ��ʽ��
	public final static String ISO8583RETDESC_PIN_BLOCK_ERROR = "PIN ��ʽ��";

	// 8583�������򽻻����Ĳ��ܲ���������Issuer or switch is inoperative
	public final static String ISO8583RETCODE_INOPERATIVE_ERROR = "91";
	// 8583�������򽻻����Ĳ��ܲ���������Issuer or switch is inoperative
	public final static String ISO8583RETDESC_INOPERATIVE_ERROR = "�������򽻻����Ĳ��ܲ���";

	public final static String ISO8583RETCODE_INVOIDTRAN_ERROR = "57";
	// 8583�������򽻻����Ĳ��ܲ���������Issuer or switch is inoperative
	public final static String ISO8583RETDESC_INVOIDTRAN_ERROR = "�������Ĳ�֧�ָÿ��Ĵ��ֽ���";

	// 8583��������ת����ԭ�������󣬵�δ�յ�������Ӧ��ʱ����������ֱ��������Ӧ��Ϊ��ȱ�ݵĳɹ�����
	public final static String ISO8583RETCODE_COMM_ERROR = "A2";

	// 8583����жң�1��CVN��֤ʧ�� 2�����Ͻ��׵Ľ�����Ϣ�����ʹ�
	public final static String ISO8583RETCODE_DONOTHONOR_ERROR = "05";
	// 8583����жң�1��CVN��֤ʧ�� 2�����Ͻ��׵Ľ�����Ϣ�����ʹ�
	public final static String ISO8583RETDESC_DONOTHONOR_ERROR = "����ж�";

	// 8583��������ϵͳ�쳣��ʧЧ
	public final static String ISO8583RETCODE_SWITCH_SYSTEM_MALFUNCTION = "96";
	// 8583��������ϵͳ�쳣��ʧЧ
	public final static String ISO8583RETDESC_SWITCH_SYSTEM_MALFUNCTION = "��������ϵͳ�쳣��ʧЧ";

	/**
	 * ��֯ISO8583����
	 * 
	 * @param strWay
	 *            ��������
	 * @param strChannelName
	 *            ��������
	 * @param map
	 *            ��Ŵ����������
	 * @param oXmlCache
	 *            XML�����ļ���ȡ����
	 * @return byte[] ��֯��ɵķ��ذ�
	 */
	public byte[] organizeISO8583Msg(Map<String, String> map) {
		// /////////////////////////////////////////////
		// ������¼��ϸ���ױ��ģ���������
		StringBuffer strLogData = new StringBuffer(128);
		// /////////////////////////////////////////////

		// ���к�
		String strBranchNum = map.get("STRBRANCHNUM");
		// �ն˱��
		String strTermNum = map.get("STRTERMNUM");
		// ����������ȡ�����ļ�����������Ϣ
		StringBuffer bufTRCD = new StringBuffer(16);
		// �����������С
		int CONSTRUCT_BUF_LEN = 1536;
		// ��ʼ���������ĵĳ���
		int iOffset = 0;
		// ����������
		byte[] byteArrBuf = new byte[CONSTRUCT_BUF_LEN];

//		//����4λ�����򳤶�   ��Ȫ�Ǳ�׼��8583�������⴦��
//		System.arraycopy("0000".getBytes(), 0, byteArrBuf, iOffset, 4);
		
		// ��ʼ��λͼ
		String strBitMap = "10000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		// �ý������õı�����
		String strField = null;
		// ���ü���MAC����
		String strMACField = null;
		// �������Ķ�Ӧ����Ӧ���ĵĽ�������
		String strMsgTypeID = null;
		// ���ڴ����ҪMACУ�������
		StringBuffer strMACData = new StringBuffer(128);
		// �Ƿ���ҪMACУ��
		boolean bMACFlag = false;
		// �����Ҫ�����
		Map<String, String> keyMap = new HashMap<String, String>(60, 0.8f);
		// ������ü���MAC����
		Map<String, String> keyMACMap = new HashMap<String, String>(60, 0.8f);

		// debug
		// XmlConfCache.getInstance().init();

		// �õ�ISO8583��������
		List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getReqFieldList();

		if (null != map.get("strValue") && null != map.get("strMacFields")) 
		{
			// �ý�������Ҫ�ı�����
			strField = map.get("strValue");
			// �ý��׵�MACУ����
			strMACField = map.get("strMacFields");
			
		}
		else 
		{
			String strJrn = "organizeISO8583Msg beanMap is null ��������ʱ�����Ҳ���:" + map.get("strValue") + "��Ӧ�Ľ�������\r\n";
			System.out.println(strJrn);
//			strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
			return null;
		}

		// ��ʼ��λͼת����char����
		char[] cBitMap = strBitMap.toCharArray();
		// �ѽ�������Ҫ�ı������ֳ�����
		String[] strFieldBuf = strField.split(",");
		// �ý�������Ҫ�ı���������λͼMAP������ý������õ��ļ����ƣ�ͬʱ�޸ĳ�ʼ��λͼ
		for (int j = 0; j < strFieldBuf.length; j++) 
		{
			int iField = DataConversionBack.str2Int(strFieldBuf[j], -1);
			// ����������и��򣬵���map��û�д����ֵ����ô����������,128����
			if (iField == 128 || (map.get("FIELD" + iField) != null && !"".equals(map.get("FIELD" + iField)))) 
			{
				keyMap.put("FIELD" + iField, "FIELD" + iField);
				cBitMap[iField - 1] = '1';
			}
		}

		// ���ɸý��׵�λͼ
		StringBuffer BitMapBuf = new StringBuffer(128);
		BitMapBuf.append(cBitMap);
		strBitMap = BitMapBuf.toString();
		BitMapBuf.delete(0, BitMapBuf.length());

		// ����У��ı������γ�MAP��ֵ��
		if (keyMap.containsKey("FIELD128") && !(null == strMACField) && !("".equals(strMACField))) 
		{
			// ȡ������������MACУ����
			bMACFlag = true;
			String[] strMACFieldBuf = strMACField.split(",");
			for (int d = 0; d < strMACFieldBuf.length; d++) 
			{
				int iField = DataConversionBack.str2Int(strMACFieldBuf[d], -1);
				keyMACMap.put("FIELD" + iField, "FIELD" + iField);
			}
			// ��Ϣ��ʶ�������У��
			keyMACMap.put("MESSAGETYPE", "MESSAGETYPE");
		}

		// ���ݱ���ͷ3����
//		int times = 1;
//		
//		//��Ȫũ���б���ͷΪ���ȣ���װʱ����װ����ͷ
//			// �ѱ���ͷ�γ�MAP��ֵ��
//		for (int m = 0; m < times; m++) 
//		{
//			keyMap.put("HDFIELD" + (m + 1), "HDFIELD" + (m + 1));
//		}
		
		//����4λ�����򳤶�   ��Ȫ�Ǳ�׼��8583�������⴦��
		System.arraycopy("0000".getBytes(), 0, byteArrBuf, iOffset, 4);
		iOffset = iOffset + 4;
		
		
		// �ѱ��ı�ʶ�γɼ�ֵ��
		keyMap.put("MESSAGETYPE", "MESSAGETYPE");

		// ��λͼ�γɼ�ֵ��
		keyMap.put("BITMAP", "BITMAP");
		// �����ʼ��FIELD128��
		map.remove("FIELD128");
		map.put("FIELD128", "0000000000000000000000000000000000000000000000000000000000000000");
		map.remove("BITMAP");
		// ����λͼ
		map.put("BITMAP", strBitMap);
		
//		iOffset += strBitMap.length();
		
		if (null != reqTrans) 
		{
			// ѭ��ISO8583�����������ö���
			for (int n = 0; n < reqTrans.size(); n++) 
			{
				// ȡ��������
				Map<String, String> fieldMap = (Map<String, String>) reqTrans.get(n);
				// ȡ������������
				String strFieldName = fieldMap.get("strName");
			
				// �ý��ױ������Ƿ��������
				if (keyMap.containsKey(strFieldName)) 
				{
					// �Ա��������ݽ���Ԥ���������Ƿ�������Ĭ��ֵ
					String strValue = DataConversionBack.getFieldDefVal(map.get(strFieldName), fieldMap.get("strValue"));
					// �Ա�����ֵתΪbyte
					byte[] bValue = ValueToByte(fieldMap, strValue, map);
					
					
					if (null == bValue) 
					{
						String strJrn = "organizeISO8583Msg bValue is null ��������ʱ,ת��������ʧ�ܣ�\r\n";
//						strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//						LoggerUtil.w(ISO8583Comm.class, strJrn);
						return null;
					}
					// �����˸���ΪMACУ����,���ø�������װMACDATA
					if (bMACFlag && keyMACMap.containsKey(strFieldName)) 
					{
						String strMACValue = new String(bValue).trim();
						if ("FIELD90".equals(strFieldName)) 
						{
							strMACValue = strMACValue.substring(0, 20); // 90�������ΪMACУ����ֻȡǰ20λ
						}
						strMACData.append(strMACValue).append(" ");
					}

					// �����Ǻ�ESB���Թ����м��ϵ���ϸ��־��������ʽʹ��ʱ����رգ������������
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
						else if (iType == 4)//bcdѹ����
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
						System.out.println("��"+strFieldName+"����"+strValue.length()+"["+strTempValue+"]");
					} 
					catch (Exception e) 
					{
						StringWriter sw = new StringWriter(1024 * 4);
						e.printStackTrace(new PrintWriter(sw));
						String strJrn = "ESB���Թ�������װ��ϸ��־�����׳��쳣:" + "\r\n";
//						strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + sw + "\r\n";
//						LoggerUtil.w(ISO8583Comm.class, strJrn);
					}

					// ////////////////////////////////////////////////////////////////////////////////////////
					// ��ת����������ݷ���byteArrBuf
					System.arraycopy(bValue, 0, byteArrBuf, iOffset, bValue.length);
					// �ۼӱ��ĳ���
					iOffset += bValue.length;

				}
			}
		}

		// MAC����128��Ҳ����FIELD128
		String strMac = "";
		// ���������128����strMACData��Ϊ��
		if (bMACFlag)
		{
			// ����MAC
			byte[] bMac = getMac(strMACData.toString(), map);
			strMACData.delete(0, strMACData.length());
			if (null == bMac) 
			{
				String strJrn = "organizeISO8583Msg bMac is null ��������ʱ,����MACʧ�ܣ�\r\n";
//				strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
				return null;
			}
			// ��MAC����byteArrBuf
			System.arraycopy(bMac, 0, byteArrBuf, iOffset - 8, 8);
			strMac = new String(bMac);
		}
		
		String msglength = String.valueOf(iOffset - 4);
        while(msglength.length() < 4)
        {
            msglength = "0" + msglength;
        }
        System.arraycopy(msglength.getBytes(), 0, byteArrBuf, 0, 4);
		
		// ����ʵ����װ�õı��ĳ�������1���µ�byte[]
		byte[] byteSendBuf = new byte[iOffset];
		// ����ʵ�ʷ��ͱ�������
		System.arraycopy(byteArrBuf, 0, byteSendBuf, 0, iOffset);

		// �����Ǻ�ESB���Թ����м��ϵ���ϸ��־
		// ///////////////////////////////////////////////////////////////////////////////////
		try {
			Object[] obj = strLogData.toString().split("\\|");
			for (int length = 0; length < obj.length; length++) 
			{
				if (length == 0) 
				{
					String strJrn = "����ISO8583������";
//					strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//					LoggerUtil.w(ISO8583Comm.class, strJrn);
				}
				if (obj[length].toString().indexOf("FIELD128") != -1)
				{
					String strJrn = "Debug��" + obj[length].toString().substring(0, obj[length].toString().indexOf('[')) + "[ " + strMac + " ]";
//					strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//					LoggerUtil.w(ISO8583Comm.class, strJrn);
					continue;
				}

				String strJrn = "Debug��" + obj[length].toString();
//				strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
////				JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
			}
		} 
		catch (Exception e) 
		{
			StringWriter sw = new StringWriter(1024 * 4);
			e.printStackTrace(new PrintWriter(sw));
			String strJrn = "ESB���Թ����м��ϵ���ϸ��־�����׳��쳣:" + "\r\n";
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
	 * ����8583����
	 */
	public Map<String, String> analyzeISO8583MsgRes(Map<String, String> map, byte[] byResFiledVal)
	{

		boolean bolRet = false;
		// ���Դ���ϸ��־
		StringBuffer strLogData = new StringBuffer(128);
		// ���ڴ����ҪMACУ�������
		StringBuffer strMACData = new StringBuffer(128);
		// �Ƿ���ҪMACУ��
		boolean bMACFlag = false;
		// �����������ⷽ��
		String strFomatFuncs = null;
		// ���ü���MAC����
		String strMACField = null;
		// �����Ҫ�����
		Map<String, String> keyMap = new HashMap<String, String>(60, 0.8f);
		// ��ŷ��ص����ֵ
		Map<String, String> returnMap = new HashMap<String, String>();
		// ��ż���MAC����
		Map<String, String> keyMACMap = new HashMap<String, String>(60, 0.8f);
		// �����Ҫ�����⴦����
		Map<String, String> FomatFuncMap = null;
		//ȡλͼ��ʱ����
		byte[] btemp = new byte[8];
		//ȡ���ĳ�����ʱ����
		byte[] totalLength = new byte[4];
		//ȡ���Ľ���������ʱ����
		byte[] bytearrMessageID = new byte[4];
		
		StringWriter sw = null;
		PrintWriter psw = null;
		try 
		{
			// ESB8583���ݱ���ͷ����3
			int iPagHeadLength = 6;
			// ��ʼ���������ĵĳ���
			int iOffset = 0;
			// λͼ�ڱ��ĵ���ʼλ��
			int iBitmap = iPagHeadLength + 4;
			
			//ȥ�����ر���ͷ
			//���ĳ���
//	        System.arraycopy(byResFiledVal, iOffset, totalLength, 0, 4);
//	        iOffset += 4;
//	        System.out.println(new String(totalLength));
	        
	        //ȡ��������
	        System.arraycopy(byResFiledVal, iOffset, bytearrMessageID, 0, 4);
			iOffset = iOffset + 4;
			System.out.println(new String(bytearrMessageID));
			
			//ȡλͼ
			System.arraycopy(byResFiledVal, iOffset, btemp, 0, 8);
			iOffset += 8;
			//��64λ��λͼ��ת���������ַ���
			String strBitMap = DataConversion.atob(btemp);
			
			  //Map ��һλ�� 1 ʱ����������չλͼ
		    if ("1".equals(strBitMap.substring(0, 1)))
		    {
		        System.arraycopy(byResFiledVal, iOffset, btemp, 0, 8);
		        strBitMap += DataConversion.atob(btemp);
		        iOffset += 8;
		    }
		    System.out.println("���ر���λͼ��Ϣ��"+strBitMap);
			
			// �õ�ISO8583��������
			List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getResFieldList();

			// ��λͼת����MAP��ֵ��
			char[] cBitMap = strBitMap.toCharArray(); // ת����char����
			for (int i = 0; i < cBitMap.length; i++)
			{
				if (cBitMap[i] == '1') 
				{
					keyMap.put("FIELD" + (i + 1), "FIELD" + (i + 1));
				}
			}
			// ͨ�����������bitmap�����ļ��ж�Ӧ��������Ϣ
//			Map<String, String> beanMap = reqBitmap.get(strTRCD);
//			if (null != beanMap) 
			if(null != map.get("strFomatFuncs") || null != map.get("strMacFields"))
			{
				// ���⴦�����Ӧ��������
				strFomatFuncs = map.get("strFomatFuncs");
				// �ý��׵�MACУ����
				strMACField = map.get("strMacFields");
			} 
			else 
			{
				String strJrn = "analyzeISO8583Msg beanMap is null ����Ӧ����ʱ�����Ҳ���:" + "��Ӧ�Ľ�������\r\n";
				return null;
			}
			// ����У��ı������γ�MAP��ֵ��
			if (strMACField != null && strMACField.length() != 0) 
			{
				// ȡ������������MACУ����
				String[] strMACFieldBuf = strMACField.split(",");
				for (int d = 0; d < strMACFieldBuf.length; d++) 
				{
					// ����MACУ�������MAP
					int iField = DataConversionBack.str2Int(strMACFieldBuf[d], -1);
					keyMACMap.put("FIELD" + iField, "FIELD" + iField);
				}
				// ��Ϣ��ʶ�������У��
				keyMACMap.put("MESSAGETYPE", "MESSAGETYPE");
				bMACFlag = true;

			}
			// ����Ҫִ����ת�������γ�MAP��ֵ��
			if (strFomatFuncs != null && strFomatFuncs.length() != 0) 
			{
				FomatFuncMap = new HashMap<String, String>(20, 0.8f);
				// ������˷����������γ�Map��ֵ��
				String[] strFomatFuncsBuf = strFomatFuncs.split(",");
				for (int j = 0; j < strFomatFuncsBuf.length; j++)
				{
					try 
					{
						// ȡ��ǰ3λ�����
						int iField = DataConversionBack.str2Int(strFomatFuncsBuf[j].substring(0, 3), -1);
						// �ӵ�4λ��ȡ��������
						String strFuncName = strFomatFuncsBuf[j].substring(3);
						FomatFuncMap.put("FIELD" + iField, strFuncName);
					} 
					catch (Exception e) 
					{
						sw = new StringWriter(1024 * 4);
						psw = new PrintWriter(sw);
						e.printStackTrace(psw);
						String strJrn = "analyzeISO8583Msg strFomatFuncs Exception �ֽ���ת������ʱ����\r\n";
						return null;
					}
					finally
					{
						releaseRes(sw, psw);
					}
				}
			}
			// ����ͷ3����
//			int times = 1;
//			// �鱨��ͷ
//			for (int j = 0; j < times; j++) 
//			{
//				keyMap.put("HDFIELD" + (j + 1), "HDFIELD" + (j + 1));
//			}
			// �ѱ��ı�ʶ�γɼ�ֵ��
//			keyMap.put("MESSAGETYPE", "MESSAGETYPE");
//			// ��λͼ�γɼ�ֵ��
//			keyMap.put("BITMAP", "BITMAP");
			if (null != reqTrans) 
			{
				for (int m = 0; m < reqTrans.size(); m++) 
				{
					// ȡ��������
					Map<String, String> fieldMap = (Map<String, String>) reqTrans.get(m);
					// ȡ������������
					String strFieldName = fieldMap.get("strName");
					// �ý��ױ������Ƿ��������
					if (keyMap.containsKey(strFieldName))
					{
						// �ⱨ���������ֵ
						String strFieldValue = null;
						// �����Ƿ���Ҫ�������ⷽ������
						if (FomatFuncMap != null && FomatFuncMap.containsKey(strFieldName)) 
						{
							strFieldValue = fieldFomatFuncs(FomatFuncMap.get(strFieldName), byResFiledVal, iOffset, map);
						} 
						else 
						{
							// ��������������ַ���
							strFieldValue = ByteToValue(fieldMap, byResFiledVal, iOffset);
						}
						// ��ǰ�⵽�ı���λ��
						iOffset = Integer.valueOf(strFieldValue.substring(0, 4), 10).intValue();

						// ȡ���������ֵ
						strFieldValue = strFieldValue.substring(4);
						
						// �����Ǻ�ESB���Թ����м��ϵ���ϸ��־��������ʽʹ��ʱ����رգ������������
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

						// �����˸���ΪMACУ����
						if (bMACFlag && keyMACMap.containsKey(strFieldName)) 
						{
							String strMACValue = strFieldValue;
							if ("FIELD90".equals(strFieldName)) 
							{
								// 90�������ΪMACУ����ֻȡǰ20λ
								strMACValue = strMACValue.substring(0, 20);
							}
							strMACData.append(strMACValue).append(" ");
						}
						// �������õ����ݳ���������ȡ��������ı������е�����
						if (strFieldValue.length() != 0) 
						{
							int iElementType = Integer.parseInt(fieldMap.get("strElementType"));
							switch (iElementType)
							{
							case 2:
								strFieldValue = strFieldValue.substring(2);// 2λ���䳤
								break;
							case 3:
								strFieldValue = strFieldValue.substring(3);// 3λ���䳤
								break;
							case 4:
								strFieldValue = strFieldValue.substring(4);// 3λ���䳤
								break;
							}
							// map�����·������������
							map.remove(strFieldName);
							returnMap.put(strFieldName, strFieldValue);
							System.out.println("�����ƣ�" + strFieldName + "==��ֵ��[" + strFieldValue + "]");
						}
					}
				}
			}

			// �����Ǻ�ESB���Թ����м��ϵ���ϸ��־��������ʽʹ��ʱ����رգ������������
			// ///////////////////////////////////////////////////////////
			Object[] obj = strLogData.toString().split("\\|");
			for (int length = 0; length < obj.length; length++) 
			{
				if (length == 0) 
				{
					String strJrn = "����ISO8583������";
				}
			}

			map.put("MACDATA", strMACData.toString().trim());
			// ������׳ɹ��Ϳ�ʼ��֤MAC �����û�в���
			if (map.containsKey("FIELD39")) 
			{
				if (bMACFlag) 
				{ 
					// �����ҪУ��MAC,����Ҫ����MAC���յ���128��Ƚϣ������ɹ�
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

			// �����Ǻ�ESB���Թ����м��ϵ���ϸ��־��������ʽʹ��ʱ����رգ������������
			// ///////////////////////////////////////////////////////////
//			Object[] obj = strLogData.toString().split("\\|");
//			for (int length = 0; length < obj.length; length++) 
//			{
//				if (length == 0) 
//				{
//					String strJrn = "����ISO8583������";
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
	 * �ֽ�ISO8583����
	 * 
	 * @param strWay
	 *            ��������
	 * @param strChannelName
	 *            ��������
	 * @param map
	 *            ��Ŵ���������
	 * @param byResFiledVal
	 *            ��������
	 * @param oXmlCache
	 *            XML�����ļ���ȡ����
	 * @return Map ���ķ������
	 */
	public Map<String, String> analyzeISO8583Msg(Map<String, String> map, byte[] byResFiledVal) 
	{
		boolean bolRet = false;
		// //////////////////////////////////////////////////
		// ���Դ���ϸ��־
		StringBuffer strLogData = new StringBuffer(128);
		// ///////////////////////////////////////////////////
		// ���к�
//		String strBranchNum = map.get("STRBRANCHNUM");
		// �ն˱��
//		String strTermNum = map.get("STRTERMNUM");
		// ���ڴ����ҪMACУ�������
		StringBuffer strMACData = new StringBuffer(128);
		// �Ƿ���ҪMACУ��
		boolean bMACFlag = false;
		// �����������ⷽ��
		String strFomatFuncs = null;
		// ���ü���MAC����
		String strMACField = null;
		// �����Ҫ�����
		Map<String, String> keyMap = new HashMap<String, String>(60, 0.8f);
		// ��ŷ��ص����ֵ
		Map<String, String> returnMap = new HashMap<String, String>();
		// ��ż���MAC����
		Map<String, String> keyMACMap = new HashMap<String, String>(60, 0.8f);
		// �����Ҫ�����⴦����
		Map<String, String> FomatFuncMap = null;
		StringWriter sw = null;
		PrintWriter psw = null;
		try 
		{
			// ESB8583���ݱ���ͷ����3
			int iPagHeadLength = 6;
			// ��ʼ���������ĵĳ���
			int iOffset = 0;
			// λͼ�ڱ��ĵ���ʼλ��
			int iBitmap = iPagHeadLength + 4;
			// ȡ��������׵Ľ��״���
			String strTRCD = null;
			if (map.get("PROCESSINGCODE") != null && map.get("RESMESSAGETYPE") != null)
			{
				strTRCD = map.get("RESMESSAGETYPE") + "_" + map.get("PROCESSINGCODE");
			}
			// �õ�ISO8583��������
//			List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getFieldList();
			List<Map<String, String>> reqTrans = XmlConfCache.getInstance().getReqFieldList();
			// �õ�����λͼ����
			//Map<String, Map<String, String>> reqBitmap = XmlConfCache.getInstance().getProCodeMap();
			// ������õ���λͼ
			byte[] btemp = new byte[16];
			System.arraycopy(byResFiledVal, iBitmap, btemp, 0, 16);
			String strBitMap = DataConversionBack.atob(btemp);
//			System.out.println("λͼ��" + strBitMap);

			// ��λͼת����MAP��ֵ��
			char[] cBitMap = strBitMap.toCharArray(); // ת����char����
			for (int i = 0; i < cBitMap.length; i++)
			{
				if (cBitMap[i] == '1') 
				{
					keyMap.put("FIELD" + (i + 1), "FIELD" + (i + 1));
				}
			}
			// ͨ�����������bitmap�����ļ��ж�Ӧ��������Ϣ
//			Map<String, String> beanMap = reqBitmap.get(strTRCD);
//			if (null != beanMap) 
			if(null != map.get("strFomatFuncs") || null != map.get("strMacFields"))
			{
				// ���⴦�����Ӧ��������
				strFomatFuncs = map.get("strFomatFuncs");
				// �ý��׵�MACУ����
				strMACField = map.get("strMacFields");
			} 
			else 
			{
				String strJrn = "analyzeISO8583Msg beanMap is null ����Ӧ����ʱ�����Ҳ���:" + strTRCD + "��Ӧ�Ľ�������\r\n";
//				strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
//				JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
				return null;
			}
			// ����У��ı������γ�MAP��ֵ��
			if (strMACField != null && strMACField.length() != 0) 
			{
				// ȡ������������MACУ����
				String[] strMACFieldBuf = strMACField.split(",");
				for (int d = 0; d < strMACFieldBuf.length; d++) 
				{
					// ����MACУ�������MAP
					int iField = DataConversionBack.str2Int(strMACFieldBuf[d], -1);
					keyMACMap.put("FIELD" + iField, "FIELD" + iField);
				}
				// ��Ϣ��ʶ�������У��
				keyMACMap.put("MESSAGETYPE", "MESSAGETYPE");
				bMACFlag = true;

			}
			// ����Ҫִ����ת�������γ�MAP��ֵ��
			if (strFomatFuncs != null && strFomatFuncs.length() != 0) 
			{
				FomatFuncMap = new HashMap<String, String>(20, 0.8f);
				// ������˷����������γ�Map��ֵ��
				String[] strFomatFuncsBuf = strFomatFuncs.split(",");
				for (int j = 0; j < strFomatFuncsBuf.length; j++)
				{
					try 
					{
						// ȡ��ǰ3λ�����
						int iField = DataConversionBack.str2Int(strFomatFuncsBuf[j].substring(0, 3), -1);
						// �ӵ�4λ��ȡ��������
						String strFuncName = strFomatFuncsBuf[j].substring(3);
						FomatFuncMap.put("FIELD" + iField, strFuncName);
					} 
					catch (Exception e) 
					{
						sw = new StringWriter(1024 * 4);
						psw = new PrintWriter(sw);
						e.printStackTrace(psw);
						String strJrn = "analyzeISO8583Msg strFomatFuncs Exception �ֽ���ת������ʱ����\r\n";
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
			// ����ͷ3����
			int times = 1;
			// �鱨��ͷ
			for (int j = 0; j < times; j++) 
			{
				keyMap.put("HDFIELD" + (j + 1), "HDFIELD" + (j + 1));
			}
			// �ѱ��ı�ʶ�γɼ�ֵ��
			keyMap.put("MESSAGETYPE", "MESSAGETYPE");
			// ��λͼ�γɼ�ֵ��
			keyMap.put("BITMAP", "BITMAP");
			if (null != reqTrans) 
			{
				for (int m = 0; m < reqTrans.size(); m++) 
				{
					// ȡ��������
					Map<String, String> fieldMap = (Map<String, String>) reqTrans.get(m);
					// ȡ������������
					String strFieldName = fieldMap.get("strName");
					// �ý��ױ������Ƿ��������
					if (keyMap.containsKey(strFieldName))
					{
						// �ⱨ���������ֵ
						String strFieldValue = null;
						// �����Ƿ���Ҫ�������ⷽ������
						if (FomatFuncMap != null && FomatFuncMap.containsKey(strFieldName)) 
						{
							strFieldValue = fieldFomatFuncs(FomatFuncMap.get(strFieldName), byResFiledVal, iOffset, map);
						} 
						else 
						{
							// ��������������ַ���
							strFieldValue = ByteToValue(fieldMap, byResFiledVal, iOffset);
						}
						// ��ǰ�⵽�ı���λ��
						iOffset = Integer.valueOf(strFieldValue.substring(0, 4), 10).intValue();

						// ȡ���������ֵ
						strFieldValue = strFieldValue.substring(4);
						
						// �����û�в��Ե�
						if (!"0800".equals(map.get("MESSAGETYPE")) && "FIELD128".equals(strFieldName)) 
						{
							strFieldValue = new String(DataConversionBack.hex2Byte(strFieldValue));
						}

						// �����Ǻ�ESB���Թ����м��ϵ���ϸ��־��������ʽʹ��ʱ����رգ������������
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

						// �����˸���ΪMACУ����
						if (bMACFlag && keyMACMap.containsKey(strFieldName)) 
						{
							String strMACValue = strFieldValue;
							if ("FIELD90".equals(strFieldName)) 
							{
								// 90�������ΪMACУ����ֻȡǰ20λ
								strMACValue = strMACValue.substring(0, 20);
							}
							strMACData.append(strMACValue).append(" ");
						}
						// �������õ����ݳ���������ȡ��������ı������е�����
						if (strFieldValue.length() != 0) 
						{
							int iElementType = Integer.parseInt(fieldMap.get("strElementType"));
							switch (iElementType)
							{
							case 2:
								strFieldValue = strFieldValue.substring(2).trim();// 2λ���䳤
								break;
							case 3:
								strFieldValue = strFieldValue.substring(3).trim();// 3λ���䳤
								break;
							case 4:
								strFieldValue = strFieldValue.substring(4).trim();// 3λ���䳤
								break;
							}
							// map�����·������������
							map.remove(strFieldName);
							returnMap.put(strFieldName, strFieldValue);
							System.out.println("�����ƣ�" + strFieldName + "=-=��ֵ��" + strFieldValue);
						}
					}
				}
			}

			// �����Ǻ�ESB���Թ����м��ϵ���ϸ��־��������ʽʹ��ʱ����رգ������������
			// ///////////////////////////////////////////////////////////
			Object[] obj = strLogData.toString().split("\\|");
			for (int length = 0; length < obj.length; length++) 
			{
				if (length == 0) 
				{
					String strJrn = "����ISO8583������";
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
//			String strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "From ESB macDataBuff Map SUCCESS �ֽ�ESB��Ӧ���ĳɹ� macDataBuff=" + strMACData.toString().trim() + "\r\n";
////			JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
			// ������׳ɹ��Ϳ�ʼ��֤MAC �����û�в���
			if (map.containsKey("FIELD39")) 
			{
				if (bMACFlag) 
				{ 
					// �����ҪУ��MAC,����Ҫ����MAC���յ���128��Ƚϣ������ɹ�
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

			// �����Ǻ�ESB���Թ����м��ϵ���ϸ��־��������ʽʹ��ʱ����رգ������������
			// ///////////////////////////////////////////////////////////
			Object[] obj = strLogData.toString().split("\\|");
			for (int length = 0; length < obj.length; length++) 
			{
				if (length == 0) 
				{
					String strJrn = "����ISO8583������";
//					strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + strJrn + "\r\n";
////					JournalThread.getInstance().append(strBranchNum,JournalThread.LEVEL_3, strJrn);
//					LoggerUtil.w(ISO8583Comm.class, strJrn);
				}

//				String strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + obj[length].toString() + "\r\n";
////				JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
			}
			// /////////////////////////////////////////////

//			String strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "From ESB analyzeISO8583Msg Map Exception �ֽ�ESB��Ӧ���ĳ����쳣 MAP:" + map + "\r\n";
////			JournalThread.getInstance().append(strBranchNum, JournalThread.LEVEL_3, strJrn);
//			LoggerUtil.w(ISO8583Comm.class, strJrn);
			if (strMACData != null) 
			{
//				strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "From ESB macDataBuff Map Exception �ֽ�ESB��Ӧ���ĳ����쳣 macDataBuff:" + strMACData.toString().trim() + "\r\n";
////				JournalThread.getInstance().append(strBranchNum,JournalThread.LEVEL_3, strJrn);
//				LoggerUtil.w(ISO8583Comm.class, strJrn);
			}

			sw = new StringWriter(1024 * 4);
			psw = new PrintWriter(sw);
			e.printStackTrace(psw);
//			strJrn = "[" + new com.hiaward.xbanktsvc.common.tools.DateEx().getTimeStrFull() + "]" + "ISO8583Comm.analyzeISO8583Msg Exception: �ֽ�ESB��Ӧ���ĳ����쳣 :" + sw + "\r\n";
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
			// ���к�
			String strBranchNum = map.get("STRBRANCHNUM");
			// �ն˱��
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
			String MACData = macDataBuff.toUpperCase(); // ת��Ϊ��д
			MACData = MACData.replaceAll("[^A-Z0-9,.' ']", "");// ������ĸ(A-Z)������(0-9)���ո񣬶���(��)�͵��(.)������ַ���ɾȥ
			MACData = MACData.replaceAll("[ ]+", " ");// ����һ���������ո���һ���ո����
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
	 * ����MAC���ݼ���MAC
	 * 
	 * @param strMac
	 *            ����MAC������
	 * @return byte[] ���������MAC
	 */
	/*private byte[] getMac(String strMac, Map<String, String> map) 
	{
		byte[] bMac64 = null;
		// ת��Ϊ��д
		String MACData = strMac.toString().toUpperCase();

		// ������ĸ(A-Z)������(0-9)���ո񣬶���(��)�͵��(.)������ַ���ɾȥ
		MACData = MACData.replaceAll("[^A-Z0-9,.' ']", "");

		// ����һ���������ո���һ���ո����
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
			String strJrn = "����MAC�����ĳ����׳��쳣:" + "\r\n";
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
	 * ����MAC���ݼ���MAC
	 * @param strMac           ����MAC������
	 * @return byte[]          ���������MAC
	 */
	private byte[]  getMac(String strMac, Map<String, String> map) 
	{
	      byte[]	bMac64 = null;
	      //ת��Ϊ��д
	      String  MACData = strMac.toString().toUpperCase();
	      
	      //������ĸ(A-Z)������(0-9)���ո񣬶���(��)�͵��(.)������ַ���ɾȥ
		  MACData =MACData.replaceAll("[^A-Z0-9,.' ']", "");
		  
		  //����һ���������ո���һ���ո����
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
	 * ��byte[]תΪ��ֵ
	 * 
	 * @param entity
	 *            ����������ʵ��bean
	 * @param byResFiledVal
	 *            ��������
	 * @param iOffset
	 *            ��ǰ�ֽ�ƫ����
	 * @return String ��ֵ
	 */
	private String ByteToValue(Map<String, String> fieldMap, byte[] byResFiledVal, int iOffset) 
	{
		String strFieldValue = null;
		// �����ݳ���
		int iFieldLength = Integer.parseInt(fieldMap.get("strLength").trim());
		// ������
		int iElementType = Integer.parseInt(fieldMap.get("strElementType").trim());

		// ������
		String strFieldName = fieldMap.get("strName");
		if (iElementType == 1) // FIX
		{
			// ����������
			int iStrType = Integer.parseInt(fieldMap.get("strType"));
			if (iStrType == 1 || iStrType == 2) // �ַ���
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
			else if (iStrType == 3) // ������
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
	 * ����ֵתΪbyte[]
	 * 
	 * @param entity
	 *            ����������ʵ��bean
	 * @param strValue
	 *            ��ֵ
	 * @return byte[] ��byte[]��ʽ
	 */
	private byte[] ValueToByte(Map<String, String> fieldMap, String strValue, Map<String, String> map) {

		byte[] bFieldValue = null;
		StringWriter sw = null;
		PrintWriter psw = null;
		try 
		{
			// �����ݳ���
			int iFieldLength = Integer.parseInt(fieldMap.get("strLength"));
			// ������
			int iElementType = Integer.parseInt(fieldMap.get("strElementType"));

			bFieldValue = new byte[iFieldLength];
			// FIX
			if (iElementType == 1) 
			{
				// ����������
				int iStrType = Integer.parseInt(fieldMap.get("strType"));
				if (iStrType == 1) 
				{ // �ַ���
					// �ַ������ͣ�����룬�Ҳ��ո�
					strValue = DataConversionBack.getSufCNfixStr(strValue, ' ',
							iFieldLength);
					bFieldValue = strValue.getBytes();
				}
				else if (iStrType == 2)
				{// ����
					// ���������ͣ��Ҷ��룬��'0'
					strValue = DataConversionBack.getZeroPrefixStr(strValue, iFieldLength);
					// ��װMacData
					bFieldValue = strValue.getBytes();
				} 
				else if (iStrType == 3)
				{
					if ("FIELD52".equals(fieldMap.get("strName")))
					{
						// ������
						byte[] bTemp = DataConversionBack.hex2Byte(strValue);
						// ���������ͣ����������ַ���תbyte
						System.arraycopy(bTemp, 0, bFieldValue, 0, iFieldLength);
					} 
					else
					{
						// ������
						byte[] bTemp = DataConversionBack.atoc(strValue);
						// ���������ͣ����������ַ���תbyte
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

				// 48���÷�һ������⴦��
				if ("FIELD48".equals(fieldMap.get("strName")) && map.containsKey("FIELD48_1")) 
				{
					String strLen = String.valueOf(DataConversionBack.hex2Byte(strValue).length);
					strLen = DataConversionBack.getZeroPrefixStr(strLen, 3);
					int ilen = Integer.parseInt(strLen) + 3;
					byte[] byte48_3 = new byte[ilen];
					// �ȷ��볤��
					System.arraycopy(strLen.getBytes(), 0, byte48_3, 0, strLen.getBytes().length);
					byte[] bVal = DataConversionBack.hex2Byte(strValue);
					// �ٷ���ֵ
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
			String strJrn = "�鵥��������������ʱ�����׳��쳣  ISO8583Comm.ValueToByte Exception:" + "\r\n";
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

	// �ͷ���Դ
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

