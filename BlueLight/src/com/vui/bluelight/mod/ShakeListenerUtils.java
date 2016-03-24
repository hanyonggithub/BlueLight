package com.vui.bluelight.mod;

import java.util.Random;

import com.vui.bluelight.utils.LogUtils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
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
	
	public ShakeListenerUtils(ModeShakingActivity context,int number,OnShakedListener onShakedListener){
		super();
		this.number=number;
		vibrator = (Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE); 
		this.activity=context;
		this.onShakedListener =onShakedListener;
	}

	long lastShakeTime;
	int range=11;
	@Override
	public void onSensorChanged(SensorEvent event){
		int sensorType = event.sensor.getType();
		//values[0]:X轴，values[1]：Y轴，values[2]：Z轴  :
		//其中Z轴为重力加速度  
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_ACCELEROMETER)
		{
			if ((Math.abs(values[0]) >=range || Math.abs(values[1]) >= range|| Math
					.abs(values[2]) >= range))
			{
				if(!activity.isOpenShaking){
						LogUtils.i("llpp: 摇一摇功能已经被关闭：");
						return;
				}
				long currentTimeMillis = System.currentTimeMillis();
				long dtime=currentTimeMillis-lastShakeTime;
				if(dtime<1500){
					//LogUtils.i("llpp==========连续摇动，被终止");
					return;
				}
				lastShakeTime=currentTimeMillis;
				vibrator.vibrate(350);
				Random random =new Random();
				int nextInt = random.nextInt(number);
				LogUtils.i("llpp:传感器改变=nSensorChanged +sensorType:"+sensorType+"值："+(int)values[0]+":"+(int)values[1]+":"+(int)values[2]+"nextInt: "+nextInt);
				onShakedListener.OnShaked(nextInt);
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		//当传感器精度改变时回调该方法，Do nothing. 
	}

}
