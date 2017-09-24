package example;

import java.awt.image.BufferedImage;
import java.io.IOException;

import entities.Animation;
import entities.GameObject;
import utils.Debug;
import utils.Sprites;

public class Water extends GameObject {

	public Water(int x, int y) {
		super(x, y, 100, 100);

		try {
			BufferedImage[][] animations = new BufferedImage[1][];
			BufferedImage sheet = Sprites.loadSpriteSheet("images//water.png");
			
			animations[0] = Sprites.getSpriteStrip(sheet, 0, 0, 15, 32, 32);
			
			Animation animation = new Animation(animations);
			
			this.setAnimation(animation);
			this.autoAnimate = true;
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
