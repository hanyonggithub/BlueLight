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
			int dotColor = cus_view_halring.getDotColor();
			Fragment timerRGBFrg=getFragmentManager().findFragmentByTag("timerRGBFrg");
			
			if (timerRGBFrg!=null&&timerRGBFrg instanceof TimerRGBFragment) {
				
				((TimerRGBFragment)timerRGBFrg).setChooseViewColor(dotColor);
			}
		}
		getFragmentManager().popBackStack();
	}

	private void initWheelView(View view) {
		wva2 = (WheelView) view.findViewById(R.id.main_wv2);
		final ImageView iv_roll_cir = (ImageView) view.findViewById(R.id.iv_roll_cir);
		cus_view_halring = (TimerHalfRingView) view.findViewById(R.id.cus_view_halring);
		final RotateView timer_ring = (RotateView) view.findViewById(R.id.timer_ring);
		final ImageView timer_ring_color = (ImageView) view.findViewById(R.id.timer_ring_color);
		timer_ring.setViews(cus_view_halring, iv_roll_cir, timer_ring_color);
		timer_ring.setOnColorChangeListener(this);
		wva2.setCustomWidth(ScreenUtils.getScreenWidth(getActivity()) / 8);
		wva2.setIsDrawLine(false);
		wva2.setOffset(1);
		wva2.setItems(getSetTime(0, 100));
		wva2.setCurrentPosition(50);
		wva2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			int lastPosition = 0;

			@Override
			public void onSelected(int selectedIndex, String item) {

				int item_i = Integer.parseInt(item);
				if (item_i > lastPosition) {// 向上滑动
					// timer_ring.updateUI(0.10,false);
				} else if (item_i < lastPosition) { // 向下
					// timer_ring.updateUI(0.05,true);
				}

				try {
					int index = Integer.parseInt(item);
					float radius = 0;
					if (index >= 0 && index <= 50) {
						radius = (index * 180 / 100) + 270;
					} else if (index > 50 && index <= 100) {
						radius = (index - 50) * 180 / 100;
					}
					LogUtils.e("index" + Integer.parseInt(item) + ",rotation=" + radius);
					timer_ring.setRotation(radius);
				} catch (Exception e) {
					e.printStackTrace();
				}
				lastPosition = item_i;
			}

		});

		final WheelView main_wv = (WheelView) view.findViewById(R.id.main_wv);
		main_wv.setCustomWidth(ScreenUtils.getScreenWidth(getActivity()) / 8);
		main_wv.setIsDrawLine(false);
		main_wv.setOffset(1);
		main_wv.setItems(getSetTime(0, 100));
		main_wv.setCurrentPosition(50);
		main_wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			int lastPosition = 0;

			@Override
			public void onSelected(int selectedIndex, String item) {
				LogUtils.i("llpp======selectedIndex: " + selectedIndex + ", item: " + item + "   displayItemCount:"
						+ main_wv.displayItemCount);
				int item_i = Integer.parseInt(item);
				if (item_i > lastPosition) {// 向上滑动
					timer_ring.updateUI(0.05, false);
				} else if (item_i < lastPosition) { // 向下
					timer_ring.updateUI(0.025, true);
				}
				lastPosition = item_i;
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
	public void onColorChange(int color, double rotation) {
		int index = 50;
		if (rotation >= 0 && rotation < 90) {
			index = (int) ((rotation / 90) * 50) + 50;
		} else if (rotation <= 270) {
			index = 100 - ((int) (rotation - 90) * 50 / 180);
		} else if (rotation > 270 && rotation <= 360) {
			index = (int) ((rotation - 270) * 50 / 90);
		}
		LogUtils.e("rotation=" + rotation + ",index=" + index + ",R:" + Color.red(color) + ",G:" + Color.green(color)
				+ ",B:" + Color.blue(color));
		wva2.setCurrentPosition(index);
	}
}