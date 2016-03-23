package com.vui.bluelight.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vui.bluelight.MainActivity;
import com.vui.bluelight.R;
import com.vui.bluelight.SettingActivity;
import com.vui.bluelight.UserActivity;
import com.vui.bluelight.base.view.TopBarView;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SettingFragment extends Fragment implements OnClickListener{
	private TopBarView tbv;
	ListView lvw_setting;
	int[] icons={R.drawable.user,R.drawable.about_us,R.drawable.recovery,R.drawable.guide,R.drawable.shop_now,R.drawable.contact_us,R.drawable.share,R.drawable.upgrade};
	String[] texts={"User","about us","recovery","user guide","shop now","contact us","share","upgrade"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_setting, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		tbv=(TopBarView) view.findViewById(R.id.topbar);
		tbv.getLeftBtn().setOnClickListener(this);
		tbv.setTitleText("setting");
		lvw_setting=(ListView) view.findViewById(R.id.lvw_setting);
		List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
		for(int i=0;i<icons.length;i++){
			Map<String,Object> listItem=new HashMap<String,Object>();
			listItem.put("Icon", icons[i]);
			listItem.put("Text", texts[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter=new SimpleAdapter(getActivity(),listItems,R.layout.item_setting,new String[]{"Icon","Text"},new int[]{R.id.icon,R.id.text});
		lvw_setting.setAdapter(simpleAdapter);
		lvw_setting.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				switch (position) {
				case 0:
					UserFragment muserfragment = new UserFragment();
					((MainActivity) getActivity()).replaceFrg(muserfragment);
					break;

				default:
					break;
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		getFragmentManager().popBackStack();
	}
}
