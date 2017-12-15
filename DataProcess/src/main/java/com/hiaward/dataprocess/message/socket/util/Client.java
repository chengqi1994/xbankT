package com.hiaward.dataprocess.message.socket.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.hiaward.dataprocess.message.socket.util.ClientSocketFactory;


public class Client {    
    
//	   public static void main(String args[]) throws Exception {    
//	      //为了简单起见，所有的异常都直接往外抛    
//	     String host = "127.0.0.1";  //要连接的服务端IP地址    
//	     int port = 8089;   //要连接的服务端对应的监听端口    
//	     //与服务端建立连接    
//	     Socket client = new Socket(host, port);    
//	      //建立连接后就可以往服务端写数据了    
//	     Writer writer = new OutputStreamWriter(client.getOutputStream());    
//	      writer.write("Hello Server.");    
//	      writer.write("eof\n");    
//	      writer.flush();    
//	      //写完以后进行读操作    
//	     BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));    
//	      //设置超时间为10秒    
//	     client.setSoTimeout(10*1000);    
//	      StringBuffer sb = new StringBuffer();    
//	      String temp;    
//	      int index;    
//	      try {    
//	         while ((temp=br.readLine()) != null) {    
//	            if ((index = temp.indexOf("eof")) != -1) {    
//	                sb.append(temp.substring(0, index));  
//	                System.out.println(sb);
//	                break;    
//	            }    
//	            sb.append(temp);    
//	         }    
//	      } catch (SocketTimeoutException e) {    
//	         System.out.println("数据读取超时。");    
//	      }    
//	      System.out.println("from server: " + sb);    
//	      writer.close();    
//	      br.close();    
//	      client.close();    
//	   }
/*	public static void main(String args[]) throws Exception { 
		Client aaa = new Client();
		Map<String, Object> mapreq = new HashMap<String, Object>();
		mapreq.put("transMsgIP", "192.168.13.101");
		mapreq.put("transMsgPort", 8009);
		byte [] bateaa={1,1,1,1,1,1,};
		mapreq.put("transMsg", bateaa);
		mapreq.put("branchNum", "9900");
		aaa.transMsg(mapreq);
	}*/
	   
//	@Override
//	@MethodName("ISO8583Info")
	public Map<String, Object> transMsg(Map<String, Object> map) throws Exception {
		byte[] byteArrRet = null;
		
		String hostIP = map.get("transMsgIP").toString();
		Integer hostPort = 0;
		hostPort = (Integer)map.get("transMsgPort");
		byte[] byteArrRequest = null;
		byteArrRequest = (byte[])map.get("transMsg");
		String branchNum = map.get("branchNum").toString();
		if(null == hostIP || "".equals(hostIP) || hostPort == 0){
			
		}
		String strJournal = "";
		Socket socket = null;
		InputStream input = null;
		OutputStream output = null;
		DataInputStream din = null;
		socket = ClientSocketFactory.createSocketWithTimeout(hostIP,
				hostPort, ClientSocketFactory.DEF_CONNECT_TIMEOUT_MS);
		
		if (socket == null) {
			// 记录日志流水
			String strJrn = "Failed to connect (" + hostIP + ":" + hostPort + ")\r\n";
		}
		try {
//			String strCommTimeout = new MiscDb().get("0000", "CommTimeout", "35");
			int iCommTimeout = 30;//设置30秒超时
			socket.setSoTimeout(iCommTimeout * 1000);
			socket.setSoLinger(true, 3);
			input = socket.getInputStream();
			output = socket.getOutputStream();
			//din = new DataInputStream(input);
//			byte[] paperByte = DataConversion.toSHH(new Short(String.valueOf(byteArrRequest.length)));
//			output.write(paperByte, 0, paperByte.length);
			System.out.println("发送报文:" + new String(byteArrRequest,"GBK"));
			output.write(byteArrRequest, 0, byteArrRequest.length);
			output.flush();
			// 记录日志流水
			String strJrn = "HostReq-->"+ String.valueOf(byteArrRequest.length) +"\r\n" + DataConversion.formatBinaryData(byteArrRequest,byteArrRequest.length)+ "\r\n";
			strJournal += "[" + new DateEx().getTimeStrFull() + "]" + strJrn;
			
			byte[] byteArrHeadLen = new byte[4];
			int iRead = input.read(byteArrHeadLen, 0, 4);
	    	
			if (4 == iRead) 
			{
				int sResponseLen = Integer.parseInt(new String(byteArrHeadLen), 10);
			    byte[] byteArrResponse = new byte[sResponseLen];
			    sResponseLen = input.read(byteArrResponse);
			    byteArrRet = byteArrResponse;
			    System.out.println("接收报文:" + new String(byteArrRet,"UTF-8"));
			}
			
			// 记录日志流水
			strJrn = "HostResp-->"+ String.valueOf(byteArrRet.length) +"\r\n" + DataConversion.formatBinaryData(byteArrRet,byteArrRet.length)+ "\r\n";
			strJournal += "[" + new  DateEx().getTimeStrFull() + "]" + strJrn;
			
		}
		// 通讯超时时会出现该异常
		catch (InterruptedIOException e) {
			// 记录日志流水
			StringWriter sw = new StringWriter(1024 * 4);
			e.printStackTrace(new PrintWriter(sw));
			String strJrn = "(" + hostIP + ":" + hostPort + ")" + sw + "\r\n";
			strJournal += "[" + new  DateEx().getTimeStrFull() + "]" + strJrn;
			JournalThread.getInstance().append(branchNum, JournalThread.LEVEL_1, strJournal);
//			throw new CommException(CommException.TYPE_RECEIVE_TIMEOUT);
		} catch (Exception e) {
			// 记录日志流水
			StringWriter sw = new StringWriter(1024 * 4);
			e.printStackTrace(new PrintWriter(sw));
			String strJrn = "(" + hostIP + ":" + hostPort + ")" + sw + "\r\n";
			strJournal += "[" + new  DateEx().getTimeStrFull() + "]" + strJrn;
			JournalThread.getInstance().append(branchNum, JournalThread.LEVEL_1, strJournal);
			e.printStackTrace();
//			throw new CommException(CommException.TYPE_RECEIVE_FAILED);
		}
		finally
		{
			if (input != null)
				try {
					input.close();
				} catch (Exception e) {
				}
			if (output != null)
				try {
					output.close();
				} catch (Exception e) {
				}
			if (socket != null)
				try {
					socket.close();
				} catch (Exception e) {
				}
		}
		JournalThread.getInstance().append(branchNum, JournalThread.LEVEL_1, strJournal);
		Map<String, Object> mapMsg = new HashMap<String, Object>();
		mapMsg.put("response", byteArrRet);
		System.out.println(byteArrRet);
		return mapMsg;
	}
	
	
//	@Override
//	@MethodName("XMLInfo")
	public Map<String, Object> transMessage(Map<String, Object> map) throws Exception {
		byte[] byteArrRet = null;
		
		String hostIP = map.get("transMsgIP").toString();
		Integer hostPort = 0;
		hostPort = (Integer)map.get("transMsgPort");
		
		String tranReqInfo =  map.get("transInfo").toString();
		byte[] byteArrRequest = null;
		byteArrRequest = tranReqInfo.getBytes("UTF-8");
		
		String strJournal = "";
		Socket socket = null;
		InputStream input = null;
		OutputStream output = null;
		DataInputStream din = null;
		socket = ClientSocketFactory.createSocketWithTimeout(hostIP,
				hostPort, ClientSocketFactory.DEF_CONNECT_TIMEOUT_MS);
		
		
		System.out.println("发送xml报文");
		
		if (socket == null) {
			// 记录日志流水
			String strJrn = "Failed to connect (" + hostIP + ":" + hostPort + ")\r\n";
		}
		try {
//			String strCommTimeout = new MiscDb().get("0000", "CommTimeout", "35");
			int iCommTimeout = 30;
			socket.setSoTimeout(iCommTimeout * 1000);
			socket.setSoLinger(true, 5);
			input = socket.getInputStream();
			output = socket.getOutputStream();
			//din = new DataInputStream(input);
			byte[] paperByte = DataConversion.toSHH(new Short(String.valueOf(byteArrRequest.length)));
			
			
			output.write(paperByte, 0, paperByte.length);
			output.write(byteArrRequest, 0, byteArrRequest.length);
			output.flush();
			
			
			
			// 记录日志流水
			String strJrn = "HostReq-->"+ String.valueOf(byteArrRequest.length) +"\r\n" + DataConversion.formatBinaryData(byteArrRequest,byteArrRequest.length)+ "\r\n";
			strJournal += "[" + new  DateEx().getTimeStrFull() + "]" + strJrn;
			
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			
			int iLen = 0;
			byte[] buf = new byte[1024*64];
			try{
				while( -1 != (iLen = input.read(buf)) ){
					byteStream.write(buf,0,iLen);
				}
				byteArrRet = byteStream.toByteArray();
				
			}catch (Exception e) {

			}finally{
				if (byteStream != null)
					try {
						byteStream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
			 
			// 记录日志流水
			strJrn = "HostResp-->"+ String.valueOf(byteArrRet.length) +"\r\n" + DataConversion.formatBinaryData(byteArrRet,byteArrRet.length)+ "\r\n";
			strJournal += "[" + new  DateEx().getTimeStrFull() + "]" + strJrn;
			
		}
		// 通讯超时时会出现该异常
		catch (InterruptedIOException e) {
			// 记录日志流水
			StringWriter sw = new StringWriter(1024 * 4);
			e.printStackTrace(new PrintWriter(sw));
			String strJrn = "(" + hostIP + ":" + hostPort + ")" + sw + "\r\n";
			strJournal += "[" + new  DateEx().getTimeStrFull() + "]" + strJrn;
		//	JournalThread.getInstance().append(branchNum, JournalThread.LEVEL_1, strJournal);
//			throw new CommException(CommException.TYPE_RECEIVE_TIMEOUT);
		} catch (Exception e) {
			// 记录日志流水
			StringWriter sw = new StringWriter(1024 * 4);
			e.printStackTrace(new PrintWriter(sw));
			String strJrn = "(" + hostIP + ":" + hostPort + ")" + sw + "\r\n";
			strJournal += "[" + new  DateEx().getTimeStrFull() + "]" + strJrn;
		//	JournalThread.getInstance().append(branchNum, JournalThread.LEVEL_1, strJournal);
//			throw new CommException(CommException.TYPE_RECEIVE_FAILED);
		}
		finally
		{
			if (input != null)
				try {
					input.close();
				} catch (Exception e) {
				}
			if (output != null)
				try {
					output.close();
				} catch (Exception e) {
				}
			if (socket != null)
				try { 
					socket.close();
				} catch (Exception e) {
				}
		}
//		JournalThread.getInstance().append(branchNum, JournalThread.LEVEL_1, strJournal);
		Map<String, Object> mapMsg = new HashMap<String, Object>();
		
		mapMsg.put("response", byteArrRet);
		
		mapMsg.put("transMap", map.get("transInfo"));
		return mapMsg;
	}
	
	
//	@Override
//	@MethodName("webInfo")
	public Map<String, Object> transMessageWeb(Map<String, Object> map) throws Exception {
		byte[] byteArrRet = null;
		
		String hostIP = map.get("transMsgIP").toString();
		Integer hostPort = 0;
		hostPort = (Integer)map.get("transMsgPort");
		
		String tranReqInfo =  map.get("transInfo").toString();
		byte[] byteArrRequest = null;
		byteArrRequest = tranReqInfo.getBytes("UTF-8");
		
		
		
		
	/*	JaxWsProxyFactoryBean factoryBean=new JaxWsProxyFactoryBean();   
        factoryBean.getInInterceptors().add(new LoggingInInterceptor());   
        factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());   
        factoryBean.setServiceClass(WebServiceDealmsg.class);   
        factoryBean.setAddress("http://192.168.13.71:8081/dealMsg/dealWebservice");   
        WebServiceDealmsg approvalService=(WebServiceDealmsg) factoryBean.create();   
        byteArrRet = approvalService.dealMsg(byteArrRequest);
		Map<String, Object> mapMsg = new HashMap<String, Object>();
		
		mapMsg.put("response", byteArrRet);
		mapMsg.put("transMap", map.get("transInfo"));
		return mapMsg;*/
		return null;
	}

		
}     