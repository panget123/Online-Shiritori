package Helper;

import com.sunrin.shiritori.R;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class Util {
	private Util() {
	}
	
	static Util util = new Util();
	public static Util getInstace() {
		return util;
	}

	public void fragmentReplace(Fragment newFragment, FragmentActivity thisFragment, boolean isAddBackStack) {
		
        // replace fragment
        final FragmentTransaction transaction = thisFragment.getSupportFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
        if(isAddBackStack)
        	transaction.addToBackStack(null);
        
        transaction.replace(R.id.container, newFragment);
 
        // Commit the transaction
        transaction.commit();
 
    }
}
