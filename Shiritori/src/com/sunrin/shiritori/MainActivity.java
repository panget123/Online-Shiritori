package com.sunrin.shiritori;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import Helper.HttpManager;
import Helper.MessageReceivedListener;
import Helper.StatusUpdateListener;
import Helper.URLS;
import Helper.Util;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity {

	final String TAG = "MainActivity";

	public ArrayList<Participant> mParticipants = null;
	MessageReceivedListener mMsgReceivedListener = null;
	StatusUpdateListener mStatusUpdateListener = null;
	
	public User user = null;
	Util util = Util.getInstace();

	MainFragment frag_main;
	SplashFragment frag_splash; 
	public GameFragment frag_game;

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
		user = new User();
		
		frag_main 	= new MainFragment();
		frag_splash = new SplashFragment();
		frag_game 	= new GameFragment();
		
		mStatusUpdateListener = new StatusUpdateListener(this);
		mMsgReceivedListener = new MessageReceivedListener(this);
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
	
	public void sendData(String data) {
		for (Participant p : mParticipants) {
			if (p.getParticipantId().equals(user.getId()))
				continue;
			if (p.getStatus() != Participant.STATUS_JOINED)
				continue;

			Games.RealTimeMultiplayer.sendUnreliableMessage(getApiClient(), data.getBytes(), user.getRoom_id(),
					p.getParticipantId());
		}
	}

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

	public void leaveRoom() {
		Log.d(TAG, "Leaving room.");
		if(user.getRoom_id() == null)
			return;
		
		Games.RealTimeMultiplayer.leave(getApiClient(), mStatusUpdateListener, user.getRoom_id());
		user.clear();
		frag_game.enemy.clear();
		getSupportFragmentManager().popBackStack();
		util.hideSoftInput(frag_game.et_message, MainActivity.this);
	}

	public void showWaitingRoom(Room room) {
		final int MIN_PLAYERS = Integer.MAX_VALUE;
		Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(getApiClient(), room, MIN_PLAYERS);

		startActivityForResult(i, 1000);
	}

	@Override
	public void onSignInFailed() {
		Log.e(TAG, "SignIn Failed");
	}

	@Override
	public void onSignInSucceeded() {
		Log.d(TAG, "SignIn Successed");
		util.fragmentReplace(frag_main, this, false);
	}
	
	public Handler GameInitializing = new Handler() 
	{
		public void handleMessage(android.os.Message msg) 
		{
			util.AlphabeticallySort(mParticipants);
			for(int i=0; i<mParticipants.size(); i++) // set Enemy Participant Data & set Turn
			{
				if(mParticipants.get(i).getParticipantId().equals(user.getId())) 
				{
					boolean turn = (i == 0) ? true : false;
					user.setTurn(turn);
					frag_game.enemy.setTurn(!turn);
				}
			}

			util.fragmentReplace(frag_game, MainActivity.this, true);
			getSupportFragmentManager().executePendingTransactions();
			sendData("I" + user.getName());
		};
	};
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(getSupportFragmentManager().findFragmentById(R.id.container).getId() == frag_game.getId())
			leaveRoom();
		else
			finish();
	}
}
