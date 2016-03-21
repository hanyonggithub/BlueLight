package com.vui.bluelight.music;



import com.vui.bluelight.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ServiceTestActivity extends Activity{
TextView tvw_play;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_service_test);
		tvw_play=(TextView) findViewById(R.id.tvw_paly);
		tvw_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		
	}
	
}
