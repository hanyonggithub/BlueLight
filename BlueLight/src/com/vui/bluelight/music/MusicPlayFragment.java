package com.vui.bluelight.music;

import java.lang.ref.WeakReference;

import com.vui.bluelight.MainActivity;
import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.base.view.VisualizerView;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MusicPlayFragment extends Fragment implements OnClickListener {
	private TopBarView tbv;
	private ImageView ivw_light_mode;
	private ImageView ivw_music_download;
	private TextView tvw_music_name;
	private ImageView ivw_music_list;
	private ImageView ivw_music_last;
	private ImageView ivw_music_play;
	private ImageView ivw_music_next;
	private ImageView ivw_music_recycle;
	private SeekBar sbr;

	Intent intent;// 音乐播放服务对应intent
	private Visualizer mVisualizer;// 频谱器
	private MusicService mService;// 音乐服务
	private VisualizerView mVisualizerView;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = new Intent(getActivity(), MusicService.class);
		handler = new MyHandler(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_music_play, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tbv = (TopBarView) view.findViewById(R.id.topbar);
		ivw_light_mode = (ImageView) view.findViewById(R.id.ivw_light_mode);
		ivw_music_download = (ImageView) view.findViewById(R.id.ivw_music_download);
		tvw_music_name = (TextView) view.findViewById(R.id.tvw_music_name);

		ivw_music_list = (ImageView) view.findViewById(R.id.ivw_music_list);
		ivw_music_last = (ImageView) view.findViewById(R.id.ivw_music_last);
		ivw_music_play = (ImageView) view.findViewById(R.id.ivw_music_play);
		ivw_music_next = (ImageView) view.findViewById(R.id.ivw_music_next);
		ivw_music_recycle = (ImageView) view.findViewById(R.id.ivw_music_recyle);
		sbr = (SeekBar) view.findViewById(R.id.sbr);

		mVisualizerView = (VisualizerView) view.findViewById(R.id.visulizer);

		tbv.setTitleText("localmusic");
		tbv.setRightText("online music");

		ivw_light_mode.setOnClickListener(this);
		ivw_music_download.setOnClickListener(this);
		ivw_music_list.setOnClickListener(this);
		ivw_music_last.setOnClickListener(this);
		ivw_music_play.setOnClickListener(this);
		ivw_music_next.setOnClickListener(this);
		ivw_music_recycle.setOnClickListener(this);
		tbv.getLeftBtn().setOnClickListener(this);
		sbr.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				if (mService != null) {
					mService.setProgress(seekBar.getProgress());
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

			}
		});

		getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);

	}

	@Override
	public void onResume() {
		super.onResume();
		if (mVisualizer != null) {
			mVisualizerView.setVisualizer(mVisualizer);
			mVisualizer.setEnabled(true);
			handler.removeMessages(1);
			handler.sendEmptyMessageDelayed(1, 100);
		}
		if(tvw_music_name!=null&&mService!=null){
			tvw_music_name.setText(mService.getCurMusicName());
		}
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getFragmentManager().popBackStack();
			break;
		case R.id.ivw_light_mode:

			break;
		case R.id.ivw_music_download:

			break;
		case R.id.ivw_music_list:
			MusicListFragment musicListfragment = new MusicListFragment();
			((MainActivity) getActivity()).replaceFrg(musicListfragment);
			break;
		case R.id.ivw_music_last:
			if (mService != null) {
				mService.last();
				handler.removeMessages(1);
				handler.sendEmptyMessageDelayed(1, 100);
			}
			tvw_music_name.setText(mService.getCurMusicName());
			break;
		case R.id.ivw_music_play:
			if (mService != null) {
				mService.play();
				handler.removeMessages(1);
				handler.sendEmptyMessageDelayed(1, 100);
			}
			tvw_music_name.setText(mService.getCurMusicName());
			break;
		case R.id.ivw_music_next:
			if (mService != null) {
				mService.next();
				handler.removeMessages(1);
				handler.sendEmptyMessageDelayed(1, 100);
			}
			tvw_music_name.setText(mService.getCurMusicName());
			break;
		case R.id.ivw_music_recyle:

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
			handler.removeMessages(1);
			handler.sendEmptyMessageDelayed(1, 100);
			tvw_music_name.setText(mService.getCurMusicName());
		}
	};

	static class MyHandler extends Handler {
		private final WeakReference<MusicPlayFragment> mFrgRef;

		public MyHandler(MusicPlayFragment frg) {
			mFrgRef = new WeakReference<MusicPlayFragment>(frg);
		}

		@Override
		public void handleMessage(Message msg) {
			MusicPlayFragment mFrg = mFrgRef.get();
			if (mFrg != null) {
				if (mFrg.mService.player.isPlaying()) {
					mFrg.sbr.setProgress(
							mFrg.mService.player.getCurrentPosition() * 1000 / mFrg.mService.player.getDuration());
					sendEmptyMessageDelayed(1, 100);
				}

			}
		}

	}

}
