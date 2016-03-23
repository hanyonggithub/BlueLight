package com.vui.bluelight.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vui.bluelight.R;
import com.vui.bluelight.utils.LogUtils;

public class ColorPickerView extends ImageView implements OnTouchListener{

	private Context context;
	private int CX;
	private int CY;


	public ColorPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ColorPickerView(Context context) {
		super(context);
		init(context);
	}


	@SuppressLint("ClickableViewAccessibility")
	private void init(Context context) {	
		LogUtils.i("llpp:--init---==============: ");
		this.setOnTouchListener(this);
		this.context=context;
	}

	View moveView;
	@SuppressLint("ClickableViewAccessibility")
	public void setMoveView(View moveView){
		diameter_moveView = (int) context.getResources().getDimension(R.dimen.rgb_ring_movedot_size);
		lp_moveView = new RelativeLayout.LayoutParams(diameter_moveView,diameter_moveView);
		this.moveView=moveView;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {		
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(CX==0){
				CX = this.getWidth()/2;
				CY = this.getHeight()/2;
			}			
			LogUtils.i("llpp:=====按下=======:图片的高度是:"+this.getWidth()+"宽:"+this.getHeight());
		case MotionEvent.ACTION_MOVE:
			int BX = (int) event.getX();
			int BY = (int) event.getY();
			
			double getdistance = getdistance(CX, CY, BX, BY);
			//因为图片的的长宽比圆稍大。故要减去部分
			if(getdistance>=(CX-CX/11)){
				return true;
			}
			LogUtils.i("llpp:=====移动=======:X:"+BX+"Y:"+event.getY()+"");
			lp_moveView.topMargin=BY-diameter_moveView/2;
			lp_moveView.leftMargin=BX-diameter_moveView/2;
			moveView.setLayoutParams(lp_moveView);
			int colorFromColorRing = getColorFromColorRing(BX, BY);
			onGetColor.onGetColor(colorFromColorRing);
		}
		return true;
	}

	OnGetColorListener onGetColor;
	private Drawable drawable;
	private RelativeLayout.LayoutParams lp_moveView;
	private int diameter_moveView;
	public void setOnGetColorListener(OnGetColorListener onGetColor){
		this.onGetColor=onGetColor;
	}

	public interface OnGetColorListener{
		public void onGetColor(int color);
	}
	/**
	 * 从彩盘获取颜色
	 * @param x
	 * @param y
	 * @return
	 */
	private int getColorFromColorRing(int x,int y) {
		BitmapDrawable background = (BitmapDrawable) this.getDrawable();
		Bitmap bitmap = background.getBitmap();
		int pixel=0;
		try {
			pixel = bitmap.getPixel(x, y);
		} catch (Exception e) {
			pixel=Color.parseColor("#00000000");
			LogUtils.e("llpp:颜色越界===========");
			//e.printStackTrace();
		}
		LogUtils.i("获取的颜色是：pixel："+pixel);

		return pixel;
	}
	
	/**
	 * 计算两点间的长度
	 * @param aX
	 * @param aY
	 * @param bX
	 * @param bY
	 * @return
	 */
	private double getdistance(double aX,double aY,double bX,double bY){
		return  Math.sqrt(Math.pow(bX-aX, 2)+Math.pow(bY-aY, 2));
	}

}
