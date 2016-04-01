package com.vui.bluelight.utils;

import java.util.Random;

public class DataFormatUtils {
	
	public static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		b = null;
		return b2;
	}
	
	
	public static String toHexStr(int a){
		String result=Integer.toHexString(a);
		if(result.length()==1){
			result="0".concat(result);
		}
		return result;
	}
	
	public static String getSUM(){
		Random random=new Random();
		return DataFormatUtils.toHexStr(random.nextInt(255));
		
	}
}
