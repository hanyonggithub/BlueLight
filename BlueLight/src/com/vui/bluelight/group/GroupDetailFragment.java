package com.vui.bluelight.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.utils.LogUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GroupDetailFragment extends Fragment implements OnClickListener {

	TopBarView tbv;
	ListView lvw_group_detial;
	String[] devices = { "vui0022585", "vui0022586", "vui0022587", "vui0022588" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_group_detail, null);
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
		lvw_group_detial = (ListView) view.findViewById(R.id.lvw_group_detial);
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < devices.length; i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("device", devices[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.item_group_detail,
				new String[] { "device" }, new int[] { R.id.tvw_device_name });
		lvw_group_detial.setAdapter(simpleAdapter);
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
			
		default:
			break;
		}
	}
	
	BaseAdapter adapter=new BaseAdapter() {
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Holder holder;
			if(convertView==null){
				convertView=LayoutInflater.from(getActivity()).inflate(R.layout.frag_group_detail, null);
				holder=new Holder();
				holder.tvw_device_name=(TextView) convertView.findViewById(R.id.tvw_device_name);
				holder.ivw_delete=(ImageView)convertView.findViewById(R.id.ivw_delete);
				convertView.setTag(holder);
			}
			holder=(Holder) convertView.getTag();
			holder.tvw_device_name.setText(devices[position]);
			holder.ivw_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					LogUtils.e("删除第"+position+"个设备");
					adapter.notifyDataSetChanged();
				}
			});
			return null;
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
			return devices.length;
		}
	};
	
	static class Holder{
		TextView tvw_device_name;
		ImageView ivw_delete;
	}
	
	
}
