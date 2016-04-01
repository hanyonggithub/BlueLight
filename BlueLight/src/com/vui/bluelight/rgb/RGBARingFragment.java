package com.vui.bluelight.rgb;

import java.util.ArrayList;

import com.vui.bluelight.R;
import com.vui.bluelight.ble.BleUtils;
import com.vui.bluelight.customview.ColorPickerView;
import com.vui.bluelight.customview.RotateView;
import com.vui.bluelight.customview.TimerHalfRingView;
import com.vui.bluelight.customview.WheelView;
import com.vui.bluelight.customview.ColorPickerView.OnGetColorListener;
import com.vui.bluelight.customview.RotateView.OnColorChangeListener;
import com.vui.bluelight.utils.DataFormatUtils;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.ScreenUtils;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class RGBARingFragment extends Fragment implements OnColorChangeListener{
	private View view;
	private WheelView wva2;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){
			view = inflater.inflate(R.layout.aty_rgb, null);
			initWheelView(view);
		}
		return view;
	}

	private static final int BRIGHTNESS_MAX=100;
	private static final int BRIGHTNESS_MIN=0;
	int during=0;
	private void initWheelView(View view ) {
		wva2 = (WheelView) view.findViewById(R.id.main_wv2);
		final ImageView iv_roll_cir = (ImageView) view.findViewById(R.id.iv_roll_cir);
		cus_view_halring = (TimerHalfRingView)view.findViewById(R.id.cus_view_halring);
		dotColor=cus_view_halring.getDotColor();
		final RotateView timer_ring = (RotateView) view.findViewById(R.id.timer_ring);
		final ImageView timer_ring_color = (ImageView) view.findViewById(R.id.timer_ring_color);
		timer_ring.setViews(iv_roll_cir,timer_ring_color);	
		timer_ring.setOnColorChangeListener(this);
		wva2.setCustomWidth(ScreenUtils.getScreenWidth(getActivity())/3);
		wva2.setIsDrawLine(false);
		wva2.setOffset(1);
		wva2.setItems(getSetTime(0,BRIGHTNESS_MAX-BRIGHTNESS_MIN));
		cus_view_halring.setDotBrightness(BRIGHTNESS_MAX);
		//亮度
		wva2.setCurrentPosition(cus_view_halring.getDotBrightness());
		wva2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				super.onSelected(selectedIndex, item);
				int brightness = Integer.parseInt(item)+BRIGHTNESS_MIN;
				cus_view_halring.setDotBrightness(brightness);
				onColorChange(dotColor,0,0);
				LogUtils.i("llpp:===============设置点点的亮度为："+brightness);

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

	int dotColor;
	private TimerHalfRingView cus_view_halring;
	@Override
	public void onColorChange(int color,float angle,int mode) {
		dotColor=color;
		int dotBrightness = cus_view_halring.getDotBrightness();
		int red = Color.red(color)*dotBrightness/BRIGHTNESS_MAX;
		int green = Color.green(color)*dotBrightness/BRIGHTNESS_MAX;
		int blue = Color.blue(color)*dotBrightness/BRIGHTNESS_MAX;
		int argb = Color.rgb(red, green, blue);
		cus_view_halring.setDotColor(argb);
	}
}