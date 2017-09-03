package example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import core.GameEngine;
import entities.Animation;
import handlers.CameraHandler;
import handlers.GameHandler;
import handlers.Handler;
import handlers.MapHandler;

public class test {

	public static void main(String[] args) {
		GameEngine engine = new GameEngine();  // Create Game Engine
		engine.setGameTitle("Game");           // Set Game Title
		engine.setGameHeight(9);               // Setup aspect ratio
		engine.setGameWidth(16);               // 16x9
		engine.setGameScale(75);
		engine.setBackground(Color.WHITE);     // Set Background Color
		engine.setTargetFPS(120.0);            // Set Desired FPS
		engine.setGameHertz(120.0);            // Set Target Refresh Rate
		//engine.enableMinimap(true);            // Enable a minimap

		Player p = new Player();  // Create Player
		((GameHandler) engine.getHandler(GameEngine.GAME_HANDLER)).addGameObject(p);     // Add player to game
		((CameraHandler) engine.getHandler(GameEngine.CAMERA_HANDLER)).followObject(p);  // Attach camera to player
		
		// Create alot of water to show how FPS isn't really effected
		Water w;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				System.out.println(i + "\t" + j);
				w = new Water(500 + 100*i, j*100);
				((GameHandler) engine.getHandler(GameEngine.GAME_HANDLER)).addGameObject(w);  // Add water to game
				w.getAnimation().setSpeed(Animation.MEDIUM); // Set animation speed to Medium
			}
		}
		Tree t = new Tree(-200, 100);
		((GameHandler) engine.getHandler(GameEngine.GAME_HANDLER)).addGameObject(t);
		
		// Create a blue block
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, 100, 100);
		
		// Create a green block
		BufferedImage img2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) img2.getGraphics();
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 100, 100);
		
		// Setup map
		MapHandler map = (MapHandler) GameEngine.getGameEngine().getHandler(GameEngine.MAP_HANDLER);
		map.registerImage(0, img); // Assign blue block ID 0
		map.registerImage(1, img2); // Assign green block ID 1
		// Values represent block IDs
		map.setMap(new int[][] {
			{0, 0, 0},
			{0, 1, -1, 1},
			{1, 0},
			{0, 0}
		});
		
		Handler game = engine.getHandler(GameEngine.GAME_HANDLER); // Save a reference to the original game handler
		engine.setHandler(GameEngine.GAME_HANDLER, new MainMenu(game)); // Set the game handler to the main menu
		
		engine.setup(); // Setup Window and start game
	}

}
