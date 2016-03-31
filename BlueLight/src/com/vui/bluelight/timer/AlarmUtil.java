package com.vui.bluelight.timer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.vui.bluelight.customview.SwipeAdapter;
import com.vui.bluelight.timer.entity.TimerEntity;
import com.vui.bluelight.timer.entity.TimerEntity.Data;
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
		if(startTime==0){
			return 0L;
		}
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

	
	/**
	 * 获取最近的任务时间
	 * @param context
	 * @param isMusicType 是否是音乐
	 * @return 返回两个元素是数组 第一个是小时 第二个分钟
	 */
	public static int[] getLatestTaskTime(Context context,boolean isMusicType){
		int[] lastetTime = new int[2];
		long latestTime =0;
		if(isMusicType){
			latestTime = getLatestMusicTime(context);
		}else{
			latestTime = getLatestLightTime(context);
		}
		long interval = latestTime-System.currentTimeMillis();
		int hour = 60*60*1000;
		lastetTime[0]=(int) (interval/hour);
		lastetTime[1]= (int) (interval%hour)/1000/60;
		LogUtils.i("llpp:=====最近的任务距离现在：hours:"+lastetTime[0]+" minutes："+lastetTime[1]);
		return lastetTime;
	}
	/**
	 * 获取最近的light定时任务的开始时间
	 *  返回-1 表示没有获取到
	 * 每新建一个灯光或者删除一个灯光都对最近将活动的灯光有影响，时间也对最近活动的灯光有影响，
	 */
	private static long getLatestLightTime(Context context){
		TimerEntity timerEntity = SwipeAdapter.getTimerEntity(context);
		ArrayList<Long> dates = new ArrayList<Long>();
		if(timerEntity!=null){
			List<Data> items = timerEntity.items;
			if(items!=null){
				int size = items.size();
				for (int i = 0; i < size; i++) {
					Data data = items.get(i);
					if(data.timerType!=TimerEntity.TIMERTYPE_MUSIC&&data.state==1){//如果不等于音乐类型,并且是开启状态
						List<Long> alarmTimes = data.alarmTimes;
						for (int j = 0; j < alarmTimes.size()-1; j++) {
							Long time = alarmTimes.get(j);
							if(time==0){//如果今天没有任务
								continue;
							}
							//把所有的开启定时任务的数据全部取出来，放到一个集合当中，并且保证每个数据的时间都大于当前时间
							Long startTime = getStartTime(time);
							dates.add(startTime);
						}
					}
				}
				//给数据排序
				Collections.sort(dates);
				
				for (int i = 0; i < dates.size(); i++) {
					String formedDate = TimeUtils.getFormedDate(dates.get(i));
					LogUtils.i("llpp:排序后的日期数据是："+formedDate);
				}
				LogUtils.i("llpp:返回的最近日期是："+TimeUtils.getFormedDate(dates.get(0)));
				return dates.get(0);
			}else{
				LogUtils.i("llpp:=====获取最近的任务失败 没有数据=====items = null");
			}
		}else{
			LogUtils.i("llpp:=====获取最近的任务失败 没有数据=====timerEntity = null");
		}
		return -1;
	}


	/**
	 * 获取最近的音乐定时任务的开始时间
	 * 返回-1 表示没有获取到
	 */
	private static long getLatestMusicTime(Context context){
		TimerEntity timerEntity = SwipeAdapter.getTimerEntity(context);
		ArrayList<Long> dates = new ArrayList<Long>();
		if(timerEntity!=null){
			List<Data> items = timerEntity.items;
			if(items!=null){
				int size = items.size();
				for (int i = 0; i < size; i++) {
					Data data = items.get(i);
					if(data.timerType==TimerEntity.TIMERTYPE_MUSIC&&data.state==1){//如果不等于音乐类型,并且是开启状态
						List<Long> alarmTimes = data.alarmTimes;
						for (int j = 0; j < alarmTimes.size()-1; j++) {
							Long time = alarmTimes.get(j);
							if(time==0){//如果今天没有任务
								continue;
							}
							//把所有的开启定时任务的数据全部取出来，放到一个集合当中，并且保证每个数据的时间都大于当前时间
							Long startTime = getStartTime(time);
							dates.add(startTime);
						}
					}
				}
				//给数据排序
				Collections.sort(dates);
				for (int i = 0; i < dates.size(); i++) {
					String formedDate = TimeUtils.getFormedDate(dates.get(i));
					LogUtils.i("llpp:排序后的日期数据是："+formedDate);
				}
				LogUtils.i("llpp:返回的最近日期是："+TimeUtils.getFormedDate(dates.get(0)));
				return dates.get(0);
			}else{
				LogUtils.i("llpp:=====获取最近的任务失败 没有数据=====items = null");
			}
		}else{
			LogUtils.i("llpp:=====获取最近的任务失败 没有数据=====timerEntity = null");
		}
		return -1;
		
	}
}