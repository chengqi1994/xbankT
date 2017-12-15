package com.hiaward.dataprocess.message.socket.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * <p>Title: 日期工具类</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Hiaward</p>
 * @author ZhuJian
 * @version 1.0
 * <p>2006/9/1 ZhuJian First release</p>
 */
public class DateEx extends Date
{
  /**
   * <p>构造函数（取当前日期时间）</p>
   */
  public DateEx()
  {
    super();
  }

  /**
   * <p>构造函数</p>
   * @param date Date 日期对象
   */
  public DateEx(Date date)
  {
    super.setTime(date.getTime());
  }

  /**
   * <p>按格式解析日期时间字符串，并设置解析结果日期时间</p>
   * @param strDate Date String in format ("yyyy/MM/dd HH:mm:ss");
   * @return boolean 是否成功
   */
  public boolean parseDate(String strDate)
  {
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Date date = sdf.parse(strDate);
      super.setTime(date.getTime());
      return true;
    }
    catch (Exception e)
    {
    }
    return false;
  }

  /**
   * <p>取年份</p>
   * @return int 年份
   */
  @Override
public int getYear()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    return calendar.get(Calendar.YEAR);
  }

  /**
   * <p>取月份</p>
   * @return int 月份
   */
  @Override
public int getMonth()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    return 1 + calendar.get(Calendar.MONTH);
  }

  /**
   * <p>取日期</p>
   * @return int 日期
   */
  public int getDayOfMonth()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * <p>取星期几</p>
   * @return int 从1计到7
   */
  public int getDayOfWeek()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    // 西方 1-7 分别表示 星期天－星期六
    int i = calendar.get(Calendar.DAY_OF_WEEK);
    if (i == 1)
      return 7;
    else
      return i-1;
  }

  /**
   * <p>取小时</p>
   * @return int 小时
   */
  public int getHourOfDay()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    return calendar.get(Calendar.HOUR_OF_DAY);
  }

  /**
   * <p>取分钟</p>
   * @return int 分钟
   */
  public int getMinute()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    return calendar.get(Calendar.MINUTE);
  }

  /**
   * <p>取秒数</p>
   * @return int 秒数
   */
  public int getSecond()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(this);
    return calendar.get(Calendar.SECOND);
  }

  /**
   * <p>Formats a Date into a date/time string</p>
   * @return String the formatted time string. format ("yyyy/MM/dd HH:mm:ss")
   */
  public String getDateTimeStrFull()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    return sdf.format(this);
  }

  /**
   * <p>Formats a Date into a date/time string</p>
   * @return String the formatted time string. format ("yyyyMMdd HHmmss")
   */
  public String getDateTimeStrSimple()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
    return sdf.format(this);
  }

  /**
   * <p>Formats a Date into a date/time string</p>
   * @return String the formatted time string. format ("yyyyMMdd")
   */
  public String getDateStrSimple()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    return sdf.format(this);
  }
  
  /**
   * <p>Formats a Date into a date/time string</p>
   * @return String the formatted time string. format ("yyyy-MM-dd")
   */
  public String getDateStrSimple(Date date)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(this);
  }

  /**
   * <p>Formats a Date into a date/time string</p>
   * @return String the formatted time string. format ("HHmmss")
   */
  public String getTimeStrSimple()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
    return sdf.format(this);
  }

  /**
   * <p>Formats a Date into a date/time string</p>
   * @return String the formatted time string. format ("HH:mm:ss")
   */
  public String getTimeStrFull()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    return sdf.format(this);
  }

  /**
   * <p>得到某一个月的最大天数</p>
   * @param strYear 年数
   * @param strMonth 月份
   * @return int 最大天数
   * <p>所有月份值都介于1-12</p>
   */
  public static int getMonthDayNumber(String strYear, String strMonth)
  {
    int year = DataConversion.str2Int(strYear, 1980);
    int month = DataConversion.str2Int(strMonth, 1);
    return getMonthDayNumber(year, month);
  }

  /**
   * <p>得到某一个月的最大天数</p>
   * @param year 年数
   * @param month 月份
   * @return int 最大天数
   * <p>所有月份值都介于1-12</p>
   */
  public static int getMonthDayNumber(int year, int month)
  {
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      // 如果是本月，就返回今天的日期
      if (cal.get(Calendar.YEAR) == year && cal.get(Calendar.MONTH)+1 == month)
        return cal.get(Calendar.DATE);

      cal.set(year, month-1, 1);
      return cal.getActualMaximum(Calendar.DATE);
  }

  /**
   * <p>得到一段日期之间的所有年月集合，包括起始和结束日期本身</p>
   * @param strStartDate 起始日期
   * @param strEndDate 结束日期
   * @return Object[] 年月字符串数组
   */
  public static Object[] getMonthArray(String strStartDate, String strEndDate)
  {
    ArrayList strArrList = new ArrayList();
    if (strStartDate.length() == 6 && strEndDate.length() == 6)
    {
      int iStartYear = DataConversion.str2Int(strStartDate.substring(0,4), 1980);
      int iStartMonth = DataConversion.str2Int(strStartDate.substring(4,6), 1);
      int iEndYear = DataConversion.str2Int(strEndDate.substring(0,4), 1980);
      int iEndMonth = DataConversion.str2Int(strEndDate.substring(4,6), 1);

      Calendar calEnd = Calendar.getInstance();
      calEnd.set(iEndYear, iEndMonth, 1);

      Calendar calStart = Calendar.getInstance();
      calStart.set(iStartYear, iStartMonth-1, 1);
      while (calStart.before(calEnd))
      {
        String str = DataConversion.getZeroPrefixStr(String.valueOf(calStart.get(Calendar.YEAR)), 4) +
          DataConversion.getZeroPrefixStr(String.valueOf(calStart.get(Calendar.MONTH)+1), 2);
        strArrList.add(str);
        calStart.add(Calendar.MONTH, 1);
      }
    }
    return strArrList.toArray();
  }

  /**
   * <p>Formats a Date into a date/time string</p>
   * @param strDate Date String in format ("yyyyMMddHHmmss");
   * @return String the formatted time string. format ("yyyy/MM/dd HH:mm:ss")
   */
  public String getDateTimeStrFull(String strDate)
  {
	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  Date date = null;
	 try
	 {
		 SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); 
         date = format.parse(strDate);   
	 }
	 catch(Exception ex)
	 {
		 date = this;
	 }
    
    return sdf.format(date);
  }
  
	public String getDateTimeMillisecondStrFull()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
		return sdf.format(this);
	}
  
	public String getStringDate_MonthToSec(){
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		String str = sdf.format(this);
		return str;
	}
	
	/**
	 * 获取入日期以前的日期
	 * @param date 时间
	 * @param type  1:一天前   2:一周前  3:一个月前
	 */
	public String getPreviousDay(Date date,String type) {
		SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		if("1".equals(type)){
			cl.add(Calendar.DAY_OF_YEAR, -1);	//一天
		}else if("2".equals(type)){
			cl.add(Calendar.WEEK_OF_YEAR, -1);	//一周
		}else if("3".equals(type)){
			cl.add(Calendar.MONTH, -1);			//一个月
		}
		Date dateFrom = cl.getTime();
		return sdf.format(dateFrom);
	}
}
