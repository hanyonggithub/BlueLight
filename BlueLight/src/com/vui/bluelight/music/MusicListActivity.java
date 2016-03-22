package com.vui.bluelight.music;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.music.entity.MusicInfo;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.TimeUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MusicListActivity extends Activity {
	TopBarView tbv;
	ListView lvw_music_list;
	List<MusicInfo> musicList;
	BaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.frag_music_list);
		tbv = (TopBarView) findViewById(R.id.topbar);
		tbv.setTitleText("localmusic");
		tbv.setRightText("online music");
		lvw_music_list = (ListView) findViewById(R.id.lvw_music_list);
		adapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Holder holder;
				if (convertView == null) {
					convertView = LayoutInflater.from(MusicListActivity.this).inflate(R.layout.item_music_list, null);
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
//				holder.tvw_music_time.setText(String.valueOf(musicList.get(position).getDuration()));
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
		lvw_music_list.setAdapter(adapter);
		new Thread() {
			public void run() {
				musicList=MusicLoader.instance(MusicListActivity.this.getContentResolver()).getMusicList();
				MusicListActivity.this.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				});
			};

		}.start();
		
		lvw_music_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent=new Intent(MusicListActivity.this,MusicPlayActivity.class);
				intent.putExtra("url", musicList.get(position).getUrl());
				LogUtils.e("url="+musicList.get(position).getUrl());
				startActivity(intent);
				
			}
		});
	}

	static class Holder {
		TextView tvw_music_name;
		TextView tvw_music_author;
		TextView tvw_music_time;
	}
	


}
