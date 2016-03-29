package com.vui.bluelight.rgb;

import java.util.ArrayList;

import com.vui.bluelight.R;
import com.vui.bluelight.customview.RotateView;
import com.vui.bluelight.customview.RotateView.OnColorChangeListener;
import com.vui.bluelight.customview.TimerHalfRingView;
import com.vui.bluelight.customview.WheelView;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.ScreenUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RGBADimmingFragment extends Fragment implements OnColorChangeListener{
	private View view;
	private WheelView wva2;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){
			view = inflater.inflate(R.layout.aty_rgb_dimming, null);
			initWheelView(view);
		}
		return view;
	}


	private void initWheelView(View view ) {
		wva2 = (WheelView) view.findViewById(R.id.main_wv2);
		final ImageView iv_roll_cir = (ImageView) view.findViewById(R.id.iv_roll_cir);
		final TimerHalfRingView cus_view_halring = (TimerHalfRingView)view.findViewById(R.id.cus_view_halring);
		final RotateView timer_ring = (RotateView) view.findViewById(R.id.timer_ring);
		final ImageView timer_ring_color = (ImageView) view.findViewById(R.id.timer_ring_color);
		timer_ring.setViews(iv_roll_cir,timer_ring_color);	
		timer_ring.setOnColorChangeListener(this);
		wva2.setCustomWidth(ScreenUtils.getScreenWidth(getActivity())/8);
		wva2.setIsDrawLine(false);
		wva2.setOffset(1);
		wva2.setItems(getSetTime(0,100));
		wva2.setCurrentPosition(50);
		wva2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			int lastPosition=0;
			@Override
			public void onSelected(int selectedIndex, String item) {
				LogUtils.i( "llpp======selectedIndex: " + selectedIndex + ", item: " + 
						item+"   displayItemCount:"+wva2.displayItemCount);	
				int item_i = Integer.parseInt(item);
				if(item_i>lastPosition){//向上滑动
//					timer_ring.updateUI(0.10,false);
				}else if(item_i<lastPosition){	//向下
//					timer_ring.updateUI(0.05,true);
				}
				try {
					int index = Integer.parseInt(item);
					float radius = 0;
					if (index >= 0 && index <= 50) {
						radius=(index*180/100)+270;
					}else if(index>50&&index<=100){
						radius=(index-50)*180/100;
					}
					LogUtils.e("index" + Integer.parseInt(item) + ",rotation=" + radius);
					timer_ring.setRotation(radius);
				} catch (Exception e) {
					e.printStackTrace();
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
	
	@Override
	public void onColorChange(int color,float angle) {
	/*	int index = 50;
		if (rotation >= 0 && rotation < 90) {
			index = (int) ((rotation / 90) * 50) + 50;
		} else if (rotation <= 270) {
			index = 100 - ((int) (rotation - 90) * 50 / 180);
		} else if (rotation > 270 && rotation <= 360) {
			index = (int) ((rotation - 270) * 50 / 90);
		}
		LogUtils.e("rotation=" + rotation + ",index=" + index + ",R:" + Color.red(color) + ",G:" + Color.green(color)
				+ ",B:" + Color.blue(color));
		wva2.setCurrentPosition(index);*/
	}
}
