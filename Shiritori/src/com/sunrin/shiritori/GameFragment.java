package com.sunrin.shiritori;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import Helper.HttpManager;
import Helper.URLS;
import Helper.Util;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GameFragment extends Fragment implements OnClickListener {
	EditText et_message;
	public TextView tv_pan, tv_time;
	public User enemy, player;
	MainActivity main = null;
	Util util;
	int time;
	public final static int SCORE = 20;
	public CountDownTimer mCountDown = null;

	public GameFragment() {
		enemy  = new User();
		util = Util.getInstace();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		main = (MainActivity)getActivity();
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
			player.setTv_score((TextView)rootView.findViewById(R.id.tv_score1));
			enemy.setTv_name((TextView)rootView.findViewById(R.id.name2));
			enemy.setIv_profile((ImageView)rootView.findViewById(R.id.user2));
			enemy.setTv_score((TextView)rootView.findViewById(R.id.tv_score2));
		} else {
			player.setTv_name((TextView)rootView.findViewById(R.id.name2));
			player.setIv_profile((ImageView)rootView.findViewById(R.id.user2));
			player.setTv_score((TextView)rootView.findViewById(R.id.tv_score2));
			enemy.setTv_name((TextView)rootView.findViewById(R.id.name1));
			enemy.setIv_profile((ImageView)rootView.findViewById(R.id.user1));
			enemy.setTv_score((TextView)rootView.findViewById(R.id.tv_score1));
		}
		
		et_message = (EditText)rootView.findViewById(R.id.et_send);
		tv_pan = (TextView)rootView.findViewById(R.id.word_pan);
		tv_time = (TextView)rootView.findViewById(R.id.tv_time);
		
		Button btn_send = (Button)rootView.findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		time = 20;
		
		mCountDown = new CountDownTimer(time * 1000, 1 * 1000) {
			
			@Override
			public void onTick(long time) {
				tv_time.setText("남은시간 : " + (time / 1000) + "초");
			}
			
			@Override
			public void onFinish() {
				mCountDown.start();
				
				if(player.isTurn()) 
					new getWordTask().execute();
				
			}
		};
		
		mCountDown.start();

		if(player.isTurn())
			new getWordTask().execute();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_send:
			String text = et_message.getText().toString();
			et_message.setText("");
			if(player.isTurn() && text.length() == 3)
				new isWordTask().execute(text);
				break;
		}
	}

	public class getWordTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			return new HttpManager().GET(URLS.get_word);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			tv_pan.setText(result);
			main.sendData('F' + result);
		}
	}

	public class isWordTask extends AsyncTask<String, String, Boolean> {

		String word = null;

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				word = URLEncoder.encode(arg0[0], "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}

			return Boolean.valueOf(new HttpManager().GET(URLS.is_kung + "?word=" + word));
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Log.e("is_kung", ""+result);
			try {
				word = URLDecoder.decode(word, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return;
			}
			
			if(result) {
				main.sendData('O' + word);
				tv_pan.setText(word);
				player.setTurn(false);
				player.setScore(player.getScore() + SCORE);
				player.getTv_score().setText("" + player.getScore());
				enemy.setTurn(true);
				mCountDown.cancel();
				mCountDown.start();
			}
		}
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
