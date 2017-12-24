package com.bridgelabz.location.model;

public class LocationDto {

	private long locationId;

	private String country;

	private int zip;

	private String place;

	private String state;

	private int stateid;

	private String city;

	private int cityid;

	private String area;

	private LatLng location;

	public LocationDto() {
		location = new LatLng();
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

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public void copy(Loc loc) {
		this.locationId = loc.getLocationId();
		this.city = loc.getCity();
		this.place = loc.getPlace();
		this.country = loc.getCountry();
		this.zip = loc.getZip();
		this.state = loc.getState();
		this.stateid = loc.getStateid();
		this.cityid = loc.getCityid();
		this.location.setLat(loc.getLat());
		this.location.setLon(loc.getLon());
		this.area = loc.getArea();
	}
}
