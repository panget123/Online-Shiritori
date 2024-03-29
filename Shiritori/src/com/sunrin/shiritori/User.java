package com.sunrin.shiritori;

import android.widget.ImageView;
import android.widget.TextView;

public class User {
	private String name;
	private String id;
	private boolean turn;
	private String room_id;
	private TextView tv_name, tv_score;
	private ImageView iv_profile;
	private int score;
	
	public boolean isTurn() {
		return turn;
	}
	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	
	public String getRoom_id() {
		return room_id;
	}
	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	public TextView getTv_name() {
		return tv_name;
	}
	public void setTv_name(TextView tv_name) {
		this.tv_name = tv_name;
	}
	public ImageView getIv_profile() {
		return iv_profile;
	}
	public void setIv_profile(ImageView iv_profile) {
		this.iv_profile = iv_profile;
	}
	
	public void clear() {
		setName(null);
		setId(null);
		setRoom_id(null);
		setTurn(false);
		setTv_name(null);
		setIv_profile(null);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		if(score >= 0)
			this.score = score;
		else
			this.score = 0;
		
		tv_score.setText("" + getScore());
	}
	public TextView getTv_score() {
		return tv_score;
	}
	public void setTv_score(TextView tv_score) {
		this.tv_score = tv_score;
	}
}
