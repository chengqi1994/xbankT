package com.hiaward.dataprocess.message.socket.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 服务器文件流水处理线程类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: Hiaward
 * </p>
 * 
 * @author zhujian
 * @version 1.0 zhujian 2007/1/4
 */
public class JournalThread extends Thread
{
    /**
     * <p>
     * 服务器文件流水对象类
     * </p>
     */
    protected class JournalObject
    {
		public String strBranchNum;
		public String strJournal;
		public int iLevel;
    }

    // 最大缓存的对象数
    protected static int MAX_JOURNALOBJ_NUM = 1024;
    // 流水的级别(1-3)，1为只记最重要的，2次之，3最后
    public static int LEVEL_NONE = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;
    public static int LEVEL_3 = 3;
    
   
    // 当前的级别设置
    protected static int iCurLevel = LEVEL_3;

    // 服务器流水文件的目录
    public static String XBANKTRANS_TRACEPATH = "C:/home/atmp/log/";
    
    // 服务器流水文件的目录(行方)
    public static String XBANKTRANS_TRACEPATH_BANK = "C:/home/atmp/banklog/";
    
    // 测试日志
    public static String XBANKTRANS_TRADEPATH = "";
    /*// 压缩后的服务器流水文件的目录
    public static String XBANKTRANS_ZIPTRACEPATH = "/home/atmp/usr/xBankTrans/TraceZip/";
    // 数据库导出的文件目录
    public static String XBANKTRANS_DBEXPORTPATH = "/home/atmp/usr/xBankTrans/DbExport/";
    // 压缩后的数据库导出的文件目录
    public static String XBANKTRANS_ZIPDBEXPORTPATH = "/home/atmp/usr/xBankTrans/DbExport/";*/

    // 缓存对象
    protected ArrayList arrJournalBuf = new ArrayList(MAX_JOURNALOBJ_NUM);
    
    // 缓存对象
    protected List<JournalObjectBank> arrJournalBufBank = new ArrayList<JournalObjectBank>(MAX_JOURNALOBJ_NUM);

    // 静态实例
    protected static JournalThread instance = null;
    
    // 测试日志是否开启 false-关闭
    private boolean isOpenTestLog = false;

    /**
     * <p>
     * 获取对象实例
     * </p>
     * 
     * @return LinxViewProxyThread 对象实例
     */
    public static JournalThread getInstance()
    {
		synchronized (XBANKTRANS_TRACEPATH)
		{
		    if (instance == null)
		    {
		    	instance = new JournalThread();
				try
				{
					File traceFile = new File(XBANKTRANS_TRACEPATH);
					if(!traceFile.exists()){
						traceFile.mkdirs();
					}
				    instance.start();
				}
				catch (Exception e)
				{
				}
		    }
		}
		return instance;
    }

    public void append(String strBranchNum, Exception e)
    {
	    // 记录日志流水
		StringWriter sw = new StringWriter(1024 * 4);
		sw.write("[" + new DateEx().getTimeStrFull()
			+ "]");
		e.printStackTrace(new PrintWriter(sw));
		append(strBranchNum, JournalThread.LEVEL_1, sw.toString() + "\r\n");
    }

    /**
     * <p>
     * 添加流水
     * </p>
     * 
     * @param strBranchNum
     *            String 分行号
     * @param level
     *            int 流水级别
     * @param strJournal
     *            String 流水内容
     */
    public void append(String strBranchNum, int level, String strJournal)
    {
		if (strJournal == null || strJournal.length() == 0) return;
	
		if (strBranchNum == null || strBranchNum.trim().length() == 0) strBranchNum = "0000";
		JournalObject jobj = new JournalObject();
		jobj.strBranchNum = strBranchNum;
		jobj.iLevel = level;
		jobj.strJournal = strJournal;
		synchronized (XBANKTRANS_TRACEPATH)
		{
		    if (arrJournalBuf.size() >= MAX_JOURNALOBJ_NUM) arrJournalBuf.remove(0);
		    arrJournalBuf.add(jobj);
		}
    }
    
    /**
     * 行方日志
     * @param journalObjBank
     */
    public void append(JournalObjectBank journalObjBank)
    {
		synchronized (XBANKTRANS_TRACEPATH_BANK)
		{
		    if (arrJournalBufBank.size() >= MAX_JOURNALOBJ_NUM) arrJournalBufBank.remove(0);
		    arrJournalBufBank.add(journalObjBank);
		}
    }

    /**
     * <p>
     * 重载基类的run()函数，运行实际的线程处理程序
     * </p>
     */
    public void run()
    {
		while (true)
		{
		    try
		    {
		    	super.sleep(1000);
		    }
		    catch (Exception e)
		    {
		    }
		    Object[] arrJObj = null;
		    Object[] arrJObjBank = null;
		    synchronized (XBANKTRANS_TRACEPATH)
		    {
				arrJObj = arrJournalBuf.toArray();
				arrJournalBuf.clear();
				
				arrJObjBank = arrJournalBufBank.toArray();
				arrJournalBufBank.clear();
		    }
		    if ((arrJObj == null || arrJObj.length <= 0) && (arrJObjBank == null || arrJObjBank.length <= 0)) continue;
		    for (int i = 0; i < arrJObj.length; i++)
		    {
				JournalObject jobj = (JournalObject) arrJObj[i];
				processJournal(jobj.strBranchNum, jobj.iLevel, jobj.strJournal);
		    }
		    
		    for(int j = 0; j < arrJObjBank.length; j++)
		    {
		    	JournalObjectBank jobjBank =(JournalObjectBank) arrJObjBank[j];
		    	processJournalBank(jobjBank);
		    }
		}
    }

    /**
     * <p>
     * 处理流水对象
     * </p>
     * 
     * @param strBranchNum
     *            String 分行号
     * @param level
     *            int 流水级别
     * @param strJournal
     *            String 流水内容
     * @return boolean 是否成功
     */
    protected boolean processJournal(String strBranchNum, int level,
	    String strJournal)
    {
		if (level > JournalThread.LEVEL_NONE && level <= iCurLevel)
		{
		    String strDirPath = XBANKTRANS_TRACEPATH + strBranchNum;
		    
		    String strJournalFilePath = "";
		    if(isOpenTestLog)
		    {
		        strJournalFilePath = strDirPath + '/'
					    + XBANKTRANS_TRADEPATH + ".log";
		    }
		    
		    
		    String strJournalFilePath1 = strDirPath + '/'
				    + new DateEx().getDateStrSimple() + ".log";
		    
		    try
		    {
			File dir = new File(strDirPath);
			if (dir.isDirectory() || dir.mkdir())
			{
				if(isOpenTestLog)
			    {
					FileWriter fw = new FileWriter(strJournalFilePath, true);
				    fw.write(getStrLog(strJournal));
				    fw.close();
			    }
			   
			    FileWriter fw1 = new FileWriter(strJournalFilePath1, true);
			    fw1.write(getStrLog(strJournal));
			    fw1.close();
			    
			    
			    return true;
			}
		    }
		    catch (Exception e)
		    {
		    }
		}
		return false;
    }
    
    protected boolean processJournalBank(JournalObjectBank journalObjBank)
    {
    		
		    String strDirPath = XBANKTRANS_TRACEPATH_BANK ;
		 
		    String strJournalFilePath = strDirPath + File.separator
				    + new DateEx().getDateStrSimple() + ".log";
		    
		    try
		    {
				File dir = new File(strDirPath);
				if (dir.isDirectory() || dir.mkdir())
				{
				    FileWriter fw = new FileWriter(strJournalFilePath, true);
				    fw.write(getStrLogBank(journalObjBank));
				    fw.close();
	
				    return true;
				}
		    }
		    catch (Exception e)
		    {
		    }
    		
    		return false;
     }
    
    /**
     * 
     * 日志输入内容控制
     * @param object
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getStrLog(String strJournal)
    {        
        String oneChar = strJournal.substring(0, 1);
        if("{".equals(oneChar)){
        	return FormatUtil.formatJsonToStr(strJournal) + "\r\n" ;
        }else{
        	return strJournal + "\r\n" ;
        }
        // 过滤字符串中的磁道信息    
    }
    
    private String getStrLogBank(JournalObjectBank journalObjBank)
    {      
    	String strSplit = "|+|";
        String result = "";
        result = journalObjBank.getTransTime() + strSplit 
        	      + journalObjBank.getChnlNo() + strSplit
        	      + journalObjBank.getRetCode() + strSplit
        	      + journalObjBank.getTransCode() + strSplit
        	      + journalObjBank.getCostTime() 
                  + "\n";
        return result;
    }
}
