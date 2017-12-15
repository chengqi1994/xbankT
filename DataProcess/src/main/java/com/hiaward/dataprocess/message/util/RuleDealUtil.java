package com.hiaward.dataprocess.message.util;

import java.util.HashMap;
import java.util.Map;

public class RuleDealUtil {
	public static RuleDealUtil instance = null;
	
	public static RuleDealUtil getInstance() {
		
		if(instance == null)
		{
			instance = new RuleDealUtil();
		}
		return instance;
	}

	public String check(String rule, String rulepro, String value) {
		String rulevalue = "";
		Map<String, String> map = new HashMap<String, String>();
		if(null == value || value.equals("")){
			rulevalue = "";
		}else {
			if(Constant.T0001.equalsIgnoreCase(rule)){
				//T0001
				map = RuleDealUtil.getInstance().T0001(rulepro, value);
			}else if(Constant.T0002.equalsIgnoreCase(rule)){
				//T0002
				map = RuleDealUtil.getInstance().T0002(rulepro, value);
				
			}else{
				//Z0001
				map = RuleDealUtil.getInstance().Z0001(rule,rulepro, value);
			}
		}
		if("success".equalsIgnoreCase(map.get("message"))){
			rulevalue = map.get("TT0000");
		}
		return rulevalue;
	}
	
	
	public Map<String, String> T0001(String rulepro, String value){
		Map<String, String> map = new HashMap<String, String>();
		String []profix = rulepro.split(",");
		if(Constant.PRE_FILL.equalsIgnoreCase(profix[0])){
			value = profix[1] + value;
			map.put("code", "TT0000");
			map.put("message", value);
		}else if(Constant.BACK_UP.equalsIgnoreCase(profix[0])){
			value = value + profix[1];
			map.put("code", "TT0000");
			map.put("message", value);
		}else{
			System.out.println("T0001传输数据格式不正确，请检查！");
			map.put("code", "TC0001");
			map.put("message", "T0001入参不符合要求,请检查！");
			return map;
		}
		return map;
	}
	
	public Map<String, String> T0002(String rulepro, String value){
		Map<String, String> map = new HashMap<String, String>();
		String pro = "";
		String back = "";
		String end = "";
		if(rulepro.contains(",")){
			String []profix = rulepro.split(",");
			if(profix.length > 2){
				System.out.println("T0002传输数据格式不正确，请检查！");
				map.put("code", "TC0001");
				map.put("message", "T0002入参不符合要求,请检查！");
				return map;	
			}
			//开始位数是否在字符串长度范围内
//			String result = value.toString();
//			System.out.println("value的值：" + result);
//			System.out.println("null长度:" + result.length());
			if(Integer.valueOf(profix[0]) > 0 && Integer.valueOf(profix[0]) <= value.length()){
			pro = value.substring(0, Integer.valueOf(profix[0]) - 1);
				if(Constant.END_FILL.equalsIgnoreCase(profix[1])){//直到最后一位都变成*
					for(int i = 0;i < (value.length() - pro.length()); i++){
						back += "*";
					}
				}else{
					//变*位数是否超过字符串长度
					if(Integer.valueOf(profix[1]) <= (value.length() - Integer.valueOf(profix[0]) + 1)){
						for(int i = 0;i < Integer.valueOf(profix[1]);i++){
							back += "*";
						}
						end = value.substring(Integer.valueOf(profix[0]) + Integer.valueOf(profix[1]) - 1);
					}else{
						System.out.println("T0002传输数据格式不正确，请检查！");
						map.put("code", "TC0001");
						map.put("message", "T0002入参不符合要求,请检查！");
						return map;
					}
				}
				value = pro + back + end;
				map.put("code", "TT0000");
				map.put("message", value);
			}else{
				System.out.println("T0002传输数据格式不正确，请检查！");
				map.put("code", "TC0001");
				map.put("message", "T0002入参不符合要求,请检查！");
				return map;
			}
		}else{//将指定位变为*
				if(Integer.valueOf(rulepro) > 0 && Integer.valueOf(rulepro) <= value.length()){
					value = value.substring(0, Integer.valueOf(rulepro) - 1) + "*" + value.substring(Integer.valueOf(rulepro));
					map.put("code", "TT0000");
					map.put("message", value);
				}else{
					System.out.println("T0002传输数据格式不正确，请检查！");
					map.put("code", "TC0001");
					map.put("message", "T0002入参不符合要求,请检查！");
					return map;
				}
		}
		return map;
	}
	/**
	 * 自定义处理方法
	 * @param rule
	 * @param rulepro
	 * @param value
	 * @return
	 */
	public Map<String, String> Z0001(String rule,String rulepro, String value){
		Map<String, String> map = new HashMap<String, String>();
		int type = Integer.valueOf(rule.substring(rule.length() - 4, rule.length()));
		switch (type) {

		default:
			map.put("code", "TT0000");
			map.put("message", value);
			break;
		}
		return map;
	}
	
	
	public static void main(String[] args) {
		RuleDealUtil ruleDeal = new RuleDealUtil();
//		System.out.println("StringStr".length());
//		System.out.println(ruleDeal.check("T0001", "3,4567", "valuevaluevalue"));
		System.out.println(ruleDeal.check("T0002", "3,1", "valuevaluevalue"));
//		System.out.println(ruleDeal.check("Z0001", "3,4567", "valuevalue"));
//		ruleDeal.check("T0001", "1,0000", "value");
	}
	
	
}
