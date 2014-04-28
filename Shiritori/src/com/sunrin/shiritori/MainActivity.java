package com.sunrin.shiritori;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.BaseGameActivity;

import Helper.HttpManager;
import Helper.MessageReceivedListener;
import Helper.StatusUpdateListener;
import Helper.URLS;
import Helper.Util;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends BaseGameActivity {

	final String TAG = "MainActivity";

	public ArrayList<Participant> mParticipants = null;
	MessageReceivedListener mMsgReceivedListener = null;
	StatusUpdateListener mStatusUpdateListener = null;
	
	public User user = null;
	Util util = Util.getInstace();

	MainFragment frag_main;
	SplashFragment frag_splash; 
	GameFragment frag_game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializing();
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, frag_splash).commit();
		}
	}
	
	public void initializing() {
		frag_main 	= new MainFragment();
		frag_splash = new SplashFragment();
		frag_game 	= new GameFragment();
		
		user = new User();
		
		mMsgReceivedListener = new MessageReceivedListener();
		mStatusUpdateListener = new StatusUpdateListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

//	public void sendMessage() {
//		String Message = frag_game.et_message.getText().toString();
//		if(Message.length() < 1)
//			return;
//		if(mMyTurn && Message.length() == 3) {
//			new isWordTask().execute(Message);
//		} else {
//			//frag_game.changeList(mMyName + " : " + Message);
//			Message = 'M' + mMyName + " : " + Message;
//			sendData(Message);
//		}
//
//		frag_game.et_message.setText("");
//	}
//
//	public void sendData(String data) {
//		for (Participant p : mParticipants) {
//			if (p.getParticipantId().equals(mMyId))
//				continue;
//			if (p.getStatus() != Participant.STATUS_JOINED)
//				continue;
//
//			Games.RealTimeMultiplayer.sendUnreliableMessage(getApiClient(), data.getBytes(), mRoomId,
//					p.getParticipantId());
//		}
//	}

	public void startQuickGame() {
		final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 3;
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
				MAX_OPPONENTS, 0);
		RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(mStatusUpdateListener);
		rtmConfigBuilder.setMessageReceivedListener(mMsgReceivedListener);
		rtmConfigBuilder.setRoomStatusUpdateListener(mStatusUpdateListener);
		rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
		Games.RealTimeMultiplayer.create(getApiClient(), rtmConfigBuilder.build());
	}

//	public void startGame() {
//		if(mMyId.equals(frag_game.user[0].getId())) {
//			new getWordTask().execute();
//			mMyTurn = true;
//		}
//
//		frag_game.setColor(mMyTurn, mMyId);
//		Log.e("myTurn", "" + mMyTurn);
//	}

	public void leaveRoom() {
		Log.d(TAG, "Leaving room.");
		Games.RealTimeMultiplayer.leave(getApiClient(), mStatusUpdateListener, user.getRoom_id());
		user.setRoom_id(null);
		getSupportFragmentManager().popBackStack();
		util.hideSoftInput(frag_game.et_message, MainActivity.this);
	}

	public void showWaitingRoom(Room room) {
		final int MIN_PLAYERS = Integer.MAX_VALUE;
		Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(getApiClient(), room, MIN_PLAYERS);

		startActivityForResult(i, 1000);
	}

//	@Override
//	public void onActivityResult(int requestCode, int responseCode,
//			Intent intent) {
//		super.onActivityResult(requestCode, responseCode, intent);
//		// we got the result from the "waiting room" UI.
//		if (responseCode == Activity.RESULT_OK) {
//			Log.e(TAG, "OnActivityResult");
//			// ready to start playing
//			
//		} else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
//			// player indicated that they want to leave the room
//			leaveRoom();
//			Log.e(TAG, "RESULT_LEFT_ROOM");
//		} else if (responseCode == Activity.RESULT_CANCELED) {
//			// Dialog was cancelled (user pressed back key, for instance). In our game,
//			// this means leaving the room too. In more elaborate games, this could mean
//			// something else (like minimizing the waiting room UI).
//			Log.e(TAG, "RESULT_CANCLED");
//			if(requestCode == 1000) {
//				mRoomId = null;
//			} else {
//				leaveRoom();
//			}
//		}
//
//	}

	@Override
	public void onSignInFailed() {
		Log.e(TAG, "SignIn Failed");
	}

	@Override
	public void onSignInSucceeded() {
		Log.d(TAG, "SignIn Successed");
		util.fragmentReplace(frag_main, this, false);
	}
	
//	Handler exChangeInfo = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			util.fragmentReplace(frag_game, MainActivity.this, true);
//			getSupportFragmentManager().executePendingTransactions();
//			for (int i=0; i<mParticipants.size(); i++) {
//				frag_game.user[i].setId(mParticipants.get(i).getParticipantId()); // Set Users ID
//				Log.e("Set Id", "" + mParticipants.get(i).getDisplayName());
//			}
//			
//			for (int i=0; i<mParticipants.size(); i++) { 
//				// Exchange Users Name
//				if (mParticipants.get(i).getParticipantId().equals(mMyId)) {
//					frag_game.user[i].getName().setText(mMyName);
//					continue;
//				}
//				if (mParticipants.get(i).getStatus() != Participant.STATUS_JOINED)
//					continue;
//
//				Games.RealTimeMultiplayer.sendUnreliableMessage(getApiClient(), ('N' + mMyName).getBytes(), mRoomId,
//						mParticipants.get(i).getParticipantId());
//
//				util.showSoftInput(frag_game.et_message, MainActivity.this);
//			}
//			
//			Log.d(TAG, "Starting game (waiting room returned OK).");
//		};
//	};

//	public class getWordTask extends AsyncTask<String, String, String> {
//
//		@Override
//		protected String doInBackground(String... arg0) {
//			return new HttpManager().GET(URLS.get_word);
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			frag_game.tv_pan.setText(result);
//			sendData('F' + result);
//		}
//	}
//
//	public class isWordTask extends AsyncTask<String, String, Boolean> {
//		
//		String word = null;
//		
//		@Override
//		protected Boolean doInBackground(String... arg0) {
//			try {
//				word = URLEncoder.encode(arg0[0], "utf-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//				return false;
//			}
//			
//			return Boolean.valueOf(new HttpManager().GET(URLS.is_kung + "?word=" + word));
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//			super.onPostExecute(result);
//			Log.e("is_kung", ""+result);
//			try {
//				word = URLDecoder.decode(word, "utf-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return;
//			}
//			if(result) {
//				sendData('O' + word);
//				frag_game.tv_pan.setText(word);
//				mMyTurn = false;
//				frag_game.setColor(mMyTurn, mMyId);
//			}
//		}
//	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(getSupportFragmentManager().findFragmentById(R.id.container).getId() == frag_game.getId())
			leaveRoom();
		else
			finish();
	}
}
