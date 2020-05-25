package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Contents{

	@SerializedName("quotes")
	private List<QuotesItem> quotes;

	public List<QuotesItem> getQuotes(){
		return quotes;
	}
}