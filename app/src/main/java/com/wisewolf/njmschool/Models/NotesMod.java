package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class NotesMod{

	@SerializedName("school")
	private String school;

	@SerializedName("clas")
	private String clas;

	@SerializedName("subject")
	private String subject;

	@SerializedName("pdf_upload")
	private String pdfUpload;

	@SerializedName("description")
	private String description;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	public String getSchool(){
		return school;
	}

	public String getClas(){
		return clas;
	}

	public String getSubject(){
		return subject;
	}

	public String getPdfUpload(){
		return pdfUpload;
	}

	public String getDescription(){
		return description;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}
}