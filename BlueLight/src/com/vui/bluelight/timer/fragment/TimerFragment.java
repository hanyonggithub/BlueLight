package com.vui.bluelight.timer.fragment;
import java.util.List;
import com.vui.bluelight.R;
import com.vui.bluelight.customview.SwipeAdapter;
import com.vui.bluelight.customview.SwipeListView;
import com.vui.bluelight.timer.TimerActivity;
import com.vui.bluelight.timer.entity.TimerEntity;
import com.vui.bluelight.timer.entity.TimerEntity.Data;
import com.vui.bluelight.utils.LogUtils;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

public class TimerFragment extends Fragment{

	private SwipeListView mListView;
	private Activity activity;
	private SwipeAdapter swipeAdapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		View view = inflater.inflate(R.layout.frag_timer, null);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initTitleBar(view);
		initView(view);
	}

	private void addTestData(){
		try {
			SharedPreferences sp = activity.getSharedPreferences(SwipeAdapter.sp_timer_light_list,
					Context.MODE_PRIVATE);
			String data = sp.getString(SwipeAdapter.key_timer_light_list, null);
			if(data==null){
				/*InputStream open = activity.getAssets().open("data_json/timer_data.json");
				byte[] buffer=new byte[open.available()];
				open.read(buffer);
			    data = new String(buffer);
				
				sp.edit().putString(SwipeAdapter.key_timer_light_list,data).commit();
				open.close();*/
			}else{
				LogUtils.i("llpp=============timer json data has exist:");	
			}
			
		} catch (Exception e) {
			LogUtils.i("llpp=============read timer gson data Exception :"+e.toString());			
			e.printStackTrace();
		}
	}
	
	private void initTitleBar(View view) {
		View back = view.findViewById(R.id.back);
		View right_btn = view.findViewById(R.id.right_btn);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		right_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "开发中。。。", 0).show();
			}
		});
	}
	private void initView(View view) {
		mListView = (SwipeListView)view.findViewById(R.id.listview);
		mListView.setRightViewWidth(activity.getResources().getDimensionPixelSize(R.dimen.timer_swipelistview_right_width));
		View headView = View.inflate(activity, R.layout.timer_addnewtimer, null);//tv_add
		View tv_add = headView.findViewById(R.id.tv_add);
		tv_add.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				 TimerRGBFragment timerRGBFragment = new TimerRGBFragment();
				TimerActivity.switchFragment(TimerFragment.this,timerRGBFragment,(TimerActivity) getActivity());
			}
		});
		mListView.addHeaderView(headView);
		
		swipeAdapter = new SwipeAdapter(activity, mListView.getRightViewWidth(),
				new SwipeAdapter.IOnItemRightClickListener() {
			@Override
			public void onRightClick(View v, int position,View parentView) {
				deleteTimer(position,parentView);
			}
		});
		mListView.setAdapter(swipeAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*Toast.makeText(activity, "item onclick " + position, Toast.LENGTH_SHORT)
				.show();*/
			}
		});
	}
	private void deleteTimer(int position,final View view) {
		try {
			TimerEntity timerEntity = swipeAdapter.timerEntity;
			if(timerEntity!=null){
				List<Data> items = timerEntity.items;
				if(items!=null){
					items.remove(position);
					SwipeAdapter.saveToLocal(swipeAdapter.timerEntity,getActivity());
					swipeAdapter.notifyDataSetChanged();
				}else{
					LogUtils.e("llpp==============items==null");
				}
			}else{
				LogUtils.e("llpp==============timerEntity==null");
			}
		} catch (Exception e) {
		LogUtils.e("llpp:===============delete timer data failed:"+e.toString());
			e.printStackTrace();
		}
	}

	
}

