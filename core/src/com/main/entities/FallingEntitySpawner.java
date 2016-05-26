package com.main.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.main.Constants;
import com.main.utils.Utils;

public class FallingEntitySpawner extends Entity{

	@SuppressWarnings("unused")
	private static final String TAG = "FallingEntitySpawner";
	
	private static final float ENTITY_WIDTH = 64, ENTITY_HEIGHT = 64;
	/**
	 * How fast the falling Entities fall down the screen.
	 */
	private static final float FALL_SPEED = 300.0f;
	/**
	 * SPAWN_TIME is in Milliseconds. 1000 millis = 1 second.
	 */
	private static long SPAWN_TIME = 300;
	
	
	private long lastEntitySpawn;
	/**
	 * The pool that holds all the currently Falling FallingTextureEntities.
	 */
	private final Array<FallingTexturedEntity> activeFallingEntities;
	/**
	 * The pool that holds all the 'dead' or inactive FallingTextureEntities.
	 */
	private final Pool<FallingTexturedEntity> fallingEntityPool;
	
	private Texture fallingEntityTexture;
	
	public FallingEntitySpawner(Texture fallingEntityTexture) {
		super.init(0, 0);
		lastEntitySpawn = 0;
		activeFallingEntities = new Array<FallingTexturedEntity>();
		fallingEntityPool = new Pool<FallingTexturedEntity>() {
			@Override
			protected FallingTexturedEntity newObject() {
				return new FallingTexturedEntity();
			}
		}; 
		this.fallingEntityTexture = fallingEntityTexture;
	}

	public void render(SpriteBatch batch) {
		for(FallingTexturedEntity entity : activeFallingEntities) {
			entity.render(batch);
		}
	}
	
	@Override
	public void update(float delta) {
		if(TimeUtils.millis() - lastEntitySpawn > SPAWN_TIME) {
			spawnFallingEntity(delta);
		}
		for(FallingTexturedEntity entity : activeFallingEntities) {
			entity.update(delta);
			if(!entity.alive)
				freeFallingEntity(entity);
		}
	}
	
	public void dispose() {
		activeFallingEntities.clear();
		fallingEntityPool.clear();
	}
	
	/**
	 * removes the falling entity from the Array of active falling entities to the Pool of dead falling entities.
	 * @param entity - FallingTexturedEntity
	 */
	private void freeFallingEntity(FallingTexturedEntity entity) {
		for(int i = activeFallingEntities.size; i-- > 0;) {
			entity = activeFallingEntities.get(i);
			if(!entity.alive) {
				activeFallingEntities.removeIndex(i);
				fallingEntityPool.free(entity);
			}
		}
	}
	
	/**
	 * Spawns a new Falling entity if no falling entities exist in the pool.
	 * @param delta
	 */
	private void spawnFallingEntity(float delta) {
		lastEntitySpawn = TimeUtils.millis();
		if(fallingEntityPool.getFree() > 0) {
			FallingTexturedEntity entity = fallingEntityPool.obtain();
			entity.init(fallingEntityTexture, getRandomXPos(), getRandomYPos(), ENTITY_WIDTH, ENTITY_HEIGHT);
			entity.setSpeed(FALL_SPEED);
			activeFallingEntities.add(entity);
		} else {
			FallingTexturedEntity entity = new FallingTexturedEntity();
			entity.init(fallingEntityTexture, getRandomXPos(), getRandomYPos(), ENTITY_WIDTH, ENTITY_HEIGHT);
			entity.setSpeed(FALL_SPEED);
			activeFallingEntities.add(entity);
		}
	}
	
	private float getRandomXPos() {
		return (float)(Math.random() * (Constants.GAME_WIDTH - ENTITY_WIDTH));
	}
	
	private float getRandomYPos() {
		return (float) (Math.random() * (Constants.GAME_HEIGHT + ENTITY_HEIGHT)) + Constants.GAME_HEIGHT; //We want the Entity to spawn above the screen, out of sight.
	}
	
	public void checkCollisionsWithPlayer(PlayerTexturedEntity player) {
		for(FallingTexturedEntity entity : getActiveEntities(player.getX(), player.getY(), player.getWidth() * 4)) {
			if(player.getRect().overlaps(entity.getRect())) {
				player.subtractLife();
				entity.alive = false;
				freeFallingEntity(entity);
			}
		}
	}
	
	public Array<FallingTexturedEntity> getActiveEntities(float x, float y, float distance) {
		Array<FallingTexturedEntity> entities = new Array<FallingTexturedEntity>();
		for(FallingTexturedEntity entity : activeFallingEntities) {
			if(Utils.getDistance(x, y, entity.getX(), entity.getY()) < distance) 
				entities.add(entity);
		}
		return entities;
	}
	
	public Array<FallingTexturedEntity> getActiveEntities() {
		return activeFallingEntities;
	}

}
