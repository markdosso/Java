
/* Game.java
 * Space Invaders Main Program
 * Authors: Bryn Pearce, Mark Dosso, Yifan Lu
 * Date: Spring 2016
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Game extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferStrategy strategy; // take advantage of accelerated graphics
	private boolean waitingForKeyPress = true; // true if game held up until
												// a key is pressed
	private boolean showHelp = false;
	private boolean leftPressed = false; // true if left arrow key currently
											// pressed
	private boolean rightPressed = false; // true if right arrow key currently
											// pressed
	private boolean upPressed = false; // true if up arrow key currently pressed
	private boolean firePressed = false; // true if firing
	public boolean paused = false; // pause the game by pressing p
	public boolean pausePressed = false; // whether game is paused
	private boolean gameRunning = true;
	private ArrayList entities = new ArrayList(); // list of entities
	private int lives = 3;
	private ArrayList removeEntities = new ArrayList(); // list of entities
														// to remove this loop

	private Entity ship; // the ship
	private long lastFire = 0; // time last shot fired
	private long firingInterval = 300; // interval between shots (ms)
	private int entityCount = 0;
	private double rotationRequired = 0; // rotation degree
	private double velocity = 0;
	private int totalPoints = 0;
	private int invincibleCounter = 0;
	private int invincibleFlash = 0;
	/*
	 * Construct our game and set it running.
	 */

	public Game() {
		// create a frame to contain game
		JFrame container = new JFrame("Asteroids");

		// get hold the content of the frame
		JPanel panel = (JPanel) container.getContentPane();

		// set up the resolution of the game
		panel.setPreferredSize(new Dimension(800, 600));
		panel.setLayout(null);

		// set up canvas size (this) and add to frame
		setBounds(0, 0, 800, 600);
		panel.add(this);

		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);

		// make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// if user closes window, shutdown game and jre
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			} // windowClosing
		});

		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());

		// request focus so key events are handled by this canvas
		requestFocus();

		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// initialize entities
		initEntities();
		Entity splash = new ImageEntity(this, "sprites/splashscreen.jpg", 0, 0, 4);
		entities.add(splash);

		// start the game
		gameLoop();
	} // constructor

	/*
	 * initEntities input: none output: none purpose: Initialise the starting
	 * state of the ship and alien entities. Each entity will be added to the
	 * array of entities in the game.
	 */
	private void initEntities() {
		// create the ship and put in center of screen
		ship = new ShipEntity(this, "sprites/0.png", 370, 530);
		entities.add(ship);
		for (int i = 0; i < 5; i++) {
			int x;
			int y;

			do {
				x = (int) (Math.random() * 750) + 50;
				y = (int) (Math.random() * 550) + 50;

			} while (x >= 250 && x <= 500 && y >= 450 && y <= 600);
			Entity asteroid = new AsteroidEntity(this, "sprites/largeasteroid.png", x, y, 3);
			entities.add(asteroid);
			entityCount++;
		} // for

		/*
		 * for (int i = 0; i < 1; i++) { Entity alien = new AlienEntity(this,
		 * "sprites/alien.png", 100, 100, 4); entities.add(alien); } // for
		 */
	} // initEntities

	/*
	 * Remove an entity from the game. It will no longer be moved or drawn.
	 */
	public void removeEntity(Entity entity) {
		removeEntities.add(entity);
	} // removeEntity

	/*
	 * Notification that the player has died.
	 */
	public void notifyDeath() {
		// message = "The power of Bryn has destroyed you";
		lives--;
		rotationRequired = 0;
		if (lives <= 0) {
			//reset ship
			rotationRequired = 0;
			velocity = 0;
			entities.clear();
			
			Entity lose = new ImageEntity(this, "sprites/gameover.jpg", 0, 0, 4);
			entities.add(lose);
			waitingForKeyPress = true;
		} // if
		
		

	} // notifyDeath

	/*
	 * Notification than an alien has been killed
	 */
	public void notifyAlienKilled(Entity object) {
		entityCount--;
		int x = 0;
		int y = 0;

		x = object.getX();
		y = object.getY();

		if (object.type == 2) {
			for (int i = 0; i < 2; i++) {
				Entity asteroid = new AsteroidEntity(this, "sprites/smallasteroid.png", x, y, 1);
				entities.add(asteroid);
				entityCount++;
			} // for
		} else if (object.type == 3) {
			for (int i = 0; i < 2; i++) {
				Entity asteroid = new AsteroidEntity(this, "sprites/mediumasteroid.png", x, y, 2);
				entities.add(asteroid);
				entityCount++;
			} // for
		} // else
		if (entityCount <= 5) {
			spawnAlien();
		} // if

		if (object.type == 1) {
			totalPoints += 500;
		} else if (object.type == 2) {
			totalPoints += 200;
		} else if (object.type == 3) {
			totalPoints += 100;
		} else if (object.type == 4) {
			totalPoints += 300;
		} // else if
	} // notifyAlienKilled

	public void spawnAlien() {
		int x;
		int y;

		do {
			x = (int) (Math.random() * 750) + 50;
			y = (int) (Math.random() * 550) + 50;

		} while (x < ship.getX() + 100 && x > ship.getX() - 100 && y < ship.getY() + 100 && y > ship.getY() - 100);

		int rand1 = (int) (Math.random() * 10 + 1);

		if (rand1 == 7 || rand1 == 8 || rand1 == 9 || rand1 == 10) {
			Entity asteroid = new AsteroidEntity(this, "sprites/largeasteroid.png", x, y, 3);
			entities.add(asteroid);
		} else if (rand1 == 4 || rand1 == 5 || rand1 == 6) {
			Entity asteroid = new AsteroidEntity(this, "sprites/mediumasteroid.png", x, y, 3);
			entities.add(asteroid);
		} else if (rand1 == 2 || rand1 == 3) {
			Entity asteroid = new AsteroidEntity(this, "sprites/smallasteroid.png", x, y, 3);
			entities.add(asteroid);
		} else if (rand1 == 1) {
			Entity alien = new AlienEntity(this, "sprites/alien.png", x, y, 4);
			entities.add(alien);
		}

			entityCount++;
	} // spawnAlien

	/* Attempt to fire. */
	public void tryToFire() {
		// check that we've waited long enough to fire
		if ((System.currentTimeMillis() - lastFire) < firingInterval) {
			return;
		} // if

		// otherwise add a shot
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this, "sprites/shot.png", ship.getX() + 10, ship.getY() + 7, -500,
				rotationRequired, true);
		entities.add(shot);
	} // tryToFire

	/*
	 * gameLoop input: none output: none purpose: Main game loop. Runs
	 * throughout game play. Responsible for the following activities: -
	 * calculates speed of the game loop to update moves - moves the game
	 * entities - draws the screen contents (entities, text) - updates game
	 * events - checks input
	 */
	public void gameLoop() {
		long lastLoopTime = System.currentTimeMillis();
		// keep loop running until game ends
		while (gameRunning) {
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();
			if (pausePressed) {
				paused = true;
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} // try catch

				continue;
			} // if

			// calc. time since last update, will be used to calculate
			// entities movement

			// get graphics context for the accelerated surface and make it
			// black
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 600);
			g.setFont(new Font("Arial", Font.PLAIN, 20));

			// move each entity
			if (!waitingForKeyPress) {
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);
					entity.move(delta);

				} // for

			} // if

			// aliens shoot
			if (!waitingForKeyPress) {
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = (Entity) entities.get(i);
					int x = 0;
					int y = 0;
					// entity.getX() + 41
					// entity.getY() + 21
					int degree = 0;

					int rand1 = (int) (Math.random() * 8);// ;
					switch (rand1) {
					case 0:
						x = entity.getX();
						y = entity.getY() + 30;
						degree = 0;
						break;
					case 1:
						x = entity.getX() + 20;
						y = entity.getY() + 40;
						degree = 45;
						break;
					case 2:
						x = entity.getX() - 30;
						y = entity.getY();
						degree = 90;
						break;
					case 3:
						x = entity.getX() + 20;
						y = entity.getY() - 20;
						degree = 135;
						break;
					case 4:
						x = entity.getX() + 20;
						y = entity.getY() - 20;
						degree = 180;
						break;
					case 5:
						x = entity.getX() + 20;
						y = entity.getY() - 20;
						degree = 225;
						break;
					case 6:
						x = entity.getX() + 50;
						y = entity.getY();
						degree = 270;
						break;
					case 7:
						x = entity.getX() + 15;
						y = entity.getY() + 30;
						degree = 315;
						break;
					} // switch
					if (entity instanceof AlienEntity && (int) (Math.random() * 100) >= 95) {
						ShotEntity shot = new ShotEntity(this, "sprites/shot.png", x, y, 300, degree, false);
						entities.add(shot);
					} // if
				} // for
			} // if

			// draw all entities
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);
				if (entity instanceof ShipEntity && ((ShipEntity)ship).getInvincible() && invincibleFlash < 3) {
					invincibleFlash++;
				} else {
					entity.draw(g);
					if (entity instanceof ShipEntity) {
						invincibleFlash = 0;
					}
				}
			} // for
			

			// draw points and lives
			g.setColor(Color.white);
			g.drawString("Points: " + totalPoints + "   Lives: " + lives, 10, 18);

			// show help
			if (showHelp) {
				Entity help = new ImageEntity(this, "sprites/help.jpg", 0, 0, 4);
				entities.add(help);
				waitingForKeyPress = true;
				showHelp = false;

			}
			// brute force collisions, compare every entity
			// against every other entity. If any collisions
			// are detected notify both entities that it has
			// occurred
			for (int i = 0; i < entities.size(); i++) {
				for (int j = i + 1; j < entities.size(); j++) {
					Entity me = (Entity) entities.get(i);
					Entity him = (Entity) entities.get(j);

					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					} // if
				} // inner for
			} // outer for

			// remove dead entities
			entities.removeAll(removeEntities);
			removeEntities.clear();

			// clear graphics and flip buffer
			g.dispose();
			strategy.show();

			// respond to user moving ship
			if ((leftPressed) && (!rightPressed)) {
				// ship.setHorizontalMovement(-moveSpeed);
				rotationRequired = rotationRequired - 5;
				if (rotationRequired < 0) {
					rotationRequired = 355;
				}
				ship.setShipDegree(rotationRequired);

			} else if ((rightPressed) && (!leftPressed)) {
				rotationRequired = rotationRequired + 5;
				if (rotationRequired > 355) {
					rotationRequired = 0;
				}
				ship.setShipDegree(rotationRequired);
			}

			if (upPressed) {
					velocity -= 0.05;
			} // else
			else {
				if (velocity <= 0) {
					velocity += 0.1;
				} // if
			} // else

			ship.changeShipVelocity(velocity, rotationRequired, upPressed);
			// if spacebar pressed, try to fire
			if (firePressed) {
				tryToFire();
			} // if

			// pause
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
			
			if (((ShipEntity)ship).getInvincible() == true) {
				invincibleCounter++;
				//System.out.println(invincibleCounter);
			} // if
			
			if (invincibleCounter > 300) {
				((ShipEntity)ship).setInvincible(false);
				invincibleCounter = 0;
			} // if 

		} // while

	} // gameLoop

	/*
	 * startGame input: none output: none purpose: start a fresh game, clear old
	 * data
	 */
	private void startGame() {
		// clear out any existing entities and initialize a new set
		entities.clear();

		initEntities();
		entityCount -= 5;

		// blank out any keyboard settings that might exist
		velocity = 0;
		totalPoints = 0;
		rotationRequired = 0;
		lives = 3;
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		firePressed = false;
	} // startGame

	/*
	 * inner class KeyInputHandler handles keyboard input from the user
	 */
	private class KeyInputHandler extends KeyAdapter {

		/*
		 * The following methods are required for any class that extends the
		 * abstract class KeyAdapter. They handle keyPressed, keyReleased and
		 * keyTyped events.
		 */
		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_H) {
				showHelp = true;
			}
			// if waiting for keypress to start game, do nothing
			if (waitingForKeyPress) {
				return;
			} // if

			// respond to move left, right or fire
			if (e.getKeyCode() == 65) {
				leftPressed = true;
			} // if

			if (e.getKeyCode() == 68) {
				rightPressed = true;
			} // if

			if (e.getKeyCode() == 87) {
				upPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_Q) {
				if (paused) {
					paused = false;
					pausePressed = false;

				} else {
					pausePressed = true;
				}
			} // if

		} // keyPressed

		public void keyReleased(KeyEvent e) {
			// if waiting for keypress to start game, do nothing
			if (waitingForKeyPress) {
				return;
			} // if

			// respond to move left, right or fire
			if (e.getKeyCode() == 65) {
				leftPressed = false;
			} // if

			if (e.getKeyCode() == 68) {
				rightPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			} // if

			if (e.getKeyCode() == 87) {
				upPressed = false;
			} // if

		} // keyReleased

		public void keyTyped(KeyEvent e) {

			// if waiting for key press to start game
			if (waitingForKeyPress) {

				waitingForKeyPress = false;
				startGame();

			} // if waitingForKeyPress

			// if escape is pressed, end game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			} // if escape pressed

		} // keyTyped

	} // class KeyInputHandler

	/**
	 * Main Program
	 */
	public static void main(String[] args) {
		// instantiate this object
		new Game();
	} // main
} // Game
