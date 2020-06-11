package com.wisewolf.njmschool.Models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("metadata")
	private Metadata metadata;

	@SerializedName("upload")
	private Upload upload;

	@SerializedName("link")
	private String link;

	@SerializedName("description")
	private String description;

	@SerializedName("privacy")
	private Privacy privacy;

	@SerializedName("language")
	private Object language;

	@SerializedName("type")
	private String type;

	@SerializedName("pictures")
	private Pictures pictures;

	@SerializedName("duration")
	private int duration;

	@SerializedName("download")
	private List<DownloadItem> download;

	@SerializedName("modified_time")
	private String modifiedTime;

	@SerializedName("parent_folder")
	private ParentFolder parentFolder;

	@SerializedName("stats")
	private Stats stats;

	@SerializedName("content_rating")
	private List<String> contentRating;

	@SerializedName("review_page")
	private ReviewPage reviewPage;

	@SerializedName("embed")
	private Embed embed;

	@SerializedName("categories")
	private List<Object> categories;

	@SerializedName("height")
	private int height;

	@SerializedName("release_time")
	private String releaseTime;

	@SerializedName("app")
	private App app;

	@SerializedName("created_time")
	private String createdTime;

	@SerializedName("transcode")
	private Transcode transcode;

	@SerializedName("last_user_action_event_date")
	private String lastUserActionEventDate;

	@SerializedName("uri")
	private String uri;

	@SerializedName("tags")
	private List<Object> tags;

	@SerializedName("license")
	private Object license;

	@SerializedName("resource_key")
	private String resourceKey;

	@SerializedName("name")
	private String name;

	@SerializedName("width")
	private int width;

	@SerializedName("files")
	private List<FilesItem> files;

	@SerializedName("user")
	private User user;

	@SerializedName("status")
	private String status;

	public Metadata getMetadata(){
		return metadata;
	}

	public Upload getUpload(){
		return upload;
	}

	public String getLink(){
		return link;
	}

	public String getDescription(){
		return description;
	}

	public Privacy getPrivacy(){
		return privacy;
	}

	public Object getLanguage(){
		return language;
	}

	public String getType(){
		return type;
	}

	public Pictures getPictures(){
		return pictures;
	}

	public int getDuration(){
		return duration;
	}

	public List<DownloadItem> getDownload(){
		return download;
	}

	public String getModifiedTime(){
		return modifiedTime;
	}

	public ParentFolder getParentFolder(){
		return parentFolder;
	}

	public Stats getStats(){
		return stats;
	}

	public List<String> getContentRating(){
		return contentRating;
	}

	public ReviewPage getReviewPage(){
		return reviewPage;
	}

	public Embed getEmbed(){
		return embed;
	}

	public List<Object> getCategories(){
		return categories;
	}

	public int getHeight(){
		return height;
	}

	public String getReleaseTime(){
		return releaseTime;
	}

	public App getApp(){
		return app;
	}

	public String getCreatedTime(){
		return createdTime;
	}

	public Transcode getTranscode(){
		return transcode;
	}

	public String getLastUserActionEventDate(){
		return lastUserActionEventDate;
	}

	public String getUri(){
		return uri;
	}

	public List<Object> getTags(){
		return tags;
	}

	public Object getLicense(){
		return license;
	}

	public String getResourceKey(){
		return resourceKey;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrivacy(Privacy privacy) {
		this.privacy = privacy;
	}

	public void setLanguage(Object language) {
		this.language = language;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPictures(Pictures pictures) {
		this.pictures = pictures;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setDownload(List<DownloadItem> download) {
		this.download = download;
	}

	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public void setParentFolder(ParentFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public void setContentRating(List<String> contentRating) {
		this.contentRating = contentRating;
	}

	public void setReviewPage(ReviewPage reviewPage) {
		this.reviewPage = reviewPage;
	}

	public void setEmbed(Embed embed) {
		this.embed = embed;
	}

	public void setCategories(List<Object> categories) {
		this.categories = categories;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public void setTranscode(Transcode transcode) {
		this.transcode = transcode;
	}

	public void setLastUserActionEventDate(String lastUserActionEventDate) {
		this.lastUserActionEventDate = lastUserActionEventDate;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setTags(List<Object> tags) {
		this.tags = tags;
	}

	public void setLicense(Object license) {
		this.license = license;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setFiles(List<FilesItem> files) {
		this.files = files;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName(){
		return name;
	}

	public int getWidth(){
		return width;
	}

	public List<FilesItem> getFiles(){
		return files;
	}

	public User getUser(){
		return user;
	}

	public String getStatus(){
		return status;
	}
}