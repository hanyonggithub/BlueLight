package com.vui.bluelight.rgb;

import com.vui.bluelight.R;
import com.vui.bluelight.ble.BleUtils;
import com.vui.bluelight.customview.ColorPickerView;
import com.vui.bluelight.customview.ColorPickerView.OnGetColorListener;
import com.vui.bluelight.utils.DataFormatUtils;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RGBPieFragment extends Fragment{
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view==null){
			view = inflater.inflate(R.layout.aty_rgb_ring, null);
			initView(view);
		}
		return view;
	}

	private void initView(View view) {
		final View view_colorpreview = view.findViewById(R.id.view_colorpreview);
		ColorPickerView iv_roll_cir = (ColorPickerView) view.findViewById(R.id.iv_roll_cir);
		View move_view =  view.findViewById(R.id.move_view);
		iv_roll_cir.setMoveView(move_view);
		iv_roll_cir.setOnGetColorListener(new OnGetColorListener() {		
			@Override
			public void onGetColor(int color) {
				view_colorpreview.setBackgroundColor(color);
				
//				((RGBMainActivity)getActivity()).setSelectedColor(color);
				String deal="a3".concat(DataFormatUtils.toHexStr(Color.red(color))).concat(DataFormatUtils.toHexStr(Color.green(color))).concat(DataFormatUtils.toHexStr(Color.blue(color))).concat("ff").concat("ffffff");
				BleUtils.getInstance().write(deal);
			}
		});
	}
}