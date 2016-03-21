package com.vui.bluelight.mod;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.vui.bluelight.R;
import com.vui.bluelight.timer.entity.TimerEntity;
import com.vui.bluelight.utils.LogUtils;

public class ModeShakingActivity extends Activity{

	Context context = this;
	private GridView grirdView;
	int gridViewHeight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_modeshaking);
		initTypeChooseView();
		initSetGridView();

	}
	private void initSetGridView() {
		grirdView = (GridView) findViewById(R.id.grirv_mode);
		ViewTreeObserver viewTreeObserver = grirdView.getViewTreeObserver();
		viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener(
				) {
			@Override
			public void onGlobalLayout() {
				if(grirdView.getHeight()!=0){
					gridViewHeight=grirdView.getHeight();
					grirdView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					initGridView();
				}
				LogUtils.i("llpp:GridView 测量的高度是："+grirdView.getHeight());
			}
		});
	}
	private void initTypeChooseView() {
		RadioGroup rg_type_select_content =  (RadioGroup) findViewById(R.id.rg_type_select_content);
		rg_type_select_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.rb_rgb){
					Toast.makeText(context, "开发中", Toast.LENGTH_SHORT).show();
				}else if(checkedId==R.id.rb_scene){
					Toast.makeText(context, "开发中", Toast.LENGTH_SHORT).show();
				}else if(checkedId==R.id.rb_flicker){
					Toast.makeText(context, "开发中", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	private void initGridView() {
		ListAdapter adapter = new MyAdapter();
		grirdView.setAdapter(adapter);

	}
	class MyAdapter extends BaseAdapter implements ListAdapter {
		@Override
		public int getCount() {	
			return 12;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout llt = new LinearLayout(context);
			llt.setOrientation(LinearLayout.VERTICAL);
			AbsListView.LayoutParams layoutParams = 
					new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,gridViewHeight/4);

			LinearLayout.LayoutParams layoutParams_tv = 
					new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,gridViewHeight/4/2);
			TextView textView = new TextView(context);
			textView.setText(position<10? "00"+position:"0"+position);
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(Color.parseColor("#ffffff"));
			textView.setLayoutParams(layoutParams_tv);

			LinearLayout.LayoutParams layoutParams_color = 
					new LinearLayout.LayoutParams(gridViewHeight/4/2,gridViewHeight/4/2);
			layoutParams_color.gravity=Gravity.CENTER;
			Drawable drawable = context.getResources().getDrawable(R.drawable.bg_timer_cir_orange);
			TextView tv_color = new TextView(context);
			tv_color.setLayoutParams(layoutParams_color);
			tv_color.setBackgroundDrawable(drawable);

			llt.addView(textView);
			llt.addView(tv_color);
			llt.setLayoutParams(layoutParams);
			return llt;
		}
	}
}
