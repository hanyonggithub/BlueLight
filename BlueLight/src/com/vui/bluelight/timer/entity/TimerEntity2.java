package com.vui.bluelight.timer.entity;

public class TimerEntity2 {
	public int type;//灯光定时 0 音乐定时2
	public int isOn;//关 0 开1
	public int weekdays;//定时周， 用一个byte 表示 ，转化为对应十进制整数
	public int color;//选择的颜色
	public int duration;//灯光开启的时间
	public int musicId;//音乐id
	
}
