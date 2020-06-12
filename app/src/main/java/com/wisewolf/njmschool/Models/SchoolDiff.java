package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class SchoolDiff{

	@SerializedName("fatherName")
	private String fatherName;

	@SerializedName("houseName")
	private Object houseName;

	@SerializedName("mobileNum")
	private long mobileNum;

	@SerializedName("address")
	private Object address;

	@SerializedName("clas")
	private String clas;

	@SerializedName("name")
	private String name;

	@SerializedName("section")
	private String section;

	@SerializedName("id")
	private int id;

	@SerializedName("category")
	private String category;

	@SerializedName("userid")
	private String userid;

	public String getFatherName(){
		return fatherName;
	}

	public Object getHouseName(){
		return houseName;
	}

	public long getMobileNum(){
		return mobileNum;
	}

	public Object getAddress(){
		return address;
	}

	public String getClas(){
		return clas;
	}

	public String getName(){
		return name;
	}

	public String getSection(){
		return section;
	}

	public int getId(){
		return id;
	}

	public String getCategory(){
		return category;
	}

	public String getUserid(){
		return userid;
	}
}