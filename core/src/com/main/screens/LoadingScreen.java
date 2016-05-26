package com.main.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.main.Application;
import com.main.Constants;

public class LoadingScreen implements Screen {

	private static final String TAG = LoadingScreen.class.getSimpleName();
	
	private final Application app;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	
	/**
	 * The amount of the assets that the AssetManager has loaded.
	 */
	private float progress;
	
	private BitmapFont font;
	private GlyphLayout loading_layout;
	
	public LoadingScreen(final Application app) {
		this.app = app;
		this.cam = new OrthographicCamera();
		cam.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
	}
	
	@Override
	public void show() {	
		cam.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
		shapeRenderer = new ShapeRenderer();
		font = app.getFont("fonts/RobotoLight.ttf", 16, Color.WHITE);
		loading_layout = new GlyphLayout(font, "Loading...");
		progress = 0;
		loadAssets();
	}
	
	/**
	 * Loads all of the needed assets for the game.
	 */
	private void loadAssets() {
		//mainMenu background
		app.assets.load("imgs/background.png", Texture.class);
		//game background
		app.assets.load("imgs/game_background.png", Texture.class);
		//player
		app.assets.load("imgs/player.png", Texture.class);
		//fallingEntity number 1:
		app.assets.load("imgs/fallingEntity_1.png", Texture.class);
	}

	@Override
	public void render(float delta) {	
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
		if(app.assets.isLoaded("imgs/background.png", Texture.class)) {
			app.batch.begin();
			app.batch.draw(app.assets.get("imgs/background.png", Texture.class), 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
			app.batch.end();
		}
		app.batch.begin();
		
		font.draw(app.batch, loading_layout, Constants.GAME_WIDTH / 2f - loading_layout.width / 2, Constants.GAME_HEIGHT / 6f + loading_layout.height / 2);
		
		app.batch.end();
		
		
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(16, Constants.GAME_HEIGHT / 2f + 8, Constants.GAME_WIDTH - 32, 16);
		shapeRenderer.setColor(Color.GOLD);
		shapeRenderer.rect(16, Constants.GAME_HEIGHT / 2f + 8, (Constants.GAME_WIDTH - 32) * progress, 16);	
		shapeRenderer.end();
		
		update(delta);
	}
	
	private void update(float delta) {
		progress = MathUtils.lerp(progress, app.assets.getProgress(), 0.1f);
		if(app.assets.update() && app.assets.getProgress() - progress > 0.05f) {
			//change screens
			Gdx.app.debug(TAG, "Finished Loading Assets.");
			app.setScreen(app.getMainMenuScreen());
		}
	}

	@Override
	public void resize(int width, int height) {		
		cam.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {dispose();}

	@Override
	public void dispose() {		
		shapeRenderer.dispose();
		font.dispose();
	}

}
