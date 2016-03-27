package com.vui.bluelight.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.graphics.BitmapFactory;

public class BitmapUtils {
	public static Bitmap compressBmpFromBmp(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, 99, baos);
		while (baos.toByteArray().length / 1024 > 100) {
			LogUtils.e("baos.lenght=" + baos.toByteArray().length);
			baos.reset();
			options -= 5;
			LogUtils.e("options==" + options);
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	public static Bitmap compressImageFromStream(InputStream is) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		// Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		Log.e("BitmapUtils", "newOption.outWidth=" + w + ",newOpts.OutHeight=" + h);
		float hh = 200f;//
		float ww = 200f;//
		int be = 1;
		if (w > h && w > ww) {
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置采样率
		Log.e("BitmapUtils", "采样率=" + be);

		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
		try {
			Log.e("bitmapUtils", "is.lenght=" + is.available());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// bitmap = BitmapFactory.decodeStream(is, null, newOpts);
		byte[] data;
		try {
			data = IOUtils.toByteArray(is);
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, newOpts);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.e("BitmapUtils", "finally size=" + bitmap.getByteCount());

		return bitmap;
	}

	/** 保存方法 */
	public static void saveBitmap(Bitmap bm, String path, String picName) {

		File f = new File(path, picName);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getBitmapFromFile(String path) {
		return BitmapFactory.decodeFile(path);
	}
}
