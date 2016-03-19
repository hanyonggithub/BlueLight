package com.vui.bluelight.music;

import java.io.IOException;
import java.util.List;

import com.vui.bluelight.music.entity.MusicInfo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

public class MusicPlayerService2 extends Service {
	public MediaPlayer mMediaPlayer;// 音频
	public Visualizer mVisualizer;// 频谱器
	private Equalizer mEqualizer; // 均衡器
	List<MusicInfo> musicList;
	private int mPosition;
	private int mProgress;

	@Override
	public void onCreate() {
		super.onCreate();
		musicList = MusicLoader.instance(getContentResolver()).getMusicList();
		mMediaPlayer = MediaPlayer.create(this, Uri.parse(musicList.get(0).getUrl()));
		mMediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				mMediaPlayer.reset();
				return false;
			}
		});

		 setupVisualizerFxAndUi();// 添加频谱到界面
		 setupEqualizeFxAndUi();// 添加均衡器到界面 // 并添加音频

		// mVisualizer.setEnabled(true);// false 则不显示
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
//				mVisualizer.setEnabled(false);// false 则不显示
			}
		});

		if (mMediaPlayer.isPlaying()) {
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

		// startMusic();

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
		mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
		// 采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		// 设置允许波形表示，并且捕获它
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MsgBinder();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mMediaPlayer != null) {
			mVisualizer.release();
			mMediaPlayer.release();
			mEqualizer.release();
			mMediaPlayer = null;
		}
	}

	public class MsgBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 * 
		 * @return
		 */
		public MusicPlayerService2 getService() {
			return MusicPlayerService2.this;
		}
	}

	public void startMusic() {
		mMediaPlayer.start();// 开始播放
		mMediaPlayer.setLooping(true);// 循环播放
	}

	public void play() {
		if (mMediaPlayer != null) {

			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
			} else {
				mMediaPlayer.start();
			}
		}

	}

	public void playNext() {
		if (mPosition == musicList.size() - 1) {
			mPosition = 0;
		} else {
			mPosition += 1;
		}
		mMediaPlayer.reset();
		try {
			mMediaPlayer.setDataSource(this, Uri.parse(musicList.get(mPosition).getUrl()));
			mMediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mMediaPlayer.start();
	}

	public void playLast() {
		if (mPosition == 0) {
			mPosition = musicList.size();
		} else {
			mPosition -= 1;
		}
		mMediaPlayer.reset();
		try {
			mMediaPlayer.setDataSource(this, Uri.parse(musicList.get(mPosition).getUrl()));
			mMediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mMediaPlayer.start();
	}
	public MusicInfo getCurrentMusic(){
		return musicList==null?null:musicList.get(mPosition);
	}
}
