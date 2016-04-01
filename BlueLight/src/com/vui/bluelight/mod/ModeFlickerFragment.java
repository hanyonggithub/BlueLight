package com.vui.bluelight.mod;

import java.util.ArrayList;
import java.util.Arrays;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.customview.WheelView;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.ScreenUtils;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ModeFlickerFragment extends Fragment implements OnTouchListener, OnClickListener {
	private WheelView wv_mode;
	private WheelView wv_time;
	private String[] modes = { "Green flicker", "Yellow flicker", "Red flicker", "Green flicker", "Yellow flicker",
			"Red flicker", "Green flicker", "Yellow flicker", "Red flicker", "Green flicker", "Yellow flicker",
			"Red flicker", "Green flicker", "Yellow flicker", "Red flicker" };
	private TopBarView tbv;
	private int mode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.frag_mode_flicker, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		view.setOnTouchListener(this);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tbv = (TopBarView) view.findViewById(R.id.topbar);
		if (mode == 1) {
			tbv.setVisibility(View.VISIBLE);
			tbv.setTitleText("livingroom");
			tbv.getLeftBtn().setOnClickListener(this);
			tbv.getRightBtn().setOnClickListener(this);
		} else if (mode == 0) {
			tbv.setVisibility(View.GONE);
		}

		wv_mode = (WheelView) view.findViewById(R.id.wv_mode);
		wv_time = (WheelView) view.findViewById(R.id.wv_time);

		wv_mode.setCustomWidth(ScreenUtils.getScreenWidth(getActivity()));
		wv_mode.setIsDrawLine(false);
		wv_mode.setOffset(5);
		wv_mode.setItems(Arrays.asList(modes));
		wv_mode.setCurrentPosition(5);
		wv_mode.setTextColorSelected("#00ff00");
		wv_mode.setTextColorNormal("#ffffff");
		wv_mode.setTextSizeNormal(18);
		wv_mode.setTextSizeSelected(18);
		wv_mode.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			int lastPosition = 0;

			@Override
			public void onSelected(int selectedIndex, String item) {
				int item_i = selectedIndex;
				if (item_i > lastPosition) {// 向上滑动
				} else if (item_i < lastPosition) { // 向下
				}
				lastPosition = item_i;
				// LogUtils.e("选中了"+modes[selectedIndex]+",position="+selectedIndex);
			}

		});

		wv_time.setCustomWidth(ScreenUtils.getScreenWidth(getActivity()) / 8);
		wv_time.setIsDrawLine(false);
		wv_time.setOffset(1);
		wv_time.setItems(getSetTime(0, 100));
		wv_time.setCurrentPosition(50);
		wv_time.setTextSizeSelected(20);
		wv_time.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			int lastPosition = 0;

			@Override
			public void onSelected(int selectedIndex, String item) {
				int item_i = selectedIndex;
				if (item_i > lastPosition) {// 向上滑动

				} else if (item_i < lastPosition) { // 向下
				}
				lastPosition = item_i;
			}

		});
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	private ArrayList<String> getSetTime(int minValue, int maxValue) {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = minValue; i < maxValue + 1; i++) {
			arrayList.add(i + "");
		}
		return arrayList;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getFragmentManager().popBackStack();
			break;
		case R.id.right_btn:
			getFragmentManager().popBackStack();
			break;

		default:
			break;
		}
	}
}
