package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Success{

	@SerializedName("total")
	private int total;

	public int getTotal(){
		return total;
	}
}