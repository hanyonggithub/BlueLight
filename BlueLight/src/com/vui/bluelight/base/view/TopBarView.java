package com.vui.bluelight.base.view;

import com.vui.bluelight.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TopBarView extends RelativeLayout {

	ImageView leftBtn;
	TextView title;
	TextView rightBtn;

	public TopBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public TopBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public TopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public void init(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.v_topbar, null);
		this.addView(view);
		requestLayout();
	}
	
	public void setTitleText(String title){
		if(title!=null){
			TextView tvw_title=(TextView) this.findViewById(R.id.title);
			tvw_title.setText(title);
		}
	}
	
	public void setRightText(String righttext){
		if(righttext!=null){
			TextView tvw_right=(TextView) this.findViewById(R.id.right_btn);
			tvw_right.setText(righttext);
		}
	}
	
	public ImageView getLeftBtn(){
		if(leftBtn==null){
			leftBtn=(ImageView) findViewById(R.id.back);
		}
		return leftBtn;
	}
	
	public TextView getTitleView(){
		if(title==null){
			title=(TextView) findViewById(R.id.title);
		}
		return title;
	}
	
	public TextView getRightBtn(){
		if(rightBtn==null){
			rightBtn=(TextView) findViewById(R.id.right_btn);
		}
		return rightBtn;
	}

}
