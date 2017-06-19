package com.gree.android_asynctask;

public class Course {
	private String imgUrl;
	private String title;
	private String desc;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "Course [imgUrl=" + imgUrl + ", title=" + title + ", desc="
				+ desc + "]";
	}

}
