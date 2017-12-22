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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((altMob == null) ? 0 : altMob.hashCode());
		result = prime * result + ((houseInfo == null) ? 0 : houseInfo.hashCode());
		result = prime * result + ((mob == null) ? 0 : mob.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + residentId;
		result = prime * result + spId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resident other = (Resident) obj;
		if (altMob == null) {
			if (other.altMob != null)
				return false;
		} else if (!altMob.equals(other.altMob))
			return false;
		if (houseInfo == null) {
			if (other.houseInfo != null)
				return false;
		} else if (!houseInfo.equals(other.houseInfo))
			return false;
		if (mob == null) {
			if (other.mob != null)
				return false;
		} else if (!mob.equals(other.mob))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		if (residentId != other.residentId)
			return false;
		if (spId != other.spId)
			return false;
		return true;
	}
	
	

}
