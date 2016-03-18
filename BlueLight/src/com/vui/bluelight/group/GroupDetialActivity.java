package com.vui.bluelight.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class GroupDetialActivity extends Activity{
	TopBarView tbv;
	ListView lvw_group_detial;
	String[] devices={"vui0022585","vui0022586","vui0022587","vui0022588"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_group_detail);
		tbv=(TopBarView) findViewById(R.id.topbar);
		tbv.setTitleText("group");
		lvw_group_detial=(ListView) findViewById(R.id.lvw_group_detial);
		List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
		for(int i=0;i<devices.length;i++){
			Map<String,Object> listItem=new HashMap<String,Object>();
			listItem.put("device", devices[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_group_detail,new String[]{"device"},new int[]{R.id.tvw_device_name});
		lvw_group_detial.setAdapter(simpleAdapter);
	}
}
