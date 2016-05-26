package com.main.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.main.Application;
import com.main.Constants;
import com.main.entities.FallingEntitySpawner;
import com.main.entities.OnDeathListener;
import com.main.entities.PlayerTexturedEntity;
import com.main.graphics.GameOverHUD;
import com.main.graphics.SequenceListener;
import com.main.utils.Utils;

public class GameScreen implements Screen {

	private final String TAG = GameScreen.class.getSimpleName();
	
	private final Application app;
	
	private OrthographicCamera cam;
	
	private FallingEntitySpawner spawner;
	private PlayerTexturedEntity player;
	
	private GameOverHUD gameOver;
	private BitmapFont gameOverTitleFont, gameOverBtnFont;
	private Texture background;
	
	public GameScreen(final Application app) {
		this.app = app;
		cam = new OrthographicCamera();
	}
	
	@Override
	public void show() {
		cam.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
		spawner = new FallingEntitySpawner(app.assets.get("imgs/fallingEntity_1.png", Texture.class));
		player = new PlayerTexturedEntity(app.assets.get("imgs/player.png", Texture.class), Constants.GAME_WIDTH / 2f - 32, 128, 64, 64);
		player.setSpeed(200.0f);
		
		gameOverTitleFont = app.getFont("fonts/curlyText.ttf", 128, Color.WHITE);
		gameOverBtnFont = app.getFont("fonts/Segoe.ttf", Utils.getFontSize((int)GameOverHUD.BUTTON_WIDTH, (int)GameOverHUD.BUTTON_HEIGHT, 
				Constants.ROBOTO_REGULAR_SIZE_MULTIPLIER), Color.BLACK);
		if(background == null) {
			background = app.assets.get("imgs/game_background.png", Texture.class);
		}
		setUpGameOverHUD();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		app.batch.setProjectionMatrix(cam.combined);
		
		app.batch.begin();
		app.batch.draw(background, 0, 0, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
		player.render(app.batch);
		spawner.render(app.batch);
		if(!player.alive()) {
			//render dead overlay Hud.
			gameOver.render(app.batch);
		}
		app.batch.end();

		update(delta);
	}
	
	private void update(float delta) {
		if(player.alive()) {
			player.update(delta);
			spawner.update(delta);
			spawner.checkCollisionsWithPlayer(player);
		} else {
			gameOver.update(delta);
		}
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) app.setScreen(app.getMainMenuScreen());
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
		spawner.dispose();
		gameOver.dispose();
		gameOverTitleFont.dispose();
		gameOverBtnFont.dispose();
	}
	
	private void setUpGameOverHUD(){
		gameOver = new GameOverHUD("Game Over!", gameOverTitleFont, gameOverBtnFont,
						new SequenceListener() {//main menu sequence listener
							@Override
							public void onMinimizeStarted() {}
							@Override
							public void onMinimizeFinished() { 
								app.setScreen(app.getMainMenuScreen()); }							
							@Override
							public void onFadeStarted() {}
							@Override
							public void onFadeFinished() {}
						}, new SequenceListener() { //retry sequence listener
							@Override
							public void onMinimizeStarted() {}
							@Override
							public void onMinimizeFinished() {						
								app.setScreen(app.getGameScreen());
							}
							@Override
							public void onFadeStarted() {}
							@Override
							public void onFadeFinished() {}
						});
		
		player.deathListener = new OnDeathListener() {
			@Override
			public void onDeath() {
				gameOver.updateScore(player.getScore());	
			}
		};
	}
}
