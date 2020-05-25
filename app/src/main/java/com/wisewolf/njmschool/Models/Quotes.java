package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Quotes{

	@SerializedName("copyright")
	private Copyright copyright;

	@SerializedName("baseurl")
	private String baseurl;

	@SerializedName("contents")
	private Contents contents;

	@SerializedName("success")
	private Success success;

	public Copyright getCopyright(){
		return copyright;
	}

	public String getBaseurl(){
		return baseurl;
	}

	public Contents getContents(){
		return contents;
	}

	public Success getSuccess(){
		return success;
	}
}