package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Credits{

	@SerializedName("total")
	private int total;

	@SerializedName("options")
	private List<String> options;

	@SerializedName("uri")
	private String uri;

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