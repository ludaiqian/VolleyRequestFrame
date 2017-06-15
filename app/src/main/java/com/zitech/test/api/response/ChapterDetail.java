package com.zitech.test.api.response;

import com.zitech.test.api.entity.Chapter;

import java.util.List;

public class ChapterDetail extends Response {
	private String textBookId;// 当前书本id（选择书本页面默认选中使用）
	private String textBookName;// 当前书本名称（只选了书本未设置进度）
	// chapter
	private List<Chapter> chapter;

	public String getTextBookId() {
		return textBookId;
	}

	public void setTextBookId(String textBookId) {
		this.textBookId = textBookId;
	}

	public String getTextBookName() {
		return textBookName;
	}

	public void setTextBookName(String textBookName) {
		this.textBookName = textBookName;
	}

	public List<Chapter> getChapter() {
		return chapter;
	}

	public void setChapter(List<Chapter> chapter) {
		this.chapter = chapter;
	}

}
