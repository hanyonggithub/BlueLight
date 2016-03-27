package com.vui.bluelight.timer;

import java.util.Date;

import com.vui.bluelight.R;
import com.vui.bluelight.utils.LogUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		LogUtils.i("llpp:=收到广播"+intent.getStringExtra("name")+new Date(System.currentTimeMillis()));
		Vibrator vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE); 
		vibrator.vibrate(3000);
	
	
	}

}
