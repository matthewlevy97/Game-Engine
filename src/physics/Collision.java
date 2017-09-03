package physics;

import entities.GameObject;

public class Collision {
	
	public static boolean isColliding(GameObject go1, GameObject go2) {
		if(go1.equals(go2) || go1 == null || go2 == null) { return false; }
		return go1.getBoundingBox().intersects(go2.getBoundingBox());
	}

}