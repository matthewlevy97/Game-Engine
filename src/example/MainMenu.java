package example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import core.GameEngine;
import handlers.CameraHandler;
import handlers.Handler;
import handlers.OptionMenuHandler;

public class MainMenu extends OptionMenuHandler {

	private Handler game;

	public MainMenu(Handler game) {
		super();
		this.game = game;

		this.options.add("Start Game");
		this.options.add("Load Game");
		this.options.add("Credits");
		this.selectedOption = 0;
		
		this.enterKey = KeyEvent.VK_SPACE;
	}

	@Override
	public void draw(Graphics2D g) {
		// Generic drawing
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GameEngine.getGameEngine().getGameWidth(), GameEngine.getGameEngine().getGameHeight());

		g.setColor(Color.WHITE);
		int x = 250;
		int height = 100;
		int i = 1;
		for (String s : options) {
			if(i - 1 == selectedOption) {
				g.drawString(s + " *", x, height * i);
			} else {
				g.drawString(s, x, height * i);
			}
			i++;
		}
	}

	@Override
	protected void optionSelected(int selectedOption) {
		if (selectedOption == 0) {
			GameEngine.getGameEngine().setHandler(GameEngine.GAME_HANDLER, game);
			((CameraHandler) GameEngine.getGameEngine().getHandler(GameEngine.CAMERA_HANDLER)).followObject(this.cameraOriginalObject);
		}
	}

}
