package com.vui.bluelight.timer.fragment;

import java.util.ArrayList;

import com.vui.bluelight.R;
import com.vui.bluelight.customview.RotateView;
import com.vui.bluelight.customview.SwipeAdapter;
import com.vui.bluelight.customview.TimerHalfRingView;
import com.vui.bluelight.customview.WheelView;
import com.vui.bluelight.timer.TimerActivity;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.ScreenUtils;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseCCTFragment extends Fragment{
	private View view;
	private TimerHalfRingView cus_view_halring;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){
			view = inflater.inflate(R.layout.frag_choose_cct, null);
			initTitleBar(view);
			initWheelView(view);
		}
		return view;
	}

	private void initTitleBar(View view) {
		View back = view.findViewById(R.id.back);
		View right_btn = view.findViewById(R.id.right_btn);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backLastFragment(false);
			}

		});
		right_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {			
				backLastFragment(true);
			}
		});
	}

	private void backLastFragment(boolean isOk) {
		TimerActivity activity = (TimerActivity) getActivity();
		Fragment lastFragment = activity.getLastFragment();
		if(isOk){
		int dotColor = cus_view_halring.getDotColor();
		if(lastFragment instanceof TimerRGBFragment){
			((TimerRGBFragment)lastFragment).setChooseViewColor(dotColor);
		}
		}
		TimerActivity.switchFragment(ChooseCCTFragment.this, lastFragment, (TimerActivity) getActivity());
	}

	private void initWheelView(View view ) {
		final WheelView wva2 = (WheelView) view.findViewById(R.id.main_wv2);
		final ImageView iv_roll_cir = (ImageView) view.findViewById(R.id.iv_roll_cir);
		cus_view_halring = (TimerHalfRingView)view.findViewById(R.id.cus_view_halring);
		final RotateView timer_ring = (RotateView) view.findViewById(R.id.timer_ring);
		final ImageView timer_ring_color = (ImageView) view.findViewById(R.id.timer_ring_color);
		timer_ring.setViews(cus_view_halring,iv_roll_cir,timer_ring_color);	
		wva2.setCustomWidth(ScreenUtils.getScreenWidth(getActivity())/8);
		wva2.setIsDrawLine(false);
		wva2.setOffset(1);
		wva2.setItems(getSetTime(-50,50));
		wva2.setCurrentPosition(50);
		wva2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			int lastPosition=0;
			@Override
			public void onSelected(int selectedIndex, String item) {
				LogUtils.i( "llpp======selectedIndex: " + selectedIndex + ", item: " + 
						item+"   displayItemCount:"+wva2.displayItemCount);	
				int item_i = Integer.parseInt(item);
				if(item_i>lastPosition){//向上滑动
					timer_ring.updateUI(0.10,false);
				}else if(item_i<lastPosition){	//向下
					timer_ring.updateUI(0.05,true);
				}
				lastPosition=item_i;
			}

		});

		final WheelView main_wv = (WheelView) view.findViewById(R.id.main_wv);
		main_wv.setCustomWidth(ScreenUtils.getScreenWidth(getActivity())/8);
		main_wv.setIsDrawLine(false);
		main_wv.setOffset(1);
		main_wv.setItems(getSetTime(-50,50));
		main_wv.setCurrentPosition(50);
		main_wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			int lastPosition=0;
			@Override
			public void onSelected(int selectedIndex, String item) {
				LogUtils.i( "llpp======selectedIndex: " + selectedIndex + ", item: " + 
						item+"   displayItemCount:"+main_wv.displayItemCount);	
				int item_i = Integer.parseInt(item);
				if(item_i>lastPosition){//向上滑动
					timer_ring.updateUI(0.05,false);
				}else if(item_i<lastPosition){	//向下
					timer_ring.updateUI(0.025,true);
				}
				lastPosition=item_i;
			}

		});



	}


	private ArrayList<String> getSetTime(int minValue,int maxValue) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = minValue; i < maxValue+1; i++) {
			arrayList.add(i+"");
		}
		return arrayList;
	}
}