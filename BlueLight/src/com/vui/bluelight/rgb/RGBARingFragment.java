package com.vui.bluelight.rgb;

import java.util.ArrayList;

import com.vui.bluelight.R;
import com.vui.bluelight.customview.WheelView;
import com.vui.bluelight.utils.LogUtils;
import com.vui.bluelight.utils.ScreenUtils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RGBARingFragment extends Fragment{
	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	  		Bundle savedInstanceState) {
	  	
	  	return inflater.inflate(R.layout.aty_rgb_ring, null);
	  }
	    
	    @Override
	  public void onViewCreated(View view, Bundle savedInstanceState) {
	  	super.onViewCreated(view, savedInstanceState);
	 
	 
	  	
	  }
}
