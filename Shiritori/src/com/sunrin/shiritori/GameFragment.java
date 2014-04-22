package com.sunrin.shiritori;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class GameFragment extends Fragment{
	EditText et_message;
	TextView tv_pan;
	User[] user = new User[2];

	public GameFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_game, container,
				false);
		Log.e("GameFrag", "OncreateView");
		
		et_message = (EditText)rootView.findViewById(R.id.et_send);
		tv_pan = (TextView)rootView.findViewById(R.id.word_pan);
		user[0] = new User(rootView, R.id.name1, R.id.user1);
		user[1] = new User(rootView, R.id.name2, R.id.user2);
		
		return rootView;
	}
	
	public void setName(int index, String name) {
		user[index].getName().setText(name);
	}
	
	public void setColor(boolean mMyTurn, String mMyId) {
		for(int i=0; i<user.length; i++) {
			if(mMyTurn && user[i].getId().equals(mMyId)) // 내 턴이면서 내 아이디면 이 위치를 붉게
				user[i].getProfile().setBackgroundColor(Color.RED);
			else if(!mMyTurn && !user[i].getId().equals(mMyId)) // 내 턴 아니고 이게 내 아이디도 아니면 이 위치를 붉게
				user[i].getProfile().setBackgroundColor(Color.RED);
			else // 나머진 하얗게
				user[i].getProfile().setBackgroundColor(Color.WHITE);
		}
	}
}
