package com.hiaward.dataprocess.message.util;

/**
 * 
 * @author Administrator
 *
 */
public class CommonUtil {

	/**
	 * 非空判断并输出
	 * 
	 * @param obj
	 * @return
	 */
	public static String checkString(Object obj)
	{
		if (!"null".equals(obj) && null != obj && !"".equals(obj))
		{
			if (null == obj.toString() || "null".equals(obj.toString()))
			{
				return null;
			}
			return obj.toString();
		} else
		{
			return null;
		}
	}

	/**
	 * 非空判断并输出
	 * 
	 * @param obj
	 * @return
	 */
	public static String checkRate(Object obj)
	{
		if (!"null".equals(obj) && null != obj && !"".equals(obj))
		{
			if (null == obj.toString() || "null".equals(obj.toString()))
			{
				return "0";
			}
			return obj.toString();
		} else
		{
			return "0";
		}
	}

	/**
	 * 非空判断并输出
	 * 
	 * @param obj
	 * @return
	 */
	public static double checkValue(Object obj)
	{
		if (!"null".equals(obj) && null != obj && !"".equals(obj))
		{
			if (null == obj.toString() || "null".equals(obj.toString()))
			{
				return 0;
			}
			return Double.valueOf(obj.toString());
		} else
		{
			return 0;
		}
	}

	/**
	 * 非空判断并输出
	 * 
	 * @param obj
	 * @return
	 */
	public static int checkInt(Object obj)
	{
		if (!"null".equals(obj) && null != obj && !"".equals(obj))
		{
			if (null == obj.toString() || "null".equals(obj.toString()))
			{
				return 0;
			}
			return Integer.parseInt(obj.toString());
		} else
		{
			return 0;
		}
	}
	
	
	/**
	 * 非空判断并输出
	 * 
	 * @param obj
	 * @return
	 */
	public static long checkLong(Object obj)
	{
		if (!"null".equals(obj) && null != obj && !"".equals(obj))
		{
			if (null == obj.toString() || "null".equals(obj.toString()))
			{
				return 0;
			}
			return Long.parseLong(obj.toString());
		} else
		{
			return 0;
		}
	}
	
}
