package com.baibeiyun.eazyfair.model;

import java.util.List;

public class ThreeModel {
	private String three;
	List<FourModel>four_list;

	public String getThree() {
		return three;
	}

	public void setThree(String three) {
		this.three = three;
	}

	@Override
	public String toString() {
		return "ThreeModel [three=" + three + "]";
	}

	public List<FourModel> getFour_list() {
		return four_list;
	}

	public void setFour_list(List<FourModel> four_list) {
		this.four_list = four_list;
	}
	
	
	

}
