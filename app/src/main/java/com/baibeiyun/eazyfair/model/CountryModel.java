package com.baibeiyun.eazyfair.model;

import java.util.List;

public class CountryModel {
	private String country;
	List<ProvinceModel>province_list;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	

	public List<ProvinceModel> getProvince_list() {
		return province_list;
	}

	public void setProvince_list(List<ProvinceModel> province_list) {
		this.province_list = province_list;
	}

	@Override
	public String toString() {
		return "CountryModel [country=" + country + "]";
	}
	
}
