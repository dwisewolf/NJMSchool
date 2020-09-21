package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NoticeMod{

	@SerializedName("image")
	private String image;

	@SerializedName("school")
	private String school;

	@SerializedName("classes")
	private List<String> classes;

	@SerializedName("id")
	private String id;

	public String getImage(){
		return image;
	}

	public String getSchool(){
		return school;
	}

	public List<String> getClasses(){
		return classes;
	}

	public String getId(){
		return id;
	}
}