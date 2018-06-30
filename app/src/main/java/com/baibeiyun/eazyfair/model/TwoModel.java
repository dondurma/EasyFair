package com.baibeiyun.eazyfair.model;

import java.util.List;

public class TwoModel {
	private String two;
	List<ThreeModel>three_list;

	public String getTwo() {
		return two;
	}

	public void setTwo(String two) {
		this.two = two;
	}

	@Override
	public String toString() {
		return "TwoModel [two=" + two + "]";
	}

	public List<ThreeModel> getThree_list() {
		return three_list;
	}

	public void setThree_list(List<ThreeModel> three_list) {
		this.three_list = three_list;
	}
	
	

}
