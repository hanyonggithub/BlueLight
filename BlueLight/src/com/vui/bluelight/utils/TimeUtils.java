package com.vui.bluelight.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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


	/**
	 * 得到星期几
	 * @return
	 */
	public static int getDayOfWeek(){
		//不减一返回1到7 但是欧美的周日为第一天 ,返回一时表示周日，二时表示周一，故减一
		 int week=Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1;
		 if(week==0){
			 week=7;
		 }
		return 	week;
	}

	/**
	 * 获取格式化后的日期
	 * @param date
	 * @return
	 */
	public static String getFormedDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss",Locale.getDefault());
		String date_f = simpleDateFormat.format(date);
		return date_f;
	}
	
	/**
	 * 获取格式化后的日期
	 * @param date
	 * @return
	 */
	public static String getFormedDate(Long time) {
		Date date = new Date(time);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss",Locale.getDefault());
		String date_f = simpleDateFormat.format(date);
		return date_f;
	}
}
