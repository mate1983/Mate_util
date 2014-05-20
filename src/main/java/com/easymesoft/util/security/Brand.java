package com.easymesoft.util.security;

import java.io.Serializable;

import org.apache.commons.lang.ObjectUtils;

public class Brand implements Serializable {
	private String provinceId;
	private String cityId;

	public Brand() {
		this.provinceId = null;
		this.cityId = null;
	}

	public Brand(String provinceId, String cityId) {
		this.provinceId = provinceId;
		this.cityId = cityId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public boolean equals(Brand aBrand) {
		if (aBrand != null) {
			boolean provinceEqual = (this.provinceId != null) ? (this.provinceId.equals(aBrand
					.getProvinceId())) : (aBrand.getProvinceId() == null);
			boolean cityEqual = (this.cityId != null) ? (this.cityId.equals(aBrand.getCityId()))
					: (aBrand.getCityId() == null);

			return (provinceEqual && cityEqual);
		} else {
			return false;
		}
	}

	public boolean superior(Brand aBrand) {
		if (aBrand != null) {
			boolean provinceEqual = (this.provinceId != null) ? (this.provinceId.equals(aBrand
					.getProvinceId())) : (aBrand.getProvinceId() == null);
			boolean citySuperior = (ObjectUtils.toString(this.cityId).trim().equals(""))
					&& (aBrand.getCityId() != null);

			return (provinceEqual && citySuperior);
		} else {
			return false;
		}
	}

	public boolean equalsOrSuperior(Brand aBrand) {
		return equals(aBrand) || superior(aBrand);
	}
}
