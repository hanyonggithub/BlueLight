package com.vui.bluelight;

import com.vui.bluelight.base.view.TopBarView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

public class SettingActivity extends Activity{
	TopBarView tbv;
	ListView lvw_setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_setting);
		tbv=(TopBarView) findViewById(R.id.topbar);
		lvw_setting=(ListView) findViewById(R.id.lvw_setting);
	}
	
	
}
