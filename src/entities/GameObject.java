package entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.UUID;

public abstract class GameObject {

	protected String object_uuid;

	protected boolean drawable;
	protected boolean movable;
	protected boolean remove;

	protected int x, y;
	protected int width, height;
	protected int dx, dy;
	protected int speed;
	protected double gravity;

	protected float interpolation;

	protected boolean autoAnimate;

	protected BufferedImage sprite;
	protected Animation animation;

	protected Rectangle boundingBox;
	protected int xOffset, yOffset;

	public GameObject(int x, int y) {
		this(x, y, 1, 1);
	}
	public GameObject(int x, int y, int width, int height) {
		this.object_uuid = UUID.randomUUID().toString();

		this.drawable = false;
		this.movable = true;
		this.remove = false;

		this.x = x;
		this.y = y;

		this.width = width > 0 ? width : 1;
		this.height = height > 0 ? height : 1;

		this.dx = 0;
		this.dy = 0;
		this.speed = 1;
		this.gravity = 0;
		
		this.interpolation = 0f;
		this.autoAnimate = false;
		
		this.sprite = null;
		this.animation = null;

		this.boundingBox = new Rectangle(x, y, width, height);
		this.xOffset = 0;
		this.yOffset = 0;
	}

	// ABSTRACT METHODS
	public abstract void update();
	public abstract void collidingObject(GameObject go);

	// DRAW METHOD
	public void draw(Graphics2D g) {
		if(drawable) {
			Image img = null;
			if(sprite != null) {
				img = sprite;
			} else if(animation != null) {
				img = animation.getImage();

				// Only move animation slide if moving
				if(autoAnimate || (dx | dy) != 0) {
					animation.nextSlide();
				} else {
					animation.setSlide(0);
				}
			}

			if(img != null) {
				int tempX = (int) (dx * interpolation + (x - dx));
				int tempY = (int) (dy * interpolation + (y - dy));
				g.drawImage(img, tempX, tempY, width, height, null);

				// For Debugging Collisions
				//g.setColor(Color.blue);
				//g.draw(boundingBox);
			}
		}
	}

	// GETTERS
	public String getUUID() { return object_uuid; }

	public boolean isMovable() { return movable; }
	public boolean isDrawable() { return drawable; }

	public int getX() { return x; }
	public int getY() { return y; }

	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public int getDx() { return dx; }
	public int getDy() { return dy; }

	public Image getSprite() { return sprite; }
	public Animation getAnimation() { return animation; }

	public boolean isAutoAnimating() { return autoAnimate; }

	public Rectangle getBoundingBox() {
		boundingBox.x = x + xOffset;
		boundingBox.y = y + yOffset;
		return boundingBox;
	}

	/**
	 * Returns true if this object should be removed
	 * 
	 * @return
	 */
	public boolean remove() {
		return remove;
	}

	// SETTERS
	public void setObjectUUID(String uuid) {
		this.object_uuid = uuid;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public void setInterpolation(float interpolation) {
		this.interpolation = interpolation;
	}

	public void setWidth(int width) {
		this.width = (width > 0 ? width : 1);
		boundingBox.width = this.width;
	}
	public void setHeight(int height) {
		this.height = (height > 0 ? height : 1);
		boundingBox.height = this.height;
	}

	public void autoAnimate(boolean animate) {
		this.autoAnimate = animate;
	}

	public void setBoundingBoxSize(int xOffset, int yOffset, int width, int height) {
		if(xOffset > 0) {
			this.xOffset = xOffset;
		}

		if(yOffset > 0) {
			this.yOffset = yOffset;
		}

		if(width > 0) {
			boundingBox.width = width;
		}

		if(height > 0) {
			boundingBox.height = height;
		}
	}

	public void setSprite(BufferedImage sprite) {
		if(sprite != null) {
			this.sprite = sprite;
			this.animation = null;
			this.drawable = true;
		}
	}
	public void setAnimation(Animation animation) {
		if(animation != null) {
			this.animation = animation;
			this.sprite = null;
			this.drawable = true;
		}
	}

	public void setGravity(double gravity) {
		this.gravity = gravity;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof GameObject)) {
			return false;
		}
		if(((GameObject) obj).getUUID().equals(this.object_uuid)) {
			return true;
		}
		
		return false;
	}
}
