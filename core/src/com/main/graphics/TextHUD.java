package com.main.graphics;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextHUD extends HUD {

	private BitmapFont font;
	private GlyphLayout textGlyph;
	
	public TextHUD(float x, float y, BitmapFont font, GlyphLayout textGlyph) {
		super.initVars(x, y, textGlyph.width, textGlyph.height);
		this.font = font;
		this.textGlyph = textGlyph;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		font.draw(batch, textGlyph, x, y);
	}

	@Override
	public void update(float delta) {}
	
	public void updateText(String text) {
		this.textGlyph.setText(font, text);
	}
	
	public void updateText(BitmapFont font, String text) {
		this.font = font;
		this.textGlyph.setText(font, text);
	}

}
