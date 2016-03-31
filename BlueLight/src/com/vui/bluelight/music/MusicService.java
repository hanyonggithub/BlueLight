package com.vui.bluelight.music;

import java.io.IOException;
import java.util.List;

import com.vui.bluelight.Constants;
import com.vui.bluelight.R;
import com.vui.bluelight.music.entity.MusicInfo;
import com.vui.bluelight.utils.SharepreferenceUtils;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service {

	private static final String TAG = "MyService";
	public MediaPlayer player; //
	public Visualizer mVisualizer;// 频谱器
	private Equalizer mEqualizer; // 均衡器
	private List<MusicInfo> musicList;
	private int position;
	private int progress;
	private Handler mHandler;

	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}

	@Override
	public void onCreate() {
//		Toast.makeText(this, "My Service created", Toast.LENGTH_LONG).show();
		Log.i(TAG, "onCreate");
		// 获取手机音乐集合
		musicList = MusicLoader.instance(getContentResolver()).getMusicList();
		mHandler=new Handler();
		//上次播放的位置或者是第一首歌
		//从sharepreference获取上次播放歌曲，是否要记录上次歌曲播放位置？
		position=(Integer) SharepreferenceUtils.get(MusicService.this, Constants.MusicMsg.LAST_SONG_POSITION, 0);
/*		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {*/
				if(musicList!=null&&musicList.size()>0){
					player = MediaPlayer.create(MusicService.this, Uri.parse(musicList.get(position).getUrl()));
					player.setLooping(false);

					player.setOnErrorListener(new OnErrorListener() {
						@Override
						public boolean onError(MediaPlayer mp, int what, int extra) {
							player.reset();
							return false;
						}
					});

					// 实例化Visualizer，参数SessionId可以通过MediaPlayer的对象获得
					mVisualizer = new Visualizer(player.getAudioSessionId());
					// 采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
					mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
					// 设置允许波形表示，并且捕获它
					// mVisualizer.setEnabled(true);
					player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							mVisualizer.setEnabled(false);// false 则不显示
						}
					});

					mEqualizer = new Equalizer(0, player.getAudioSessionId());
					mEqualizer.setEnabled(true);// 启用均衡器
					player.start();
				}
			/*}*/
	/* }, 1000); */
		
		
		

	}
	


	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		if (player != null) {
			player.stop();
			player.release();
			player = null;
		}
		
		
	}

	@Override
	public void onStart(Intent intent, int startid) {
	
	}
	public void play(){
		if(player.isPlaying()){
			player.pause();
		}else{
			player.start();
		}
	
	}
	public void play(int position){
		this.position=position;
		player.reset();
		try {
			player.setDataSource(MusicService.this, Uri.parse(musicList.get(position).getUrl()));
			player.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.start();
	}
	public void next(){
		if(position==musicList.size()-1){
			position=0;
		}else{
			position+=1;
		}
		player.reset();
		try {
			player.setDataSource(MusicService.this, Uri.parse(musicList.get(position).getUrl()));
			player.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.start();
		
	}
	public void last(){
		if(position==0){
			position=musicList.size()-1;
		}else{
			position-=1;
		}
		player.reset();
		try {
			player.setDataSource(MusicService.this, Uri.parse(musicList.get(position).getUrl()));
			player.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		player.start();
		
	}
	public void setMode(int mode){
		
	}
	public void setProgress(int progress){
		player.seekTo(player.getDuration()*progress/1000);
		player.start();
	}
	
	public List<MusicInfo> getMusicList(){
		return musicList;
	}
	
	public String getCurMusicName(){
		return musicList.get(position).getTitle();
	}
	
	public MusicInfo getCurrentMusic(){
		return musicList.get(position);
	}
	
	public class MyBinder extends Binder {
		/**
		 * 获取当前Service的实例
		 * 
		 * @return
		 */
		public MusicService getService() {
			return MusicService.this;
		}
	}

}
