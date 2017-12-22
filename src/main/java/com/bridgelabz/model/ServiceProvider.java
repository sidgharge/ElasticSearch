package com.bridgelabz.model;

import java.io.Serializable;

public class ServiceProvider implements Serializable {

	private static final long serialVersionUID = 1L;

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
