package com.vui.bluelight.mod;

import com.vui.bluelight.R;
import com.vui.bluelight.mod.ShakeListenerUtils.OnShakedListener;
import com.vui.bluelight.utils.GsonUtils;
import com.vui.bluelight.utils.LogUtils;

import android.app.Fragment;
import android.app.Service;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ModeShakingFragment extends Fragment implements OnClickListener{
	private GridView grirdView;
	int gridViewHeight;
	private ShakeListenerUtils shakeUtils;
	private SensorManager mSensorManager;
	private ModeShakingEntity modeShakingEntity;
	private BaseAdapter adapter;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.frag_mode_shaking, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		modeShakingEntity = GsonUtils.getGson(getActivity(), "data_json/data_mode.json", ModeShakingEntity.class);
		initSetGridView(view);
		setShakdUpdateUI();
		initOnOff(view);
	}
	
	
	
	/**
	 * 注意默认为true
	 */
	
	private void initOnOff(View root) {
		if(shakeUtils!=null){
			shakeUtils.setEnabled(true);
		}
		CheckBox cb_onoff = (CheckBox) root.findViewById(R.id.cb_onoff);
		cb_onoff.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {		
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					buttonView.setText("ON");
					shakeUtils.setEnabled(true);
				}else{
					buttonView.setText("OFF");
					shakeUtils.setEnabled(false);
					final int size = modeShakingEntity.items.size();
					for (int i = 0; i < size; i++) {
						modeShakingEntity.items.get(i).isShaked=false;

					}					
					adapter.notifyDataSetChanged();
				}

			}
		});
	}

	
	private void setShakdUpdateUI() {
		final int size = modeShakingEntity.items.size();
		shakeUtils = new ShakeListenerUtils(getActivity(), size, new OnShakedListener() {	
			@Override
			public void OnShaked(int generalNumber) {				
				LogUtils.i("llpp: ===摇到的值是："+generalNumber+" 数据集的大小是："+size);
				if(generalNumber<size&&generalNumber>=0){
					for (int i = 0; i < size; i++) {
						if(generalNumber==i){	
							modeShakingEntity.items.get(i).isShaked=true;
						}else{
							modeShakingEntity.items.get(i).isShaked=false;
						}
					}					
					adapter.notifyDataSetChanged();
				}else{
					LogUtils.e("llpp: ===摇到的值是："+generalNumber+" 数据集的大小是："+size);
				}

			}
		}) ;
	}

	private void initSetGridView(View root) {
		grirdView = (GridView) root.findViewById(R.id.grirv_mode);
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

	private void initGridView() {
		adapter = new MyAdapter(modeShakingEntity);
		grirdView.setAdapter(adapter);


	}
	
	

	@Override
	public void onResume()
	{
		super.onResume();

		//获取传感器管理服务 
		mSensorManager = (SensorManager) getActivity()
				.getSystemService(Service.SENSOR_SERVICE);
		//加速度传感器  
		mSensorManager.registerListener(shakeUtils,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				//还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，  
				//根据不同应用，需要的反应速率不同，具体根据实际情况设定  
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mSensorManager.unregisterListener(shakeUtils);
		
	}


	class MyAdapter extends BaseAdapter implements ListAdapter {
		ModeShakingEntity modeShakingEntity;
		public MyAdapter(ModeShakingEntity modeShakingEntity ){
			this.modeShakingEntity=modeShakingEntity;
		}
		@Override
		public int getCount() {	
			return modeShakingEntity.items.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			LinearLayout llt = new LinearLayout(getActivity());
			llt.setOrientation(LinearLayout.VERTICAL);
			int itemHeight=gridViewHeight/4;
			AbsListView.LayoutParams layoutParams = 
					new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,itemHeight);

			int tv_height=itemHeight/3;
			LinearLayout.LayoutParams layoutParams_tv = 
					new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,tv_height);
			TextView textView = new TextView(getActivity());
			textView.setText(modeShakingEntity.items.get(position).text);
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(Color.parseColor("#ffffff"));
			textView.setLayoutParams(layoutParams_tv);

			final int tvcolorHeight=itemHeight-tv_height;
			LinearLayout.LayoutParams layoutParams_color = 
					new LinearLayout.LayoutParams(tvcolorHeight,tvcolorHeight);
			layoutParams_color.gravity=Gravity.CENTER;
			Shape s =new Shape() {	
				@Override
				public void draw(Canvas canvas, Paint paint) {			
					//LogUtils.i("color:"+modeShakingEntity.items.get(position).color);
					try {
						paint.setColor(Color.parseColor(modeShakingEntity.items.get(position).color));
					} catch (Exception e) {
						paint.setColor(Color.parseColor("#ffffff"));
						LogUtils.e("llpp:"+"设置颜色出现异常");
						e.printStackTrace();
					}
					canvas.drawCircle(tvcolorHeight/2, tvcolorHeight/2, tvcolorHeight/2*0.8f, paint);
				}
			};
			Drawable drawable=new ShapeDrawable(s);
			TextView tv_color = new TextView(getActivity());
			tv_color.setLayoutParams(layoutParams_color);
			tv_color.setBackgroundDrawable(drawable);
			if(modeShakingEntity.items.get(position).isShaked){
				LogUtils.i("llpp: 摇到数据：开始缩放===========");
				tv_color.setScaleX(1.15f);
				tv_color.setScaleY(1.15f);
				textView.setTextSize(14);
				llt.setBackgroundColor(Color.parseColor("#66C1C1C1"));
			}

			llt.addView(tv_color);
			llt.addView(textView);
			llt.setLayoutParams(layoutParams);
			return llt;
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			getFragmentManager().popBackStack();
			break;
		case R.id.right_btn:
			getFragmentManager().popBackStack();
			break;

		default:
			break;
		}
	}

	
}
