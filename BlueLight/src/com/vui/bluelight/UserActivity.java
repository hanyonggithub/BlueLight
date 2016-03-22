package com.vui.bluelight;

import com.vui.bluelight.base.view.TopBarView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class UserActivity extends Activity{
	TopBarView tbv;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.frag_user);
	tbv=(TopBarView) findViewById(R.id.topbar);
	tbv.setTitleText("User");
}
	
	
}
