package com.vui.bluelight.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
	
	public static String parseMills2TimeStr(int mills){
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。  
		formatter.setTimeZone(TimeZone.getTimeZone("ETC/GMT-0"));
		  
		String hms = formatter.format((long)mills);  
		if(hms.startsWith("00:")){
			hms=hms.substring(3,hms.length());
		}
		return hms;
		
	}
}
