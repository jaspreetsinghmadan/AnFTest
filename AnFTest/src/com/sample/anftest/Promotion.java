package com.sample.anftest;

import java.io.Serializable;

public class Promotion implements Serializable  {
	private static final long serialVersionUID = 1L;
	public static class Button implements Serializable{
		private static final long serialVersionUID = 2L;
		String mButtonTarget;
		String mButtonTitle;
		
		public Button(String target, String title){
			mButtonTarget = target;
			mButtonTitle = title;
		}
	}
	Button mButton;
	String mDescription;
	String mFooter;
	String mImage;
	String mTitle;
	long mId;
	
	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public Button getButton() {
		return mButton;
	}

	public void setButton(Button mButton) {
		this.mButton = mButton;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getFooter() {
		return mFooter;
	}

	public void setFooter(String mFooter) {
		this.mFooter = mFooter;
	}

	public String getImage() {
		return mImage;
	}

	public void setImage(String mImage) {
		this.mImage = mImage;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public Promotion(Button button,String des,String footer,String imageUrl,String title){
		mButton = button;
		mDescription = des;
		mImage = imageUrl;
		mFooter = footer;
		mTitle = title;
	}
	
	public Promotion(){
		
	}
}
