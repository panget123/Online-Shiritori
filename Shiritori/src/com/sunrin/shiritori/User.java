package com.sunrin.shiritori;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class User {
	private String id;
	private String name;
	private boolean trun;
	private String room_id;
	private String creator_id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isTrun() {
		return trun;
	}
	public void setTrun(boolean trun) {
		this.trun = trun;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRoom_id() {
		return room_id;
	}
	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	public String getCreator_id() {
		return creator_id;
	}
	public void setCreator_id(String creator_id) {
		this.creator_id = creator_id;
	}
	
	public boolean isCreator() {
		return getName().equals(getCreator_id());
	}
}
