package com.bridgelabz.location.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "locationMumbai3")
public class Loc {

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

	private float lat;

	@Column(name = "lng")
	private float lon;

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

	@Override
	public boolean equals(Object obj) {
		Loc location = (Loc) obj;
		if ((location.getLat() == this.lat) && location.getLon() == this.lon) {
			return true;
		}
		return false;
	}

	public void copyFromLocationDetails(Loc location, LocationDetails locationDetails) {
		// this.locationId = location.getLocationId();
		this.city = location.getCity();
		this.place = locationDetails.getName();
		this.country = location.getCountry();
		// this.zip = location.getZip();
		this.state = location.getState();
		this.stateid = location.getStateid();
		this.cityid = location.getCityid();
		this.lat = (float) locationDetails.getLocation().getLat();
		this.lon = (float) locationDetails.getLocation().getLon();
		this.area = locationDetails.getAddress();
	}
}