package com.vui.bluelight.timer;

import com.vui.bluelight.R;
import com.vui.bluelight.utils.LogUtils;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class TimerActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_timer);
		initView();
		initFragment();
	}

	private void initFragment() {
		FragmentManager fm=getFragmentManager();
		FragmentTransaction transacction=fm.beginTransaction();
		TimerFragment timerFragment =  new TimerFragment();
		LogUtils.i("================llpp");
		transacction.replace(R.id.id_content, timerFragment).commit();
	}

	private void initView() {
		View llt_home = findViewById(R.id.llt_home);
		View llt_switcher = findViewById(R.id.llt_switcher);
		View llt_brightness = findViewById(R.id.llt_brightness);
		
		llt_home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(TimerActivity.this, "====", 0).show();
				
			}
		});
		llt_switcher.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(TimerActivity.this, "====", 0).show();
				
			}
		});
		llt_brightness.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(TimerActivity.this, "====", 0).show();
				
			}
		});
	}

}
