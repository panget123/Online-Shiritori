package com.sunrin.shiritori;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class User {
	private TextView name;
	private ImageView profile;
	public User(View v, int name_id, int profile_id) {
		name = (TextView)v.findViewById(name_id);
		profile = (ImageView)v.findViewById(profile_id);
	}
	public TextView getName() {
		return name;
	}
	public void setName(TextView name) {
		this.name = name;
	}
	public ImageView getProfile() {
		return profile;
	}
	public void setProfile(ImageView profile) {
		this.profile = profile;
	}
}
