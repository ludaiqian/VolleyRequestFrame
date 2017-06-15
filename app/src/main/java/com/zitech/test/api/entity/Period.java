package com.zitech.test.api.entity;

public class Period {
	private String Id;// 课时id
	private String name;// 课时名称
	private String shortName;// String 简称

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
