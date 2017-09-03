package handlers;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import core.GameEngine;
import entities.GameObject;
import example.Player;
import physics.Collision;
import utils.Debug;

public class GameHandler extends Handler {

	protected HashMap<String, GameObject> gameObjects;
	protected ArrayList<GameObject> visibleObjects;

	protected CameraHandler camera;

	private float interpolation;

	@Override
	public void setup() {
		gameObjects = new HashMap<String, GameObject>();
		visibleObjects = new ArrayList<GameObject>();

		camera = (CameraHandler) GameEngine.getGameEngine().getHandler(GameEngine.CAMERA_HANDLER);
	}

	// GETTERS
	public float getInterpolation() {
		return this.interpolation;
	}

	// SETTERS
	public void setInterpolation(float interpolation) {
		this.interpolation = interpolation;
	}

	// UPDATE AND DRAW
	public void update() {
		visibleObjects.clear();

		String[] keys = gameObjects.keySet().toArray(new String[gameObjects.size()]);
		for(int i = 0, j = keys.length; i < j; i++) {
			GameObject go = gameObjects.get(keys[i]);
			go.update();

			// If object needs to be removed, remove it
			if(go.remove()) {
				Debug.println("Removing Object With UUID: " + go.getUUID());
				gameObjects.remove(keys[i]);
				--i;
				continue;
			}

			if(camera.isShowing(go)) {
				visibleObjects.add(go);
			}

			// TODO: Find Better Algorithm
			if((go.getDx() | go.getDy()) != 0) {
				for(int k = 0; k < j; k++) {
					if(Collision.isColliding(go, gameObjects.get(keys[k]))) {
						go.collidingObject(gameObjects.get(keys[k]));
						gameObjects.get(keys[k]).collidingObject(go);
					}
				}
			}
		}

	}
	public void draw(Graphics2D g) {
		// Draw each object
		GameObject player = null;
		for(int i = 0, j = visibleObjects.size(); i < j; i++) {
			GameObject go = visibleObjects.get(i);
			if (go instanceof Player) {
				player = go;
				continue;
			}
			go.setInterpolation(interpolation);
			go.draw(g);
		}
		if (player != null) {
			player.draw(g);
		}
	}

	// ADD / GET / REMOVE GameObject
	public void getGameObject(String gameObjectName) {
		gameObjects.get(gameObjectName);
	}
	public void addGameObject(GameObject go) {
		gameObjects.put(go.getUUID(), go);
	}
	public void addGameObject(String gameObjectName, GameObject go) {
		gameObjects.put(gameObjectName, go);
	}
	public void removeGameObject(String gameObjectName) {
		gameObjects.remove(gameObjectName);
	}
	public Set<String> getGameObjectNames() {
		return gameObjects.keySet();
	}
	
	@Override
	public void onAdd() {}

	@Override
	public void onRemove() {}
}
