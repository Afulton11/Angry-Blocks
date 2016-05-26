package com.main.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.main.Constants;

public class RectHUD extends HUD {

	private Texture rectTexture;
	
	public RectHUD(float x, float y, float width, float height, Color color) {
		initVars(x, y, width, height);
		Pixmap pix = new Pixmap((int)width, (int)height, Format.RGBA8888);
		pix.setColor(color);
		pix.fillRectangle(0, 0, pix.getWidth(), pix.getHeight());
		this.rectTexture = new Texture(pix);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(rectTexture, x, y);
	}

	@Override
	public void update(float delta) {}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, Constants.GAME_HEIGHT - height);
	}
	
	public void dispose() {
		rectTexture.dispose();
	}

}
