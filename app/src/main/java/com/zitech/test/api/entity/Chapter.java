package com.zitech.test.api.entity;

import java.util.List;

public class Chapter {
	private String id;// 章id
	private String name;// 章名称（第二章 有理数）
	private String shortName;// 简称
	private List<Section> sections;// 小节列表

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

}
