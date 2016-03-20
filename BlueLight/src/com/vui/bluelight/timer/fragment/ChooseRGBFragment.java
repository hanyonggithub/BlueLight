package com.vui.bluelight.timer.fragment;

import com.vui.bluelight.R;
import com.vui.bluelight.customview.SwipeAdapter;
import com.vui.bluelight.timer.TimerActivity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ChooseRGBFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_choose_rgb, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initTitleBar(view);
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
	TimerActivity.switchFragment(ChooseRGBFragment.this, lastFragment, (TimerActivity) getActivity());
}

}