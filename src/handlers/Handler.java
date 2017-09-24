package handlers;

public abstract class Handler extends Object {
	
	public Handler() {
		setup();
	}
	
	// ABSTRACT METHODS
	public abstract void setup();
	public abstract void update();
	
	public abstract void onAdd();
	public abstract void onRemove();
}
