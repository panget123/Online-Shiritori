package Helper;

import java.util.List;

import android.util.Log;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.sunrin.shiritori.MainActivity;

public class StatusUpdateListener implements RoomUpdateListener, RoomStatusUpdateListener{

	final String TAG = "StatusUpdateListener";
	MainActivity main;
	
	public StatusUpdateListener(MainActivity main) {
		this.main = main;
	}

	@Override
	public void onDisconnectedFromRoom(Room arg0) {
		Log.e(TAG, "onDisconnectedFromRoom");
		main.leaveRoom();
	}

	@Override
	public void onP2PConnected(String arg0) {
	}

	@Override
	public void onP2PDisconnected(String arg0) {
	}

	@Override
	public void onPeerDeclined(Room arg0, List<String> arg1) {
	}

	@Override
	public void onPeerInvitedToRoom(Room arg0, List<String> arg1) {
	}

	@Override
	public void onPeerJoined(Room arg0, List<String> arg1) {
	}

	@Override
	public void onPeerLeft(Room arg0, List<String> arg1) {
	}

	@Override
	public void onPeersConnected(Room arg0, List<String> arg1) {
	}

	@Override
	public void onPeersDisconnected(Room arg0, List<String> arg1) {
	}

	@Override
	public void onRoomAutoMatching(Room arg0) {
		Log.e(TAG, "onRoomAutoMatching");
	}

	@Override
	
	public void onRoomConnecting(Room room) {
		Log.e(TAG, "onRoomConnecting");
		
		if(room == null)
			return;
		
		// get room ID, participants and my ID:
		main.user.setRoom_id(room.getRoomId());
		main.mParticipants = room.getParticipants();
		main.user.setId(room.getParticipantId(Games.Players.getCurrentPlayerId(main.getApiClient())));
		main.user.setName(room.getParticipant(main.user.getId()).getDisplayName());

		// print out the list of participants (for debug purposes)
		Log.d(TAG, "Room ID: " + main.user.getRoom_id());
		Log.d(TAG, "My ID " + main.user.getId());
		Log.d(TAG, "MY NAME " + main.user.getName());
		Log.d(TAG, "<< CONNECTED TO ROOM>>");
	}

	@Override
	public void onJoinedRoom(int arg0, Room arg1) {
		Log.e(TAG, "onJoinedRoom");
	}

	@Override
	public void onLeftRoom(int arg0, String arg1) {
		Log.e(TAG, "onLeftRoom");
		main.leaveRoom();
	}

	@Override
	public void onRoomConnected(int arg0, Room arg1) {
		Log.e(TAG, "onRoomConnected");
	}

	@Override
	public void onRoomCreated(int arg0, Room room) {
		if(room == null)
			return;
		Log.e(TAG, "onRoomCreated");
		
		main.showWaitingRoom(room);
	}

	@Override
	public void onConnectedToRoom(Room room) {
		Log.d(TAG, "onConnectedToRoom.");
		
		main.GameInitializing.sendEmptyMessage(0);
	}
}
