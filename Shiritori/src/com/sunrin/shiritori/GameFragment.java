package com.sunrin.shiritori;

import Helper.Util;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GameFragment extends Fragment{
	EditText et_message;
	TextView tv_pan;
	public User enemy, player;
	Util util;
	
	public GameFragment() {
		enemy  = new User();
		util = Util.getInstace();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		player = ((MainActivity)getActivity()).user;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_game, container,
				false);
		Log.e("GameFrag", "OncreateView");
		
		
		if(player.isTurn()) {
			player.setTv_name((TextView)rootView.findViewById(R.id.name1));
			player.setIv_profile((ImageView)rootView.findViewById(R.id.user1));
			enemy.setTv_name((TextView)rootView.findViewById(R.id.name2));
			enemy.setIv_profile((ImageView)rootView.findViewById(R.id.user2));
		} else {
			player.setTv_name((TextView)rootView.findViewById(R.id.name2));
			player.setIv_profile((ImageView)rootView.findViewById(R.id.user2));
			enemy.setTv_name((TextView)rootView.findViewById(R.id.name1));
			enemy.setIv_profile((ImageView)rootView.findViewById(R.id.user1));
		}
		
		et_message = (EditText)rootView.findViewById(R.id.et_send);
		tv_pan = (TextView)rootView.findViewById(R.id.word_pan);
		
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		util.showSoftInput(et_message, getActivity());
	}
	
//	public void setName(int index, String name) {
//		user[index].getName().setText(name);
//	}
//	
//	public void setColor(boolean mMyTurn, String mMyId) {
//		for(int i=0; i<user.length; i++) {
//			if(mMyTurn && user[i].getId().equals(mMyId)) // 내 턴이면서 내 아이디면 이 위치를 붉게
//				user[i].getProfile().setBackgroundColor(Color.RED);
//			else if(!mMyTurn && !user[i].getId().equals(mMyId)) // 내 턴 아니고 이게 내 아이디도 아니면 이 위치를 붉게
//				user[i].getProfile().setBackgroundColor(Color.RED);
//			else // 나머진 하얗게
//				user[i].getProfile().setBackgroundColor(Color.WHITE);
//		}
//	}
}
