package com.vui.bluelight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vui.bluelight.base.view.TopBarView;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SettingActivity extends Activity{
	TopBarView tbv;
	ListView lvw_setting;
	int[] icons={R.drawable.user,R.drawable.about_us,R.drawable.recovery,R.drawable.guide,R.drawable.shop_now,R.drawable.contact_us,R.drawable.share,R.drawable.upgrade};
	String[] texts={"User","about us","recovery","user guide","shop now","contact us","share","upgrade"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aty_setting);
		tbv=(TopBarView) findViewById(R.id.topbar);
		tbv.setTitleText("setting");
		lvw_setting=(ListView) findViewById(R.id.lvw_setting);
//		lvw_setting.setDividerHeight(5);
//		lvw_setting.setDivider(new ColorDrawable(Color.WHITE));
		List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
		for(int i=0;i<icons.length;i++){
			Map<String,Object> listItem=new HashMap<String,Object>();
			listItem.put("Icon", icons[i]);
			listItem.put("Text", texts[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,R.layout.item_setting,new String[]{"Icon","Text"},new int[]{R.id.icon,R.id.text});
		lvw_setting.setAdapter(simpleAdapter);
		lvw_setting.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				switch (position) {
				case 0:
					Intent intent=new Intent(SettingActivity.this,UserActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
			}
		});
	
	}
	
	
}
