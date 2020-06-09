package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Interactions{

	@SerializedName("watchlater")
	private Watchlater watchlater;

	@SerializedName("report")
	private Report report;

	public Watchlater getWatchlater(){
		return watchlater;
	}

	public Report getReport(){
		return report;
	}
}