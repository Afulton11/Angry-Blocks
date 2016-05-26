package com.main.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.main.graphics.HUD;

public class Button extends HUD {

	/**
	 * BUTTON_PRESS is the button needed to be pressed on a button for it to react to as being pressed down.
	 */
	private final int BUTTON_PRESS = Input.Buttons.LEFT;
	
	private Texture background;
	private BitmapFont font;
	private GlyphLayout textGlyph;
	
	private ButtonClickListener clickListener;
	
	private boolean isClicked;

	/**
	 * Creates a new Button.
	 * @param x - x position
	 * @param y - y position
	 * @param width 
	 * @param height
	 * @param backgroundColor of the Button
	 * @param font - font for the text to be rendered in
	 * @param text - text to render over the button
	 */
	public Button(float x, float y, float width, float height, Color backgroundColor, BitmapFont font, String text) {
		initVars(x, y, width, height);
		Pixmap pix = new Pixmap((int)width, (int)height, Format.RGBA8888);
		pix.setColor(backgroundColor);
		pix.fill();
		this.background = new Texture(pix);
		this.font = font;
		textGlyph = new GlyphLayout(font, text);
	}
	/**
	 * Creates a new Button with a clickListener
	 * @param x - x position
	 * @param y - y position
	 * @param width 
	 * @param height
	 * @param backgroundColor of the Button
	 * @param font - font for the text to be rendered in
	 * @param text - text to render over the button
	 * @param clickListener - for when the button is clicked.
	 */
	public Button(float x, float y, float width, float height, Color backgroundColor, BitmapFont font, String text, ButtonClickListener clickListener) {
		initVars(x, y, width, height);
		Pixmap pix = new Pixmap((int)width, (int)height, Format.RGBA8888);
		pix.setColor(backgroundColor);
		pix.fill();
		this.background = new Texture(pix);
		this.font = font;
		textGlyph = new GlyphLayout(font, text);
		
		this.clickListener = clickListener;
	}
	private float timer;
	@Override
	public void render(SpriteBatch batch) {
		if(isFading || timer > 0) {
			batch.setColor(1, 1, 1, fadeStart);
		}
		batch.draw(background, x, y, width, height);
		if(!isMinimizing && !isFading && timer <= 0)
			font.draw(batch, textGlyph, x + width / 2 - textGlyph.width / 2, y + height / 2 + textGlyph.height / 2);
		
		if(isFading || timer > 0) {
			batch.setColor(1, 1, 1, 1);
		}
	}

	@Override
	public void update(float delta) {
		if(!isMinimizing && !isFading) {
			if(isClicked && !Gdx.input.isButtonPressed(BUTTON_PRESS)) { //WAS CLICKED && BUTTON_PRESS WAS RELEAED...
				if(clickListener != null) {
					clickListener.onRelease();
				}
				isClicked = false;
			}
		}
		checkSequences(); 
		//we use a timer so that the button doesn't come back immediately after finishing its fade.
		if(isFading) timer = 1;
		else timer -= delta;
	}
	@Override
	public void dispose() {
		background.dispose(); //the background is generated in the Constructor therefore, it needs to be disposed in the class itself.
	}
	
	public void checkPress(int button, float x, float y) {
		if(!isClicked && button == BUTTON_PRESS && getRect().contains(x, y))  {
			if(clickListener != null) {
				clickListener.onClick();
			}
			isClicked = true;
		}
	}
	
	public void setClickListener(ButtonClickListener listener) {
		this.clickListener = listener;
	}
	
	public ButtonClickListener getClickListener() {
		return clickListener;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
}
