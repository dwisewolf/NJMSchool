package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Connections{

	@SerializedName("shared")
	private Shared shared;

	@SerializedName("albums")
	private Albums albums;

	@SerializedName("moderated_channels")
	private ModeratedChannels moderatedChannels;

	@SerializedName("folders")
	private Folders folders;

	@SerializedName("watchlater")
	private Watchlater watchlater;

	@SerializedName("portfolios")
	private Portfolios portfolios;

	@SerializedName("groups")
	private Groups groups;

	@SerializedName("videos")
	private Videos videos;

	@SerializedName("membership")
	private Membership membership;

	@SerializedName("pictures")
	private Pictures pictures;

	@SerializedName("appearances")
	private Appearances appearances;

	@SerializedName("feed")
	private Feed feed;

	@SerializedName("watched_videos")
	private WatchedVideos watchedVideos;

	@SerializedName("followers")
	private Followers followers;

	@SerializedName("channels")
	private Channels channels;

	@SerializedName("following")
	private Following following;

	@SerializedName("block")
	private Block block;

	@SerializedName("categories")
	private Categories categories;

	@SerializedName("likes")
	private Likes likes;

	@SerializedName("items")
	private Items items;

	@SerializedName("comments")
	private Comments comments;

	@SerializedName("related")
	private Related related;

	@SerializedName("credits")
	private Credits credits;

	@SerializedName("versions")
	private Versions versions;

	@SerializedName("texttracks")
	private Texttracks texttracks;

	@SerializedName("available_albums")
	private AvailableAlbums availableAlbums;

	@SerializedName("recommendations")
	private Recommendations recommendations;

	public Shared getShared(){
		return shared;
	}

	public Albums getAlbums(){
		return albums;
	}

	public ModeratedChannels getModeratedChannels(){
		return moderatedChannels;
	}

	public Folders getFolders(){
		return folders;
	}

	public Watchlater getWatchlater(){
		return watchlater;
	}

	public Portfolios getPortfolios(){
		return portfolios;
	}

	public Groups getGroups(){
		return groups;
	}

	public Videos getVideos(){
		return videos;
	}

	public Membership getMembership(){
		return membership;
	}

	public Pictures getPictures(){
		return pictures;
	}

	public Appearances getAppearances(){
		return appearances;
	}

	public Feed getFeed(){
		return feed;
	}

	public WatchedVideos getWatchedVideos(){
		return watchedVideos;
	}

	public Followers getFollowers(){
		return followers;
	}

	public Channels getChannels(){
		return channels;
	}

	public Following getFollowing(){
		return following;
	}

	public Block getBlock(){
		return block;
	}

	public Categories getCategories(){
		return categories;
	}

	public Likes getLikes(){
		return likes;
	}

	public Items getItems(){
		return items;
	}

	public Comments getComments(){
		return comments;
	}

	public Related getRelated(){
		return related;
	}

	public Credits getCredits(){
		return credits;
	}

	public Versions getVersions(){
		return versions;
	}

	public Texttracks getTexttracks(){
		return texttracks;
	}

	public AvailableAlbums getAvailableAlbums(){
		return availableAlbums;
	}

	public Recommendations getRecommendations(){
		return recommendations;
	}
}