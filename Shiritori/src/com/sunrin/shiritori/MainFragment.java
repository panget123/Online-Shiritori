package com.sunrin.shiritori;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements OnClickListener{
	public MainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		Button btn_quick_match = (Button)rootView.findViewById(R.id.btn_quick_match);
		btn_quick_match.setOnClickListener(this);
		
		return rootView;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_quick_match:
			((MainActivity)getActivity()).startQuickGame();
			break;
		}
	}
}
