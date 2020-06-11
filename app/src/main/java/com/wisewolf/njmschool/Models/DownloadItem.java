package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DownloadItem{

	@SerializedName("created_time")
	private String createdTime;

	@SerializedName("expires")
	private Date expires;

	@SerializedName("size")
	private int size;

	@SerializedName("width")
	private int width;

	@SerializedName("link")
	private String link;

	@SerializedName("fps")
	private float fps;

	@SerializedName("type")
	private String type;

	@SerializedName("quality")
	private String quality;

	@SerializedName("height")
	private int height;

	@SerializedName("md5")
	private String md5;

	public String getCreatedTime(){
		return createdTime;
	}

	public Date getExpires(){
		return expires;
	}

	public int getSize(){
		return size;
	}

	public int getWidth(){
		return width;
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

	public int getHeight(){
		return height;
	}

	public String getMd5(){
		return md5;
	}
}