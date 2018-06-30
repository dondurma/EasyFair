package com.baibeiyun.eazyfair.model;

import java.util.List;

public class ProvinceModel {
	private String province;
	List<CityModel>city_list;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Override
	public String toString() {
		return "ProvinceModel [province=" + province + "]";
	}

	public List<CityModel> getCity_list() {
		return city_list;
	}

	public void setCity_list(List<CityModel> city_list) {
		this.city_list = city_list;
	}
	
	

}
