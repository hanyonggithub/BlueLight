package com.vui.bluelight.customview;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vui.bluelight.R;
import com.vui.bluelight.utils.LogUtils;

/**
 * Created by Administrator on 2016/3/22.
 */
@SuppressLint("ClickableViewAccessibility")
public class RotateView extends FrameLayout implements OnTouchListener {
	public RotateView(Context context) {
		super(context);
		init(context);
	}

	public RotateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RotateView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}
	Context context;
	float AX;
	float AY;
	float CX;
	float CY;
	float BX;
	float BY;
	private void init (Context context){
		LogUtils.i("llpp:--init---==============: ");
		this.setOnTouchListener(this);
		this.context=context;
	}

	float lastDegree=5;
	private double rotation;
	private ImageView timer_ring_color;
	private TimerHalfRingView cus_view_halring;
	@Override
	public boolean onTouch(View view, MotionEvent event) {	
		if(CX==0){
			CX = this.getWidth()/2;
			CY = this.getHeight()/2;
		}
		switch (event.getAction()){
		case MotionEvent.ACTION_DOWN:
			AX= event.getX();
			AY= event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			float BX = event.getX();
			float BY = event.getY();

			///**
			//计算AB的长度
			double c=getdistance(AX,AY,BX,BY);
			//计算AC 的长度 		
			double b = getdistance(AX,AY,CX,CY);
			//计算 BC 的长度
			double a= getdistance(BX,BY,CX,CY);
			//计算C的余弦值
			double cosa= getCosValue(a,b,c);
			//非常小
			double al =  Math.acos(cosa);
			//判断方向
			boolean direction=false;
			direction = geDirection(BX, BY);
			updateUI(al, direction);
			AX = BX;
			AY = BY;
		}
		return true;
	}

	/**
	 * 
	 * @param radian 本次要改变的角度，会被放大50-100倍 只能为正值
	 * @param direction 改变的方向 true为顺时针方向
	 */
	public void updateUI(double radian, boolean direction) {
		if(direction){
			rotation+=radian*50*2; //radianToDegrees(radian);//radian*180/Math.PI*2;
		
		}else{
			rotation-= radian*50*1;;//radianToDegrees(radian);//
		}
	
		float  rotatedDegrees=(float) rotation%360;
		LogUtils.i("llpp:方向："+direction+" 角度："+rotatedDegrees);
		//顺时针方向为正值，是在原先值的基础上进行加或者减来进行旋转、
		timer_ring_color.setRotation(rotatedDegrees);
		int colorFromColorRing = getColorFromColorRing(timer_ring_color,rotatedDegrees);
		cus_view_halring.setDotColor(colorFromColorRing);
	}

	private boolean geDirection(float BX, float BY) {
		if(Math.abs(BX-AX)>Math.abs(BY-AY)){ //如果横向移动大于纵向移动
			if(BX>AX&&BY<=CY){// 在上半部分 向右滑动 顺时针：BX>AX 
				//LogUtils.i("上半部分 向右滑动 ");
				return true;
			}else if(BX<AX&&BY<=CY){
				return false;
			}


			else if(BX<AX&&BY>=CY) { //在下半部分 向左滑动 顺时针： BX<AX 
			//	LogUtils.i("下半部分 向左滑动 ");
				return	true;
			}else if(BX>AX&&BY>=CY){
				return	false;
			}
		}else{

			if(BX<=CX&&BY<AY){ //如果左半部分 顺时针 向上
				return true;
			}else if(BX<=CX&&BY>AY){
				return false;
			}

			//右半部分 
			if(BX>=CX&&BY>AY){
				return true;
			}else if(BX>=CX&&BY<AY){
				return false;
			}	
		}


		return true;
	}

	public void setColorFollowChanceView(TimerHalfRingView view,ImageView timer_ring_color){
		cus_view_halring=view;
		this.timer_ring_color=timer_ring_color;
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
	/**
	 * 获取最后一个参数对应角的余弦值
	 * a,b,c 为三边长度
	 * 
	 * @return
	 */
	private double getCosValue(double a,double b,double c){
		return (Math.pow(a, 2)+Math.pow(b, 2)-Math.pow(c, 2))/(2*a*b);

	}


	private Point getRotatedPoint(Point center,double radius,float radian){
		//degrees=degrees;		
		int x=(int) (center.x+radius*Math.sin(radianToDegrees(radian)));
		int y=(int) (center.y+radius*Math.cos(radianToDegrees(radian)));
		//LogUtils.i("llpp:旋转"+radianToDegrees(radian)+"度的坐标是："+x+":"+y+"中心点坐标："+center.x+":"+center.y+" 半径："+(int)radius);
		return new Point(x,y);
	}
	
	/**
	 * 弧度转换为角度
	 * @param radian
	 * @return
	 */
	private double radianToDegrees(double radian){
		return (2*Math.PI/360)*radian;//0.017*radian
	}
	
	/**
	 * 从彩盘获取颜色
	 * @param x
	 * @param y
	 * @return
	 */
	private int getColorFromColorRing(ImageView timer_ring_color,float degrees) {
		BitmapDrawable background = (BitmapDrawable) timer_ring_color.getBackground();
		Bitmap bitmap = background.getBitmap();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Point center = new Point(width/2,height/2);
		double radius = width/2-width/50;
		Point rotatedPoint = getRotatedPoint(center, radius, degrees+180);
		int pixel = bitmap.getPixel(rotatedPoint.x, rotatedPoint.y);
	/*	int alpha = Color.alpha(pixel);
		int red = Color.red(pixel);
		int green = Color.green(pixel);
		int blue = Color.blue(pixel);
		int argb = Color.argb(alpha, red, green, blue);*/
		return pixel;
	}

}
