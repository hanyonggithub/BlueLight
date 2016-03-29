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
	private static final int BRIGHTNESS_MAX=30;
	private static final int BRIGHTNESS_MIN=15;
	int during=0;
	private int dotColor;
	private void initWheelView(View view) {
		wva2 = (WheelView) view.findViewById(R.id.main_wv2);
		final ImageView iv_roll_cir = (ImageView) view.findViewById(R.id.iv_roll_cir);
		cus_view_halring = (TimerHalfRingView) view.findViewById(R.id.cus_view_halring);
		final RotateView timer_ring = (RotateView) view.findViewById(R.id.timer_ring);
		final ImageView timer_ring_color = (ImageView) view.findViewById(R.id.timer_ring_color);
		timer_ring.setViews( iv_roll_cir, timer_ring_color);
		timer_ring.setOnColorChangeListener(this);
		wva2.setCustomWidth(ScreenUtils.getScreenWidth(getActivity()) / 3);
		wva2.setIsDrawLine(false);
		wva2.setOffset(1);
		wva2.setItems(getSetTime(0, 100));
		wva2.setCurrentPosition(50);
		wva2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				int brightness = Integer.parseInt(item)+BRIGHTNESS_MIN;
				cus_view_halring.setDotBrightness(brightness);
				onColorChange(dotColor,0);
				LogUtils.i("llpp:===============设置点点的亮度为："+brightness);
			}

		});

		final WheelView main_wv = (WheelView) view.findViewById(R.id.main_wv);
		main_wv.setCustomWidth(ScreenUtils.getScreenWidth(getActivity()) / 8);
		main_wv.setIsDrawLine(false);
		main_wv.setOffset(1);
		main_wv.setItems(getSetTime(0, 100));
		main_wv.setCurrentPosition(50);
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
		int dotBrightness = cus_view_halring.getDotBrightness();
		int red = Color.red(color)*dotBrightness/BRIGHTNESS_MAX;
		int green = Color.green(color)*dotBrightness/BRIGHTNESS_MAX;
		int blue = Color.blue(color)*dotBrightness/BRIGHTNESS_MAX;
		int argb = Color.rgb(red, green, blue);
		cus_view_halring.setDotColor(argb);
	}
}