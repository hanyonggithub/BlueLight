package com.vui.bluelight.mod;

import java.util.Random;

import com.vui.bluelight.R;
import com.vui.bluelight.utils.LogUtils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Vibrator;

public class ShakeListenerUtils implements SensorEventListener
{

	ModeShakingActivity activity;
	public interface OnShakedListener{
		void OnShaked(int generalNumber);
	}
	OnShakedListener onShakedListener;
	private Vibrator vibrator;
	int number;
	boolean isLoaded=false;
	@SuppressWarnings("deprecation")
	public ShakeListenerUtils(ModeShakingActivity context,int number,OnShakedListener onShakedListener){
		super();
		this.number=number;
		vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE); 
		this.activity=context;
		mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		//初始化声音
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		shake_id = soundPool.load(context, R.raw.shake, 1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				isLoaded=true;			
			}
		});
		this.onShakedListener =onShakedListener;
	}

	long lastShakeTime;
	int range=9;
	private SoundPool soundPool;
	private int shake_id;
	private AudioManager mAudioManager;
	private int lastShakedValue=-1;

	// 速度阈值，当摇晃速度达到这值后产生作用  
	private static final int SPEED_SHRESHOLD = 2000; 
	@Override
	public void onSensorChanged(SensorEvent event){
		int sensorType = event.sensor.getType();
		//values[0]:X轴，values[1]：Y轴，values[2]：Z轴  :
		//其中Z轴为重力加速度  
		float[] values = event.values;
		if ((Math.abs(values[0]) >=range || Math.abs(values[1]) >= range|| Math
				.abs(values[2]) >= 14))
		{
			if(!activity.isOpenShaking){
				LogUtils.i("llpp: 摇一摇功能已经被关闭：");
				return;
			}
			//设计时间间隔
			long currentTimeMillis = System.currentTimeMillis();
			long dtime=currentTimeMillis-lastShakeTime;
			if(dtime<1500){
				return;
			}
			lastShakeTime=currentTimeMillis;
			//播放伴随声音
			playFolowSound();
			//获取随机值
			int nextInt = getRandomValue();				
			onShakedListener.OnShaked(nextInt);
			LogUtils.i("llpp:传感器改变=nSensorChanged +sensorType:"+sensorType+"值："+(int)values[0]+":"+(int)values[1]+":"+(int)values[2]+"  nextInt: "+nextInt);
		}
	}

	private void playFolowSound() {
		int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		LogUtils.i("llpp:系统的音量是==========："+streamVolume);
		if(streamVolume>0){
			if(isLoaded){
				soundPool.play(shake_id, 0.4f, 0.4f, 1, 0, 1);
			}
		}else{
			vibrator.vibrate(350);	
		}
	}

	private int getRandomValue() {
		Random random =new Random();
		//随机获取一个范围的值
		int nextInt = random.nextInt(number);
		//如果和上上次获取的一样
		if(lastShakedValue==nextInt){
			//再获取一次
			nextInt = random.nextInt(number);
		}
		//记录本次获取的值
		lastShakedValue=nextInt;
		return nextInt;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		//当传感器精度改变时回调该方法，Do nothing. 
	}

}
