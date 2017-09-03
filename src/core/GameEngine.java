package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import handlers.CameraHandler;
import handlers.GameHandler;
import handlers.Handler;
import handlers.IOHandler;
import handlers.MapHandler;
import utils.Minimap;

/**
 * TODO:
 * 
 * GUI screens and selecting options
 * Finish SoundHandler
 * Add Javadoc for all API calls
 * 
 */

public class GameEngine extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;

	// Public Constants
	public final static String CAMERA_HANDLER = "camera";
	public final static String GAME_HANDLER = "game";
	public final static String IO_HANDLER = "io";
	public final static String MAP_HANDLER = "map";

	// THIS
	private static GameEngine ge;

	// Private Constants
	private String GAME_TITLE;

	private int GAME_WIDTH;
	private int GAME_HEIGHT;
	private int GAME_SCALE;

	private double GAME_HERTZ;
	private double TARGET_FPS;
	private int MAX_UPDATES_BEFORE_RENDER; // Lower increases CPU usage

	private int SECONDS_BETWEEN_PAUSES;

	private Color BACKGROUND_COLOR;
	// --------------------------------------

	// Private Variables
	private JFrame window;

	private HashMap<String, Handler> handlers;

	private BufferedImage image;
	private Graphics2D g;

	private Minimap minimap;

	private Thread thread;

	private static boolean running;
	private boolean paused;
	private long lastPauseTime;

	private int fps;
	private int frameCount;

	// --------------------------------------

	/**
	 * Create a game
	 */
	public GameEngine() {
		this("Game Engine");
	}
	public GameEngine(String title) {
		super();
		
		GAME_TITLE = title;
		
		// So that we can statically grab this GameEngine
		ge = this;

		running = false;

		paused = false;
		lastPauseTime = 0;

		fps = 60;
		frameCount = 0;

		BACKGROUND_COLOR = Color.BLACK;

		GAME_WIDTH = 16;
		GAME_HEIGHT = 9;
		GAME_SCALE = 75;
		GAME_HERTZ = 30.0;
		TARGET_FPS = 60;
		MAX_UPDATES_BEFORE_RENDER = 5;
		SECONDS_BETWEEN_PAUSES = 1;

		handlers = new HashMap<String, Handler>();
		addHandler(IO_HANDLER, new IOHandler());
		addHandler(CAMERA_HANDLER, new CameraHandler());
		addHandler(MAP_HANDLER, new MapHandler());
		addHandler(GAME_HANDLER, new GameHandler());

		minimap = null;

		window = new JFrame();
		window.setVisible(false);
	}

	public void setup() {
		window.setTitle(GAME_TITLE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		window.setMinimumSize(new Dimension(GAME_WIDTH * GAME_SCALE, GAME_HEIGHT * GAME_SCALE));
		window.setMaximumSize(new Dimension(GAME_WIDTH * GAME_SCALE, GAME_HEIGHT * GAME_SCALE));
		window.setPreferredSize(new Dimension(GAME_WIDTH * GAME_SCALE, GAME_HEIGHT * GAME_SCALE));
		window.setSize(new Dimension(GAME_WIDTH * GAME_SCALE, GAME_HEIGHT * GAME_SCALE));
		window.setResizable(false);

		window.setContentPane(this);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		setFocusable(true);
		requestFocus();
		
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	/**
	 * Credit for game loop to: http://www.java-gaming.org/index.php?topic=24220.0
	 */
	public void run() {
		image = new BufferedImage(getGameWidth(), getGameHeight(), BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		final double TIME_BETWEEN_UPDATES = 1000000000.0 / GAME_HERTZ;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

		double lastUpdateTime = System.nanoTime();
		double lastRenderTime = System.nanoTime();
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		running = true;
		while(running) {
			double now = System.nanoTime();
			int updateCount = 0;

			if(((IOHandler) getHandler(IO_HANDLER)).isPressed(KeyEvent.VK_P) &&
					((System.nanoTime() - lastPauseTime) / 1000000000) >= SECONDS_BETWEEN_PAUSES) {
				paused = !paused;
				lastPauseTime = System.nanoTime();
			}

			if(!paused) {
				// Do as many game updates as we need to, potentially playing catch up.
				while(now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
					updateGame();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}

				if(now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}

				float interpolation = Math.min(1.0f,  (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES));
				drawGame(interpolation);
				lastRenderTime = now;

				int thisSecond = (int) (lastUpdateTime / 1000000000);
				if(thisSecond > lastSecondTime) {
					fps = frameCount;
					frameCount = 0;
					lastSecondTime = thisSecond;
				}

				while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
					Thread.yield();

					// This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
					// You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
					// FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
					try {Thread.sleep(1);} catch(Exception e) {} 

					now = System.nanoTime();
				}
			}
		}
	}
	private void updateGame() {
		for(String handlerName : handlers.keySet()) {
			handlers.get(handlerName).update();
		}
	}
	private void drawGame(float interpolation) {
		CameraHandler camera = (CameraHandler) getHandler(GameEngine.CAMERA_HANDLER);
		MapHandler map = (MapHandler) getHandler(GameEngine.MAP_HANDLER);
		BufferedImage img = null;

		// Translate image
		g.translate(camera.getX(), camera.getY());

		// Clear screen
		g.setColor(getBackgroundColor());
		g.fillRect(-camera.getX(), -camera.getY(), camera.getWidth(), camera.getHeight());
		
		img = map.getMapImage();
		if(img != null) {
			g.drawImage(img, map.getX(), map.getY(), null);
		}
		img = null;

		// Draw every game object
		((GameHandler)getHandler(GAME_HANDLER)).setInterpolation(interpolation);
		((GameHandler)getHandler(GAME_HANDLER)).draw(g);
		frameCount++;

		// Revert Translation
		g.translate(-camera.getX(), -camera.getY());

		// Create minimap
		if(minimap != null) {
			img = minimap.createMiniMap(image);
		}

		// Draw game onto screen
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);

		// Draw Mini Map if exists
		if(minimap != null) {
			g2.drawImage(img, minimap.getX(), minimap.getY(), null);
		}
		g2.dispose();
	}
	// END GAME LOOP

	// Stops Game
	public void stopGame() {
		running = false;
	}

	// Starts game when attached to JFrame
	public void addNotify() {
		super.addNotify();
		addKeyListener((IOHandler) getHandler(IO_HANDLER));
	}

	// GETTERS
	public int getFPS() { return fps; }

	// SETTERS
	public void pauseGame() {
		paused = !paused;
	}

	// ---------------------------------------------------------------
	// CONFIG OPTIONS
	public String getGameTitle() { return GAME_TITLE; }
	public int getGameWidth() { return GAME_WIDTH * GAME_SCALE; }
	public int getGameHeight() { return GAME_HEIGHT * GAME_SCALE; }
	public int getGameScale() { return GAME_SCALE; }
	public double getGameHertz() { return GAME_HERTZ; }
	public double getTargetFPS() { return TARGET_FPS; }
	public Color getBackgroundColor() { return BACKGROUND_COLOR; }

	public boolean setGameTitle(String title) {
		if(!GameEngine.running) { 
			GAME_TITLE = title;
		}
		return GameEngine.running;
	}
	/**
	 * Default is 16
	 * @param width - Will be multiplied by the game scale
	 * @return
	 */
	public boolean setGameWidth(int width) {
		if(!GameEngine.running) { 
			GAME_WIDTH = width;
		}
		return GameEngine.running;
	}
	/**
	 * Default is 9
	 * @param height - Will be multiplied by the game scale
	 * @return
	 */
	public boolean setGameHeight(int height) {
		if(!GameEngine.running) { 
			GAME_HEIGHT = height;
		}
		return GameEngine.running;
	}
	/**
	 * Default is 75
	 * @param scale
	 * @return
	 */
	public boolean setGameScale(int scale) {
		if(!GameEngine.running) { 
			GAME_SCALE = scale;
		}
		return GameEngine.running;
	}
	/**
	 * Default is 30
	 * @param hertz
	 * @return
	 */
	public boolean setGameHertz(double hertz) {
		if(!GameEngine.running) { 
			GAME_HERTZ = hertz;
		}
		return GameEngine.running;
	}
	/**
	 * Default is 60
	 * @param targetFPS
	 * @return
	 */
	public boolean setTargetFPS(double targetFPS) {
		if(!GameEngine.running) { 
			TARGET_FPS = targetFPS;
		}
		return GameEngine.running;
	}

	public void setIcon(BufferedImage img) {
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		frame.setIconImage(img);
	}

	public void setMinimap(Minimap minimap) {
		this.minimap = minimap;
	}
	public void enableMinimap(boolean b) {
		if(b && minimap == null) {
			minimap = new Minimap(100, 25, getGameHeight() - 150);
		} else if(!b) {
			minimap = null;
		}
	}

	@Override
	public void setBackground(Color c) {
		BACKGROUND_COLOR = c;
	}

	// ADD / GET / REMOVE Handlers
	public Handler getHandler(String handlerName) {
		return handlers.get(handlerName);
	}
	// Set and Add do the same thing, so just  call the other
	public void setHandler(String handlerName, Handler handler) {
		addHandler(handlerName, handler);
	}
	public void addHandler(String handlerName, Handler handler) {
		handlers.put(handlerName, handler);
	}
	public Handler removeHandler(String handlerName) {
		return handlers.remove(handlerName);
	}
	public Set<String> getHandlerNames() {
		return handlers.keySet();
	}

	// GET GameEngine
	public static GameEngine getGameEngine() {
		return ge;
	}
}
