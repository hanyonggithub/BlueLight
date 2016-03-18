package com.vui.bluelight.group;

import com.j256.ormlite.stmt.query.In;
import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupActivity extends Activity {
	GridView gvw_group;
	GridView gvw_devices;
	String[] groups = { "livingroom", "bedroom", "kitchen", "guestroom", "readingroom", "meetingroom" };
	String[] devices = { "vui0001", "vui0002", "vui0003", "vui0004", "vui0005", "vui0006", "vui0007", "vui0008",
			"vui0009", "vui0010" };
	TopBarView tbv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_group);
		tbv=(TopBarView) findViewById(R.id.topbar);
		tbv.setTitleText("group");
		gvw_group = (GridView) findViewById(R.id.gvw_group);
		gvw_devices = (GridView) findViewById(R.id.gvw_devices);
		gvw_group.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tvw = new TextView(GroupActivity.this);
				tvw.setText(groups[position]);
				tvw.setBackgroundResource(R.drawable.round_rect_white);
				tvw.setGravity(Gravity.CENTER);
				tvw.setTextColor(Color.WHITE);
				tvw.setTextSize(20);
				tvw.setPadding(0, 10, 0, 10);
				return tvw;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return groups[position];
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return groups.length;
			}
		});
		gvw_devices.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tvw = new TextView(GroupActivity.this);
				tvw.setText(devices[position]);
				tvw.setTextColor(Color.WHITE);
				tvw.setTextSize(20);
				tvw.setGravity(Gravity.CENTER);
				return tvw;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return devices[position];
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return devices.length;
			}
		});
		
		
		gvw_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent=new Intent(GroupActivity.this,GroupDetialActivity.class);
				startActivity(intent);				
			}
		});
		
		gvw_group.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				showDialog();
				return true;
			}
		});
		gvw_devices.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
			}
		});
		
		gvw_devices.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				return false;
			}
		});
	}
	
	public void showDialog(){
		Toast.makeText(GroupActivity.this,"删除group", Toast.LENGTH_SHORT).show();
	}

}
