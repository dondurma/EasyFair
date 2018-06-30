package com.baibeiyun.eazyfair.model;

import java.util.List;

public class OneModel {
	private String one;
	List<TwoModel>two_list;

	public String getOne() {
		return one;
	}

	public void setOne(String one) {
		this.one = one;
	}
	

	public List<TwoModel> getTwo_list() {
		return two_list;
	}

	public void setTwo_list(List<TwoModel> two_list) {
		this.two_list = two_list;
	}

	@Override
	public String toString() {
		return "OneModel [one=" + one + "]";
	}
	
}
