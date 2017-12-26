package com.bridgelabz.location.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "locationMumbai3")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "locationId")
	private long locationId;

	private String country;

	private int zip;

	@Column(name = "location")
	private String place;

	private String state;

	private int stateid;

	private String city;

	private int cityid;

	private String area;

	@JsonProperty(access = Access.WRITE_ONLY)
	private float lat;

	@JsonProperty(access = Access.WRITE_ONLY)
	private float lng;

	@Transient
	private LatLng latLng = new LatLng();

	public Location() {
		System.out.println("Called");
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getZip() {
		return zip;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getStateid() {
		return stateid;
	}

	public void setStateid(int stateid) {
		this.stateid = stateid;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCityid() {
		return cityid;
	}

	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public float getLat() {
		System.out.println("Getter called lat");
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
		this.latLng.setLat(lat);
		System.out.println("Setter called lat");
	}

	public float getLng() {
		System.out.println("Getter called lng");
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
		this.latLng.setLon(lng);
		System.out.println("Setter called lat");
	}

	public LatLng getLatLng() {
		System.out.println("Getter called latlon");
		latLng.setLat(lat);
		latLng.setLon(lng);
		return latLng;
	}


	public void setLatLng(LatLng latLng) {
		this.lat = latLng.getLat();
		this.lng = latLng.getLon();
		this.latLng = latLng;
	}

	/*
	 * @Override public boolean equals(Object obj) { Location location = (Location)
	 * obj; if ((location.getLat() == this.lat) && latLng.getLon() == this.lng) {
	 * return true; } return false; }
	 */

	public void copyFromLocationDetails(Location location, LocationDetails locationDetails) {
		// this.locationId = location.getLocationId();
		this.city = location.getCity();
		this.place = locationDetails.getName();
		this.country = location.getCountry();
		// this.zip = location.getZip();
		this.state = location.getState();
		this.stateid = location.getStateid();
		this.cityid = location.getCityid();
		this.lat = (float) locationDetails.getLocation().getLat();
		this.lng = (float) locationDetails.getLocation().getLon();
		this.area = locationDetails.getAddress();
	}
}