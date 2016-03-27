package com.vui.bluelight.timer;

import java.util.Date;

import com.vui.bluelight.utils.LogUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

public class AlarmService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.i("llpp==========onCreate===============");
	}
	
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	LogUtils.i("llpp==========onStartCommand===============");
	LogUtils.i("llpp:=消息"+intent.getStringExtra("name")+new Date(System.currentTimeMillis()));
	Vibrator vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE); 
	vibrator.vibrate(300);
/*    new Thread(new Runnable() {
		
		@Override
		public void run() {
			LogUtils.i("llpp==========onStartCommand===============");
			while(true){
				LogUtils.i("llpp==========onStartCommand===============");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}).start();*/
	return Service.START_STICKY;
}

@Override
public void onDestroy() {
	super.onDestroy();
	LogUtils.i("llpp==========onDestroy===============");
}
}
