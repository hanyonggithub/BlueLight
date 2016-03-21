package com.vui.bluelight.timer.fragment;

import java.util.ArrayList;

import com.vui.bluelight.R;
import com.vui.bluelight.customview.SwipeAdapter;
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

	private ImageView iv_roll_cir;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_choose_cct, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initTitleBar(view);
		initColorSelector(view);
		initWheelView(view);
	}

private void initColorSelector(View view) {
	iv_roll_cir = (ImageView) view.findViewById(R.id.iv_roll_cir);
		
	}

private void initTitleBar(View view) {
	View back = view.findViewById(R.id.back);
	View right_btn = view.findViewById(R.id.right_btn);
	back.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			backLastFragment();
		}

	});
		right_btn.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			backLastFragment();
		}
	});
}

private void backLastFragment() {
	TimerActivity activity = (TimerActivity) getActivity();
	Fragment lastFragment = activity.getLastFragment();
	TimerActivity.switchFragment(ChooseCCTFragment.this, lastFragment, (TimerActivity) getActivity());
}
private void initWheelView(View view) {
	final WheelView wva = (WheelView) view. findViewById(R.id.main_wv);
	final WheelView wva2 = (WheelView) view. findViewById(R.id.main_wv2);
	final View cus_view_halring = (View) view. findViewById(R.id.cus_view_halring);
	
	wva.setCustomWidth(ScreenUtils.getScreenWidth(getActivity())/8);
	wva.setIsDrawLine(false);
	wva.setOffset(1);
	wva.setItems(getSetTime(101));
	wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
		@Override
		public void onSelected(int selectedIndex, String item) {
			LogUtils.i( "llpp======selectedIndex: " + selectedIndex + ", item: " + 
					item+"displayItemCount:"+wva.displayItemCount);	
			iv_roll_cir.setRotation(Integer.parseInt(item)*360/100);
			cus_view_halring.setRotation(Integer.parseInt(item)*360/100);
			wva2.setCurrentPosition(selectedIndex-1);
		}

	});
	
	
	wva2.setCustomWidth(ScreenUtils.getScreenWidth(getActivity())/8);
	wva2.setIsDrawLine(false);
	wva2.setOffset(1);
	wva2.setItems(getSetTime(101));
	wva2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
		@Override
		public void onSelected(int selectedIndex, String item) {
			LogUtils.i( "llpp======selectedIndex: " + selectedIndex + ", item: " + 
					item+"displayItemCount:"+wva2.displayItemCount);	
			iv_roll_cir.setRotation(Integer.parseInt(item)*360/100);
			cus_view_halring.setRotation(Integer.parseInt(item)*360/100);
			wva.setCurrentPosition(selectedIndex-1);
		}

	});
	
}

private ArrayList<String> getSetTime(int maxTime) {
	ArrayList<String> arrayList = new ArrayList<String>();
	for (int i = 0; i < maxTime; i++) {
		arrayList.add(i<10? "0"+i:i+"");
	}
	return arrayList;
}
}