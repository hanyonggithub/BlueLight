package com.vui.bluelight;

import com.vui.bluelight.base.view.VisualizerView;
import com.vui.bluelight.group.GroupActivity;
import com.vui.bluelight.main.HomeFragment;
import com.vui.bluelight.main.UserFragment;
import com.vui.bluelight.mod.ModeShakingActivity;
import com.vui.bluelight.music.MusicPlayActivity;
import com.vui.bluelight.music.MusicPlayFragment;
import com.vui.bluelight.music.MusicPlayerService2;
import com.vui.bluelight.music.MusicService;
import com.vui.bluelight.rgb.RGBMainActivity;
import com.vui.bluelight.timer.TimerActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final float VISUALIZER_HEIGHT_DIP = 200f;// 频谱View高度

	private Visualizer mVisualizer;// 频谱器
	private MusicService mService;// 音乐服务
	private Intent intent;// 音乐播放服务对应的intent

	private VisualizerView mVisualizerView;

	private ImageView setting;
	private RelativeLayout rlt_user_image;

	private LinearLayout llt_rgbw;
	private LinearLayout llt_mode;
	private LinearLayout llt_timer;
	private LinearLayout llt_group;

	private TextView tvw_music_name;
	private ImageView ivw_music_last;
	private ImageView ivw_music_play;
	private ImageView ivw_music_next;

	FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		intent = new Intent(MainActivity.this, MusicService.class);
		startService(intent);
		fm = getFragmentManager();

		HomeFragment homeFragment = new HomeFragment();
		fm.beginTransaction().replace(R.id.flt_content, homeFragment, "homeFrg").commit();
	}

	public void replaceFrg(Fragment frg, String tag) {
		fm.beginTransaction().replace(R.id.flt_content, frg, tag).addToBackStack(null).commit();
	}

	public void addFrg(Fragment frg) {
		fm.beginTransaction().add(R.id.flt_content, frg).addToBackStack(null).commit();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {

		default:
			break;
		}

	}

	public void setListener() {

	}

	public View getBottom() {
		return findViewById(R.id.inc_foot);

	}

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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			UserFragment userfrg = (UserFragment) fm.findFragmentByTag("userFrg");
			if (userfrg != null) {
				userfrg.onActivityResult(requestCode, resultCode, data);
			}
		}
	};

}
