package com.vui.bluelight.timer;
import java.util.Calendar;
import java.util.List;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.TimeUtils;

public class AlarmUtil {


	//private static final long INTERVALMILLIS = 7*24*60*60*1000;
	private static final long INTERVALMILLIS = 5*1000;
	/**
	 * 设置闹钟
	 * @param context
	 * @param alarmTimes 设置闹钟的时间
	 */
	public static void setAlarm(Context context,List<Long> alarmTimes) {
		LogUtils.i("要设置闹钟的集合的大小是："+alarmTimes.size());
		for (int i = 0; i < alarmTimes.size(); i++) {
			if(alarmTimes.get(i)==0){
				LogUtils.i("闹钟  "+i+"  等于0");
				continue;
			}
			LogUtils.i("闹钟："+TimeUtils.getFormedDate(alarmTimes.get(i))+" 将被设置");
			Intent intent = new Intent(context,AlarmService.class);
			int requestCode = getRequestCode(alarmTimes, i);
			PendingIntent sender = PendingIntent.getService(context,requestCode , intent,PendingIntent.FLAG_UPDATE_CURRENT);
			long startTime = getStartTime(alarmTimes.get(i));
			LogUtils.i("闹钟："+TimeUtils.getFormedDate(startTime)+"  被设置"+" 请求码："+requestCode);
			AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			manager.setRepeating(AlarmManager.RTC_WAKEUP,
					startTime, INTERVALMILLIS, sender);
		}	
	}

	/**
	 * 获取请求码
	 * @param alarmTimes
	 * @param i
	 * @return
	 */
	private static int getRequestCode(List<Long> alarmTimes, int i) {
		int requestCode = (int) (alarmTimes.get(i)/1000/10);
		return requestCode;
	}

	/**
	 * 开始时间必须大于当前的时间
	 * @param startTime
	 * @return
	 */
	private static Long getStartTime(Long startTime) {
		if(startTime<=System.currentTimeMillis()){
			LogUtils.i("增加7天前 时间是："+TimeUtils.getFormedDate(startTime));
			Calendar instance = Calendar.getInstance();
			instance.setTimeInMillis(startTime);
			instance.add(Calendar.DAY_OF_MONTH, 7);
			startTime=instance.getTimeInMillis();
			LogUtils.i("增加7天后 时间是："+TimeUtils.getFormedDate(startTime));
			//递归获取开始时间
			getStartTime(startTime);
		}else{
			return startTime;
		}
		return startTime;
	}

	/**
	 * 取消闹钟 
	 * @param context
	 * @param alarmTimes 取消的闹钟的时间
	 */
	public static  void cancelAlarm(Context context,List<Long> alarmTimes){
		LogUtils.i("要取消的闹钟的集合的大小是："+alarmTimes.size());
		for (int i = 0; i < alarmTimes.size(); i++) {	
			Intent intent = new Intent(context,AlarmService.class);
			int requestCode = getRequestCode(alarmTimes, i);
			LogUtils.i("闹钟："+TimeUtils.getFormedDate(alarmTimes.get(i))+" 被取消"+" 请求码："+requestCode);
			PendingIntent sender = PendingIntent.getService(context,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			manager.cancel(sender);
		}
	}
}