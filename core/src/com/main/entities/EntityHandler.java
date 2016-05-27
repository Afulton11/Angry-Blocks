package com.main.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class EntityHandler {

	private static final String TAG = EntityHandler.class.getSimpleName();
	
	public Array<Entity> entities;
	
	public EntityHandler() {
		this.entities = new Array<Entity>();
	}
	
	public void render(SpriteBatch batch) {
		for(Entity e : entities) {
			e.render(batch);
		}
	}
	
	public void update(float delta) {
		for(Entity e : entities) {
			e.update(delta);
		}
		checkEntityCollisions();
	}
	
	public void dispose() {
		for(Entity e : entities) {
			e.dispose();
		}
		entities.clear();
	}
	
	private void checkEntityCollisions() {
		PlayerTexturedEntity player = getPlayer();
		for(Entity e : entities) {
			if(e instanceof FallingEntitySpawner) {
				FallingEntitySpawner spawner = (FallingEntitySpawner) e;
				for(FallingTexturedEntity fallingEntity : spawner.getActiveEntities(player.getX(), player.getY(), player.getWidth() * 4)) {
					if(fallingEntity.isFalling() && player.getRect().overlaps(fallingEntity.getRect())) {
						player.subtractLife();
						spawner.freeFallingEntity(fallingEntity);
					} else if(!fallingEntity.isFalling() &&player.getRect().overlaps(fallingEntity.getRect()))	 {
						for(int i = player.getLives(); i-- > 0;) {
							player.subtractLife();
						}
					}
				}
			}
		}
	}
	
	/**
	 * adds an entity to the list of entities. The Player should be at index 0 for efficiency.
	 * @param entities
	 */
	public void addEntities(Entity... entities) {
		this.entities.addAll(entities);
	}
		
	public Entity getEntity(int index) {
		return entities.get(index);
	}
	
	/**
	 * gets the player from the list of entities. If no player is found, it will return null.
	 * @return
	 */
	public PlayerTexturedEntity getPlayer() {
		if(entities.get(0) instanceof PlayerTexturedEntity) {
			return (PlayerTexturedEntity) entities.get(0);
		} else {
			for(Entity e : entities) {
				if(e instanceof PlayerTexturedEntity){
					return (PlayerTexturedEntity) e;
				}
			}
		}
		return null;
	}

}
