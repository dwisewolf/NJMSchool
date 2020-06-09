package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class SizesItem{

	@SerializedName("width")
	private int width;

	@SerializedName("link")
	private String link;

	@SerializedName("height")
	private int height;

	public int getWidth(){
		return width;
	}

	public String getLink(){
		return link;
	}

	public int getHeight(){
		return height;
	}
}