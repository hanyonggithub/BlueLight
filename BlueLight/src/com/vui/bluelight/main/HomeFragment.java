package com.vui.bluelight.main;

import com.vui.bluelight.MainActivity;
import com.vui.bluelight.R;
import com.vui.bluelight.SettingActivity;
import com.vui.bluelight.UserActivity;
import com.vui.bluelight.base.view.VisualizerView;
import com.vui.bluelight.group.GroupActivity;
import com.vui.bluelight.mod.ModeShakingActivity;
import com.vui.bluelight.music.MusicPlayFragment;
import com.vui.bluelight.music.MusicService;
import com.vui.bluelight.rgb.RGBMainActivity;
import com.vui.bluelight.timer.TimerActivity;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener {

	Intent intent;// 音乐播放服务对应intent
	private Visualizer mVisualizer;// 频谱器
	private MusicService mService;// 音乐服务
	private VisualizerView mVisualizerView;

	private ImageView setting;
	private RelativeLayout rlt_user_image;

	private LinearLayout llt_music_play;
	private LinearLayout llt_rgbw;
	private LinearLayout llt_mode;
	private LinearLayout llt_timer;
	private LinearLayout llt_group;

	private TextView tvw_music_name;
	private ImageView ivw_music_last;
	private ImageView ivw_music_play;
	private ImageView ivw_music_next;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = new Intent(getActivity(), MusicService.class);
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
		rlt_user_image = (RelativeLayout) view.findViewById(R.id.rlt_user_image);

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
		rlt_user_image.setOnClickListener(this);
		llt_music_play.setOnClickListener(this);
		ivw_music_last.setOnClickListener(this);
		ivw_music_play.setOnClickListener(this);
		ivw_music_next.setOnClickListener(this);

		llt_rgbw.setOnClickListener(this);
		llt_mode.setOnClickListener(this);
		llt_timer.setOnClickListener(this);
		llt_group.setOnClickListener(this);
		getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
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
			((MainActivity) getActivity()).replaceFrg(settingfragment);
			break;
		case R.id.rlt_user_image:
			UserFragment muserfragment = new UserFragment();
			((MainActivity) getActivity()).replaceFrg(muserfragment);
			break;
		case R.id.llt_music_play:
			MusicPlayFragment musicPlayfragment = new MusicPlayFragment();
			((MainActivity) getActivity()).replaceFrg(musicPlayfragment);
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
			
			 intent = new Intent(getActivity(), RGBMainActivity.class);
			 getActivity().startActivity(intent);
			
			break;
		case R.id.llt_mode:
			
			  intent = new Intent(getActivity(),ModeShakingActivity.class);
			  getActivity().startActivity(intent);
			 
			break;
		case R.id.llt_timer:
			
			 intent = new Intent(getActivity(), TimerActivity.class);
			 getActivity().startActivity(intent);
			 
			break;
		case R.id.llt_group:
			
			 intent = new Intent(getActivity(), GroupActivity.class);
			  getActivity().startActivity(intent);
			 
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
