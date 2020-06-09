package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Upload{

	@SerializedName("complete_uri")
	private Object completeUri;

	@SerializedName("form")
	private Object form;

	@SerializedName("size")
	private Object size;

	@SerializedName("link")
	private Object link;

	@SerializedName("approach")
	private Object approach;

	@SerializedName("upload_link")
	private Object uploadLink;

	@SerializedName("redirect_url")
	private Object redirectUrl;

	@SerializedName("status")
	private String status;

	public Object getCompleteUri(){
		return completeUri;
	}

	public Object getForm(){
		return form;
	}

	public Object getSize(){
		return size;
	}

	public Object getLink(){
		return link;
	}

	public Object getApproach(){
		return approach;
	}

	public Object getUploadLink(){
		return uploadLink;
	}

	public Object getRedirectUrl(){
		return redirectUrl;
	}

	public String getStatus(){
		return status;
	}
}