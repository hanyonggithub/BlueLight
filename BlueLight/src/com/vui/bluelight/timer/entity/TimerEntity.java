package com.vui.bluelight.timer.entity;

import java.util.List;

public class TimerEntity {
	public static final int TIMERTYPE_UNSELECT=0;
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
		public List<Long> alarmTimes; //
		public List<Integer> week;
		public int TimerType;// 定时类型 RGB white music flicker 默认没有选择
	
	}
}
