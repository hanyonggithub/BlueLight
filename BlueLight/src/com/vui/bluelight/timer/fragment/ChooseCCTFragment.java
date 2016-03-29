package com.vui.bluelight.timer.fragment;

import java.util.ArrayList;

import com.vui.bluelight.R;
import com.vui.bluelight.customview.RotateView;
import com.vui.bluelight.customview.RotateView.OnColorChangeListener;
import com.vui.bluelight.customview.TimerHalfRingView;
import com.vui.bluelight.customview.WheelView;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.ScreenUtils;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ChooseCCTFragment extends Fragment implements OnColorChangeListener {
	private View view;
	private TimerHalfRingView cus_view_halring;
	private WheelView wva2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
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
		if (isOk) {
			Fragment timerRGBFrg=getFragmentManager().findFragmentByTag("timerRGBFrg");
			
			if (timerRGBFrg!=null&&timerRGBFrg instanceof TimerRGBFragment) {
				
				((TimerRGBFragment)timerRGBFrg).saveChoosedColorAndTime(dotColor,during);
			}
		}
		getFragmentManager().popBackStack();
	}
	private static final int BRIGHTNESS_MAX=99;
	private static final int BRIGHTNESS_MIN=0;
	int during=0;
	private int dotColor;
	private void initWheelView(View view) {
		wva2 = (WheelView) view.findViewById(R.id.main_wv2);
		final ImageView iv_roll_cir = (ImageView) view.findViewById(R.id.iv_roll_cir);
		cus_view_halring = (TimerHalfRingView) view.findViewById(R.id.cus_view_halring);
		dotColor=cus_view_halring.getDotColor();
		final RotateView timer_ring = (RotateView) view.findViewById(R.id.timer_ring);
		final ImageView timer_ring_color = (ImageView) view.findViewById(R.id.timer_ring_color);
		timer_ring.setViews(iv_roll_cir, timer_ring_color);
		wva2.setCustomWidth(ScreenUtils.getScreenWidth(getActivity()) / 8);
		wva2.setIsDrawLine(false);
		wva2.setOffset(1);
		wva2.setItems(getSetTime(BRIGHTNESS_MIN, BRIGHTNESS_MAX));
		wva2.setCurrentPosition((BRIGHTNESS_MAX-BRIGHTNESS_MIN)/2+1);
		wva2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
			}
		});

		timer_ring.setOnColorChangeListener(this);

		final WheelView main_wv = (WheelView) view.findViewById(R.id.main_wv);
		main_wv.setCustomWidth(ScreenUtils.getScreenWidth(getActivity()) / 8);
		main_wv.setIsDrawLine(false);
		main_wv.setOffset(1);
		main_wv.setItems(getSetTime(0, 30));
		main_wv.setCurrentPosition(0);
		main_wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				during = Integer.parseInt(item);
			}
		});

	}

	private ArrayList<String> getSetTime(int minValue, int maxValue) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = minValue; i < maxValue + 1; i++) {
			arrayList.add(i + "");
		}
		return arrayList;
	}

	@Override
	public void onColorChange(int color,float angle) {
		dotColor=color;
		cus_view_halring.setDotColor(dotColor);
		//-360-360
		if(angle<0){
			angle+=360;
		}
		int wheelNumber_new = getWheelNumber(angle);	
			wva2.setCurrentPosition(wheelNumber_new);	
	}
	
	private int getWheelNumber(float angle) {
		//[-90,90]度对应[0,99]
		//(90,270)度对应[98,1]
		double number=0;
		//100个数度平均分成180分，每份是 
		
		if(angle>=270||angle<=90){		
			float average=100f/(180f);
			if(angle>=270){
				angle=angle-360;//转成负值
			}		
			number = Math.ceil(average*angle);//
			//-50,49
				number=number-1;
			number+=50;//0,99
		}else{//90-270
			float average=98f/(180f);
			number = average*angle;//49.-146.===>98，1
			number=146-number+1;//97+1,0+1  	
		}
		LogUtils.i("number===："+number);
		LogUtils.i("llpp:--旋转的角度为："+angle);
		return (int) number;
	}
}