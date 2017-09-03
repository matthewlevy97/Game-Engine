package handlers;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import core.GameEngine;
import entities.GameObject;

public abstract class OptionMenuHandler extends GameHandler {
	
	protected GameObject cameraOriginalObject;
	protected ArrayList<String> options;
	protected int selectedOption;
	
	protected int upKey, downKey, enterKey;
	
	public OptionMenuHandler() {
		super();
		
		// Change camera settings
		cameraOriginalObject = this.camera.getFollowing();
		this.camera.fixCamera(0, 0);

		this.options = new ArrayList<String>();
		selectedOption = -1;
		
		upKey = KeyEvent.VK_W;
		downKey = KeyEvent.VK_S;
		enterKey = KeyEvent.VK_ENTER;
	}

	public String getSelected() {
		if (selectedOption >= 0 && selectedOption < options.size()) {
			return options.get(selectedOption);
		}
		return null;
	}

	public void setSelectedOption(int selected) {
		// Short circuiting FTW
		if (selected < 0 || selected >= options.size()) {
			return;
		}
		selectedOption = selected;
	}

	@Override
	public void update() {
		// Get the IO Handler
		IOHandler io = (IOHandler) GameEngine.getGameEngine().getHandler(GameEngine.IO_HANDLER);

		if (io.isPressed(upKey)) {
			io.toggleKey(upKey, false);
			selectedOption--;
		} else if (io.isPressed(downKey)) {
			io.toggleKey(downKey, false);
			selectedOption++;
		} else if (io.isPressed(enterKey)) {
			io.toggleKey(enterKey, false);
			optionSelected(selectedOption);
		}
		
		if (selectedOption < 0) {
			selectedOption = options.size() - 1;
		}
		if (selectedOption >= options.size()) {
			selectedOption = 0;
		}
		
		System.out.println(selectedOption);
	}
	
	protected abstract void optionSelected(int selectedOption);
	
	@Override
	public abstract void draw(Graphics2D g);
}
