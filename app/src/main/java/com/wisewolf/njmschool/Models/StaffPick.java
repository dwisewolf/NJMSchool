package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class StaffPick{

	@SerializedName("normal")
	private boolean normal;

	@SerializedName("best_of_the_month")
	private boolean bestOfTheMonth;

	@SerializedName("best_of_the_year")
	private boolean bestOfTheYear;

	@SerializedName("premiere")
	private boolean premiere;

	public boolean isNormal(){
		return normal;
	}

	public boolean isBestOfTheMonth(){
		return bestOfTheMonth;
	}

	public boolean isBestOfTheYear(){
		return bestOfTheYear;
	}

	public boolean isPremiere(){
		return premiere;
	}
}