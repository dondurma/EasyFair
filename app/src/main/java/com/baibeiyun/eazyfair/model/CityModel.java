package com.baibeiyun.eazyfair.model;

import java.util.List;

public class CityModel {
	private String city;
	List<AreaModel>area_list;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "CityModel [city=" + city + "]";
	}

	public List<AreaModel> getArea_list() {
		return area_list;
	}

	public void setArea_list(List<AreaModel> area_list) {
		this.area_list = area_list;
	}
	
	
	

}
