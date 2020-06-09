package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Stats{

	@SerializedName("plays")
	private int plays;

	public int getPlays(){
		return plays;
	}
}