package com.vui.bluelight.timer.entity;

import java.util.ArrayList;
import java.util.List;

import com.vui.bluelight.utils.DataFormatUtils;
import com.vui.bluelight.utils.LogUtils;

import android.graphics.Color;

public class TimerEntity {
	public static final int TIMERTYPE_RGB=1;
	public static final int TIMERTYPE_WHITE=2;
	public static final int TIMERTYPE_MUSIC=3;
	public static final int TIMERTYPE_FLICKER=4;
	
	public List<Data> items;
	public static class Data{
		public int type;   //是开灯还是关灯
		public int state; //是否开启该定时任务
		public String time; 
		public int color;
		public int during;//亮灯的持续时间
		public List<Long> alarmTimes; //
		public List<Integer> week;
		public int timerType;// 定时类型 RGB white music flicker 默认没有选择
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			return super.equals(o);
		}
	
	}
	
	public static String parseToOrder(Data data,int index){
		if(index>15){
			LogUtils.e("超出定时组数！");
			return "";
		}
		String No="B".concat(Integer.toHexString(index));
		int week=0;
		if(data.type==1){
			week+=(1<<7);
		}
		for(int i=0;i<7;i++){
			if(data.week.get(i)==1){
				week+=(1<<i);
			}
		}
	
		String weekStr=DataFormatUtils.toHexStr(week);
		String[] times=data.time.split(":");
		String h=DataFormatUtils.toHexStr(Integer.parseInt(times[0]));
		String m=DataFormatUtils.toHexStr(Integer.parseInt(times[1]));
		String r=DataFormatUtils.toHexStr(Color.red(data.color));
		String g=DataFormatUtils.toHexStr(Color.green(data.color));
		String b=DataFormatUtils.toHexStr(Color.blue(data.color));
		String w="00";
		return No+weekStr+h+m+r+g+b+w+DataFormatUtils.getSUM();
	}
	
	public static Data parseToData(String order){
		Data data=new Data();
		data.week=new ArrayList<Integer>();
		String[] orders=order.split(" ");
		if(!orders[0].equals("B0")){
			return null;
		}
		int week=Integer.parseInt(orders[1],16);
		
			data.type=week>>7;
				data.week.add(week&1);
				data.week.add(week&(1<<1));
				data.week.add(week&(1<<2));
				data.week.add(week&(1<<3));
				data.week.add(week&(1<<4));
				data.week.add(week&(1<<5));
				data.week.add(week&(1<<6));
				data.week.add(week&(1<<7));
			data.time=String.format("%02d", Integer.parseInt(orders[2],16))+":"+String.format("%02d", Integer.parseInt(orders[2],16));
			data.state=1;
			data.color=Color.parseColor("#"+orders[4]+orders[5]+orders[6]);
			
			return data;
	}
	
	public static Data parseToData(byte[] bytes){
		
		
		return null;
	}
}
