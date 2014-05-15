package Helper;

import android.util.Log;

import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.sunrin.shiritori.GameFragment;
import com.sunrin.shiritori.MainActivity;
import com.sunrin.shiritori.User;

public class MessageReceivedListener implements RealTimeMessageReceivedListener {
	
	final String TAG = "MessageReceived";
	
	MainActivity main = null;
	GameFragment frag_game = null;
	User player, enemy;
	Util util = null;
	public MessageReceivedListener(MainActivity main) {
		this.main = main;
		this.frag_game = ((GameFragment)main.frag_game);
		this.util = Util.getInstace();
	}
	
	@Override
	public void onRealTimeMessageReceived(RealTimeMessage rtm) {
		byte flag = rtm.getMessageData()[0];
		if(player == null) {
			player = frag_game.player;
			enemy  = frag_game.enemy;
		}
		
		Log.e(TAG, "" + new String(rtm.getMessageData()));
		String message = new String(rtm.getMessageData()).substring(1);
		
		switch(flag) {
		case 'I':
			enemy.setName(message);
			while(!frag_game.isAdded());
			main.sendData("Get Data");
			break;
		case 'G':
			Log.e("player Name", player.getName());
			player.getTv_name().setText(player.getName());
			enemy.getTv_name().setText(enemy.getName());
			for(Participant p : main.mParticipants) {
				if(p.getParticipantId().equals(player.getId()))
					util.setImageFromParticipant(main, p, player.getIv_profile());
				else if(p.getParticipantId().equals(enemy.getId()))
					util.setImageFromParticipant(main, p, enemy.getIv_profile());
			}
			break;
			
		case 'F':
			frag_game.tv_pan.setText(message);
			break;
			
		case 'O':
			frag_game.tv_pan.setText(message);
			player.setTurn(true);
			enemy.setTurn(false);
			enemy.setScore(enemy.getScore() + frag_game.time);
			frag_game.setColor();
			frag_game.mCountDown.cancel();
			frag_game. mCountDown.start();
			break;
		}
	}
}
