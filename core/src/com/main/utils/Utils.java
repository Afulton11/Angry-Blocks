package com.main.utils;

import com.badlogic.gdx.math.Rectangle;
import com.main.Constants;

public final class Utils {

	@SuppressWarnings("unused")
	private static final String TAG = "Utils";
	
	public static final int getFontSize(int boundaryWidth, int boundaryHeight, float fontMultiplier) {
		int fontSize = 0;
		if(boundaryWidth < boundaryHeight) {
			fontSize = (int) (boundaryWidth * fontMultiplier/ boundaryHeight);
		} else {
			fontSize = (int) (boundaryHeight * fontMultiplier / boundaryWidth);
		}
		if(fontSize == 0) fontSize = 16;
		
		return fontSize;
	}
	
	public static final boolean isRectInScreen(Rectangle rectangle, Rectangle viewport) {
		return viewport.contains(rectangle);
	}
	
	/**
	 * True if the rectangle is in the screen bounds. (Works if the camera hasn't moved from its starting position)
	 * @param rectangle
	 * @return True if the rectangle is in the screen bounds
	 */
	public static boolean isRectInScreen(Rectangle rectangle) {
		return Constants.DEFAULT_VIEWPORT.contains(rectangle);
	}
	
	public static boolean isFloatXInScreen(float xPos, Rectangle viewport) {
		return viewport.contains(xPos, 0);
	}
	
	public static boolean isFloatYInScreen(float yPos, Rectangle viewport) {
		return viewport.contains(0, yPos);
	}
	
	public static final float getDistance(float x1, float y1, float x2, float y2) {
		float xDist = x1 - x2;
		float yDist = y1 - y2;
		return (float) Math.sqrt((xDist * xDist) + (yDist * yDist));
	}
}
