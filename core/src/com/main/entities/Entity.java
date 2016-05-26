package com.main.entities;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.main.Constants;
import com.main.utils.Utils;

public abstract class Entity implements Poolable{

	protected float x, y;
	
	public Entity() {
		reset();
	}
	
	public void init(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract void update(float delta);
	
	@Override
	public void reset() {
		x = Constants.GAME_HEIGHT * 2;
		y = Constants.GAME_WIDTH * 2;
	}
	
	public float getX(){
		return x;
	}

	public float getY() {
		return y;
	}
	
	public boolean isOutOfScreen() {
		return !(Utils.isFloatXInScreen(x, Constants.DEFAULT_VIEWPORT) && Utils.isFloatYInScreen(y, Constants.DEFAULT_VIEWPORT));
	}

}
