package com.leon.shehuibang.model.bean;

public class CommentsImage {
	private long comments_image_id;
	private long comments_id;
	private String image_url;
	private int image_width;
	private int image_height;
	private String thumbnail_url;
	private String blur_url;
	private int image_order;
	private long create_time;
	
	

	public String getBlur_url() {
		return blur_url;
	}

	public void setBlur_url(String blur_url) {
		this.blur_url = blur_url;
	}

	public long getComments_image_id() {
		return comments_image_id;
	}

	public void setComments_image_id(long comments_image_id) {
		this.comments_image_id = comments_image_id;
	}

	public long getComments_id() {
		return comments_id;
	}

	public void setComments_id(long comments_id) {
		this.comments_id = comments_id;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public int getImage_width() {
		return image_width;
	}

	public void setImage_width(int image_width) {
		this.image_width = image_width;
	}

	public int getImage_height() {
		return image_height;
	}

	public void setImage_height(int image_height) {
		this.image_height = image_height;
	}

	public String getThumbnail_url() {
		return thumbnail_url;
	}

	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}

	public int getImage_order() {
		return image_order;
	}

	public void setImage_order(int image_order) {
		this.image_order = image_order;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

}
