package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Transcode{

	@SerializedName("status")
	private String status;

	public String getStatus(){
		return status;
	}
}