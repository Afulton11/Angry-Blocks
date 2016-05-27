package com.main.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class TexturedEntity extends Entity{

	protected Texture texture;
	protected float width, height;
	
	public TexturedEntity() {
		reset();
	}
	
	public void init(Texture texture,float x, float y, float width, float height) {
		super.init(x, y);
		this.texture = texture;
		this.width = width;
		this.height = height;
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y, width, height);
	}
	
	@Override
	public void reset() {
		super.reset();
		width = 0;
		height = 0;
		texture = null;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	

}
