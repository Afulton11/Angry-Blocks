package com.main.screens;

import static com.main.Constants.GAME_HEIGHT;
import static com.main.Constants.GAME_WIDTH;
import static com.main.Constants.ROBOTO_REGULAR_SIZE_MULTIPLIER;
import static com.main.Constants.TITLE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.main.Application;
import com.main.graphics.HUDHandler;
import com.main.graphics.SequenceListener;
import com.main.graphics.TexturedHUD;
import com.main.ui.Button;
import com.main.ui.ButtonClickListener;
import com.main.utils.Utils;

public class MainMenuScreen implements Screen {

	private final String TAG = "MainMenu";
	
	private final Application app;
	private final float BTN_WIDTH = GAME_WIDTH / 3f, BTN_HEIGHT = GAME_HEIGHT / 6f;
	
	private OrthographicCamera cam;
	
	private HUDHandler hudHandler;
	private BitmapFont titleFont, btnFont;
	private GlyphLayout titleGlyph;
	
	private Texture background;
	
	public MainMenuScreen(final Application app) {
		this.app = app;
		this.cam = new OrthographicCamera();
		hudHandler = new HUDHandler();
		titleFont = app.getFont("fonts/RobotoRegular.ttf", 124, Color.GOLD);
		titleGlyph = new GlyphLayout(titleFont, TITLE);
		int btnFontSize = Utils.getFontSize((int)BTN_WIDTH, (int)BTN_HEIGHT, ROBOTO_REGULAR_SIZE_MULTIPLIER);
		btnFont = app.getFont("fonts/Segoe.ttf", btnFontSize, Color.BLACK);
	}
	
	@Override
	public void show() {
		cam.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		if(background == null) {
			background = app.assets.get("imgs/background.png", Texture.class);
		}
		loadUI();
	}

	@Override
	public void render(float delta) {
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
		app.batch.setProjectionMatrix(cam.combined);
		
		app.batch.begin();
		hudHandler.render(app.batch);
		titleFont.draw(app.batch, titleGlyph, GAME_WIDTH / 2f - titleGlyph.width / 2f, GAME_HEIGHT - GAME_HEIGHT / 8f + titleGlyph.height / 2);
		app.batch.end();
	}
	
	private void update(float delta) {
		hudHandler.update(delta);
	}

	@Override
	public void resize(int width, int height) {
		cam.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {dispose();}

	@Override
	public void dispose() {
		hudHandler.dispose();
	}
	
	private void loadUI() {
		TexturedHUD bg = new TexturedHUD(background, 0, 0, GAME_WIDTH, GAME_HEIGHT);
		hudHandler.addHud(bg);
		loadButtons();
	}
	
	private void loadButtons() {

		final Button playButton = new Button(GAME_WIDTH / 2f - BTN_WIDTH / 2, GAME_HEIGHT - BTN_HEIGHT - GAME_HEIGHT / 4f, 
				BTN_WIDTH, BTN_HEIGHT, Color.WHITE, btnFont , "Play");
		playButton.setClickListener(new ButtonClickListener() {
			@Override
			public void onClick() {}
			@Override
			public void onRelease() {
				playButton.setFade(1.0f, 0.5f);
			}
		});
		playButton.setSequenceListener(new SequenceListener() {	
			@Override
			public void onMinimizeStarted() {}	
			@Override
			public void onMinimizeFinished() {}			
			@Override
			public void onFadeStarted() {}			
			@Override
			public void onFadeFinished() {
				Gdx.app.debug(TAG, "Switching to GameScreen!");
				app.setScreen(app.getGameScreen());
			}
		});
		
		
		final Button settingsButton = new Button(GAME_WIDTH / 2f - BTN_WIDTH / 2, GAME_HEIGHT - BTN_HEIGHT * 2 - GAME_HEIGHT / 4f - BTN_HEIGHT / 2, 
				BTN_WIDTH, BTN_HEIGHT, Color.WHITE, btnFont, "Settings");
		settingsButton.setClickListener(new ButtonClickListener() {
			@Override
			public void onClick() {}
			@Override
			public void onRelease() {
				settingsButton.setFade(1.0f, 0.5f);
			}
		});
		settingsButton.setSequenceListener(new SequenceListener() {	
			@Override
			public void onMinimizeStarted() {}		
			@Override
			public void onMinimizeFinished() {}			
			@Override
			public void onFadeStarted() {}			
			@Override
			public void onFadeFinished() {
				Gdx.app.debug(TAG, "Switching to SettingScreen!");
				app.setScreen(app.getMainMenuScreen());
			}
		});
		
		final Button quitButton = new Button(GAME_WIDTH / 2f - BTN_WIDTH / 2, settingsButton.getY() - settingsButton.getHeight() - BTN_HEIGHT / 2, 
				BTN_WIDTH, BTN_HEIGHT, Color.WHITE, btnFont, "Quit Game");
		quitButton.setClickListener(new ButtonClickListener() {
			@Override
			public void onClick() {}
			@Override
			public void onRelease() {
				quitButton.setFade(1.0f, 0.5f);
			}
		});
		quitButton.setSequenceListener(new SequenceListener() {	
			@Override
			public void onMinimizeStarted() {}		
			@Override
			public void onMinimizeFinished() {}			
			@Override
			public void onFadeStarted() {}			
			@Override
			public void onFadeFinished() {
				Gdx.app.debug(TAG, "quitting the game!");
				Gdx.app.exit();
			}
		});
		
		hudHandler.addHud(playButton, settingsButton, quitButton);
	}

}
