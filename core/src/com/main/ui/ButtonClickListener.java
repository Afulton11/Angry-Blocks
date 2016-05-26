package com.main.ui;

public interface ButtonClickListener {
	/**
	 * When the button is clicked, this method will be called once.
	 */
	public void onClick();
	
	/**
	 * When the button is clicked and then released, this method will be called once.
	 */
	public void onRelease();
}
