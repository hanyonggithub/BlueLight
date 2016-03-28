package com.vui.bluelight.timer.fragment;

import com.vui.bluelight.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimerListFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_timer_list, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		
		
		
	}
	
}
