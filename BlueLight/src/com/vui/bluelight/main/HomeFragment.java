package com.vui.bluelight.main;

import java.io.File;

import com.vui.bluelight.Constants;
import com.vui.bluelight.MainActivity;
import com.vui.bluelight.R;
import com.vui.bluelight.base.view.RoundImageView;
import com.vui.bluelight.base.view.VisualizerView;
import com.vui.bluelight.group.GroupFragment;
import com.vui.bluelight.mod.ModeFragment;
import com.vui.bluelight.music.MusicPlayFragment;
import com.vui.bluelight.music.MusicService;
import com.vui.bluelight.rgb.RGBMainFragment;
import com.vui.bluelight.timer.fragment.TimerFragment;
import com.vui.bluelight.utils.BitmapUtils;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.SharepreferenceUtils;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener {

	Intent intent;// 音乐播放服务对应intent
	private Visualizer mVisualizer;// 频谱器
	private MusicService mService;// 音乐服务
	private VisualizerView mVisualizerView;

	private ImageView setting;
	private RoundImageView rivw_user_image;
	
	private TextView tvw_user_name;
	private TextView tvw_lefttime_light;
	private TextView tvw_user_job;
	private TextView tvw_lefttime_music;
	

	private LinearLayout llt_music_play;
	private LinearLayout llt_rgbw;
	private LinearLayout llt_mode;
	private LinearLayout llt_timer;
	private LinearLayout llt_group;

	private TextView tvw_music_name;
	private ImageView ivw_music_last;
	private ImageView ivw_music_play;
	private ImageView ivw_music_next;
	
	private Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = new Intent(getActivity(), MusicService.class);
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			String path=Environment.getExternalStorageDirectory()+File.separator+"user_image";
			bitmap=BitmapUtils.getBitmapFromFile(path);
			LogUtils.e("获取用户图像了，bitmap==null:"+(bitmap==null));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_home, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setting = (ImageView) view.findViewById(R.id.setting);
		rivw_user_image = (RoundImageView) view.findViewById(R.id.rivw_user_image);
		
		tvw_user_name=(TextView) view.findViewById(R.id.tvw_user_name);
		tvw_user_job=(TextView) view.findViewById(R.id.tvw_user_job);
		tvw_lefttime_light=(TextView) view.findViewById(R.id.tvw_lefttime_light);
		tvw_lefttime_music=(TextView) view.findViewById(R.id.tvw_lefttime_music);
		
		

		ivw_music_last = (ImageView) view.findViewById(R.id.ivw_music_last);
		ivw_music_play = (ImageView) view.findViewById(R.id.ivw_music_play);
		ivw_music_next = (ImageView) view.findViewById(R.id.ivw_music_next);

		llt_rgbw = (LinearLayout) view.findViewById(R.id.llt_rgbw);
		llt_mode = (LinearLayout) view.findViewById(R.id.llt_mode);
		llt_timer = (LinearLayout) view.findViewById(R.id.llt_timer);
		llt_group = (LinearLayout) view.findViewById(R.id.llt_group);
		llt_music_play = (LinearLayout) view.findViewById(R.id.llt_music_play);
		mVisualizerView = (VisualizerView) view.findViewById(R.id.visualizer);

		tvw_music_name = (TextView) view.findViewById(R.id.tvw_music_name);

		setting.setOnClickListener(this);
		rivw_user_image.setOnClickListener(this);
		llt_music_play.setOnClickListener(this);
		ivw_music_last.setOnClickListener(this);
		ivw_music_play.setOnClickListener(this);
		ivw_music_next.setOnClickListener(this);

		llt_rgbw.setOnClickListener(this);
		llt_mode.setOnClickListener(this);
		llt_timer.setOnClickListener(this);
		llt_group.setOnClickListener(this);
		getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
		
		if(bitmap!=null){
			rivw_user_image.setImageBitmap(bitmap);
		}
		String name=(String)SharepreferenceUtils.get(getActivity(), Constants.UseMsg.NAME,"");
		if(!TextUtils.isEmpty(name)){
			tvw_user_name.setText(name);
			tvw_user_job.setText((String)SharepreferenceUtils.get(getActivity(), Constants.UseMsg.JOB,""));
		}
		
		
		
		Log.e("homeFrag", "onviewcreate  bindservice");
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mVisualizer != null&&mVisualizerView!=null) {
			mVisualizerView.setVisualizer(mVisualizer);
			mVisualizer.setEnabled(true);

		}
		if (tvw_music_name != null&&mService!=null) {
			tvw_music_name.setText(mService.getCurMusicName());
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.e("homefrag", "onHiddenchange -----hidden" + hidden);
		if (!hidden) {

			mVisualizerView.setVisualizer(mService.mVisualizer);
			mVisualizer.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.setting:
			SettingFragment settingfragment = new SettingFragment();
			((MainActivity) getActivity()).replaceFrg(settingfragment,"settingFrg");
			break;
		case R.id.rivw_user_image:
			UserFragment muserfragment = new UserFragment();
			((MainActivity) getActivity()).replaceFrg(muserfragment,"userFrg");
			break;
		case R.id.llt_music_play:
			MusicPlayFragment musicPlayfragment = new MusicPlayFragment();
			((MainActivity) getActivity()).replaceFrg(musicPlayfragment,"musicPlayFrg");
			break;
		case R.id.ivw_music_last:
			mService.last();
			tvw_music_name.setText(mService.getCurMusicName());
			break;
		case R.id.ivw_music_play:
			mService.play();
			tvw_music_name.setText(mService.getCurMusicName());
			break;
		case R.id.ivw_music_next:
			mService.next();
			tvw_music_name.setText(mService.getCurMusicName());
			break;
		case R.id.llt_rgbw:
			RGBMainFragment rgbMainFrg = new RGBMainFragment();
			((MainActivity) getActivity()).replaceFrg(rgbMainFrg,"rgbMainFrg");
			
			break;
		case R.id.llt_mode:
			ModeFragment modeFrg = new ModeFragment();
			((MainActivity) getActivity()).replaceFrg(modeFrg,"modeFrg");
			break;
		case R.id.llt_timer:
			/* intent = new Intent(getActivity(), TimerActivity.class);
			 getActivity().startActivity(intent);*/
				TimerFragment timerFrg = new TimerFragment();
				((MainActivity) getActivity()).replaceFrg(timerFrg,"timerFrg");
			 
			break;
		case R.id.llt_group:
			GroupFragment groupFrg = new GroupFragment();
			((MainActivity) getActivity()).replaceFrg(groupFrg,"groupFrg");
			break;
		default:
			break;
		}
	}

	public void onDestroy() {
		super.onDestroy();
		getActivity().unbindService(conn);
	};

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
			tvw_music_name.setText(mService.getCurMusicName());
		}
	};

}
