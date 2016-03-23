package com.vui.bluelight.main;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.view.ViewGroup;

public class UserFragment extends Fragment implements OnClickListener{
	private TopBarView tbv;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_user, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tbv=(TopBarView) view.findViewById(R.id.topbar);
		tbv.getLeftBtn().setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		getFragmentManager().popBackStack();
	}
}
