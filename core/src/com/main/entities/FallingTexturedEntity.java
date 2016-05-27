package com.main.entities;

import com.badlogic.gdx.graphics.Texture;
import com.main.Constants;
import com.main.utils.Utils;


public class FallingTexturedEntity extends TexturedEntity {

	protected float speed;
	private boolean isFalling;
	private float goalHeight;
	
	public FallingTexturedEntity() {
		reset();
	}	

	public void init(Texture texture, float x, float y, float width, float height, final float goalHeight) {
		super.init(texture, x, y, width, height);
		this.goalHeight = goalHeight;
		
	}
	
	@Override
	public void update(float delta) {
		if(isFalling && !Utils.equalFloats(y, goalHeight, speed * delta))
			this.y -= speed * delta;
		else if(isFalling){
			this.y = goalHeight;
			isFalling = false;
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		speed = 0;
		goalHeight = 0;
		isFalling = true;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public boolean alive(Entity player) {
		return y > player.getY() - Constants.GAME_HEIGHT;
	}
	
	public boolean isFalling() {
		return isFalling;
	}
}
