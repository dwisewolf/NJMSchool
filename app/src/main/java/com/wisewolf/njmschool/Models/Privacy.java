package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Privacy{

	@SerializedName("add")
	private boolean add;

	@SerializedName("view")
	private String view;

	@SerializedName("download")
	private boolean download;

	@SerializedName("comments")
	private String comments;

	@SerializedName("embed")
	private String embed;

	public boolean isAdd(){
		return add;
	}

	public String getView(){
		return view;
	}

	public boolean isDownload(){
		return download;
	}

	public String getComments(){
		return comments;
	}

	public String getEmbed(){
		return embed;
	}
}