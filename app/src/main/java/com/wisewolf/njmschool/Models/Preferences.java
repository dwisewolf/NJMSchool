package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Preferences{

	@SerializedName("videos")
	private Videos videos;

	public Videos getVideos(){
		return videos;
	}
}