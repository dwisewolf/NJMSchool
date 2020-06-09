package com.wisewolf.njmschool.Models;

import com.google.gson.annotations.SerializedName;

public class LocationDetails{

	@SerializedName("country")
	private Object country;

	@SerializedName("formatted_address")
	private String formattedAddress;

	@SerializedName("sub_locality")
	private Object subLocality;

	@SerializedName("country_iso_code")
	private Object countryIsoCode;

	@SerializedName("city")
	private Object city;

	@SerializedName("latitude")
	private Object latitude;

	@SerializedName("state")
	private Object state;

	@SerializedName("neighborhood")
	private Object neighborhood;

	@SerializedName("state_iso_code")
	private Object stateIsoCode;

	@SerializedName("longitude")
	private Object longitude;

	public Object getCountry(){
		return country;
	}

	public String getFormattedAddress(){
		return formattedAddress;
	}

	public Object getSubLocality(){
		return subLocality;
	}

	public Object getCountryIsoCode(){
		return countryIsoCode;
	}

	public Object getCity(){
		return city;
	}

	public Object getLatitude(){
		return latitude;
	}

	public Object getState(){
		return state;
	}

	public Object getNeighborhood(){
		return neighborhood;
	}

	public Object getStateIsoCode(){
		return stateIsoCode;
	}

	public Object getLongitude(){
		return longitude;
	}
}