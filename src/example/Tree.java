package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import entities.GameObject;
import utils.Debug;
import utils.Sprites;

public class Tree extends GameObject {

	public Tree(int x, int y) {
		super(x, y, 255, 100);
		
		try {
			BufferedImage s = Sprites.loadSpriteSheet("C:\\Users\\levym\\Downloads\\Sprite Sheets\\big_tree.png");
			
			setSprite(Sprites.getSpriteStrip(s, 0, 0, 1, 255, 255)[0]);
		} catch (IOException e) {
			Debug.println("Error loading sprite");
			e.printStackTrace();
		}
	}

	@Override
	public void update() {}

	@Override
	public void collidingObject(GameObject go) {}

}
