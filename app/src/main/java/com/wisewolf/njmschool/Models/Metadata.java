package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class Metadata{

	@SerializedName("connections")
	private Connections connections;

	@SerializedName("interactions")
	private Interactions interactions;

	public Connections getConnections(){
		return connections;
	}

	public Interactions getInteractions(){
		return interactions;
	}
}