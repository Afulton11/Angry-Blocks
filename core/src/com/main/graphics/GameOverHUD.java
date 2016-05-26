package com.main.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.main.Constants;
import com.main.ui.Button;
import com.main.ui.ButtonClickListener;

public class GameOverHUD extends RectHUD {

	public static final float BUTTON_WIDTH = Constants.GAME_WIDTH / 4f, BUTTON_HEIGHT = Constants.GAME_HEIGHT / 8f;
	
	private BitmapFont font, btnFont;
	private GlyphLayout titleGlyph, scoreGlyph;
	private HUDHandler hud;
	
	public GameOverHUD(String text, BitmapFont font, BitmapFont btnFont, SequenceListener mainMenuSequenceListener, SequenceListener retrySequenceListener) {
		super(0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, new Color(0, 0, 0, 0.5f));
		this.font = font;
		this.btnFont = btnFont;
		titleGlyph = new GlyphLayout();
		titleGlyph.setText(font, text);
		scoreGlyph = new GlyphLayout();
		scoreGlyph.setText(btnFont, "Score: ");
		hud = new HUDHandler();
		initButtons(mainMenuSequenceListener, retrySequenceListener);
	}
	
	private void initButtons(SequenceListener mainMenuSequenceListener, SequenceListener retrySequenceListener){
		final Button btnRetry = new Button(Constants.GAME_WIDTH / 6f, Constants.GAME_HEIGHT / 6f, BUTTON_WIDTH, BUTTON_HEIGHT, 
				Color.WHITE, btnFont, "Retry");
		btnRetry.setClickListener(new ButtonClickListener() {
			@Override
			public void onRelease() {btnRetry.setMinimize(0.2f, btnRetry.height / 2);}
			@Override
			public void onClick() {}
		});
		btnRetry.setSequenceListener(retrySequenceListener);
		final Button btnQuit = new Button(Constants.GAME_WIDTH - btnRetry.getX() - BUTTON_WIDTH, btnRetry.getY(), BUTTON_WIDTH, BUTTON_HEIGHT, 
				Color.WHITE, btnFont, "Menu");
		btnQuit.setClickListener(new ButtonClickListener() {
			@Override
			public void onRelease() { btnQuit.setMinimize(0.2f, btnQuit.height / 2);}
			@Override
			public void onClick() {}
		});
		btnQuit.setSequenceListener(mainMenuSequenceListener);
		
		hud.addHud(btnRetry, btnQuit);
	}
	
	public void render(SpriteBatch batch) {
		super.render(batch);
		//draw the 'ame Over!' text.
		font.draw(batch, titleGlyph, Constants.GAME_WIDTH / 2 - titleGlyph.width / 2, Constants.GAME_HEIGHT - titleGlyph.height / 2 - Constants.GAME_HEIGHT / 8);
		//draw the 'Score: ####' text
		btnFont.draw(batch, scoreGlyph, Constants.GAME_WIDTH / 2 - scoreGlyph.width / 2, Constants.GAME_HEIGHT / 2 - scoreGlyph.height / 2);
		//buttons
		hud.render(batch);
	}
	
	public void update(float delta) {
		hud.update(delta);
	}
	
	public void updateScore(int score) {
		scoreGlyph.setText(btnFont, "Score " + score);
	}
	
	public void dispose() {
		super.dispose();
		hud.dispose();
	}

}
