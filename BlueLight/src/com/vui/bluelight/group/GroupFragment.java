package com.vui.bluelight.group;

import java.util.ArrayList;
import java.util.List;

import com.vui.bluelight.MainActivity;
import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.ble.BleUtils2;
import com.vui.bluelight.ble.BtDevice;
import com.vui.bluelight.utils.LogUtils;

import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class GroupFragment extends Fragment implements OnClickListener {
	private TopBarView tbv;
	GridView gvw_group;
	GridView gvw_devices;
	String[] groups = { "livingroom", "bedroom", "kitchen", "guestroom", "readingroom", "meetingroom" };
	/*
	 * String[] devices = { "vui0001", "vui0002", "vui0003", "vui0004",
	 * "vui0005", "vui0006", "vui0007", "vui0008", "vui0009", "vui0010" };
	 */
	
	private int selectType=-1;
	private int selectPosition=-1;

	AlertDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_group, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		tbv = (TopBarView) view.findViewById(R.id.topbar);
		tbv.setTitleText("group");
		tbv.getLeftBtn().setOnClickListener(this);
		tbv.getRightBtn().setOnClickListener(this);

		gvw_group = (GridView) view.findViewById(R.id.gvw_group);
		gvw_devices = (GridView) view.findViewById(R.id.gvw_devices);
		gvw_group.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				TextView tvw = new TextView(getActivity());
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
				TextView tvw = new TextView(getActivity());
				tvw.setText(BleUtils2.mList.get(position).getName());
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
				return BleUtils2.mList.get(position);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return BleUtils2.mList.size();
			}
		});

		gvw_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_group, null);
				TextView tvw_connect_group = (TextView) view.findViewById(R.id.tvw_connect_group);
				TextView tvw_delete_group = (TextView) view.findViewById(R.id.tvw_delete_group);
				TextView tvw_edit_group = (TextView) view.findViewById(R.id.tvw_edit_group);
				TextView tvw_rename_group = (TextView) view.findViewById(R.id.tvw_rename_group);

				Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

				showDialog(view);
				tvw_connect_group.setOnClickListener(GroupFragment.this);
				tvw_delete_group.setOnClickListener(GroupFragment.this);
				tvw_edit_group.setOnClickListener(GroupFragment.this);
				tvw_rename_group.setOnClickListener(GroupFragment.this);
				btn_cancel.setOnClickListener(GroupFragment.this);
				selectType=0;
				selectPosition=position;
			}
		});

		gvw_devices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_devices, null);
				TextView tvw_connect_device = (TextView) view.findViewById(R.id.tvw_connect_device);
				ListView lvw_group = (ListView) view.findViewById(R.id.lvw_group);
				TextView tvw_add_to = (TextView) view.findViewById(R.id.tvw_add_to);
				TextView tvw_rename_device = (TextView) view.findViewById(R.id.tvw_rename_device);
				Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

				tvw_connect_device.setOnClickListener(GroupFragment.this);
				tvw_add_to.setOnClickListener(GroupFragment.this);
				tvw_rename_device.setOnClickListener(GroupFragment.this);
				btn_cancel.setOnClickListener(GroupFragment.this);

				showDialog(view);

				selectType=1;
				selectPosition=position;
				
				lvw_group.setAdapter(new BaseAdapter() {

					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = new TextView(getActivity());
						}
						((TextView) convertView).setText(groups[position]);
						((TextView) convertView).setTextSize(20);
						((TextView) convertView).setTextColor(Color.parseColor("#88000000"));;
						((TextView) convertView).setPadding(0, 5, 0, 5);

						((TextView) convertView).setGravity(Gravity.CENTER);
						LayoutParams parmas = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						convertView.setLayoutParams(parmas);
						return convertView;
					}

					@Override
					public long getItemId(int position) {
						return position;
					}

					@Override
					public Object getItem(int position) {
						return null;
					}

					@Override
					public int getCount() {
						return groups.length;
					}
				});

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getFragmentManager().popBackStack();
			break;
		case R.id.right_btn:
			getFragmentManager().popBackStack();
			break;
		case R.id.tvw_connect_group:
			LogUtils.e("connect group");
			if(selectType==0){//连接一组设备
				
			}else if(selectType==1){
				BtDevice device=BleUtils2.mList.get(selectPosition);
				List<BtDevice> list=new ArrayList<BtDevice>();
				list.add(device);
				BleUtils2.getInstance().connect(list);
			}
			break;
		case R.id.tvw_delete_group:
			LogUtils.e("delete group");
			break;
		case R.id.tvw_edit_group:
			LogUtils.e("edit group");
			GroupDetailFragment groupDetialFrg = new GroupDetailFragment();
			((MainActivity) getActivity()).replaceFrg(groupDetialFrg, "groupDetialFrg");

			break;
		case R.id.tvw_rename_group:
			LogUtils.e("rename group");
			break;
		case R.id.tvw_connect_device:
			LogUtils.e("connect device");
			BleUtils2.getInstance().connect(BleUtils2.mList.get(selectPosition).getAddress());
			break;
		case R.id.tvw_rename_device:
			LogUtils.e("rename device");
			break;
		case R.id.btn_cancel:

			break;

		default:
			break;
		}
		dismissDialog();
	}

	public void dismissDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public void showDialog(View view) {
		dialog = new AlertDialog.Builder(getActivity()).setView(view).create();
		Window window = dialog.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.BottomDialogStyle); // 添加动画
		dialog.show();
		((ViewGroup) view.getParent()).setBackgroundColor(Color.TRANSPARENT);
		((ViewGroup) view.getParent().getParent()).setBackgroundColor(Color.TRANSPARENT);

	}

}
