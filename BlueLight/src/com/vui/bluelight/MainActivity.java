package com.vui.bluelight;

import com.vui.bluelight.base.view.VisualizerView;
import com.vui.bluelight.group.GroupActivity;
import com.vui.bluelight.mod.ModeShakingActivity;
import com.vui.bluelight.music.MusicPlayActivity;
import com.vui.bluelight.music.MusicPlayerService2;
import com.vui.bluelight.music.MusicPlayerService3;
import com.vui.bluelight.timer.TimerActivity;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private static final float VISUALIZER_HEIGHT_DIP = 200f;// 频谱View高度

	private MediaPlayer mMediaPlayer;// 音频
	private Visualizer mVisualizer;// 频谱器
	private Equalizer mEqualizer; // 均衡器

	private LinearLayout mLayout;// 代码布局
	private VisualizerView mBaseVisualizerView;

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

	private MusicPlayerService2 mMusicPlayService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		findView();
		setListener();

		mBaseVisualizerView = new VisualizerView(this);
		mBaseVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, // 宽度
				(int) (VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)// 高度
		));
		// 将频谱View添加到布局
		mLayout.addView(mBaseVisualizerView);
		Intent intent=new Intent(MainActivity.this,MusicPlayerService3.class);
		startService(intent);
		
		/*Intent intent = new Intent(MainActivity.this, MusicPlayerService2.class);
	
		bindService(intent, new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mMusicPlayService = ((MusicPlayerService2.MsgBinder) service).getService();
				startMusic();
			}
		}, Context.BIND_AUTO_CREATE);*/

	}

	public void findView() {

		setting = (ImageView) findViewById(R.id.setting);

		rlt_user_image = (RelativeLayout) findViewById(R.id.rlt_user_image);
		mLayout = (LinearLayout) findViewById(R.id.llt_music_play);

		ivw_music_last = (ImageView) findViewById(R.id.ivw_music_last);
		ivw_music_play = (ImageView) findViewById(R.id.ivw_music_play);
		ivw_music_next = (ImageView) findViewById(R.id.ivw_music_next);

		llt_rgbw = (LinearLayout) findViewById(R.id.llt_rgbw);
		llt_mode = (LinearLayout) findViewById(R.id.llt_mode);
		llt_timer = (LinearLayout) findViewById(R.id.llt_timer);
		llt_group = (LinearLayout) findViewById(R.id.llt_group);
		
		tvw_music_name = (TextView) findViewById(R.id.tvw_music_name);

	}

	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.setting:
			intent = new Intent(MainActivity.this, SettingActivity.class);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.rlt_user_image:
			intent = new Intent(MainActivity.this, UserActivity.class);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.llt_music_play:
			intent = new Intent(MainActivity.this, MusicPlayActivity.class);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.ivw_music_last:
			mMusicPlayService.playLast();
			setMusicName();
			break;
		case R.id.ivw_music_play:
			mMusicPlayService.play();
			setMusicName();
			break;
		case R.id.ivw_music_next:
			mMusicPlayService.playNext();
			setMusicName();
			break;
		case R.id.llt_rgbw:
			
			break;
		case R.id.llt_mode:
			intent = new Intent(MainActivity.this, ModeShakingActivity.class);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.llt_timer:
			intent = new Intent(MainActivity.this, TimerActivity.class);
			MainActivity.this.startActivity(intent);
			break;
		case R.id.llt_group:
			intent = new Intent(MainActivity.this, GroupActivity.class);
			MainActivity.this.startActivity(intent);
			break;

		default:
			break;
		}

	}
	public void setListener() {
		setting.setOnClickListener(this);
		rlt_user_image.setOnClickListener(this);
		mLayout.setOnClickListener(this);
		
		ivw_music_last.setOnClickListener(this);
		ivw_music_play.setOnClickListener(this);
		ivw_music_next.setOnClickListener(this);
		
		llt_rgbw.setOnClickListener(this);
		llt_mode.setOnClickListener(this);
		llt_timer.setOnClickListener(this);
		llt_group.setOnClickListener(this);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void startMusic() {
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// 设置音频流 -

		// mMediaPlayer = MediaPlayer.create(this, R.raw.aaaass);//实例化
		// MediaPlayer 并添加音频
		mMediaPlayer = mMusicPlayService.mMediaPlayer;

		setupVisualizerFxAndUi();// 添加频谱到界面
//		 setupEqualizeFxAndUi();//添加均衡器到界面
		// setupPlayButton();//添加按钮到界面
		/*
		 * mMediaPlayer.start();//开始播放 mMediaPlayer.setLooping(true);//循环播放
		 */
		/*
		 * mVisualizer.setEnabled(true);//false 则不显示
		 * mMediaPlayer.setOnCompletionListener(new
		 * MediaPlayer.OnCompletionListener() {
		 * 
		 * @Override public void onCompletion(MediaPlayer mp) {
		 * mVisualizer.setEnabled(false);//false 则不显示 } });
		 * 
		 * mMediaPlayer.start();//开始播放 mMediaPlayer.setLooping(true);//循环播放
		 */
//		mMusicPlayService.startMusic();
		 setMusicName();
	}

	/**
	 * 通过mMediaPlayer返回的AudioSessionId创建一个优先级为0均衡器对象 并且通过频谱生成相应的UI和对应的事件
	 */
	private void setupEqualizeFxAndUi() {

		mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
		mEqualizer.setEnabled(true);// 启用均衡器

	}

	/**
	 * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
	 */
	private void setupVisualizerFxAndUi() {
		// 实例化Visualizer，参数SessionId可以通过MediaPlayer的对象获得
//		mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
		// mVisualizer=mMusicPlayService.mVisualizer;
		// 采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
//		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		mVisualizer=mMusicPlayService.mVisualizer;
		// 设置允许波形表示，并且捕获它
		mBaseVisualizerView.setVisualizer(mVisualizer);
		mVisualizer.setEnabled(true);// false 则不显示
	}

	@Override
	protected void onPause() {
		super.onPause();
	
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

	public void setMusicName(){
		tvw_music_name.setText(mMusicPlayService.getCurrentMusic().getTitle());
	}

}
