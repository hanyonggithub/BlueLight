package com.vui.bluelight.mod;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.mod.ModeShakingActivity.MyAdapter;
import com.vui.bluelight.mod.ShakeListenerUtils.OnShakedListener;
import com.vui.bluelight.utils.GsonUtils;
import com.vui.bluelight.utils.LogUtils;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ModeFragment extends Fragment implements OnClickListener{
	int gridViewHeight;
	private ShakeListenerUtils shakeUtils;
	private SensorManager mSensorManager;
	private ModeShakingEntity modeShakingEntity;
	private BaseAdapter adapter;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.frag_mode, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initTitleBar(view);
		initTypeChooseView(view);
		ModeShakingFragment modeShakingFrg=new ModeShakingFragment();
		getChildFragmentManager().beginTransaction().replace(R.id.flt_content, modeShakingFrg, "modeShakingFrg").commit();
	}
	
	private void initTitleBar(View root){
		TopBarView tbv=(TopBarView) root.findViewById(R.id.topbar);
		tbv.setTitleText("mode");
		tbv.getLeftBtn().setOnClickListener(this);
		tbv.getRightBtn().setOnClickListener(this);
	}
	

	private void initTypeChooseView(View root) {
		RadioGroup rg_type_select_content =  (RadioGroup) root.findViewById(R.id.rg_type_select_content);
		rg_type_select_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.rb_rgb){
					ModeShakingFragment modeShakingFrg=new ModeShakingFragment();
					getChildFragmentManager().beginTransaction().replace(R.id.flt_content, modeShakingFrg, "modeShakingFrg").commit();
				}else if(checkedId==R.id.rb_scene){
					ModeScenceFragment modeScenceFrg=new ModeScenceFragment();
					getChildFragmentManager().beginTransaction().replace(R.id.flt_content, modeScenceFrg, "modeScenceFrg").commit();
				}else if(checkedId==R.id.rb_flicker){
					ModeFlickerFragment modeFlickerFrg=new ModeFlickerFragment();
					getChildFragmentManager().beginTransaction().replace(R.id.flt_content, modeFlickerFrg, "modeFlickerFrg").commit();
				}

			}
		});

	}

	
	

	@Override
	public void onResume()
	{
		super.onResume();

		//获取传感器管理服务 
		mSensorManager = (SensorManager) getActivity()
				.getSystemService(Service.SENSOR_SERVICE);
		//加速度传感器  
		mSensorManager.registerListener(shakeUtils,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				//还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，  
				//根据不同应用，需要的反应速率不同，具体根据实际情况设定  
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensorManager.unregisterListener(shakeUtils);
		
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
