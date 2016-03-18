package com.vui.bluelight;

import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.base.view.VisualizerView;
import com.vui.bluelight.group.GroupActivity;
import com.vui.bluelight.music.MusicListActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	
	

    private static final float VISUALIZER_HEIGHT_DIP = 200f;//频谱View高度

    private MediaPlayer mMediaPlayer;//音频
    private Visualizer mVisualizer;//频谱器
    private Equalizer mEqualizer; //均衡器

    private LinearLayout mLayout;//代码布局
    VisualizerView mBaseVisualizerView;
    TopBarView tbv;
    ImageButton play;
    ImageView setting;
    
    private LinearLayout llt_rgbw;
    private LinearLayout llt_mode;
    private LinearLayout llt_timer;
    private LinearLayout llt_group;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		tbv=(TopBarView) findViewById(R.id.topbar);
		mLayout=(LinearLayout) findViewById(R.id.llt_music_play);
		
        setVolumeControlStream(AudioManager.STREAM_MUSIC);//设置音频流 - STREAM_MUSIC：音乐回放即媒体音量


        mMediaPlayer = MediaPlayer.create(this, R.raw.aaaass);//实例化 MediaPlayer 并添加音频

        setupVisualizerFxAndUi();//添加频谱到界面
        setupEqualizeFxAndUi();//添加均衡器到界面
//        setupPlayButton();//添加按钮到界面


        mVisualizer.setEnabled(true);//false 则不显示
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
            	  mVisualizer.setEnabled(false);//false 则不显示
            }
        });

        mMediaPlayer.start();//开始播放
        mMediaPlayer.setLooping(true);//循环播放
        
        setting=(ImageView) findViewById(R.id.setting);
        setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,SettingActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
        
        llt_group=(LinearLayout) findViewById(R.id.llt_group);
        llt_group.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,GroupActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
        
        mLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,MusicListActivity.class);
				MainActivity.this.startActivity(intent);
				
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
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
        mBaseVisualizerView = new VisualizerView(this);

        mBaseVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,//宽度
                (int) (VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)//高度
        ));
      
        //将频谱View添加到布局
        mLayout.addView(mBaseVisualizerView);
        //实例化Visualizer，参数SessionId可以通过MediaPlayer的对象获得
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        //采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        //设置允许波形表示，并且捕获它
        mBaseVisualizerView.setVisualizer(mVisualizer);
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && mMediaPlayer != null) {
            mVisualizer.release();
            mMediaPlayer.release();
            mEqualizer.release();
            mMediaPlayer = null;
        }
    }

}
