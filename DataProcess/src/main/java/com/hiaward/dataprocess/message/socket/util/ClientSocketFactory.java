package com.hiaward.dataprocess.message.socket.util;

import java.net.Socket;
import java.util.Date;

import net.sf.json.JSONObject;






/**
 * <p>客户端SOCKET工厂类</p>
 * @author 朱建
 * @version 2003/08/15 朱建 First release
 * <p>java.net.Socket类建立连接时,如果连接超时会等待很长的时间才能返回(Windows:30s Aix:120s)。</p>
 * <p>使用该类可以设定超时时间返回连接,避免死等。</p>
 */

public class ClientSocketFactory
{
  // 缺省连接超时毫秒数
  public static final int DEF_CONNECT_TIMEOUT_MS = 18*1000;
  
  private Socket socket = null;
  private boolean bTimeout  = false;
  private boolean  bCreateFail = false;

  class CreateSocketThread extends Thread
  {
    private ClientSocketFactory CliSockFactory;
    private String  strIP;
    private int iPort;
    public CreateSocketThread(String ip, int port, ClientSocketFactory csf)
    {
      strIP = ip;
      iPort = port;
      CliSockFactory = csf;
    }
    @Override
	public void run()
    {
      try
      {
	    /*JSONObject maxSocketJ = GetParamDatasUtil.getParamDatas("maxSockets");
      	if(maxSocketJ.isEmpty())
      	{
      		//默认值
      		maxSocketJ.put("maxSocket", 2);
      	}
      	Integer maxSocket = maxSocketJ.getInt("maxSocket");*/
    	//OnlineUserCountCache onlineUserCountCache = new OnlineUserCountCache();
		boolean sockFlag = false;
    	Socket sock = new Socket();
        sock.setReuseAddress(true);
        sock.connect(new java.net.InetSocketAddress(strIP, iPort), ClientSocketFactory.DEF_CONNECT_TIMEOUT_MS);
    	/*if(OnlineUserCountCache.get()<maxSocket){
    		OnlineUserCountCache.inc();
  			sock.connect(new java.net.InetSocketAddress(strIP, iPort), ClientSocketFactory.DEF_CONNECT_TIMEOUT_MS);
  		}else{
			Date date = new Date();
			long dateTime = date.getTime();
  			//超过100的连接放到队列里面
  			OnlineUserCountCache.offer(sock);
			while( new Date().getTime() - dateTime<45000){
				if(OnlineUserCountCache.get()<maxSocket){
					sockFlag = true;
  					//当小于100连接数时，把队列的头移出放到数列里
  					OnlineUserCountCache.inc();
  					OnlineUserCountCache.poll().connect(new java.net.InetSocketAddress(strIP, iPort), ClientSocketFactory.DEF_CONNECT_TIMEOUT_MS);
  					break;
  				}
			}
			if(new Date().getTime() - dateTime>=45000 && sockFlag == false){
				//当连接数减一的标志位设为不可减
  					Constant.dec = false;
  					//超时也把队列里的头移出队列
  					OnlineUserCountCache.poll();
  					// 设置超时标志
  					bTimeout = true;
			}
  			for(int i =0;i<10;i++){
  				System.out.print(OnlineUserCountCache.get());
  				if(OnlineUserCountCache.get()<maxSocket){
  					//当小于100连接数时，把队列的头移出放到数列里
  					OnlineUserCountCache.inc();
  					OnlineUserCountCache.poll().connect(new java.net.InetSocketAddress(strIP, iPort), ClientSocketFactory.DEF_CONNECT_TIMEOUT_MS);
  					break;
  				}else if(i == 9){
  					//当连接数减一的标志位设为不可减
  					Constant.dec = false;
  					//超时也把队列里的头移出队列
  					OnlineUserCountCache.poll();
  					// 设置超时标志
  					bTimeout = true;
  					break;
  				};
  				
  			}
  		}*/
        if (CliSockFactory.bTimeout)
        {
          // 如果超时后才返回连接，没用了，立刻关闭它，释放资源
          sock.close();
        }
        else
        {
          CliSockFactory.socket = sock;
        }
      }
      catch (Exception e)
      {
    	  e.printStackTrace();
        CliSockFactory.bCreateFail = true;
      }
    }
  }

  /**
   * <p>建立SOCKET连接对象</p>
   * @param strIP      主机地址
   * @param iPort      端口号
   * @param iTimeout   连接超时时间毫秒数
   * @return           SOCKET连接对象,失败返回null
   */
  private Socket createSocket(String ip, int port, int timeout)
  {
  		CreateSocketThread cst = new CreateSocketThread(ip, port, this);
  	    cst.start();

  	    int WAIT_PERIOD_MILLISECOND = 50;
  	    while (timeout > 0)
  	    {
  	      // 成功建立连接或者建立失败，都立刻返回
  	      if (socket != null || bCreateFail){
  	        return socket;
  	      }
  	      try
  	      {
  	        Thread.sleep(WAIT_PERIOD_MILLISECOND);
  	      }
  	      catch (InterruptedException e)
  	      {
  	      }
  	      timeout -= WAIT_PERIOD_MILLISECOND;
  	    }
  	// 设置超时标志
	bTimeout = true;
	return null;
  	
  }

  /**
   * <p>建立SOCKET连接对象</p>
   * @param strIP      主机地址
   * @param iPort      端口号
   * @param iTimeout   连接超时时间毫秒数
   * @return           SOCKET连接对象,失败返回null
   */
  public static Socket createSocketWithTimeout(String ip, int port, int timeout)
  {
    ClientSocketFactory csf = new ClientSocketFactory();
    return csf.createSocket(ip, port, timeout);
  }
}
