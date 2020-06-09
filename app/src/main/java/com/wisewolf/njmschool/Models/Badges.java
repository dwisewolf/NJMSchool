package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Badges{

	@SerializedName("weekend_challenge")
	private boolean weekendChallenge;

	@SerializedName("hdr")
	private boolean hdr;

	@SerializedName("vod")
	private boolean vod;

	@SerializedName("staff_pick")
	private StaffPick staffPick;

	@SerializedName("live")
	private Live live;

	public boolean isWeekendChallenge(){
		return weekendChallenge;
	}

	public boolean isHdr(){
		return hdr;
	}

	public boolean isVod(){
		return vod;
	}

	public StaffPick getStaffPick(){
		return staffPick;
	}

	public Live getLive(){
		return live;
	}
}