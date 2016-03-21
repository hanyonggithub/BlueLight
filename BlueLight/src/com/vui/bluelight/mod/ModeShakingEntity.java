package com.vui.bluelight.mod;

import java.util.List;

public class ModeShakingEntity {

	List<Data> items;
	
	public class Data{
		public String text;
		public String color;
		
		//内存中使用
		boolean isShaked;
	}
}
