package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Embed{

	@SerializedName("volume")
	private boolean volume;

	@SerializedName("badges")
	private Badges badges;

	@SerializedName("buttons")
	private Buttons buttons;

	@SerializedName("color")
	private String color;

	@SerializedName("html")
	private String html;

	@SerializedName("playbar")
	private boolean playbar;

	@SerializedName("title")
	private Title title;

	@SerializedName("logos")
	private Logos logos;

	@SerializedName("uri")
	private String uri;

	@SerializedName("speed")
	private boolean speed;

	public boolean isVolume(){
		return volume;
	}

	public Badges getBadges(){
		return badges;
	}

	public Buttons getButtons(){
		return buttons;
	}

	public String getColor(){
		return color;
	}

	public String getHtml(){
		return html;
	}

	public boolean isPlaybar(){
		return playbar;
	}

	public Title getTitle(){
		return title;
	}

	public Logos getLogos(){
		return logos;
	}

	public String getUri(){
		return uri;
	}

	public boolean isSpeed(){
		return speed;
	}
}