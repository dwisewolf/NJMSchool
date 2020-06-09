package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Title{

	@SerializedName("owner")
	private String owner;

	@SerializedName("name")
	private String name;

	@SerializedName("portrait")
	private String portrait;

	public String getOwner(){
		return owner;
	}

	public String getName(){
		return name;
	}

	public String getPortrait(){
		return portrait;
	}
}