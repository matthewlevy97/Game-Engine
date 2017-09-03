package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Minimap {
	
	private int x, y;
	private int size;
	
	public Minimap(int size, int x, int y) {
		this.size = size;
		this.x = x;
		this.y = y;
	}
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getSize() { return size; }
	
	public BufferedImage createMiniMap(BufferedImage map) {
		BufferedImage mini = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		Graphics g = mini.createGraphics();
		g.drawImage(map, 0, 0, size, size, null);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, size-1, size-1);
		g.dispose();
		
		return mini;
	}
	
}
