package com.vui.bluelight.music;

import java.util.ArrayList;
import java.util.List;

import com.vui.bluelight.MainActivity;
import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.music.entity.MusicInfo;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.TimeUtils;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MusicListFragment extends Fragment implements OnClickListener{

	private MusicService mService;//音乐服务
	private TopBarView tbv;
	ListView lvw_music_list;
	List<MusicInfo> musicList=new ArrayList<MusicInfo>();
	
	private TextView tvw_current_music_name;
	private TextView tvw_current_music_author;
	private TextView tvw_current_music_duration;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().bindService(new Intent(getActivity(),MusicService.class), conn, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.frag_music_list, null);
		if(view!=null&&view.getParent()!=null){
			((ViewGroup)view.getParent()).removeView(view);
		}
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tbv=(TopBarView) view.findViewById(R.id.topbar);
		lvw_music_list=(ListView) view.findViewById(R.id.lvw_music_list);
		tvw_current_music_name=(TextView) view.findViewById(R.id.tvw_current_music_name);
		tvw_current_music_author=(TextView) view.findViewById(R.id.tvw_current_music_author);
		tvw_current_music_duration=(TextView) view.findViewById(R.id.tvw_current_music_duration);
		
		tbv.setTitleText("localmusic");
		tbv.setRightText("online music");
		lvw_music_list.setAdapter(adapter);
		lvw_music_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(mService!=null){
					mService.play(position);
				}
				setCurrentMusic(mService.getCurrentMusic());
			}
		});
		
		((MainActivity)getActivity()).getBottom().setVisibility(View.GONE);
		tbv.getLeftBtn().setOnClickListener(this);
		
		
	}
	public void setCurrentMusic(MusicInfo music){
		tvw_current_music_name.setText(music.getTitle());
		tvw_current_music_author.setText(music.getArtist());
		tvw_current_music_duration.setText(TimeUtils.parseMills2TimeStr(music.getDuration()));
	}

	public void onDestroy() {
		super.onDestroy();
		getActivity().unbindService(conn);
		((MainActivity)getActivity()).getBottom().setVisibility(View.VISIBLE);
	};
	
	
	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((MusicService.MyBinder) service).getService();
			musicList.clear();
			musicList.addAll(mService.getMusicList());
			adapter.notifyDataSetChanged();
			setCurrentMusic(mService.getCurrentMusic());
		}
	};
	
	
	BaseAdapter adapter = new BaseAdapter() {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_music_list, null);
				holder = new Holder();
				holder.tvw_music_name = (TextView) convertView.findViewById(R.id.tvw_music_name);
				holder.tvw_music_author = (TextView) convertView.findViewById(R.id.tvw_music_author);
				holder.tvw_music_time = (TextView) convertView.findViewById(R.id.tvw_music_time);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.tvw_music_name.setText(musicList.get(position).getTitle());
			;
			holder.tvw_music_author.setText(musicList.get(position).getArtist());
			holder.tvw_music_time.setText(TimeUtils.parseMills2TimeStr(musicList.get(position).getDuration()));
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return musicList == null ? null : musicList.get(position);
		}

		@Override
		public int getCount() {
			return musicList == null ? 0 : musicList.size();
		}
	};
	
	static class Holder {
		TextView tvw_music_name;
		TextView tvw_music_author;
		TextView tvw_music_time;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getFragmentManager().popBackStack();
			break;

		default:
			break;
		}
	}
	
}
