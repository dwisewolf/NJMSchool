package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class ReviewPage{

	@SerializedName("link")
	private String link;

	@SerializedName("active")
	private boolean active;

	public String getLink(){
		return link;
	}

	public boolean isActive(){
		return active;
	}
}