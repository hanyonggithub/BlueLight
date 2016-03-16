package com.vui.bluelight.timer;

import com.likebamboo.adapter.SwipeAdapter;
import com.likebamboo.widget.SwipeListView;
import com.vui.bluelight.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

public class TimerFragment extends Fragment{

	private SwipeListView mListView;
	private Activity activity;
    

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		View view = inflater.inflate(R.layout.frag_timer, null);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initView(view);
	}
	

    private void initView(View view) {
        mListView = (SwipeListView)view.findViewById(R.id.listview);
        SwipeAdapter adapter = new SwipeAdapter(activity, mListView.getRightViewWidth(),
                new SwipeAdapter.IOnItemRightClickListener() {
                    @Override
                    public void onRightClick(View v, int position) {
                        // TODO Auto-generated method stub
                        Toast.makeText(activity, "right onclick " + position,
                                Toast.LENGTH_SHORT).show();
                    }
                });
       /* mListView.addHeaderView(LayoutInflater.from(activity).inflate(R.layout.list_footer, null));
        mListView.addHeaderView(LayoutInflater.from(activity).inflate(R.layout.list_footer, null));
        mListView.addHeaderView(LayoutInflater.from(activity).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(activity).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(activity).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(activity).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(activity).inflate(R.layout.list_footer, null));
        mListView.addFooterView(LayoutInflater.from(activity).inflate(R.layout.list_footer, null));*/
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(activity, "item onclick " + position, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
