package Helper;

import android.util.Log;

import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.sunrin.shiritori.GameFragment;
import com.sunrin.shiritori.MainActivity;

public class MessageReceivedListener implements RealTimeMessageReceivedListener {
	
	final String TAG = "MessageReceived";
	
	MainActivity main = null;
	public MessageReceivedListener(MainActivity main) {
		this.main = main;
	}
	
	@Override
	public void onRealTimeMessageReceived(RealTimeMessage rtm) {
		byte flag = rtm.getMessageData()[0];
		Log.e(TAG, "" + new String(rtm.getMessageData()));
		String message = new String(rtm.getMessageData()).substring(1);
		switch(flag) {
		case 'I':
			((GameFragment)main.frag_game).enemy.setName(message);
			while(!((GameFragment)main.frag_game).isAdded());
			main.sendData("Get Data");
			break;
		case 'G':
			((GameFragment)main.frag_game).player.getTv_name().setText(((GameFragment)main.frag_game).player.getName());
			((GameFragment)main.frag_game).enemy.getTv_name().setText(((GameFragment)main.frag_game).enemy.getName());
			break;
		}
	}
}
