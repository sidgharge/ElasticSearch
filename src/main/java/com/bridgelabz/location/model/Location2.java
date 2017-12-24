/*package com.bridgelabz.location.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "locationMumbai3")
public class Location2 {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "locationId")
	private long locationId;

	private String country;

	private int zip;

	private String location;

	private String state;

	private int stateid;

	private String city;

	private int cityid;

	@Column(name = "unknown_area")
	private String unknownArea;

	private float lat;

	private float lng;

	
	 * @Column(name = "unknown_col") private String unknownCol;
	 

	public long getLocationId() {
		return locationId;
	}

	public String getCountry() {
		return country;
	}

	public int getZip() {
		return zip;
	}

	public String getLocation() {
		return location;
	}

	public String getState() {
		return state;
	}

	public int getStateid() {
		return stateid;
	}

	public String getCity() {
		return city;
	}

	public int getCityid() {
		return cityid;
	}

	public String getUnknownArea() {
		return unknownArea;
	}

	public float getLat() {
		return lat;
	}

	public float getLng() {
		return lng;
	}

	
	 * public String getUnknownCol() { return unknownCol; }
	 

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setZip(int zip) {
		this.zip = zip;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStateid(int stateid) {
		this.stateid = stateid;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCityid(int cityid) {
		this.cityid = cityid;
	}

	public void setUnknownArea(String unknownArea) {
		this.unknownArea = unknownArea;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	
	 * public void setUnknownCol(String unknownCol) { this.unknownCol = unknownCol;
	 * }
	 

	public void copy(Loc location) {
		this.locationId = location.getLocationId();
		this.city = location.getCity();
		this.location = location.getLocation();
		this.country = location.getCountry();
		this.zip = location.getZip();
		this.state = location.getState();
		this.stateid = location.getStateid();
		this.cityid = location.getCityid();
		this.lat = location.getLat();
		this.lng = location.getLng();
		this.unknownArea = location.getUnknownArea();
	}
}*/