package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Live{

	@SerializedName("archived")
	private boolean archived;

	@SerializedName("streaming")
	private boolean streaming;

	public boolean isArchived(){
		return archived;
	}

	public boolean isStreaming(){
		return streaming;
	}
}