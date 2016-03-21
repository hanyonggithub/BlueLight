package com.vui.bluelight.music;

import java.io.IOException;
import java.util.List;

import com.vui.bluelight.R;
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
import android.util.Log;
import android.widget.Toast;

public class MusicPlayerService3 extends Service {

	 private static final String TAG = "MyService";  
	    MediaPlayer player;  
	  
	    @Override  
	    public IBinder onBind(Intent intent) {  
	        return null;  
	    }  
	  
	    @Override  
	    public void onCreate() {  
	        Toast.makeText(this, "My Service created", Toast.LENGTH_LONG).show();  
	        Log.i(TAG, "onCreate");  
	  
	        player = MediaPlayer.create(this, R.raw.aaaass);  
	        player.setLooping(false);  
	    }  
	  
	    @Override  
	    public void onDestroy() {  
	        Toast.makeText(this, "My Service Stoped", Toast.LENGTH_LONG).show();  
	        Log.i(TAG, "onDestroy");  
	        player.stop();  
	    }  
	  
	    @Override  
	    public void onStart(Intent intent, int startid) {  
	        Toast.makeText(this, "My Service Start", Toast.LENGTH_LONG).show();  
	        Log.i(TAG, "onStart");  
	        player.start();  
	    }  
	
}
