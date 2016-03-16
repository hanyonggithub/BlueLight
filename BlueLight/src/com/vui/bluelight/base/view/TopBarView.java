package com.vui.bluelight.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class TopBarView extends RelativeLayout{
	
	View leftBtn;
	View rightBtn;
	View title;
	
	
	public TopBarView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public TopBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	public void init(){
		if(leftBtn!=null){
			LayoutParams leftParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			this.addView(leftBtn, leftParams);
		}
		

		
		
	}
	
	protected View getLeftBtn() {
		return leftBtn;
	}
	protected void setLeftBtn(View leftBtn) {
		this.leftBtn = leftBtn;
	}
	protected View getRightBtn() {
		return rightBtn;
	}
	protected void setRightBtn(View rightBtn) {
		this.rightBtn = rightBtn;
	}
	protected View getTitle() {
		return title;
	}
	protected void setTitle(View title) {
		this.title = title;
	}
	

	




}
