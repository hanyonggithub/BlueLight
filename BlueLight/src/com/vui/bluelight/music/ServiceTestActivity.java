package com.vui.bluelight.music;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.VisualizerView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ServiceTestActivity extends Activity {
	private boolean isPlay = true;
	private boolean isShow = true;
	TextView tvw_play;
	TextView tvw_display_visualizer;
	VisualizerView mVisualizerView;
	Visualizer mVisualizer;
	Intent intent;
	MusicService mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_service_test);
		tvw_play = (TextView) findViewById(R.id.tvw_paly);
		tvw_display_visualizer = (TextView) findViewById(R.id.tvw_display_visualizer);
		mVisualizerView = (VisualizerView) findViewById(R.id.visualizer);
		intent = new Intent(ServiceTestActivity.this, MusicService.class);
		startService(new Intent(ServiceTestActivity.this, MusicService.class));
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
		tvw_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isPlay) {
					stopService(new Intent(ServiceTestActivity.this, MusicService.class));
					isPlay = false;
				} else {

					isPlay = true;

				}
			}
		});

		tvw_display_visualizer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ServiceTestActivity.this,ServiceTestActivity2.class);
				startActivity(intent);
			}
		});
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
			
			mVisualizerView.setVisualizer(mVisualizer);
			mVisualizer.setEnabled(true);
			// false 则不显示
		}
	};
	
	protected void onResume() {
		super.onResume();
		if(mVisualizer!=null){
			mVisualizerView.setVisualizer(mVisualizer);
			mVisualizer.setEnabled(true);
		}
		
	};

	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	};
}
