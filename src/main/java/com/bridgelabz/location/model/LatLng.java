package com.bridgelabz.location.model;

public class LatLng {

	float lat;

	float lon;
	
	public LatLng() {
		
	}

	public LatLng(float lat, float lng) {
		this.lat = lat;
		this.lon = lng;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

}
