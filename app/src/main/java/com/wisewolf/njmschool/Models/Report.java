package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Report{

	@SerializedName("reason")
	private List<String> reason;

	@SerializedName("options")
	private List<String> options;

	@SerializedName("uri")
	private String uri;

	public List<String> getReason(){
		return reason;
	}

	public List<String> getOptions(){
		return options;
	}

	public String getUri(){
		return uri;
	}
}