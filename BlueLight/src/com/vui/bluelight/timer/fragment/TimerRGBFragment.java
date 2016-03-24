package com.vui.bluelight.timer.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.vui.bluelight.R;
import com.vui.bluelight.R.id;
import com.vui.bluelight.customview.SwipeAdapter;
import com.vui.bluelight.customview.WheelView;
import com.vui.bluelight.music.MusicListActivity;
import com.vui.bluelight.timer.TimerActivity;
import com.vui.bluelight.timer.entity.TimerEntity;
import com.vui.bluelight.timer.entity.TimerEntity.Data;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.ScreenUtils;
import com.vui.bluelight.utils.TimeUtils;

public class TimerRGBFragment extends Fragment{
	private TimerEntity timerEntity;
	private TimerEntity.Data newTimer;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){
			view = View.inflate(getActivity(),R.layout.frag_timerrgb, null);
			initData();		
			initTitleBar(view);
			initOnOffView(view);
			initWheelView(view);
			initWeekView(view);
			initChooseView(view);
			initTypeChooseView(view);
		}


		return view;
	}

	private void initTitleBar(View view) {
		View back = view.findViewById(R.id.back);
		View right_btn = view.findViewById(R.id.right_btn);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backTimerFragment();
			}

		});
		right_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checkCanCommit = checkCanCommit();
				if(checkCanCommit){
					timerEntity.items.add(newTimer);
					SwipeAdapter.saveToLocal(timerEntity, getActivity());
					backTimerFragment();
				}else{
					Toast.makeText(getActivity(), "please choose days of the week", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
	protected boolean checkCanCommit() {
		int size = newTimer.week.size();
		for (int i = 0; i < size; i++) {
			if(newTimer.week.get(i)==1)//只要选择一个就可以通过
				return true;
		}
		//一个都没有选择
		return false;
	}

	private void backTimerFragment() {
		TimerFragment timerFragment = new TimerFragment();
		TimerActivity.switchFragment(TimerRGBFragment.this,timerFragment, (TimerActivity) getActivity());

	}
	private void initTypeChooseView(View view) {
		RadioGroup rg_type_select_content = (RadioGroup) view. findViewById(R.id.rg_type_select_content);
		rg_type_select_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.rb_rgb){
					bt_choose.setText("choose RGB color");
					bt_choose.setTag(TimerEntity.TIMERTYPE_RGB);
					rb_on.setText("lights on");
					rb_off.setText("lights off");
				}else if(checkedId==R.id.rb_white){
					bt_choose.setText("choose white color");
					bt_choose.setTag(TimerEntity.TIMERTYPE_WHITE);
					rb_on.setText("lights on");
					rb_off.setText("lights off");
				}else if(checkedId==R.id.rb_music){
					bt_choose.setText("choose music");
					bt_choose.setTag(TimerEntity.TIMERTYPE_MUSIC);
					rb_on.setText("music on");
					rb_off.setText("music off");
				}else if(checkedId==R.id.rb_flicker){
					bt_choose.setText("choose flicker style");
					bt_choose.setTag(TimerEntity.TIMERTYPE_FLICKER);
					rb_on.setText("flicker on");
					rb_off.setText("flicker off");
				}

			}
		});

	}

	public void setChooseViewColor(int color){
		bt_choose.setTextColor(color);
	}
	private ChooseRGBFragment chooseRGBFragment;
	private void initChooseView(View view) {
		bt_choose = (Button) view. findViewById(R.id.bt_choose);
		//设置默认关联
		bt_choose.setTag(TimerEntity.TIMERTYPE_RGB);
		bt_choose.setOnClickListener(new OnClickListener() {		
			private ChooseWhiteFragment chooseWhiteFragment;

			@Override
			public void onClick(View v) {	

				int tag = (Integer) v.getTag();
				switch (tag) {
				case TimerEntity.TIMERTYPE_RGB:
					if(chooseRGBFragment==null){
						chooseRGBFragment = new ChooseRGBFragment();
					}				
					TimerActivity.switchFragment(TimerRGBFragment.this,chooseRGBFragment, (TimerActivity) getActivity());
					break;
				case TimerEntity.TIMERTYPE_WHITE:
					if(chooseWhiteFragment==null){
						chooseWhiteFragment = new ChooseWhiteFragment();
					}
					TimerActivity.switchFragment(TimerRGBFragment.this,chooseWhiteFragment, (TimerActivity) getActivity());
					break;
				case TimerEntity.TIMERTYPE_MUSIC:
					Intent intent=new Intent(getActivity(), MusicListActivity.class);
					startActivity(intent);
					break;
				case TimerEntity.TIMERTYPE_FLICKER:
					Toast.makeText(getActivity(), "开发中。。。", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 选择的是星期几 默认为0表示没有选择
	 */
	int selected_week=0;
	private void initWeekView(View view) {
		final LinearLayout llt_week_content = (LinearLayout) view. findViewById(R.id.llt_week_content);
		int childCount = llt_week_content.getChildCount();
		for (int i = 0; i <childCount; i++) {
			CheckBox checkBox = (android.widget.CheckBox) llt_week_content.getChildAt(i);
			checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					switch (buttonView.getId()) {
					//
					case R.id.cb_1:
						newTimer.week.remove(0);
						newTimer.week.add(0, isChecked? 1:0);
						selected_week=1;
						break;
					case R.id.cb_2:
						newTimer.week.remove(1);
						newTimer.week.add(1, isChecked? 1:0);
						selected_week=2;
						break;
					case R.id.cb_3:
						newTimer.week.remove(2);
						newTimer.week.add(2, isChecked? 1:0);
						selected_week=3;
						break;
					case R.id.cb_4:
						newTimer.week.remove(3);
						newTimer.week.add(3, isChecked? 1:0);
						selected_week=4;
						break;
					case R.id.cb_5:
						newTimer.week.remove(4);
						newTimer.week.add(4, isChecked? 1:0);
						selected_week=5;
						break;
					case R.id.cb_6:
						newTimer.week.remove(5);
						newTimer.week.add(5, isChecked? 1:0);
						selected_week=6;
						break;
					case R.id.cb_7:
						newTimer.week.remove(6);
						newTimer.week.add(6, isChecked? 1:0);
						selected_week=7;
						break;
					default:
						break;
					}
					LogUtils.i(newTimer.week.toString());
					handSelectedTimes = handSelectedTime(selectedHour, selectedMinute);
					updateTipData(handSelectedTimes);
					//LogUtils.i(newTimer.week.toString());
				}
			});
		}
	}
	private int[] handSelectedTimes;
	private void initData() {
		timerEntity = SwipeAdapter.getTimerEntity(getActivity());
		//第一次是没有数据的
		if(timerEntity==null){
			timerEntity=new TimerEntity();
			timerEntity.items=new ArrayList<TimerEntity.Data>();
		}
		//本次新建timer的对象
		newTimer = new TimerEntity.Data();
		newTimer.state=1;//初始值 默认显示 ON
		newTimer.time="00:00";
		newTimer.type=1; //默认选中 lights on
		newTimer.TimerType=TimerEntity.TIMERTYPE_UNSELECT; //默认没有选则
		newTimer.week=new ArrayList<Integer>();
		for (int i = 0; i < 7; i++) {
			newTimer.week.add(0);//七天默认全部没有选择
		}
	}

	private void initOnOffView(View view) {
		RadioGroup rg_onoff = (RadioGroup) view.findViewById(R.id.rg_onoff);
		rb_on = (RadioButton) view.findViewById(R.id.rb_on);
		rb_off = (RadioButton) view.findViewById(R.id.rb_off);
		rg_onoff.setOnCheckedChangeListener(new OnCheckedChangeListener(
				) {		
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.rb_on){
					newTimer.type=1;
				}else{
					newTimer.type=0;
				}
				updateTipData(handSelectedTimes);
			}
		});
	}
	/**
	 * 选择的时间 默认0
	 */
	int selectedHour;
	int selectedMinute;
	private TextView tv_tip_time;
	private Button bt_choose;
	private RadioButton rb_on;
	private RadioButton rb_off;
	private View view;
	private void initWheelView(View view) {
		final WheelView wva_hour = (WheelView) view. findViewById(R.id.main_wv);
		final WheelView wva_minute = (WheelView) view. findViewById(R.id.main_wv2);
		tv_tip_time = (TextView) view.findViewById(R.id.tv_tip_time);
		//初始化时默认选择0时0分
		//tipDistanceTime = handSelectedTime(tipDistanceTime[0],tipDistanceTime[1]);
		//updateTipData();
		wva_hour.setCustomWidth(ScreenUtils.getScreenWidth(getActivity())/2);
		wva_hour.setOffset(2);
		wva_hour.setItems(getSetTime(24));
		wva_hour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				LogUtils.i( "llpp======selectedIndex: " + selectedIndex + ", item: " + 
						item+"displayItemCount:"+wva_hour.displayItemCount);
				selectedHour=Integer.parseInt(item);
				//tipDistanceTime = handSelectedTime(Integer.parseInt(item),selectedMinute);
				//updateTipData();		
			}

		});

		wva_minute.setCustomWidth(ScreenUtils.getScreenWidth(getActivity())/2);
		wva_minute.setOffset(2);
		wva_minute.setItems(getSetTime(60));
		wva_minute.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				LogUtils.i( "llpp======selectedIndex: " + selectedIndex + ", item: " + 
						item+"displayItemCount:"+wva_minute.displayItemCount);
				selectedMinute=Integer.parseInt(item);
				//	tipDistanceTime = handSelectedTime(selectedHour,Integer.parseInt(item));
				//	updateTipData();
			}
		});
	}


	private void updateTipData( int[] tipDistanceTime) {
		if(newTimer.state==1){//如果选则的是 lights on
			tv_tip_time.setText("lights on "+tipDistanceTime[0]+" hours and "+
					tipDistanceTime[1]+" minutes later");
		}else{//如果选则的是 lights off
			tv_tip_time.setText("lights off "+tipDistanceTime[0]+" hours and "+
					tipDistanceTime[1]+" minutes later");
		}
		newTimer.time= String.format("%02d",selectedHour)+":"+ String.format("%02d",selectedMinute);
	}
	/**
	 * 返回最近的时间差
	 * @param hour
	 * @param minute
	 * @return
	 */
	private int[] handSelectedTime(int hour,int minute) {
		//获取最近一天是星期几
		int closesDay = getClosesDay(hour,minute);
		if(closesDay==-1){//没有选择
			LogUtils.i("用户没有选择==========");
			return new int[2];
		}
		//获取系统是星期几
		int currentDay = TimeUtils.getDayOfWeek();
		//计算相隔多少天
		int timeInterval =closesDay-currentDay;
		if(timeInterval<0){
			timeInterval=7+timeInterval;
		}else if(timeInterval==0){
			if(hour*60+minute>getSystemTime()){
				timeInterval=0;
				
			}else{
				timeInterval=7;
			}
		}
		LogUtils.i("llpp:用户选择中,距离现在最近的是星期："+closesDay+"相隔:"+timeInterval+"天");
		int systemTime = getSystemTime();
		int totalSeletedTime = hour*60+minute+timeInterval*24*60;
		int timeDis;
		if(totalSeletedTime>systemTime){//选择的时间大于系统时间
			timeDis=totalSeletedTime-systemTime;
		}else{//选择的时间小于系统时间 ，下一周的时间
			//今天的剩余系统时间加上相隔时间
			timeDis=24*60-systemTime+totalSeletedTime;
		}
		//时间差
		int[] hour_minute=new int[2];
		hour_minute[0]=timeDis/60;
		hour_minute[1]=timeDis%60;
		return hour_minute;
	}

	/**
	 * 从用户选择的所有时间中获取和现在相隔最近的一个时间:星期几
	 * @param dayOfWeek
	 */
	private int getClosesDay(int selectedHour,int selectedMinute) {
		//是否小于系统时间
		boolean isLessSystemTime=false;
		boolean isToday=false;
		//从今天开始找，今天有分两种情况，即选择大于当前时间和小于当前时间
		int selectedTotal=selectedHour*60+selectedMinute;
		if(selectedTotal<getSystemTime()){
			isLessSystemTime=true;
		}	
		//获取系统是星期几
		int dayOfWeek = TimeUtils.getDayOfWeek();
		LogUtils.i("系统现在是星期："+dayOfWeek);
		List<Integer> week = newTimer.week;
		//遍历找到距离现在最近的一天
		for (int i = dayOfWeek; i < dayOfWeek+7; i++) {
			if(i<=7){
				Integer integer = week.get(i-1);
				if(integer!=0){//找到了
					if(isLessSystemTime){//找到了并且小于系统时间
						if(i==dayOfWeek){//如果最近时间是今天
							isToday=true;
							continue;//继续，如果后面已经没有才返回这一天
						}
					}
					return i;
				}
			}else{//i>7
				int position=i%7-1;
				Integer integer = week.get(position);
				if(integer!=0){
					return i%7;
				}
			}
		}
		return isToday? dayOfWeek:-1;
	}

	/**
	 * 
	 * @return hour*60+minute
	 */
	private int getSystemTime(){
		Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。  		
		t.setToNow(); // 取得系统时间。  
		int time=t.hour*60+t.minute; // 0-23  
		return time;
	}

	private ArrayList<String> getSetTime(int maxTime) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < maxTime; i++) {
			arrayList.add(i<10? "0"+i:i+"");
		}
		return arrayList;
	}
}
