package com.vui.bluelight;
import java.util.List;

import com.vui.bluelight.customview.SwipeAdapter;
import com.vui.bluelight.timer.AlarmUtil;
import com.vui.bluelight.timer.entity.TimerEntity;
import com.vui.bluelight.timer.entity.TimerEntity.Data;
import com.vui.bluelight.utils.LogUtils;

import android.app.Application;

public class MyApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.i("llpp:========onCreate=============");
		initAlarm();
	}

	/**
	 * 进程被干掉 先前的闹钟也会完蛋，故重启应用时，再设置一下
	 */
	private void initAlarm() {
		LogUtils.i("llpp:开始初始化闹钟=======");
		TimerEntity timerEntity= SwipeAdapter.getTimerEntity(getApplicationContext());
		if(timerEntity!=null){
			List<Data> items = timerEntity.items;
			LogUtils.i("llpp:items的大小是："+items.size());
			for (int i = 0; i <items.size(); i++) {
				Data data = items.get(i);
				List<Long> alarmTimes = data.alarmTimes;
				if(data.state==1){
					AlarmUtil.setAlarm(getApplicationContext(), alarmTimes);
				}


			}
		}else{
			LogUtils.i("llpp:================:timerEntity==null");	
		}
	}
}
