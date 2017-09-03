package physics;

import entities.GameObject;

public class Gravity {
	
	private static double gravity = 0;
	
	public double getGravity() {
		return gravity;
	}
	public void setGravity(double gravity) {
		Gravity.gravity = gravity;
	}
	
	public void applyGravity(GameObject go) {
		go.setGravity(gravity);
	}
}
