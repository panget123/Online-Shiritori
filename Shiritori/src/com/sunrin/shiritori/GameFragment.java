package com.sunrin.shiritori;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import Helper.HttpManager;
import Helper.URLS;
import Helper.Util;
import android.app.Activity;
import android.graphics.Color;
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
	public int time;
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
		
		setColor();

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		time = 20;

		mCountDown = new CountDownTimer(time * 1000, 1 * 1000) {

			@Override
			public void onTick(long timer) {
				tv_time.setText("남은시간 : " + (int) (timer / 1000) + "초");
				time = (int) (timer / 1000);
			}

			@Override
			public void onFinish() {
				mCountDown.start();

				if(player.isTurn()) {
					player.setScore(player.getScore() - SCORE);
					new getWordTask().execute();
				} else {
					enemy.setScore(enemy.getScore() - SCORE);
				}
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
				player.setScore(player.getScore() + time);
				enemy.setTurn(true);
				setColor();
				mCountDown.cancel();
				mCountDown.start();
			}
		}
	}

	public void setColor() {
		if(player.isTurn())
			player.getTv_name().setBackgroundColor(Color.RED);
		else
			player.getTv_name().setBackgroundColor(Color.WHITE);

		if(enemy.isTurn())
			enemy.getTv_name().setBackgroundColor(Color.RED);
		else
			enemy.getTv_name().setBackgroundColor(Color.WHITE);
	}
}
