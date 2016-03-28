package com.vui.bluelight.rgb;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RGBMainFragment extends Fragment implements OnClickListener{
	
	private TopBarView tbv;
	private RadioGroup rg_type_select_content;
	
	private int selectedColor= Color.parseColor("#ffffff");
	private RGBARingFragment rgbaRingFragment;
	private RGBPieFragment rgbPieFragment;
	private RGBADimmingFragment rgbaDimmingFragment;
	private FragmentManager fm;


	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_rgb_main, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tbv = (TopBarView) view.findViewById(R.id.topbar);
		tbv.setTitleText("livingroom");
		tbv.getLeftBtn().setOnClickListener(this);
		tbv.getRightBtn().setOnClickListener(this);
		
		rg_type_select_content = (RadioGroup) view.findViewById(R.id.rg_type_select_content);
		
		initFragment();
		initChooseView();
	}
	

	private void initChooseView() {
		
		rg_type_select_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_rgb:
					switchFragment(rgbaRingFragment);
					break;
				case R.id.rb_ring:
					switchFragment(rgbPieFragment);
					break;
				case R.id.rb_dim:
					switchFragment(rgbaDimmingFragment);
					break;
				default:
					break;
				}

			}
		});
	}

	private void initFragment() {
		rgbaRingFragment = new  RGBARingFragment();
		rgbPieFragment = new RGBPieFragment();
		rgbaDimmingFragment = new RGBADimmingFragment();
		switchFragment(rgbaRingFragment);
	}

	private   void switchFragment(Fragment targetFragment){
		if(fm==null)
		{
			fm=this.getChildFragmentManager();
		}
		FragmentTransaction transacction = fm.beginTransaction();
		transacction.replace(R.id.id_content, targetFragment).commit();
	}

	public void setSelectedColor(int color){
		selectedColor=color;
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
