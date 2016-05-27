package com.main.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * SlotEntity.java
 * 
 * A slot Entity acts as a 'slot' for an entity to spawn on.
 * 
 * @Author Andrew Fulton
 * Created on: May 26, 2016 at 2:22:32 PM
 */
public class SlotEntity extends Entity{

	/**
	 * The total number of SlotEntities in the game.
	 */
	public static int numSlots = 0;
	
	private int totalSpawns;
	
	public SlotEntity() {
		super.init(FallingEntitySpawner.ENTITY_WIDTH * numSlots, 0);
		totalSpawns = 0;
		numSlots++;
	}

	@Override
	public void render(SpriteBatch batch) {}

	@Override
	public void update(float delta) {}
	
	public void addSpawn() {
		totalSpawns++;
	}
	
	public int getTotalSpawns() {
		return totalSpawns;
	}
	
	public static void resetSlots() {
		numSlots = 0;
	}
}
