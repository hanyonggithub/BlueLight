package com.vui.bluelight;

import com.vui.bluelight.base.view.VisualizerView;
import com.vui.bluelight.ble.BleUtils;
import com.vui.bluelight.ble.BleUtils2;
import com.vui.bluelight.main.BrightnessFragment;
import com.vui.bluelight.main.HomeFragment;
import com.vui.bluelight.main.UserFragment;
import com.vui.bluelight.music.MusicService;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements OnClickListener {

	private Visualizer mVisualizer;// 频谱器
	private MusicService mService;// 音乐服务
	private Intent intent;// 音乐播放服务对应的intent

	private VisualizerView mVisualizerView;

	FragmentManager fm;
	
	private LinearLayout llt_home;
	private LinearLayout llt_switcher;
	private LinearLayout llt_brightness;
	
	private boolean isOn=true;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		findView();

		intent = new Intent(MainActivity.this, MusicService.class);
		startService(intent);
		fm = getFragmentManager();

		HomeFragment homeFragment = new HomeFragment();
		fm.beginTransaction().replace(R.id.flt_content, homeFragment, "homeFrg").commit();
		
		BleUtils2.getInstance().initBt(this);
	}

	public void replaceFrg(Fragment frg, String tag) {
		fm.beginTransaction().replace(R.id.flt_content, frg, tag).addToBackStack(null).commit();
	}

	public void addFrg(Fragment frg) {
		fm.beginTransaction().add(R.id.flt_content, frg).addToBackStack(null).commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.llt_home:
			
			break;
		case R.id.llt_switcher:
			if(isOn){
				//关灯
				/*String order_close_light="a4b3ffffffffffff55";
				BleUtils.getInstance().write(BleUtils.CHAR_UUID,order_close_light);*/
				
				isOn=false;
			}else{
				//开灯
				String order_open_light="a4b2ffffffffffff55";
				BleUtils2.getInstance().write(BleUtils2.CHAR_UUID,order_open_light);
			
				isOn=true;
			}
			break;
		case R.id.llt_brightness:
		
			if(brightnessFrg!=null&&brightnessFrg.isAdded()){
				fm.popBackStack();
			}else{
				brightnessFrg = new BrightnessFragment();
				addFrg(brightnessFrg);
			}

			break;
		}

	}
	public void findView(){
		llt_home=(LinearLayout) findViewById(R.id.llt_home);
		llt_switcher=(LinearLayout) findViewById(R.id.llt_switcher);
		llt_brightness=(LinearLayout) findViewById(R.id.llt_brightness);
		llt_home.setOnClickListener(this);
		llt_switcher.setOnClickListener(this);
		llt_brightness.setOnClickListener(this);
	}
	

	public View getBottom() {
		return findViewById(R.id.inc_foot);
	}


	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((MusicService.MyBinder) service).getService();
			mVisualizer = mService.mVisualizer;
			// 设置允许波形表示，并且捕获它
			mVisualizer.setEnabled(false);
			mVisualizerView.setVisualizer(mVisualizer);
			mVisualizer.setEnabled(true);
			// false 则不显示
		}
	};
	private BrightnessFragment brightnessFrg;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			UserFragment userfrg = (UserFragment) fm.findFragmentByTag("userFrg");
			if (userfrg != null) {
				userfrg.onActivityResult(requestCode, resultCode, data);
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
