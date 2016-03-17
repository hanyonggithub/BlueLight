package com.vui.bluelight.timer.entity;

import java.util.List;

public class TimerEntity {
	public List<Data> items;
	public class Data{
		public int type;
		public int state;
		public String time;
		public List<Integer> week;
	}
}
