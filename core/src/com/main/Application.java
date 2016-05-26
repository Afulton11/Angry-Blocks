package com.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.main.screens.GameScreen;
import com.main.screens.LoadingScreen;
import com.main.screens.MainMenuScreen;

public class Application extends Game {
		
	public SpriteBatch batch;
	
	/**
	 * the AssetManager manages all assets loaded/retrieved from it. It will automatically dispose of these textures when callling assets.clear().
	 */
	public AssetManager assets;

	private FreeTypeFontGenerator fontGen;
	private FreeTypeFontParameter fontParams;
	
	private LoadingScreen loadingScreen;
	private MainMenuScreen mainMenuScreen;
	private GameScreen gameScreen;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(com.badlogic.gdx.Application.LOG_DEBUG);
		batch = new SpriteBatch();
		assets = new AssetManager();
		loadingScreen = new LoadingScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		gameScreen = new GameScreen(this);
		this.setScreen(loadingScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	public void dispose() {
		super.dispose();
		batch.dispose();
		assets.clear();
		assets.dispose();
	}
	
	public BitmapFont getFont(String fontPath, int size, Color color) {
		fontGen = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
		fontParams = new FreeTypeFontParameter();
		fontParams.size = size;
		fontParams.color = color;
		BitmapFont font = fontGen.generateFont(fontParams);
		fontGen.dispose();
		return font;
	}
	
	public LoadingScreen getLoadingScreen() {
		return loadingScreen;
	}
	
	public MainMenuScreen getMainMenuScreen(){
		return mainMenuScreen;
	}
	
	public GameScreen getGameScreen() {
		return gameScreen;
	}
	
}
