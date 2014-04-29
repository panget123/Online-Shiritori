package Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.sunrin.shiritori.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Util {
	private Util() {
	}

	private static Util util = new Util();
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
		transaction.commitAllowingStateLoss();
	}

	private Bitmap addBorder(Bitmap bmp, int borderSize, int color) {
		Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
		Canvas canvas = new Canvas(bmpWithBorder);
		canvas.drawColor(color);
		canvas.drawBitmap(bmp, borderSize, borderSize, null);
		return bmpWithBorder;
	}

	public void AlphabeticallySort(ArrayList<Participant> list) {
		Comparator<Participant> sort = new Comparator<Participant>() {
			public int compare(Participant o1, Participant o2) {
				return o2.getParticipantId().compareTo(o1.getParticipantId());
			}
		};
		Collections.sort(list, sort);
	}

	public void showSoftInput(EditText et, Context c) {
		InputMethodManager imm = (InputMethodManager)c.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
	}

	public void hideSoftInput(EditText et, Context c) {
		InputMethodManager imm = (InputMethodManager)c.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(et.getWindowToken(),0);
	}
}
