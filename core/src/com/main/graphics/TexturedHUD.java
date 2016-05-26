package com.main.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class TexturedHUD extends HUD {

	private Texture texture;
	
	public TexturedHUD(Texture texture, float x, float y, float width, float height) {
		initVars(x, y, width, height);
		this.texture = texture;
	}
	
	@Override
	public void render(SpriteBatch batch) {	
		batch.draw(texture, x, y, width, height);
	}

	@Override
	public void update(float delta) {}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, width, height);
	}
	
	

}
