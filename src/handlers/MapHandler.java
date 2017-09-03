package handlers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import core.GameEngine;
import utils.Debug;

public class MapHandler extends Handler {

	private int bufferSize; // Length of group of blocks to render
	private int blockSize;  // Size of block in pixels
	private int updateDistance;  // Number of blocks needed to move past center to update
	private int lastX, lastY;  // x and y position of last map update

	private int x, y;

	private int[][] map;
	private HashMap<Integer, BufferedImage> blocks;

	private BufferedImage mapImage;
	private Graphics2D g;

	@Override
	public void setup() {
		bufferSize     = 60;
		blockSize      = 100;
		updateDistance = 10;
		lastX          = 9999;
		lastY          = 9999;

		x = 0;
		y = 0;

		map        = null;
		blocks     = new HashMap<Integer, BufferedImage>();

		mapImage   = null;
		g          = null;
	}

	@Override
	public void update() {
		if(map == null) { return; }
		if(mapImage == null) {
			int width = Math.min(blockSize, map.length);
			int height = Math.min(blockSize, map[0].length);

			mapImage = new BufferedImage(width * blockSize, height * blockSize, BufferedImage.TYPE_INT_ARGB);
			g = (Graphics2D) mapImage.getGraphics();
		}

		CameraHandler camera = (CameraHandler) GameEngine.getGameEngine().getHandler(GameEngine.CAMERA_HANDLER);
		int centerX = camera.getCenterX();
		int centerY = camera.getCenterY();

		if(Math.sqrt(Math.pow(centerX - lastX, 2) + Math.pow(centerY - lastY, 2)) < updateDistance * blockSize) { return; }
		lastX = centerX;
		lastY = centerY;

		// Correct centerX and centerY to be upper left corner to start in
		// This is scoped because it is
		{
			int temp = - bufferSize / 2 * blockSize;
			centerX = centerX + temp;
			centerY = centerY + temp;
		}

		for(int i = 0, l = map.length; i < l; i++) {
			int i2 = x + lastX + i*blockSize;

			for(int j = 0, l2 = map[i].length; j < l2; j++) {
				if (map[i][j] == -1) { continue; }
				BufferedImage block = blocks.get(map[i][j]);
				
				if(block != null) {
					g.drawImage(block, i2, y + j*blockSize, blockSize, blockSize, null);
				} else {
					Debug.println("Unknown block with ID: " + map[i][j]);
				}
			}
		}
	}

	/**
	 * Returns null if no map loaded
	 * 
	 * @return
	 */
	public BufferedImage getMapImage() {
		if(map == null) { return null; }
		return mapImage;
	}
	public int[][] getMap() { return map; }
	public HashMap<Integer, BufferedImage> getRegisteredBlocks() { return blocks; }

	public int getX() { return x; }
	public int getY() { return y; }
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}

	public void setMap(int[][] map) {
		// This method is large, but robost
		// We need to rotate the map because of the way things are drawn
		// Lets go
		int largest = 0;
		for (int i = 0, k = 0; i < map.length; i++) {
			k = map[i].length;
			if (largest < k) {
				largest = k;
			}
		}
		
		this.map = new int[largest][map.length];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				this.map[j][i] = map[i][j];
			}
		}
	}

	public boolean registerImage(int id, BufferedImage img) {
		if(!blocks.containsKey(id)) {
			blocks.put(id, img);
			return true;
		}
		return false;
	}

	@Override
	public void onAdd() {}

	@Override
	public void onRemove() {}
}
