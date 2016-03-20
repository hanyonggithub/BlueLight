package com.vui.bluelight.rgb;

import java.util.ArrayList;

import com.vui.bluelight.R;
import com.vui.bluelight.customview.WheelView;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.ScreenUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RGBFragment extends Fragment{
  @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	
	return inflater.inflate(R.layout.aty_rgb, null);
}
  
  @Override
public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
	initWheelView(view);
	
}
  

	private void initWheelView(View view ) {
		final WheelView wva2 = (WheelView) view.findViewById(R.id.main_wv2);
		final ImageView iv_roll_cir = (ImageView) view.findViewById(R.id.iv_roll_cir);
		final View cus_view_halring = (View)view.findViewById(R.id.cus_view_halring);
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
