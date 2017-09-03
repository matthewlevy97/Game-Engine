package entities;

import java.awt.Image;
import java.awt.image.BufferedImage;


public class Animation {

	public static final int FAST = 0;
	public static final int MEDIUM = 1;
	public static final int SLOW = 2;

	private BufferedImage[][] images;

	private int direction;
	private int slide;

	private int speed;
	private long tickCount;

	public Animation(BufferedImage[][] images) {
		loadImages(images);

		speed = 0;
		tickCount = System.nanoTime();
	}

	// SETTERS
	public void setDirection(int direction) {
		if(direction != this.direction) {
			this.slide = 0;
		}

		this.direction = direction;
	}
	public void setSlide(int slide) {
		this.slide = slide;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void loadImages(BufferedImage[][] images) {
		this.images = images;

		this.direction = 0;
		this.slide = 0;
	}

	// GETTERS
	public int getDirection() { return direction; }
	public int getSlide() { return slide; }
	public int getSpeed() { return speed; }
	public Image[][] getImages() { return images; }

	public void nextSlide() {
		switch(speed) {
		case MEDIUM:
			if(System.nanoTime() - tickCount > 0.5*1000000000) {
				slide = ++slide % images[direction].length;
				tickCount = System.nanoTime();
			}
			break;
		case SLOW:
			if(System.nanoTime() - tickCount > 1000000000) {
				slide = ++slide % images[direction].length;
				tickCount = System.nanoTime();
			}
			break;
		case FAST:
		default:
			slide = ++slide % images[direction].length;
			break;
		}
	}

	public BufferedImage getImage() {
		if(images == null) { return null; }

		if(direction < images.length) {
			if(slide < images[direction].length) {
				return images[direction][slide];
			}
		}

		return null;
	}
}
