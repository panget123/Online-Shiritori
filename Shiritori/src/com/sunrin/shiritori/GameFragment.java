package com.sunrin.shiritori;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class GameFragment extends Fragment{
	ArrayList<String> list;
	ArrayAdapter<String> adapter;
	ListView listview;
	EditText et_message;
	User[] user = new User[2];

	public GameFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_game, container,
				false);
		Log.e("GameFrag", "OncreateView");
		
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
		listview = (ListView)rootView.findViewById(R.id.message);
		et_message = (EditText)rootView.findViewById(R.id.et_send);
		user[0] = new User(rootView, R.id.name1, R.id.user1);
		user[1] = new User(rootView, R.id.name2, R.id.user2);
		
		listview.setAdapter(adapter);
		
		return rootView;
	}
	
	public void setName(int index, String name) {
		user[index].getName().setText(name);
	}
	
	public void changeList(String Message) {
		list.add(Message);
		adapter.notifyDataSetChanged();
		listview.setSelection(list.size());
	}
}
