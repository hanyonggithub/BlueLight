package com.vui.bluelight.main;

import com.vui.bluelight.MainActivity;
import com.vui.bluelight.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity{
	
	Handler handler=new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_splash);
		handler.postDelayed(new Runnable(){
			@Override
			public void run() {
				Intent intent=new Intent(SplashActivity.this,MainActivity.class);
				SplashActivity.this.startActivity(intent);
				SplashActivity.this.finish();
			}
		},1500);
	}
}
