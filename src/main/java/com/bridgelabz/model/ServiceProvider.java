package com.bridgelabz.model;

public class ServiceProvider {

	private int spId;

	private String name;

	public int getSpId() {
		return spId;
	}

	public void setSpId(int spId) {
		this.spId = spId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ServiceProvider [spId=" + spId + ", name=" + name + "]";
	}

}
