package com.main.entities;


public class FallingTexturedEntity extends TexturedEntitity {

	protected float speed;
	
	public boolean alive;
	
	public FallingTexturedEntity() {
		reset();
	}	

	@Override
	public void update(float delta) {
		this.y -= speed * delta;
		if(y < -height) alive = false;
	}
	
	@Override
	public void reset() {
		super.reset();
		speed = 0;
		alive = true;
	}
	public float getSpeed(){
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
