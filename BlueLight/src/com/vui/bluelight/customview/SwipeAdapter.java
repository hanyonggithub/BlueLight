
package com.vui.bluelight.customview;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vui.bluelight.R;
import com.vui.bluelight.timer.entity.TimerEntity;
import com.vui.bluelight.utils.LogUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SwipeAdapter extends BaseAdapter {
	private Context mContext = null;
	private int mRightWidth = 0;
	private IOnItemRightClickListener mListener = null;

	public interface IOnItemRightClickListener {
		void onRightClick(View v, int position,View parentView);
	}
	public SwipeAdapter(Context ctx, int rightWidth, IOnItemRightClickListener l) {
		mContext = ctx;
		mRightWidth = rightWidth;
		mListener = l;
		TimerEntity timerEntity= getTimerEntity(mContext);
		if(timerEntity!=null){
			this.timerEntity=timerEntity;
		}else{
			LogUtils.e("llpp:================:timerEntity==null");	
		}
	}

	public static final String sp_timer_light_list="timer_light_list";
	public static final String key_timer_light_list="key_timer_light_list";
	public TimerEntity timerEntity;
	/**
	 * 获取数据
	 * @param mContext
	 * @return
	 */
	public static TimerEntity getTimerEntity(Context mContext) {
		try {
			SharedPreferences sp = mContext.getSharedPreferences(sp_timer_light_list, Context.MODE_PRIVATE);
			String timer_light = sp.getString(key_timer_light_list, null);
			TimerEntity timerEntity=null;
			if(timer_light!=null){
				Gson gson = new Gson();
				timerEntity= gson.fromJson(timer_light, TimerEntity.class);				
			}else{
				LogUtils.e("llpp:================:timer_light==null");
			}
			return timerEntity;
		} catch (JsonSyntaxException e) {
			LogUtils.e("llpp:==initData=====================Exception:"+e.toString());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getCount() {
		if(timerEntity!=null){
			return null==timerEntity.items? 0:timerEntity.items.size() ;
		}else{
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder item;
		final int thisPosition = position;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.timer_lightson, parent, false);
			item = new ViewHolder();
			item.rootView=(View)convertView.findViewById(R.id.root);;
			item.item_left_ = (View)convertView.findViewById(R.id.item_left_);
			item.item_right = (View)convertView.findViewById(R.id.item_right);
			item.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			item.llt_week = (LinearLayout) convertView.findViewById(R.id.llt_week);
			item.tv_lights_type = (TextView) convertView.findViewById(R.id.tv_lightson);
			item.tv_switcher = (TextView) convertView.findViewById(R.id.tv_switcher);
			convertView.setTag(item);
		} else {
			item = (ViewHolder)convertView.getTag();
		}
		LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		item.item_left_.setLayoutParams(lp1);
		LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
		item.item_right.setLayoutParams(lp2);
		item.tv_time.setText(timerEntity.items.get(position).time);
		item.tv_switcher.setText(timerEntity.items.get(thisPosition).state==0? "OFF":"ON");
		LogUtils.i("llpp:==================item.tv_lights_type:"+item.tv_lights_type);
		
		item.tv_lights_type.setText(timerEntity.items.get(thisPosition).type==0? "lights off":"lights on");
		LinearLayout llt_week = item.llt_week;
		initWeekDot(llt_week,position);
		item.tv_switcher.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				int state = timerEntity.items.get(thisPosition).state;
				//change state
				if(state==0){
					timerEntity.items.get(thisPosition).state=1;
					item.tv_switcher.setText("ON");
				}else if(state==1){
					timerEntity.items.get(thisPosition).state=0;
					item.tv_switcher.setText("OFF");
				}
				saveToLocal(timerEntity,mContext);
			}
		});
		item.item_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) {
					mListener.onRightClick(v, thisPosition,item.rootView);
				}
			}
		});
		return convertView;
	}

	public static void saveToLocal(TimerEntity  timerEntity,Context context) {
		Gson gson = new Gson();
		String json = gson.toJson(timerEntity);
		SharedPreferences sp =context.getSharedPreferences(SwipeAdapter.sp_timer_light_list,
				Context.MODE_PRIVATE);
		sp.edit().putString(SwipeAdapter.key_timer_light_list,json).commit();
	}
	
	private void initWeekDot(LinearLayout llt_week,int position) {
		int childCount = llt_week.getChildCount();
		for (int i = 0; i < childCount; i++) {
			TextView childAt = (TextView) llt_week.getChildAt(i);
			Integer onOrOff = timerEntity.items.get(position).week.get(i);
			if(onOrOff==0){
				childAt.setBackgroundResource(R.drawable.bg_timer_circle_off);
			}else if(onOrOff==1){
				childAt.setBackgroundResource(R.drawable.bg_timer_circle_on);
			}
		}
	}

	private class ViewHolder {
		View rootView;
		View item_left_;
		View item_right;
		TextView tv_time ;
		LinearLayout llt_week;
		TextView tv_switcher;
		TextView tv_lights_type;
	}
}
