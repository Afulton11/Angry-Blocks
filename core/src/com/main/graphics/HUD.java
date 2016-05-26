package com.main.graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public abstract class HUD {
		
	protected float x, y;
	protected float width, height;
	protected float minimize, minSize, fadeFactor, fadeStart;
	protected boolean isMinimizing, isFading;
	
	private SequenceListener sequenceListener;
	
	protected void initVars(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		minimize = -1;
		minSize = 5;
		fadeFactor = -1;
	}
	
	public abstract void render(SpriteBatch batch);
	
	public abstract void update(float delta);
	
	/**
	 * An Optional HUD Method that can be used to dispose any necessary items for memory.
	 * NOTE!!!! Only Textures or fonts generated within the HUD class should be disposed. 
	 * Any Textures or fonts used as part of the constructor need to be disposed of outside of
	 * the HUD class!!!
	 */
	public void dispose() {}
	
	protected void checkSequences() {
		updateMinimize();
		updateFading();
	}
	
	private void updateMinimize() {
		if(minimize > 0 && width * height > minSize * minSize) {
			this.width = MathUtils.lerp(width, 0, minimize);
			this.height = MathUtils.lerp(height, 0, minimize);
			this.x = MathUtils.lerp(x, x + width / 2, minimize);
			this.y = MathUtils.lerp(y, y + height / 2, minimize);
		} else if(isMinimizing){
			if(sequenceListener != null) {
				sequenceListener.onMinimizeFinished();
			}
			minimize = -1;
			isMinimizing = false;
		}
	}
	
	private void updateFading() {
		if(fadeStart > 0.01f) {
			fadeStart = MathUtils.lerp(fadeStart, 0, fadeFactor);
		} else if(isFading){
			if(sequenceListener != null) {
				sequenceListener.onFadeFinished();
			}
			isFading = false;
		}
	}
	
	public void setMinimize(float minFactor, float minSize) {
		this.minimize = minFactor;
		this.minSize = minSize;
		if(minFactor > 0) {
			isMinimizing = true;
			if(sequenceListener != null) {
				sequenceListener.onMinimizeStarted();
			}
		}
	}
	
	public void setFade(float fadeStart, float fadeFactor) {
		this.fadeStart = fadeStart;
		this.fadeFactor = fadeFactor;
		if(fadeFactor > 0) {
			isFading = true;
			if(sequenceListener != null) {
				sequenceListener.onFadeStarted();
			}
		}
	}
	
	public void setSequenceListener(SequenceListener sequenceListener) {
		this.sequenceListener = sequenceListener;
	}
	
	public boolean isMinimizing() {
		return isMinimizing;
	}
	
	public boolean isFading(){
		return isFading;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
