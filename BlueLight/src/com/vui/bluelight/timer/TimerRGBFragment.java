package com.vui.bluelight.timer;

import java.util.ArrayList;
import java.util.Arrays;

import com.vui.bluelight.R;
import com.vui.bluelight.customview.WheelView;
import com.vui.bluelight.utils.LogUtils;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TimerRGBFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = View.inflate(getActivity(),R.layout.frag_timerrgb, null);
		//WheelView wva = (WheelView) inflate. findViewById(R.id.main_wv);
		LinearLayout llt_root = (LinearLayout) inflate.findViewById(R.id.llt_root);
		final WheelView wva =new WheelView(getActivity());
		llt_root.addView(wva);
		wva.setOffset(2);
		wva.setItems(getData());
		wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				LogUtils.i( "llpp======selectedIndex: " + selectedIndex + ", item: " + item+"displayItemCount:"+wva.displayItemCount);
			}
		});
		return inflate;
	}

	private ArrayList<String> getData() {
		ArrayList<String> arrayList = new ArrayList<String>();
		for (int i = 0; i < 24; i++) {
			arrayList.add(i+"");
		}
		return arrayList;
	}
}
