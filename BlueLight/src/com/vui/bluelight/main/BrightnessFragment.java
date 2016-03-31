package com.vui.bluelight.main;

import com.vui.bluelight.R;
import com.vui.bluelight.ble.BleUtils;
import com.vui.bluelight.utils.DataFormatUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BrightnessFragment extends Fragment implements OnClickListener {
	private RelativeLayout rlt_brightness_bg;
	private SeekBar sbr_light;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.frag_brightness, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}

		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		rlt_brightness_bg=(RelativeLayout) view.findViewById(R.id.rlt_brightness_bg);
		rlt_brightness_bg.setOnClickListener(this);
		sbr_light=(SeekBar) view.findViewById(R.id.sbr_light);
		sbr_light.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int w=progress*255/1000;
				String order="a3000000".concat(DataFormatUtils.toHexStr(w)).concat("ffffff55");
				BleUtils.getInstance().write(BleUtils.CHAR_UUID, order);
			}
		});
	}

	@Override
	public void onClick(View v) {
		getFragmentManager().popBackStack();
	}

	

}
