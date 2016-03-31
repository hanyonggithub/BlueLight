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


	private static final int BRIGHTNESS_MAX=99;
	private static final int BRIGHTNESS_MIN=0;
	private int during=0;
	private int dotColor;
	private TimerHalfRingView cus_view_halring;
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
