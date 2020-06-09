package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Custom{

	@SerializedName("link")
	private Object link;

	@SerializedName("sticky")
	private boolean sticky;

	@SerializedName("active")
	private boolean active;

	public Object getLink(){
		return link;
	}

	public boolean isSticky(){
		return sticky;
	}

	public boolean isActive(){
		return active;
	}
}