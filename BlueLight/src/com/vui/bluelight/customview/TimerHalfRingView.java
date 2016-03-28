package com.vui.bluelight.customview;

import com.vui.bluelight.rgb.RGBMainActivity;
import com.vui.bluelight.utils.DensityUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TimerHalfRingView extends View {

	private Paint paint;
	private RectF oval;
	public TimerHalfRingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}


	public TimerHalfRingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}



	public TimerHalfRingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	Context context;
	private void init(Context context) {
		this.context= context;
		paint = new Paint();
		paint.setColor(Color.parseColor("#ffffff"));
		paint.setStrokeWidth(DensityUtils.dp2px(context, 1));
		paint.setAntiAlias(true);  //消除锯齿   

	}
	int radius;
	int cy;
	private  int dotColor=Color.parseColor("#FF8400");
	
	@Override
	protected void onDraw(Canvas canvas) {
		int left=0;
		int top=0;
		int right=0;
		int bottom = 0;
		int width = getWidth();
		int height = getHeight();
		int diameter = height*9/10;
		cy= (height - diameter)/2;
		radius=cy/4;
		//宽度大于高度
		//高度为直径画圆 择top=0 bottom=top+diameter
		//left=(width-height)/2 right=left+diameter
		if(oval==null){
			left=(width-diameter)/2;
			top = (height-diameter)/2;
			right=left+diameter;
			bottom=top+diameter;
			oval = new RectF(left, top, right, bottom);
		}

		if(canvas!=null){
		//	LogUtils.i("开始画圆：===========："+getWidth()+":"+getHeight());
			paint.setColor(Color.parseColor("#ffffff"));
			paint.setStyle(Style.STROKE);
			canvas.drawArc(oval,180, 84, false, paint);
			canvas.drawArc(oval,-84, 80, false, paint);
			//paint.setColor(Color.parseColor(dotColor));
			paint.setColor(dotColor);
			paint.setStyle(Style.FILL);
			canvas.drawCircle(getWidth()/2, cy, radius, paint);
		}

		super.onDraw(canvas);
	}

	public void setDotColor(int color){
		this .dotColor=color;
		//谁最后设置 选定的颜色就是谁
		if(context instanceof RGBMainActivity){
			((RGBMainActivity)context).setSelectedColor(color);
		}
		
		postInvalidate();
	}
	public int getDotColor(){
		return dotColor;
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		invalidate();
	}
}
