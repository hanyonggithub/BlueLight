package com.vui.bluelight.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ColorPickView extends ImageView implements OnTouchListener{

	public ColorPickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnTouchListener(this);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float rawX = event.getRawX();
		float rawY = event.getRawY();
		return false;
	}
	
}
