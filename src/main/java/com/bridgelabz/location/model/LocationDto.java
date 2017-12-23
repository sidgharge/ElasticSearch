package com.bridgelabz.location.model;

public class LocationDto {

	private long locationId;

	private String country;

	private int zip;

	private String state;

	private int stateid;

	private String city;

	private int cityid;

	private LatLngDto location;
	
	public LocationDto() {
		this.location = new LatLngDto();
	}

	public LatLngDto getLocation() {
		return location;
	}

	public void setLocation(LatLngDto location) {
		this.location = location;
	}

	public long getLocationId() {
		return locationId;
	}

	public String getCountry() {
		return country;
	}

	public int getZip() {
		return zip;
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

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setZip(int zip) {
		this.zip = zip;
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
	
	public void copy(Location location) {
		this.locationId = location.getLocationId();
		this.city = location.getCity();
		this.country = location.getCountry();
		this.zip = location.getZip();
		this.state = location.getState();
		this.stateid = location.getStateid();
		this.cityid = location.getCityid();
		this.location.setLat(location.getLat());
		this.location.setLon(location.getLng());
	}

}
