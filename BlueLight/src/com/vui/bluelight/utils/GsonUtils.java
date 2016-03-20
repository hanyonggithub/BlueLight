package com.vui.bluelight.utils;
import java.io.InputStream;

import android.content.Context;

import com.google.gson.Gson;

public class GsonUtils {

	public  static <T> T getGson(Context context,String assetPath,Class<T> t_class){
		T t = null;
		try {
			String json = getAssertString(context, assetPath);
			Gson gson = new Gson();
			t= gson.fromJson(json, t_class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	private static String getAssertString(Context context, String assetPath)
		 {
		String json = null;
		try {
			InputStream in = context.getResources().getAssets().open(assetPath);
			byte[] buffer= new byte[in.available()];
			in.read(buffer);
			json = new String(buffer);		
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
