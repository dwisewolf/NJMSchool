package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Logos{

	@SerializedName("vimeo")
	private boolean vimeo;

	@SerializedName("custom")
	private Custom custom;

	public boolean isVimeo(){
		return vimeo;
	}

	public Custom getCustom(){
		return custom;
	}
}