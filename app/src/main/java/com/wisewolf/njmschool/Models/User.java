package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("created_time")
	private String createdTime;

	@SerializedName("metadata")
	private Metadata metadata;

	@SerializedName("preferences")
	private Preferences preferences;

	@SerializedName("gender")
	private String gender;

	@SerializedName("can_work_remotely")
	private boolean canWorkRemotely;

	@SerializedName("short_bio")
	private Object shortBio;

	@SerializedName("link")
	private String link;

	@SerializedName("available_for_hire")
	private boolean availableForHire;

	@SerializedName("bio")
	private Object bio;

	@SerializedName("uri")
	private String uri;

	@SerializedName("pictures")
	private Pictures pictures;

	@SerializedName("skills")
	private List<Object> skills;

	@SerializedName("location_details")
	private LocationDetails locationDetails;

	@SerializedName("content_filter")
	private List<String> contentFilter;

	@SerializedName("resource_key")
	private String resourceKey;

	@SerializedName("name")
	private String name;

	@SerializedName("location")
	private String location;

	@SerializedName("websites")
	private List<Object> websites;

	@SerializedName("account")
	private String account;

	public String getCreatedTime(){
		return createdTime;
	}

	public Metadata getMetadata(){
		return metadata;
	}

	public Preferences getPreferences(){
		return preferences;
	}

	public String getGender(){
		return gender;
	}

	public boolean isCanWorkRemotely(){
		return canWorkRemotely;
	}

	public Object getShortBio(){
		return shortBio;
	}

	public String getLink(){
		return link;
	}

	public boolean isAvailableForHire(){
		return availableForHire;
	}

	public Object getBio(){
		return bio;
	}

	public String getUri(){
		return uri;
	}

	public Pictures getPictures(){
		return pictures;
	}

	public List<Object> getSkills(){
		return skills;
	}

	public LocationDetails getLocationDetails(){
		return locationDetails;
	}

	public List<String> getContentFilter(){
		return contentFilter;
	}

	public String getResourceKey(){
		return resourceKey;
	}

	public String getName(){
		return name;
	}

	public String getLocation(){
		return location;
	}

	public List<Object> getWebsites(){
		return websites;
	}

	public String getAccount(){
		return account;
	}
}