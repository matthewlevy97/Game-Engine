package handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class IOHandler extends Handler implements KeyListener {
	
	private boolean[] pressedKeys = new boolean[KeyEvent.KEY_LAST];
	
	@Override
	public void setup() {}
	@Override
	public void update() {}
	
	public boolean isPressed(int key) {
		if(key > 0 && key < pressedKeys.length) {
			return pressedKeys[key];
		}
		return false;
	}
	
	public void toggleKey(int key, boolean b) {
		if(key > 0 && key < pressedKeys.length) {
			pressedKeys[key] = b;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		pressedKeys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		pressedKeys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	@Override
	public void onAdd() {}

	@Override
	public void onRemove() {}
}
