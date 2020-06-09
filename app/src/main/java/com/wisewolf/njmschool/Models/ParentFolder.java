package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class ParentFolder{

	@SerializedName("created_time")
	private String createdTime;

	@SerializedName("metadata")
	private Metadata metadata;

	@SerializedName("modified_time")
	private String modifiedTime;

	@SerializedName("resource_key")
	private String resourceKey;

	@SerializedName("name")
	private String name;

	@SerializedName("privacy")
	private Privacy privacy;

	@SerializedName("last_user_action_event_date")
	private String lastUserActionEventDate;

	@SerializedName("uri")
	private String uri;

	@SerializedName("user")
	private User user;

	public String getCreatedTime(){
		return createdTime;
	}

	public Metadata getMetadata(){
		return metadata;
	}

	public String getModifiedTime(){
		return modifiedTime;
	}

	public String getResourceKey(){
		return resourceKey;
	}

	public String getName(){
		return name;
	}

	public Privacy getPrivacy(){
		return privacy;
	}

	public String getLastUserActionEventDate(){
		return lastUserActionEventDate;
	}

	public String getUri(){
		return uri;
	}

	public User getUser(){
		return user;
	}
}