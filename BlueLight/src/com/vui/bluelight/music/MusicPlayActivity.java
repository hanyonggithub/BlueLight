package com.vui.bluelight.music;

import java.io.IOException;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.base.view.VisualizerView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicPlayActivity extends Activity implements OnClickListener {
	private MediaPlayer mMediaPlayer;// 音频
	private Visualizer mVisualizer;// 频谱器
	VisualizerView mBaseVisualizerView;
	private Equalizer mEqualizer; // 均衡器
	private MusicPlayerService2 mMusicPlayService;
	
	private TopBarView tbv;
	private TextView tvw_music_name;
	private ImageView ivw_light_mode;
	private ImageView ivw_music_download;
	private ImageView ivw_music_list;
	private ImageView ivw_music_last;
	private ImageView ivw_music_play;
	private ImageView ivw_music_next;
	private ImageView ivw_music_recycle;
	private Intent intent;
	private ServiceConnection conn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.frg_music_play);
		findView();
		setListener();
		tbv.setTitleText("local music");
		tbv.setRightText("online music");
		mBaseVisualizerView = (VisualizerView) findViewById(R.id.visualview);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// 设置音频流 -
		intent = new Intent(MusicPlayActivity.this, MusicPlayerService2.class);
		conn=new ServiceConnection() {
			@Override
			public void onServiceDisconnected(ComponentName name) {

			}
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mMusicPlayService = ((MusicPlayerService2.MsgBinder) service).getService();
				startMusic();
			}
		};
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
		
	}
	
	public void findView(){
		tbv=(TopBarView) findViewById(R.id.topbar);
		tvw_music_name=(TextView) findViewById(R.id.tvw_music_name);
		ivw_music_list=(ImageView) findViewById(R.id.ivw_music_list);
		ivw_music_last=(ImageView) findViewById(R.id.ivw_music_last);
		ivw_music_play=(ImageView) findViewById(R.id.ivw_music_play);
		ivw_music_next=(ImageView) findViewById(R.id.ivw_music_next);
		ivw_music_recycle=(ImageView) findViewById(R.id.ivw_music_recyle);
	}
	
	public void setListener(){
		ivw_music_list.setOnClickListener(this);
		ivw_music_last.setOnClickListener(this);
		ivw_music_play.setOnClickListener(this);
		ivw_music_next.setOnClickListener(this);
		ivw_music_recycle.setOnClickListener(this);
		
	}
	
	public void startMusic(){
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);// 设置音频流 -
		mMediaPlayer = mMusicPlayService.mMediaPlayer;
		mMediaPlayer.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				 mMediaPlayer.reset();
				return false;
			}
		});
//		mMediaPlayer = MediaPlayer.create(this, R.raw.aaaass);// 实例化 MediaPlayer
		   setupVisualizerFxAndUi();//添加频谱到界面
//	        setupEqualizeFxAndUi();//添加均衡器到界面													// 并添加音频
		
		 mVisualizer.setEnabled(true);//false 则不显示
	        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

	            @Override
	            public void onCompletion(MediaPlayer mp) {
	            	  mVisualizer.setEnabled(false);//false 则不显示
	            }
	        });

	        if(mMediaPlayer.isPlaying()){
	        	mMediaPlayer.reset();
	        	try {
					mMediaPlayer.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        mMediaPlayer.start();//开始播放
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
		// 采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
//		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		// 设置允许波形表示，并且捕获它
		mVisualizer=mMusicPlayService.mVisualizer;
		mBaseVisualizerView.setVisualizer(mVisualizer);
		mVisualizer.setEnabled(true);// false 则不显示
	}


	public void setMusicName(){
		tvw_music_name.setText(mMusicPlayService.getCurrentMusic().getTitle());
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ivw_music_list:
			intent=new Intent(MusicPlayActivity.this,MusicListActivity.class);
			startActivity(intent);
			
			break;
		case R.id.ivw_music_last:
			mMusicPlayService.playLast();
			setMusicName();
			break;
		case R.id.ivw_music_play:
			mMusicPlayService.play();;
			setMusicName();
			break;
		case R.id.ivw_music_next:
			mMusicPlayService.playNext();;
			setMusicName();
			break;
		case R.id.ivw_music_recyle:
			
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isFinishing() && mMediaPlayer != null) {
			mVisualizer.release();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
		unbindService(conn);
	
	}
}
