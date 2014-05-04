package Helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.multiplayer.Participant;
import com.sunrin.shiritori.R;

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
	
	public void setImageFromParticipant(Context c, Participant p, ImageView iv) {
		ImageManager im = ImageManager.create(c);    
		im.loadImage(iv, p.getIconImageUri(), R.drawable.ic_launcher);
		try{
			Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
			bitmap = addBorder(bitmap, 10);
			iv.setBackground(new BitmapDrawable(c.getResources(), bitmap));
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
	}

	private Bitmap addBorder(Bitmap bmp, int borderSize) {
		Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
		Canvas canvas = new Canvas(bmpWithBorder);
		canvas.drawBitmap(bmp, borderSize, borderSize, null);
		return bmpWithBorder;
	}
	
	public void removeElement(byte[] bs, int del) {
	    System.arraycopy(bs,del+1,bs,del,bs.length-1-del);
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
