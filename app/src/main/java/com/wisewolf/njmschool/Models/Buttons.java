package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Buttons{

	@SerializedName("scaling")
	private boolean scaling;

	@SerializedName("fullscreen")
	private boolean fullscreen;

	@SerializedName("like")
	private boolean like;

	@SerializedName("watchlater")
	private boolean watchlater;

	@SerializedName("share")
	private boolean share;

	@SerializedName("embed")
	private boolean embed;

	@SerializedName("hd")
	private boolean hd;

	public boolean isScaling(){
		return scaling;
	}

	public boolean isFullscreen(){
		return fullscreen;
	}

	public boolean isLike(){
		return like;
	}

	public boolean isWatchlater(){
		return watchlater;
	}

	public boolean isShare(){
		return share;
	}

	public boolean isEmbed(){
		return embed;
	}

	public boolean isHd(){
		return hd;
	}
}