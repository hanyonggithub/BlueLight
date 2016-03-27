package com.vui.bluelight.rgb;
import com.vui.bluelight.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RGBMainActivity extends Activity{
	Context context=this;
	
	private int selectedColor= Color.parseColor("#ffffff");
	private RGBARingFragment rgbaRingFragment;
	private RGBPieFragment rgbPieFragment;
	private RGBADimmingFragment rgbaDimmingFragment;
	private FragmentManager fm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_rgb_main);
		initTitleBar();
		initFragment();
		initChooseView();
	}

	private void initChooseView() {
		RadioGroup rg_type_select_content = (RadioGroup) findViewById(R.id.rg_type_select_content);
		rg_type_select_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_rgb:
					switchFragment(rgbaRingFragment);
					break;
				case R.id.rb_ring:
					switchFragment(rgbPieFragment);
					break;
				case R.id.rb_dim:
					switchFragment(rgbaDimmingFragment);
					break;
				default:
					break;
				}

			}
		});
	}

	private void initFragment() {
		FragmentManager fm=getFragmentManager();
		FragmentTransaction transacction = fm.beginTransaction();
		rgbaRingFragment = new  RGBARingFragment();
		rgbPieFragment = new RGBPieFragment();
		rgbaDimmingFragment = new RGBADimmingFragment();
		transacction.replace(R.id.id_content, rgbaRingFragment).commit();
	}

	private   void switchFragment(Fragment targetFragment){
		if(fm==null)
		{
			fm = this. getFragmentManager();
		}
		FragmentTransaction transacction = fm.beginTransaction();
		transacction.replace(R.id.id_content, targetFragment).commit();
	}

	private void initTitleBar() {
		View back = findViewById(R.id.back);
		TextView title=(TextView) findViewById(R.id.title);
		if(title!=null){
			title.setText("livingroom");
		}
		TextView right=(TextView) findViewById(R.id.right_btn);
		
		final TextView right_btn = (TextView) findViewById(R.id.right_btn);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		right_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				right_btn.setTextColor(selectedColor);
				//Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void setSelectedColor(int color){
		selectedColor=color;
	}
}
