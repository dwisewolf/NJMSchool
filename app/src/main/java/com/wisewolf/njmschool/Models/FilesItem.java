package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class FilesItem{

	@SerializedName("created_time")
	private String createdTime;

	@SerializedName("size")
	private int size;

	@SerializedName("link")
	private String link;

	@SerializedName("fps")
	private float fps;

	@SerializedName("type")
	private String type;

	@SerializedName("quality")
	private String quality;

	@SerializedName("md5")
	private String md5;

	@SerializedName("width")
	private int width;

	@SerializedName("height")
	private int height;

	public String getCreatedTime(){
		return createdTime;
	}

	public int getSize(){
		return size;
	}

	public String getLink(){
		return link;
	}

	public float getFps(){
		return fps;
	}

	public String getType(){
		return type;
	}

	public String getQuality(){
		return quality;
	}

	public String getMd5(){
		return md5;
	}

	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}
}