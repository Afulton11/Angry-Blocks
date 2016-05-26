package com.main.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.main.Constants;
import com.main.ui.Button;

public class HUDHandler {
	
	@SuppressWarnings("unused")
	private final String TAG = "HUDHandler";
	
	private Array<HUD> huds;
	
	public HUDHandler(){
		huds = new Array<HUD>();
	}
	
	public void render(SpriteBatch batch) {
		for(HUD h : huds) {
			h.render(batch);
		}
	}
	
	public void update(float delta) {
		for(int i = 0; i < huds.size; i++) {
			huds.get(i).update(delta);
		}
		checkHudInput();
	}
	
	public void checkHudInput() {
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			onButtonPress(Input.Buttons.LEFT);
		} else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			onButtonPress(Input.Buttons.RIGHT);
		}
	}
	
	private void onButtonPress(int button) {
		float x = Gdx.input.getX(), y = Constants.GAME_HEIGHT - Gdx.input.getY(); //we need to convert the mouse coords to libGdx coords.
		for(HUD h : huds) {
			if(h instanceof Button) {
				((Button) h).checkPress(button, x, y);
			}
		}
	}
	
	public void dispose() {
		for(HUD h : huds) {
			h.dispose();
		}
		this.huds.clear();
	}
	
	public void addHud(HUD...huds) {
		this.huds.addAll(huds);
	}
	
	public void addHudAt(HUD hud, int position) {
		Array<HUD> newHuds = new Array<HUD>();
		boolean added = false;
		for(int i = 0; i < huds.size; i++) {
			if(i == position) {
				added = true;
				newHuds.add(hud);
			}
			newHuds.add(huds.get(i));
		}
		if(!added) newHuds.add(hud);
		huds = newHuds;
	}
	
	public void removeHud(HUD...hudsToRemove) {
		Array<HUD> hudsToRemoveArray = new Array<HUD>();
		hudsToRemoveArray.addAll(hudsToRemove);
		this.huds.removeAll(hudsToRemoveArray, false);
	}
	
}
