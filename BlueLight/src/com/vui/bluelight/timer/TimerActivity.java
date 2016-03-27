package com.vui.bluelight.timer;

import com.vui.bluelight.R;
import com.vui.bluelight.timer.fragment.TimerFragment;
import com.vui.bluelight.utils.LogUtils;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.aty_timer);
		initFragment();
		initView();
		TextView title=(TextView) findViewById(R.id.title);
		if(title!=null){
			title.setText("livingroom");
		}

	}

	private void initFragment() {
		FragmentManager fm=getFragmentManager();
		FragmentTransaction transacction = fm.beginTransaction();
		TimerFragment timerFragment = new TimerFragment();
		transacction.replace(R.id.id_content, timerFragment).commit();
	}

	public static  void switchFragment(Fragment startFragment,Fragment targetFragment,TimerActivity timerActivity){
		FragmentManager fm=timerActivity.getFragmentManager();
		FragmentTransaction transacction = fm.beginTransaction();
		transacction.replace(R.id.id_content, targetFragment).commit();
		timerActivity.setLastFragment(startFragment);
	}

	Fragment lastFragment;
	public  Fragment getLastFragment() {
		return lastFragment;
	}
	private  void setLastFragment(Fragment fragment) {
		lastFragment=fragment;
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


	@Override    
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		if(keyCode == KeyEvent.KEYCODE_BACK){      
			return  true;
		}  
		return  super.onKeyDown(keyCode, event);     

	} 
}
