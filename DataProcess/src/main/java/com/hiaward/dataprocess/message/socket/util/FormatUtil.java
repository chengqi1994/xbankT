package com.hiaward.dataprocess.message.socket.util;



import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public final class FormatUtil {
	 
	    /**
	     * 格式化
	     * @param jsonStr
	     * @return
	     * @author   
	     * @Date   
	     */
	    public static String formatJsonToStr(String jsonStr) {
	        if (null == jsonStr || "".equals(jsonStr)) return "";
	        StringBuilder sb = new StringBuilder();
	        char last = '\0';
	        char current = '\0';
	        int indent = 0;
	        for (int i = 0; i < jsonStr.length(); i++) {
	            last = current;
	            current = jsonStr.charAt(i);
	            switch (current) {
	                case '{':
	                case '[':
	                    sb.append(current);
	                    sb.append('\n');
	                    indent++;
	                    addIndentBlank(sb, indent);
	                    break;
	                case '}':
	                case ']':
	                    sb.append('\n');
	                    indent--;
	                    addIndentBlank(sb, indent);
	                    sb.append(current);
	                    break;
	                case ',':
	                    sb.append(current);
	                    if (last != '\\') {
	                        sb.append('\n');
	                        addIndentBlank(sb, indent);
	                    }
	                    break;
	                default:
	                    sb.append(current);
	            }
	        }
	 
	        return sb.toString();
	    }
	 
	    /**
	     * 添加space
	     * @param sb
	     * @param indent
	     * @author  
	     * @Date   
	     */
	    private static void addIndentBlank(StringBuilder sb, int indent) {
	        for (int i = 0; i < indent; i++) {
	            sb.append('\t');
	        }
	    }
	    
	    /**
	     * 
	     * @param jsonStr
	     * @return
	     */
	    public static String formatMap(String mapStr) {
	    	 if (null == mapStr || "".equals(mapStr)) return "";
	    	 StringBuffer sb = new StringBuffer(mapStr);
	    	 
	    	 return sb.toString();
	    }
	    
	    /**
	     * 
	     * @param map
	     * @return
	     */
	    public static String formatMapToStr(Map<String, String> map) {
	    	 if (null == map || map.size() == 0) return "";
	    	 StringBuffer sb = new StringBuffer();
	    	 
	    	 for (Map.Entry<String, String> entry : map.entrySet()) {
	    		 if(StringUtils.isNotBlank(entry.getValue())){
	    			 sb.append(entry.getKey()).append("=").append(entry.getValue());
		    		 sb.append("\n");
	    		 }
	    		
	    	 }
	    	 return sb.substring(0, sb.lastIndexOf("\n")).toString();
	    }
	    
	    /**
	     * 
	     * @return
	     */
	    public static String formatXmlToString(Document document){
	    	if(null == document){
	    		return "";
	    	}
	    	 //创建字符串缓冲区 
	        StringWriter stringWriter = new StringWriter();  
	        
	        //设置文件编码  
	        OutputFormat xmlFormat = new OutputFormat();  
	        xmlFormat.setEncoding("UTF-8"); 
	        // 设置换行 
	        xmlFormat.setNewlines(true); 
	        // 生成缩进 
	        xmlFormat.setIndent(true); 
	        // 使用4个空格进行缩进, 可以兼容文本编辑器 
	        xmlFormat.setIndent("    "); 
	        
	        //创建写文件方法  
	        XMLWriter xmlWriter = new XMLWriter(stringWriter,xmlFormat);  
	        
	        try {
	        	xmlWriter.write(document);
		        xmlWriter.flush();
		        xmlWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        
	    	return stringWriter.toString();
	    }
	}