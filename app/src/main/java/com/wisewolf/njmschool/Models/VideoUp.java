package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class VideoUp{

	@SerializedName("video_link")
	private String videoLink;

	@SerializedName("thumbnail_link")
	private String thumbnailLink;

	@SerializedName("name")
	private String name;

	@SerializedName("played_time")
	private String playedTime;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private int id;

	@SerializedName("userid")
	private String userid;

	@SerializedName("timestamp")
	private String timestamp;

	public String getVideoLink(){
		return videoLink;
	}

	public String getThumbnailLink(){
		return thumbnailLink;
	}

	public String getName(){
		return name;
	}

	public String getPlayedTime(){
		return playedTime;
	}

	public String getDescription(){
		return description;
	}

	public int getId(){
		return id;
	}

	public String getUserid(){
		return userid;
	}

	public String getTimestamp(){
		return timestamp;
	}
}