package handlers;

import java.awt.Rectangle;

import core.GameEngine;
import entities.GameObject;

/**
 * 
 * TODO:
 * 
 * Currently very jumpy when following object is moving
 *
 */
public class CameraHandler extends Handler {
	
	private GameObject goLast;
	private GameObject go;

	private int x, y;
	private int centerX, centerY;
	private int width, height;
	
	private Rectangle viewPort;

	private boolean fixed;

	public CameraHandler() {
		this(null);
	}

	/**
	 * 
	 * @param go - GameObject to follow
	 * @param handler
	 */
	public CameraHandler(GameObject go) {
		super();
		followObject(go);
	}

	@Override
	public void setup() {
		this.go = null;
		this.goLast = null;

		this.x = 0;
		this.y = 0;

		this.width = GameEngine.getGameEngine().getGameWidth();
		this.height = GameEngine.getGameEngine().getGameHeight();
		
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;
		
		this.fixed = true;
		
		// Extra width and height to allow preloading GameObjects to draw
		viewPort = new Rectangle(x-50, y-50, width + 100, height + 100);
	}

	public void followObject(GameObject go) {
		if(go != null) {
			this.fixed = false;
			
			this.centerX = (width - go.getWidth()) / 2;
			this.centerY = (height - go.getHeight()) / 2;
		}

		this.goLast = this.go;
		this.go = go;
	}
	
	public void followLastObject() {
		followObject(this.goLast);
	}

	// Returns if an object is within the camera's view
	public boolean isShowing(GameObject go) {
		return viewPort.intersects(go.getBoundingBox());
	}
	
	protected GameObject getFollowing() {
		return go;
	}
	
	/**
	 * Just follow the selected object
	 */
	public void update() {
		if(go != null && !fixed) {
			x = -go.getX() + centerX;
			y = -go.getY() + centerY;
			
			// Add 50 to allow preloading GameObjects to draw
			viewPort.x = -(x + 50);
			viewPort.y = -(y + 50);
		}
	}

	// GETTERS
	public boolean isFixed() {
		return fixed;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public int getCenterX() {
		return centerX - x;
	}
	public int getCenterY() {
		return centerY - y;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	// SETTERS
	public void fixCamera() {
		fixCamera(x, y);
	}
	public void fixCamera(int x, int y) {
		this.x = x;
		this.y = y;

		this.fixed = true;
	}

	public void releaseCamera() {
		this.fixed = false;
	}

	@Override
	public void onAdd() {}

	@Override
	public void onRemove() {}
}
