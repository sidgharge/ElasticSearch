package com.bridgelabz.model;

import java.io.Serializable;

public class Resident implements Serializable {

	private static final long serialVersionUID = 1L;

	private int residentId;

	private String name;

	private String mob;

	private String houseInfo;

	private String nickName;

	private String altMob;

	private int spId;

	public int getResidentId() {
		return residentId;
	}

	public void setResidentId(int residentId) {
		this.residentId = residentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getHouseInfo() {
		return houseInfo;
	}

	public void setHouseInfo(String houseInfo) {
		this.houseInfo = houseInfo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAltMob() {
		return altMob;
	}

	public void setAltMob(String altMob) {
		this.altMob = altMob;
	}

	public int getSpId() {
		return spId;
	}

	public void setSpId(int spId) {
		this.spId = spId;
	}

	@Override
	public String toString() {
		return "Resident [residentId=" + residentId + ", name=" + name + ", mob=" + mob + ", houseInfo=" + houseInfo
				+ ", nickName=" + nickName + ", altMob=" + altMob + ", spId=" + spId + "]";
	}

}
