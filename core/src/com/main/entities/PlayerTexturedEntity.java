package com.main.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.main.Constants;
import com.main.utils.Utils;

public class PlayerTexturedEntity extends FallingTexturedEntity {
	
	private final int MAX_LIVES = 3;
	private int lives, score;
	private float scoreCounter;
	
	public OnDeathListener deathListener;
	
	private Array<Texture> damaged_textures;
	
	public PlayerTexturedEntity(Texture texture, float x, float y, float width, float height) {
		super.init(texture, x, y, width, height);
		this.damaged_textures = new Array<Texture>();
		lives = MAX_LIVES;
		damaged_textures.add(texture);
		score = 0;
	}
	
	@Override
	public void update(float delta) {
		if(alive()) {
			checkInputs(delta);
			scoreCounter += delta;
			while(scoreCounter > 1) {
				score++;
				scoreCounter--;
			}
		}
	}
	
	private void checkInputs(float delta) {
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			if(Utils.isFloatXInScreen(this.x + speed * delta + width, Constants.DEFAULT_VIEWPORT)) {
				this.x += speed * delta;
			}
		} else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			if(Utils.isFloatXInScreen(this.x - speed * delta, Constants.DEFAULT_VIEWPORT)) {
				this.x -= speed * delta;
			}
		}
	}
	
	public boolean alive(){
		return lives > 0;
	}
	
	public void subtractLife() {
		if(lives > 0) {
			lives--;
			if(MAX_LIVES - lives + 1 < damaged_textures.size) 
				this.texture = damaged_textures.get(MAX_LIVES - lives + 1);
			if(lives <= 0 && deathListener != null) {
				deathListener.onDeath();
			}
		}
	}
	
	public void setDamagedTextures(Texture...textures) {
		this.damaged_textures.addAll(textures);
	}
	
	public int getScore() {
		return score;
	}
}
