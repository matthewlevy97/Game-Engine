package example;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import core.GameEngine;
import entities.Animation;
import entities.GameObject;
import handlers.IOHandler;
import utils.Debug;
import utils.Sprites;

public class Player extends GameObject {

	public Player() {
		super(0, 0, 100, 100);
		
		try {
			BufferedImage[][] animations = new BufferedImage[8][];
			
			animations[0] = Sprites.getSpriteStrip(sheet, 0, 0, 12, 128, 128);  // Left
			animations[1] = Sprites.getSpriteStrip(sheet, 1, 0, 12, 128, 128);  // Left-Up
			animations[2] = Sprites.getSpriteStrip(sheet, 2, 0, 12, 128, 128);  // Up
			animations[3] = Sprites.getSpriteStrip(sheet, 3, 0, 12, 128, 128);  // Right-Up
			animations[4] = Sprites.getSpriteStrip(sheet, 4, 0, 12, 128, 128);  // Right
			animations[5] = Sprites.getSpriteStrip(sheet, 5, 0, 12, 128, 128);  // Right-Down
			animations[6] = Sprites.getSpriteStrip(sheet, 6, 0, 12, 128, 128);  // Down
			animations[7] = Sprites.getSpriteStrip(sheet, 7, 0, 12, 128, 128);  // Left-Down
			
			Animation animation = new Animation(animations);
			
			this.setAnimation(animation);
		} catch (IOException e) {
			Debug.println("Error loading sprite");
			e.printStackTrace();
		}
		
		setBoundingBoxSize(25, 50, 50, 50);
		speed = 5;
	}

	@Override
	public void update() {
		IOHandler io = (IOHandler) GameEngine.getGameEngine().getHandler(GameEngine.IO_HANDLER);
		
		dx = dy = 0;
		
		if(io.isPressed(KeyEvent.VK_A)) {
			dx -= speed;
			
			if(io.isPressed(KeyEvent.VK_W)) {
				dy -= speed;
				animation.setDirection(1);
			} else if(io.isPressed(KeyEvent.VK_S)) {
				dy += speed;
				animation.setDirection(7);
			} else {
				animation.setDirection(0);
			}
		} else if(io.isPressed(KeyEvent.VK_D)) {
			dx += speed;
			
			if(io.isPressed(KeyEvent.VK_W)) {
				dy -= speed;
				animation.setDirection(3);
			} else if(io.isPressed(KeyEvent.VK_S)) {
				dy += speed;
				animation.setDirection(5);
			} else {
				animation.setDirection(4);
			}
		} else {
			if(io.isPressed(KeyEvent.VK_W)) {
				dy -= speed;
				animation.setDirection(2);
			}
			if(io.isPressed(KeyEvent.VK_S)) {
				dy += speed;
				animation.setDirection(6);
			}
		}
		
		x += dx;
		y += dy;
	}

	@Override
	public void collidingObject(GameObject go) {
		if (go instanceof Water) {
			return;
		}
		// Bounce Back
		x -= dx;
		y -= dy;
	}
	
	
}
