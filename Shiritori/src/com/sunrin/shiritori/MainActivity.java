package com.sunrin.shiritori;

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

import Helper.Util;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.os.Build;

public class MainActivity extends BaseGameActivity implements 
RoomStatusUpdateListener, RealTimeMessageReceivedListener, RoomUpdateListener {

	final String TAG = "Log";
	String mRoomId = null;

	// The participants in the currently active game
	ArrayList<Participant> mParticipants = null;

	// My participant ID in the currently active game
	String mMyId = null;
	String mMyName = null;
	Util util = Util.getInstace();

	MainFragment frag_main;
	SplashFragment frag_splash; 
	GameFragment frag_game;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		frag_main = new MainFragment();
		frag_splash = new SplashFragment();
		frag_game = new GameFragment();

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, frag_splash).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void mOnClick(View v) {
		switch(v.getId()) {
		case R.id.btn_quick_match:
			startQuickGame();
			break;
		case R.id.btn_send:
			sendMessage();
			break;
		}
	}

	void sendMessage() {
		String Message = ((GameFragment) frag_game).et_message.getText().toString();
		if(Message.length() < 0)
			return;

		frag_game.et_message.setText("");
		Message = mMyName + " : " + Message;
		// Send to every other participant.
		for (Participant p : mParticipants) {
			if (p.getParticipantId().equals(mMyId))
				continue;
			if (p.getStatus() != Participant.STATUS_JOINED)
				continue;

			Games.RealTimeMultiplayer.sendUnreliableMessage(getApiClient(), ('M' + Message).getBytes(), mRoomId,
					p.getParticipantId());
		}

		frag_game.changeList(Message);
	}

	void sendData(String data) {
		for (Participant p : mParticipants) {
			if (p.getParticipantId().equals(mMyId))
				continue;
			if (p.getStatus() != Participant.STATUS_JOINED)
				continue;

			Games.RealTimeMultiplayer.sendUnreliableMessage(getApiClient(), data.getBytes(), mRoomId,
					p.getParticipantId());
		}
	}

	@Override
	public void onRealTimeMessageReceived(RealTimeMessage rtm) {
		String Message = new String(rtm.getMessageData()).substring(1);
		byte flag = rtm.getMessageData()[0];
		Log.e(TAG, Message);
		
		if(flag == 'N') {
			for(int i=0; i<frag_game.user.length; i++) {
				if(frag_game.user[i].getId().equals(rtm.getSenderParticipantId()))
					frag_game.user[i].getName().setText(Message);
			}
		}
		
		else {
			frag_game.changeList(Message);
		}
	}

	void startQuickGame() {
		final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 3;
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
				MAX_OPPONENTS, 0);
		RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
		rtmConfigBuilder.setMessageReceivedListener(this);
		rtmConfigBuilder.setRoomStatusUpdateListener(this);
		rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
		Games.RealTimeMultiplayer.create(getApiClient(), rtmConfigBuilder.build());
	}

	// Leave the room.
	void leaveRoom() {
		Log.d(TAG, "Leaving room.");
		Games.RealTimeMultiplayer.leave(getApiClient(), this, mRoomId);
		mRoomId = null;
		getSupportFragmentManager().popBackStack();
	}

	void showWaitingRoom(Room room) {
		// minimum number of players required for our game
		// For simplicity, we require everyone to join the game before we start it
		// (this is signaled by Integer.MAX_VALUE).
		final int MIN_PLAYERS = Integer.MAX_VALUE;
		Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(getApiClient(), room, MIN_PLAYERS);

		// show waiting room UI
		startActivityForResult(i, 1000);
	}

	@Override
	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);
		// we got the result from the "waiting room" UI.
		if (responseCode == Activity.RESULT_OK) {
			// ready to start playing
			util.fragmentReplace(frag_game, this, true);
			getSupportFragmentManager().executePendingTransactions();

			for (int i=0; i<mParticipants.size(); i++) 
				frag_game.user[i].setId(mParticipants.get(i).getParticipantId()); // Set Users ID
			
			
			for (int i=0; i<mParticipants.size(); i++) { 
				// Trade Users Name
				if (mParticipants.get(i).getParticipantId().equals(mMyId)) {
					frag_game.user[i].getName().setText(mMyName);
					continue;
				}
				if (mParticipants.get(i).getStatus() != Participant.STATUS_JOINED)
					continue;
				
				
				Games.RealTimeMultiplayer.sendUnreliableMessage(getApiClient(), ('N' + mMyName).getBytes(), mRoomId,
						mParticipants.get(i).getParticipantId());
			}
			Log.d(TAG, "Starting game (waiting room returned OK).");
		} else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
			// player indicated that they want to leave the room
			leaveRoom();
			Log.e(TAG, "RESULT_LEFT_ROOM");
		} else if (responseCode == Activity.RESULT_CANCELED) {
			// Dialog was cancelled (user pressed back key, for instance). In our game,
			// this means leaving the room too. In more elaborate games, this could mean
			// something else (like minimizing the waiting room UI).
			Log.e(TAG, "RESULT_CANCLED");
			if(requestCode == 1000) {
				mRoomId = null;
			} else {
				leaveRoom();
			}
		}
		
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

	@Override
	public void onJoinedRoom(int arg0, Room arg1) {
		Log.e(TAG, "onJoinedRoom");
	}

	@Override
	public void onLeftRoom(int arg0, String arg1) {
		Log.e(TAG, "onLeftRoom");
	}

	@Override
	public void onRoomConnected(int arg0, Room arg1) {
		Log.e(TAG, "onRoomConnected");
	}

	@Override
	public void onRoomCreated(int arg0, Room room) {
		Log.e(TAG, "onRoomCreated");
		showWaitingRoom(room);
	}

	@Override
	public void onConnectedToRoom(Room room) {
		Log.d(TAG, "onConnectedToRoom.");

		// get room ID, participants and my ID:
		mRoomId = room.getRoomId();
		mParticipants = room.getParticipants();
		mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(getApiClient()));
		mMyName = room.getParticipant(mMyId).getDisplayName();

		// print out the list of participants (for debug purposes)
		Log.d(TAG, "Room ID: " + mRoomId);
		Log.d(TAG, "My ID " + mMyId);
		Log.d(TAG, "MY NAME " + mMyName);
		Log.d(TAG, "<< CONNECTED TO ROOM>>");
	}

	@Override
	public void onDisconnectedFromRoom(Room arg0) {
		Log.e(TAG, "onDisconnectedFromRoom");
		leaveRoom();
	}

	@Override
	public void onP2PConnected(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onP2PDisconnected(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPeerDeclined(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPeerInvitedToRoom(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPeerJoined(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPeerLeft(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPeersConnected(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPeersDisconnected(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomAutoMatching(Room arg0) {
		Log.e(TAG, "onRoomAutoMatching");
	}

	@Override
	public void onRoomConnecting(Room arg0) {
		Log.e(TAG, "onRoomConnecting");
	}
}
