package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Pictures{

	@SerializedName("sizes")
	private List<SizesItem> sizes;

	@SerializedName("resource_key")
	private String resourceKey;

	@SerializedName("active")
	private boolean active;

	@SerializedName("type")
	private String type;

	@SerializedName("uri")
	private String uri;

	@SerializedName("total")
	private int total;

	@SerializedName("options")
	private List<String> options;

	public List<SizesItem> getSizes(){
		return sizes;
	}

	public String getResourceKey(){
		return resourceKey;
	}

	public boolean isActive(){
		return active;
	}

	public String getType(){
		return type;
	}

	public String getUri(){
		return uri;
	}

	public int getTotal(){
		return total;
	}

	public List<String> getOptions(){
		return options;
	}
}