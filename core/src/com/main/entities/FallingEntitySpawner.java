package com.main.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.main.Constants;
import com.main.utils.Utils;

public class FallingEntitySpawner extends Entity{

	@SuppressWarnings("unused")
	private static final String TAG = "FallingEntitySpawner";
	
	public static final float ENTITY_WIDTH = 64, ENTITY_HEIGHT = 64;
	/**
	 * SPAWN_TIME is in Milliseconds. 1000 millis = 1 second.
	 */
	private static final long SPAWN_TIME = 300;
	private static final float INIT_FALL_SPEED = 300.0f;
	/**
	 * How fast the falling Entities fall down the screen.
	 */
	private static float fallSpeed = INIT_FALL_SPEED;
	
	
	private long lastEntitySpawn;
	/**
	 * The pool that holds all the currently Falling FallingTextureEntities.
	 */
	private final Array<FallingTexturedEntity> activeFallingEntities;
	/**
	 * The pool that holds all the 'dead' or inactive FallingTextureEntities.
	 */
	private final Pool<FallingTexturedEntity> fallingEntityPool;
	
	/**
	 * The slots for Falling Entities to spawn in.
	 */
	private final Array<SlotEntity> slotEntities;
	
	private Texture fallingEntityTexture;
	private Entity player;
	
	public FallingEntitySpawner(Texture fallingEntityTexture, Entity player) {
		super.init(0, 0);
		this.fallingEntityTexture = fallingEntityTexture;
		this.player = player;
		lastEntitySpawn = 0;
		activeFallingEntities = new Array<FallingTexturedEntity>();
		fallingEntityPool = new Pool<FallingTexturedEntity>() {
			@Override
			protected FallingTexturedEntity newObject() {
				return new FallingTexturedEntity();
			}
		}; 
		this.slotEntities = new Array<SlotEntity>();
		for(int i = 0; i < Gdx.graphics.getWidth() / ENTITY_WIDTH; i++) {
			slotEntities.add(new SlotEntity());
		}
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
			if(!entity.alive(player))
				freeDeadFallingEntities(entity);
		}
		updatePlayerMaxHeight();
	}
	
	public void dispose() {
		activeFallingEntities.clear();
		fallingEntityPool.clear();
		slotEntities.clear();
		fallSpeed = INIT_FALL_SPEED;
		lastEntitySpawn = 0;
		SlotEntity.resetSlots();
	}
	
	/**
	 * Updates the player's max height.
	 * The max height is found by taking 
	 * ((the smallest slotEntity Spawns * {@value #ENTITY_HEIGHT}) + 
	 * (the largest slotEntity Spawns * {@value #ENTITY_HEIGHT})) / 2
	 */
	private void updatePlayerMaxHeight() {
		float maxHeight = Constants.GAME_HEIGHT - ENTITY_HEIGHT;
		float minHeight = 0;
		for(int i = 0; i < activeFallingEntities.size; i++) {
			if(getLargestSlotSize() * ENTITY_HEIGHT > maxHeight) {
				maxHeight = getLargestSlotSize() * ENTITY_HEIGHT ;
			} else if(getSmallestSlotSize() * ENTITY_HEIGHT < minHeight) {
				minHeight = getSmallestSlotSize() * ENTITY_HEIGHT;
			}
		}
		((PlayerTexturedEntity) this.player).setMaxHeight((maxHeight + minHeight) / 2);
	}
	
	/**
	 * Frees all dead FallingEntities from the activeFallingEntities List.
	 * @param deadEntity - deadEntity is used to conserve memory.
	 */
	public void freeDeadFallingEntities(FallingTexturedEntity deadEntity) {
		for(int i = activeFallingEntities.size; i-- > 0;) {
			deadEntity = activeFallingEntities.get(i);
			if(!deadEntity.alive(player)) {
				activeFallingEntities.removeIndex(i);
				fallingEntityPool.free(deadEntity);
			}
		}
	}
	
	/**
	 * removes the falling entity from the Array of active falling entities to the pool of dead falling entites.
	 * @param entity
	 */
	public void freeFallingEntity(FallingTexturedEntity entity) {
		for(int i = activeFallingEntities.size; i-- > 0;) {
			if(activeFallingEntities.get(i).equals(entity)) {
				activeFallingEntities.removeIndex(i);
				fallingEntityPool.free(entity);
				break;
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
			Vector2 xPosIndex = getRandomXPos();
			entity.init(fallingEntityTexture, xPosIndex.x, getRandomYPos() + player.getY(), 
					ENTITY_WIDTH, ENTITY_HEIGHT, slotEntities.get((int)xPosIndex.y).getTotalSpawns() * ENTITY_HEIGHT);
			entity.setSpeed(fallSpeed);
			activeFallingEntities.add(entity);
			checkCollisionsWithEntity(entity);
		} else {
			FallingTexturedEntity entity = new FallingTexturedEntity();
			Vector2 xPosIndex = getRandomXPos();
			entity.init(fallingEntityTexture, xPosIndex.x, getRandomYPos() + player.getY(), 
					ENTITY_WIDTH, ENTITY_HEIGHT, (slotEntities.get((int)xPosIndex.y).getTotalSpawns() - 1) * ENTITY_HEIGHT);
			entity.setSpeed(fallSpeed);
			activeFallingEntities.add(entity);
			checkCollisionsWithEntity(entity);
		}
		fallSpeed += delta * 25;
	}
	
	private Vector2 getRandomXPos() {
		int rand = (int) Math.floor(Math.random() * slotEntities.size);
		SlotEntity se = slotEntities.get(rand);
		se.addSpawn();
		return new Vector2(se.getX(), rand);
	}
	
	private float getRandomYPos() {
		return (float) (Math.random() * (Constants.GAME_HEIGHT + ENTITY_HEIGHT)) + Constants.GAME_HEIGHT; //We want the Entity to spawn above the screen, out of sight.
	}
	
	private int getLargestSlotSize() {
		int largest = 0;
		for(SlotEntity slot : slotEntities) {
			if(slot.getTotalSpawns() > largest) largest = slot.getTotalSpawns();
		}
		return largest;
	}
	
	private int getSmallestSlotSize() {
		int smallest = 0;
		for(SlotEntity slot : slotEntities) {
			if(slot.getTotalSpawns() < smallest) smallest = slot.getTotalSpawns();
		}
		return smallest;
	}
	
	/**
	 * Checks and entity's rectangle with all the surrounding entities. If it is overlapping any of the other entities' rectangles, then
	 * it is removed and the lastEntitySpawn is set to 0 therefore another one may respawn immediately.
	 * @param delta
	 * @param entity
	 */
	private void checkCollisionsWithEntity(FallingTexturedEntity entity) {
		for(FallingTexturedEntity fallingEntity : getActiveEntities(entity.getX(), entity.getY(), ENTITY_WIDTH * 2)) {
			if(Utils.equalFloats(entity.getX(), entity.getY()) && 
					(entity.getY() + ENTITY_HEIGHT > fallingEntity.getY() && entity.getY() < fallingEntity.getY())){
				freeFallingEntity(entity);	
				lastEntitySpawn = 0;
				break;
			}
		}
	}
	
	public void checkCollisionsWithPlayer(PlayerTexturedEntity player) {
		for(FallingTexturedEntity entity : getActiveEntities(player.getX(), player.getY(), player.getWidth() * 4)) {
			if(player.getRect().overlaps(entity.getRect())) {
				player.subtractLife();
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
