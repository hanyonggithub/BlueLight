package com.vui.bluelight.mod;

import com.vui.bluelight.R;
import com.vui.bluelight.base.view.TopBarView;
import com.vui.bluelight.utils.ScreenUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ModeScenceFragment extends Fragment{


	private GridView gvw_scence_mode;
	private int[] modes={R.drawable.mode_reading,R.drawable.mode_tv,R.drawable.mode_romantic,R.drawable.mode_party,R.drawable.mode_sleep,R.drawable.mode_wakeup};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_mode_scence, null);
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		return view;
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		gvw_scence_mode=(GridView) view.findViewById(R.id.gvw_scence_mode);
		gvw_scence_mode.setAdapter(adapter);
		gvw_scence_mode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), "选择了第"+position+"模式", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	

	private BaseAdapter adapter=new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if(convertView==null){
				convertView=new RelativeLayout(getActivity());
				LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,ScreenUtils.getScreenWidth(getActivity())/4);
				convertView.setLayoutParams(params);
				
				ImageView imageView=new ImageView(ModeScenceFragment.this.getActivity());
				RelativeLayout.LayoutParams params_image=new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(getActivity())/4,RelativeLayout.LayoutParams.MATCH_PARENT);
				params_image.addRule(RelativeLayout.CENTER_IN_PARENT);
				imageView.setLayoutParams(params_image);
				
				((ViewGroup)convertView).addView(imageView);
				holder=new Holder();
				holder.imageView=imageView;
				convertView.setTag(holder);
			}
			holder=((Holder)convertView.getTag());
			holder.imageView.setImageResource(modes[position]);
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return modes.length;
		}
	};
	
	static class Holder{
		ImageView imageView;
	}
}
