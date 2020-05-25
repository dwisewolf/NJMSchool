package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class QuotesItem{

	@SerializedName("date")
	private String date;

	@SerializedName("quote")
	private String quote;

	@SerializedName("author")
	private String author;

	@SerializedName("background")
	private String background;

	@SerializedName("length")
	private String length;

	@SerializedName("language")
	private String language;

	@SerializedName("id")
	private String id;

	@SerializedName("category")
	private String category;

	@SerializedName("permalink")
	private String permalink;

	@SerializedName("title")
	private String title;

	@SerializedName("tags")
	private List<String> tags;

	public String getDate(){
		return date;
	}

	public String getQuote(){
		return quote;
	}

	public String getAuthor(){
		return author;
	}

	public String getBackground(){
		return background;
	}

	public String getLength(){
		return length;
	}

	public String getLanguage(){
		return language;
	}

	public String getId(){
		return id;
	}

	public String getCategory(){
		return category;
	}

	public String getPermalink(){
		return permalink;
	}

	public String getTitle(){
		return title;
	}

	public List<String> getTags(){
		return tags;
	}
}