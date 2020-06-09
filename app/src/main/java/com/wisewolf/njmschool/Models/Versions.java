package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Versions{

	@SerializedName("total")
	private int total;

	@SerializedName("resource_key")
	private String resourceKey;

	@SerializedName("options")
	private List<String> options;

	@SerializedName("uri")
	private String uri;

	@SerializedName("current_uri")
	private String currentUri;

	public int getTotal(){
		return total;
	}

	public String getResourceKey(){
		return resourceKey;
	}

	public List<String> getOptions(){
		return options;
	}

	public String getUri(){
		return uri;
	}

	public String getCurrentUri(){
		return currentUri;
	}
}