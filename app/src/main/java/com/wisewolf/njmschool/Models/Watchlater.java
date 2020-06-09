package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Watchlater{

	@SerializedName("total")
	private int total;

	@SerializedName("options")
	private List<String> options;

	@SerializedName("uri")
	private String uri;

	@SerializedName("added_time")
	private Object addedTime;

	@SerializedName("added")
	private boolean added;

	public int getTotal(){
		return total;
	}

	public List<String> getOptions(){
		return options;
	}

	public String getUri(){
		return uri;
	}

	public Object getAddedTime(){
		return addedTime;
	}

	public boolean isAdded(){
		return added;
	}
}