package com.vui.bluelight.ble;

public class BleOrder {
	public class WriteOrder{
		public final static String music_mode="A10%sFFFFFFFFFFFF";
		public final static String music_sound="A2%sFFFFFFFFFFFF";
		public final static String light_color="A3%s%s%s%sFFFFFF";
		public final static String music_open="A4B0FFFFFFFFFFFF";
		public final static String music_close="A4B1FFFFFFFFFFFF";
		public final static String light_open="A4B2FFFFFFFFFFFF";
		public final static String light_close="A4B3FFFFFFFFFFFF";
		public final static String light_mode="A5%s%s%sFFFFFFFF";
		public final static String light_sub_color="C%s%s%s%sFFFFFFFF";
		public final static String time_set="A6%s%s%s%sFFFFFF";
		public final static String timer="B%s%s%s%s%s%s%s%s";
		public final static String device_info="A7FFFFFFFFFFFFFF";
		public final static String delete_timer="A8%sFFFFFFFFFFFF";
	}
	
	/**
	 * @param mode 1 rock, 2 pop,3 classic，4 jazz
	 * @return
	 */
	public static String getMusicMode(int mode){
		return String.format(BleOrder.WriteOrder.music_mode, mode);
	}
	
	/**
	 * @param soundHexStr 0X10-0XA0 代表声音灵敏度的十六进制字符串
	 * @return
	 */
	public static String getMusicSound(String soundHexStr){
		return String.format(BleOrder.WriteOrder.music_sound, soundHexStr);
	}
	
	/**
	 * @param r 十六进制值对应字符串
	 * @param g 十六进制值对应字符串
	 * @param b 十六进制值对应字符串
	 * @param w 十六进制值对应字符串
	 * @return
	 */
	public static String getLightColor(String r,String g,String b,String w){
		return String.format(BleOrder.WriteOrder.light_color,r,g,b,w);
	}
	
	
	public static String getMusicOpen(){
		return BleOrder.WriteOrder.music_open;
	}
	public static String getMusicClose(){
		return BleOrder.WriteOrder.music_close;
	}
	public static String getLightOpen(){
		return BleOrder.WriteOrder.light_open;
	}
	public static String getLightClose(){
		return BleOrder.WriteOrder.light_close;
	}
	
	public static String getLightMode(String num,String speed,String style){
		return String.format(BleOrder.WriteOrder.light_color,num,speed,style);
	}
	
	public static String getLightSubColor(String which,String r,String g,String b){
		return String.format(BleOrder.WriteOrder.light_sub_color,which,r,g,b);
	}
	
	public static String getTime(String week,String h,String m,String s){
		return String.format(BleOrder.WriteOrder.time_set,week,h,m,s);
	}
	

	public static String getTimer(String num,String week,String h,String m,String r,String g,String b,String w){
		return String.format(BleOrder.WriteOrder.timer,num,week,h,m,r,g,b,w);
	}
	
	public static String getDeviceInfo(){
		return BleOrder.WriteOrder.device_info;
	}
	
	public static String getTimerDelete(String which){
		return String.format(BleOrder.WriteOrder.delete_timer,which);
	}
	
	

	
	
}
