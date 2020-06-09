package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Videos{

	@SerializedName("privacy")
	private Privacy privacy;

	@SerializedName("total")
	private int total;

	@SerializedName("options")
	private List<String> options;

	@SerializedName("uri")
	private String uri;

	public Privacy getPrivacy(){
		return privacy;
	}

	public int getTotal(){
		return total;
	}

	public List<String> getOptions(){
		return options;
	}

	public String getUri(){
		return uri;
	}
}