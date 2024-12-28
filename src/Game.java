
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends Canvas {

	// game data
	public final int NUM_COL = 32;
	public final int NUM_ROW = 18;
	public final int TILE_SIZE = 60;
	public int windowHeight = NUM_ROW * TILE_SIZE;
	public int windowWidth = NUM_COL * TILE_SIZE;
	public int currentState = 0;
	private int prevState = 1;
	public boolean gameRunning = true;
	public boolean gameWin = false;
	private Sound sound = new Sound();

	// game maps
	private TileMap stage;
	private int[] collisionTiles = { 1, 6, 3 };
	private BufferedImage titleBackground;
	private BufferedImage gameBackground;
	private BufferedImage respawnBackground;
	private BufferedImage title;
	private BufferedImage playButton;
	private BufferedImage respawnButton;
	private final int ROOM_0 = 0;
	private final int ROOM_1 = 1;
	private final int ROOM_2 = 2;
	private final int ROOM_3 = 3;
	private final int ROOM_35 = 4;
	private final int ROOM_4 = 5;
	private final int ROOM_45 = 6;
	private final int ROOM_5 = 7;
	private final int ROOM_55 = 8;
	private final int ROOM_6 = 9;
	private final int ROOM_7 = 10;
	private final int ROOM_8 = 11;
	private final int ROOM_81 = 12;
	private final int ROOM_82 = 13;
	private final int ROOM_9 = 14;
	private final int ROOM_10 = 15;
	private final int ROOM_11 = 16;
	private final int ROOM_12 = 17;
	private boolean stage2Loaded = false;
	private boolean stage3Loaded = false;
	private boolean stage35Loaded = false;
	private boolean stage4Loaded = false;
	private boolean stage45Loaded = false;
	private boolean stage5Loaded = false;
	private boolean stage55Loaded = false;
	private boolean stage6Loaded = false;
	private boolean stage7Loaded = false;
	private boolean stage8Loaded = false;
	private boolean stage9Loaded = false;
	private boolean stage10Loaded = false;
	private boolean stage11Loaded = false;
	private boolean stage12Loaded = false;
	private boolean stage13Loaded = false;
	private boolean stage14Loaded = false;

	private BufferStrategy strategy; // take advantage of accelerated graphics
	private boolean waitingForStartGame = true; // true if game held up until

	private boolean delaySkip = false;

	public boolean attackPressed = false;
	private boolean leftPressed = false; // true if left arrow key currently pressed
	private boolean rightPressed = false; // true if right arrow key currently pressed
	private boolean upPressed = false;
	protected boolean talkPressed = false;
	protected boolean skipPressed = false;
	protected boolean displayMessage = false;
	protected String talkMessage;
	protected boolean resetInteractives = false;

	private boolean canGoUp;
	private boolean canGoDown;
	private boolean canGoRight;
	private boolean canGoLeft;

	private ArrayList<TumbleweedEntity> tumbleweedEntities = new ArrayList<>(); // list of tumbleweed entities
	private ArrayList<InteractiveEntity> interactiveEntities = new ArrayList<>(); // list of interactive entities
	private ArrayList<CollectibleEntity> collectibleEntities = new ArrayList<>(); // list of collectable entities
	// private ArrayList<Entity> enemyEntities = new ArrayList<>(); // list of enemy
	// entities

	private ArrayList<Entity> removeEntities = new ArrayList<>(); // list of entities

	private WinEntity goldBerry;
	protected PlayerEntity player; // the fox
	private AttackEntity attack;
	private TumbleweedEntity tumbleweed;
	private InteractiveEntity NPC1; //
	private InteractiveEntity NPC2; //
	private KeyEntity key1;
	private KeyEntity key2;
	private KeyEntity key3;
	private KeyEntity key4;
	private KeyEntity key5;

	private ArrayList<InteractiveEntity> interactiveEntities1 = new ArrayList<>();
	private ArrayList<InteractiveEntity> interactiveEntities2 = new ArrayList<>();

	private ArrayList<CollectibleEntity> collectibleEntities3 = new ArrayList<>();
	private ArrayList<CollectibleEntity> collectibleEntities4 = new ArrayList<>();
	private ArrayList<CollectibleEntity> collectibleEntities5 = new ArrayList<>();
	private ArrayList<CollectibleEntity> collectibleEntities8 = new ArrayList<>();
	private ArrayList<CollectibleEntity> collectibleEntities10 = new ArrayList<>();

	private double moveSpeed = 400; // hor. vel. of ship (px/s)
	private long timeSinceJump = 0; // time last jump occurred
	private long lastAttck = 0; // time since last attack occured
	private long attackInterval = 1000; // time between attacks in ms
	private long lastSpawnTime = 0; // // time since last spawn occured
	private long spawnInterval = 45; // time between spawns in ms
	private int lowerSpawnLimit = 1;
	long lastDashTime = 0;
	private boolean hasDashed = false;

	private String message = ""; // message to display while waiting for a key press

	private int xBeforeDash;

	/*
	 * Construct our game and set it running.
	 */
	public Game() {
		playMusic(0);

		stage = new TileMap(this, "maps/stage1.txt");

		// create a frame to contain game
		JFrame container = new JFrame("May the People's Republic live eternally");

		// get hold the content of the frame
		JPanel panel = (JPanel) container.getContentPane();

		// set up the resolution of the game
		panel.setPreferredSize(new Dimension(windowWidth, windowHeight));
		panel.setLayout(null);

		// set up canvas size (this) and add to frame
		setBounds(0, 0, windowWidth, windowHeight);
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
		this.addMouseListener(new MouseInput());

		// request focus so key events are handled by this canvas
		requestFocus();

		// create buffer strategy to take advantage of accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// initialize entities
		initEntities();

		// start the game
		gameLoop();
	} // constructor

	/*
	 * initEntities input: none output: none purpose: Initialise the starting state
	 * of the ship and alien entities. Each entity will be added to the array of
	 * entities in the game.
	 */
	private void initEntities() {

		attack = new AttackEntity("sprites/emptyAttack.png", -100, -100);

		try {
			titleBackground = ImageIO.read(getClass().getResourceAsStream("sprites/titleScreen.jpg"));
			gameBackground = ImageIO.read(getClass().getResourceAsStream("sprites/gameScreen.png"));
			respawnBackground = ImageIO.read(getClass().getResourceAsStream("sprites/respawnScreen.png"));
			title = ImageIO.read(getClass().getResourceAsStream("sprites/title.png"));
			playButton = ImageIO.read(getClass().getResourceAsStream("sprites/playButton.png"));
			respawnButton = ImageIO.read(getClass().getResourceAsStream("sprites/respawn.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} // try catch

		player = new PlayerEntity(this, "sprites/fox/idle1R1.png", 60, 900);

		// room one
		tumbleweed = new TumbleweedEntity(this, "sprites/tumble1.png", 1920, 915);
		tumbleweedEntities.add(tumbleweed);

		String[] NPCDialogue = { "Hello! (Press G)", "You're new huh?", "Don't Worry!", "Press Space to Jump!","Press E to Dash!", "Click to Block Bushes!", "Find Keys to Unlock Doors!", "Nice! You're a pro!" };
		NPC1 = new InteractiveEntity(this, "sprites/porcupine.png", 200, 880, NPCDialogue);
		interactiveEntities1.add(NPC1);

		// room two
		String[] daddyDialogue = { "Hey!", "You're a fox too?", "Theres a GIANT berry ahead!", "You can go grab it!!" };
		NPC2 = new InteractiveEntity(this, "sprites/fox/sleepL1.png", 1800, 930, daddyDialogue);
		interactiveEntities2.add(NPC2);

		// room three
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 2; col++) {
				CollectibleEntity coin = new CollectibleEntity(this, "sprites/berry.png", 600 + (col * 70),
						500 + (row * 70));
				collectibleEntities3.add(coin);
			} // for
		} // outer for

		key1 = new KeyEntity(this, "sprites/key.png", 1600, 900);
		collectibleEntities3.add(key1);

		// room four
		key2 = new KeyEntity(this, "sprites/key.png", 360, 300);
		collectibleEntities4.add(key2);

		// room 5
		key3 = new KeyEntity(this, "sprites/key.png", 1560, 720);
		collectibleEntities5.add(key3);
		
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 4; col++) {
				CollectibleEntity coin = new CollectibleEntity(this, "sprites/berry.png", 600 + (col * 70),
						700 + (row * 70));
				collectibleEntities5.add(coin);
			} // for
		} // outer for
		
		// room 8
		key4 = new KeyEntity(this, "sprites/key.png", 420, 61);
		collectibleEntities8.add(key4);
		key5 = new KeyEntity(this, "sprites/key.png", 1560, 360);
		collectibleEntities8.add(key5);
		
		// room 10
		goldBerry = new WinEntity(this, "sprites/goldenBerry.png", 1450, 900);
		collectibleEntities10.add(goldBerry);

		stage5Loaded = true;

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
		waitingForStartGame = true;
		currentState = 999;
	} // notifyDeath

	/*
	 * Notification that the play has killed all aliens
	 */
	public void notifyWin() {
		waitingForStartGame = true;
		currentState = 999;
	} // notifyWin

	public int getStage() {

		return currentState;

	} // getStage

	/*** STAGE LOGIC ***/

	private void changeStage() {
		talkMessage = "";
		interactiveEntities.clear();
		collectibleEntities.clear();

		System.out.println(currentState);
		switch (currentState) {
		case ROOM_1:
			stage = new TileMap(this, "maps/stage1.txt");
			interactiveEntities.addAll(interactiveEntities1);
			break;
		case ROOM_2:
			stage = new TileMap(this, "maps/stage2.txt");
			interactiveEntities.addAll(interactiveEntities2);
			break;
		case ROOM_3:
			stage = new TileMap(this, "maps/stage3.txt");
			collectibleEntities.addAll(collectibleEntities3);
			break;
		case ROOM_35:
			stage = new TileMap(this, "maps/stage3.5.txt");
			collectibleEntities.addAll(collectibleEntities3);
			break;
		case ROOM_4:
			stage = new TileMap(this, "maps/stage4.txt");
			collectibleEntities.addAll(collectibleEntities4);
			break;
		case ROOM_45:
			stage = new TileMap(this, "maps/stage4.5.txt");
			collectibleEntities.addAll(collectibleEntities4);
			break;
		case ROOM_5:
			stage = new TileMap(this, "maps/stage5.txt");
			collectibleEntities.addAll(collectibleEntities5);
			break;
		case ROOM_55:
			stage = new TileMap(this, "maps/stage5.5.txt");
			collectibleEntities.addAll(collectibleEntities5);
			break;
		case ROOM_6:
			stage = new TileMap(this, "maps/stage6.txt");
			break;
		case ROOM_7:
			stage = new TileMap(this, "maps/stage7.txt");
			break;
		case ROOM_8:
			stage = new TileMap(this, "maps/stage8.txt");
			collectibleEntities.addAll(collectibleEntities8);
			break;
		case ROOM_81:
			stage = new TileMap(this, "maps/stage8.1.txt");
			collectibleEntities.addAll(collectibleEntities8);
			break;
		case ROOM_82:
			stage = new TileMap(this, "maps/stage8.2.txt");
			collectibleEntities.addAll(collectibleEntities8);
			break;
		case ROOM_9:
			stage = new TileMap(this, "maps/stage9.txt");
			break;
		case ROOM_10:
			stage = new TileMap(this, "maps/stage10.txt");
			break;
		case ROOM_11:
			stage = new TileMap(this, "maps/stage11.txt");
			break;
		case ROOM_12:
			stage = new TileMap(this, "maps/stage12.txt");
			break;
		} // switch

	} // stageChange

	private boolean wantStageChange() {
		if (player.hitbox.getX() <= 0 && currentState > 1 && player.hitbox.getSpeedX() < 0) {
			player.hitbox.setX(1920 - player.hitbox.getWidth());
			// accounting for stages with locks
			if (currentState == ROOM_3 || currentState == ROOM_35) {
				currentState = ROOM_2;
			} else if (currentState == ROOM_4 || currentState == ROOM_45) {
				currentState = ROOM_35;
			} else if (currentState == ROOM_5 || currentState == ROOM_55) {
				currentState = ROOM_45;
			} else {
				currentState--;
			}
			return true;
		} else if (player.hitbox.getX() >= 1920 - player.hitbox.getWidth() && currentState < 13
				&& player.hitbox.getSpeedX() > 0) {
			player.hitbox.setX(0);
			if (currentState == ROOM_2 && stage35Loaded) {
				currentState = ROOM_35;
			} else if ((currentState == ROOM_3 || currentState == ROOM_35) && stage45Loaded) {
				currentState = ROOM_45;
			} else if ((currentState == ROOM_4 || currentState == ROOM_45) && stage55Loaded) {
				currentState = ROOM_55;
			} else {
				currentState++;
			} // else
			return true;
		} else if (player.hitbox.getX() >= 1920 - player.hitbox.getWidth() - 120 && currentState < 13
				&& player.hitbox.getSpeedX() > 0 && player.getNumKeys() >= 1) {
			switch (currentState) {
			case ROOM_3:
			case ROOM_4:
			case ROOM_5:
				currentState++;
				player.setNumKeys(player.getNumKeys() - 1);
			} // switch
			return true;
		} // if
		
		return false;
	} // wantStageChange

	private void checkBottomOfTiles(long delta) {
		canGoUp = true;

		for (int y = 0; y < NUM_ROW; y++) {
			for (int x = 0; x < NUM_COL; x++) {

				int tileID = stage.getTile(x, y);
				if (isCollidable(tileID)) {
					if (canGoUp) {
						canGoUp = player.hitbox.tryMoveUp(x, y, TILE_SIZE, delta);
					} // if
				} else if (tileID == 8 && player.hitbox.collided(x, y, TILE_SIZE)) {
					notifyDeath();
				}
			} // inner for
		} // outer for
	} // checkBottomOfTiles

	private void checkTopOfTiles(long delta) {
		canGoDown = true;

		for (int y = 0; y < NUM_ROW; y++) {
			for (int x = 0; x < NUM_COL; x++) {

				int tileID = stage.getTile(x, y);
				if (isCollidable(tileID)) {
					if (canGoDown) {
						canGoDown = player.hitbox.tryMoveDown(x, y, TILE_SIZE, delta);
					}
				} else if (tileID == 8 && player.hitbox.collided(x, y, TILE_SIZE)) {
					notifyDeath();
				} // else if
			} // inner for
		} // outer for
	} // checkTopOfTiles

	private void checkRightOfTiles(long delta) {
		canGoLeft = true;

		for (int y = 0; y < NUM_ROW; y++) {
			for (int x = 0; x < NUM_COL; x++) {

				int tileID = stage.getTile(x, y);
				if (isCollidable(tileID)) {
					if (canGoLeft) {
						canGoLeft = player.hitbox.tryMoveLeft(x, y, TILE_SIZE, delta);
					} // if
				} else if (tileID == 8 && player.hitbox.collided(x, y, TILE_SIZE)) {
					notifyDeath();
				}
			} // inner for
		} // outer for
	} // checkRightOfTiles

	private void checkLeftOfTiles(long delta) {
		canGoRight = true;

		for (int y = 0; y < NUM_ROW; y++) {
			for (int x = 0; x < NUM_COL; x++) {

				int tileID = stage.getTile(x, y);
				if (isCollidable(stage.getTile(x, y))) {
					if (canGoRight) {
						canGoRight = player.hitbox.tryMoveRight(x, y, TILE_SIZE, delta);
					} // if
				} else if (tileID == 8 && player.hitbox.collided(x, y, TILE_SIZE)) {
					notifyDeath();
				}
			} // inner for
		} // outer for
	} // checkLeftOfTiles

	private boolean isCollidable(int tileID) {
		for (int tile : collisionTiles) {
			if (tile == tileID) {
				return true;
			} // if
		} // for
		return false;
	} // isCollidable

	private void checkTumbleweedCollisions() {
		for (int i = 0; i < tumbleweedEntities.size(); i++) {
			Entity him = (Entity) tumbleweedEntities.get(i);
			if (player.hitbox.collidesWith(him.hitbox)) {
				him.collidedWith(player);
				player.collidedWith(him);
			} // if

			if (attack.hitbox.collidesWith(him.hitbox)) {
				attack.collidedWith(him);
				him.collidedWith(attack);
			} // if
		} // for
	} // checkEntityCollisions

	private void checkInteractiveEntities() {
		for (int j = 0; j < interactiveEntities.size(); j++) {
			Entity him = (Entity) interactiveEntities.get(j);
			if (player.hitbox.collidesWith(him.hitbox)) {
				player.collidedWith(him);
				him.collidedWith(player);
			} // if
		} // for
	} // checkInteractiveEntities

	private void checkCollectibleEntities() {
		for (int j = 0; j < collectibleEntities.size(); j++) {
			Entity him = (Entity) collectibleEntities.get(j);
			if (player.hitbox.collidesWith(him.hitbox)) {
				player.collidedWith(him);
				him.collidedWith(player);
			} // if
		} // for
	} // checkCollectibleEntities

	private void drawStage(Graphics2D g) {

		// draw only title page is stage if 0
		if (currentState == ROOM_0) {
			g.drawImage(titleBackground, 0, 0, null);
			g.drawImage(title, 568, 130, null);
			g.drawImage(playButton, 805, 550, null);
			return;
		} // if
		if (currentState == 999) {
			g.drawImage(respawnBackground, 0, 0, null);
			g.drawImage(respawnButton, 682, 550, null);
			return;
		} // if
		
		// draw game background
		g.drawImage(gameBackground, 0, 0, null);
		g.setColor(Color.black);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		g.drawString("Berries Collected: " + Integer.toString(player.getNumCoins()),
				(360 - g.getFontMetrics().stringWidth("Coins Collected")), 120);

		// draw rest of entities based on current stage
		switch (currentState) {
		case ROOM_1:
			for (int i = 0; i < interactiveEntities.size(); i++) {
				Entity interactiveEntity = (Entity) interactiveEntities.get(i);
				interactiveEntity.draw(g);
			} // for
			
			if (!displayMessage) {
				g.setColor(Color.black);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
				g.drawString("Press F to Talk", (960 - (g.getFontMetrics().stringWidth("Press F to Talk") / 2)), 550);
			} // if

			break;
		case ROOM_2:
			if (!stage2Loaded) {
				stage2Loaded = true;
			} // if

			for (int i = 0; i < interactiveEntities.size(); i++) {
				Entity interactiveEntity = (Entity) interactiveEntities.get(i);
				interactiveEntity.draw(g);
			} // for
			break;
		case ROOM_3:
			if (!stage3Loaded) {
				stage3Loaded = true;
			} // if

			for (int i = 0; i < collectibleEntities.size(); i++) {
				CollectibleEntity collectableEntity = (CollectibleEntity) collectibleEntities.get(i);

				if (!collectableEntity.collected) {
					collectableEntity.draw(g);
				} // if
			} // for
			break;
		case ROOM_35:
			if (!stage35Loaded) {
				stage35Loaded = true;
			} // if
			break;
		case ROOM_4:
			if (!stage4Loaded) {
				stage4Loaded = true;
			} // if

			for (int i = 0; i < collectibleEntities.size(); i++) {
				CollectibleEntity collectableEntity = (CollectibleEntity) collectibleEntities.get(i);
				if (!collectableEntity.collected) {
					collectableEntity.draw(g);
				} // if
			} // for
			break;
		case ROOM_45:
			if (!stage45Loaded) {
				stage45Loaded = true;
			} // if
			break;
		case ROOM_5:
			if (!stage5Loaded) {
				stage5Loaded = true;
			} // if
			collectibleEntities.removeAll(collectibleEntities);
			collectibleEntities.addAll(collectibleEntities5);

			for (int i = 0; i < collectibleEntities.size(); i++) {
				CollectibleEntity collectableEntity = (CollectibleEntity) collectibleEntities.get(i);
				if (!collectableEntity.collected) {
					collectableEntity.draw(g);
				} // if
			} // for
			break;
		case ROOM_55:
			if (!stage55Loaded) {
				stage55Loaded = true;
			} // if
			break;
		} // switch

		// draw all entities
		player.draw(g);
		for (int i = 0; i < tumbleweedEntities.size(); i++) {
			TumbleweedEntity tumbleweedEntity = tumbleweedEntities.get(i);
			tumbleweedEntity.move((long) tumbleweedEntity.hitbox.getSpeedX());
			tumbleweedEntity.draw(g);
		} // for
	} // drawStage
	
	private void tryAttack(long currentTime) {
		
		if(currentTime - lastAttck < attackInterval) {
			return;
		} // if
		
		lastAttck = currentTime;
		switch (player.getDirection()) {
		case "right":
			attack = new AttackEntity("sprites/emptyAttack.png",
					player.getHitBox().getX() + player.getHitBox().getWidth(), player.getHitBox().getY());
			System.out.println("rightAttack");
			break;
		case "left":
			attack = new AttackEntity("sprites/emptyAttack.png", player.getHitBox().getX(),
					player.getHitBox().getY());
			System.out.println("leftAttack");
			break;
		} // switch
		
		attackPressed = false;
		
	} // tryAttack
	
	private void trySpawnTumble(long currentTime) {
		
		if((currentTime - lastSpawnTime < spawnInterval) || player.getHitBox().getX() > 1280 || currentState < 2) {
			return;
		} // if
				
		lastSpawnTime = currentTime;
		int spawnChance = (int) ((Math.random() * 100 - lowerSpawnLimit + 1) + lowerSpawnLimit);
		
		if(spawnChance == 100) {
			tumbleweed = new TumbleweedEntity(this, "sprites/tumble1.png", 1920, player.hitbox.getY());
			tumbleweedEntities.add(tumbleweed);
		}
	}
	
	/*
	 * gameLoop input: none output: none purpose: Main game loop. Runs throughout
	 * game play. Responsible for the following activities: - calculates speed of
	 * the game loop to update moves - moves the game entities - draws the screen
	 * contents (entities, text) - updates game events - checks input
	 */
	public void gameLoop() {
		
		long lastLoopTime = System.currentTimeMillis();

		// keep loop running until game ends
		while (gameRunning) {

			// calc. time since last update, will be used to calculate
			// entities movement
			long delta = System.currentTimeMillis() - lastLoopTime;
			lastLoopTime = System.currentTimeMillis();

			// get graphics context for the accelerated surface and make it black
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.drawImage(gameBackground, 0, 0, null);

			drawStage(g);

			if (currentState == 0 || currentState == 999) {
				// clear graphics and flip buffer
				g.dispose();
				strategy.show();
				continue;
			}

			stage.draw(g);

			// move each tumbleweed and move player
			player.move(delta);

			// check collisions between player and each entity
			checkTumbleweedCollisions();
			checkInteractiveEntities();
			checkCollectibleEntities();

			// remove dead entities
			tumbleweedEntities.removeAll(removeEntities);
			collectibleEntities.removeAll(removeEntities);
			collectibleEntities3.removeAll(removeEntities);
			collectibleEntities4.removeAll(removeEntities);
			collectibleEntities5.removeAll(removeEntities);
			collectibleEntities8.removeAll(removeEntities);
			interactiveEntities1.removeAll(removeEntities);
			interactiveEntities2.removeAll(removeEntities);
			removeEntities.clear();

			if (displayMessage) {

				g.setColor(Color.black);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 60));
				g.drawString(talkMessage, (960 - (g.getFontMetrics().stringWidth(talkMessage) / 2)), 550);

			} // if

			// clear graphics and flip buffer
			g.dispose();
			strategy.show();

			checkBottomOfTiles(delta);
			checkTopOfTiles(delta);
			checkRightOfTiles(delta);
			checkLeftOfTiles(delta);

			if (wantStageChange()) {
				changeStage();
			} // changeStage

			// ship should not move without user input
			player.hitbox.setSpeedX(0);
			
			long currentTime = System.currentTimeMillis();
			
			// respond to user moving ship
			if ((leftPressed) && (!rightPressed) && canGoLeft) {
				player.hitbox.setSpeedX(-moveSpeed);
			} else if ((rightPressed) && (!leftPressed) && canGoRight) {
				player.hitbox.setSpeedX(moveSpeed);
			} // else if
			
			if (canGoDown && currentTime - timeSinceJump > 20) {
				player.hitbox.gravity = 1250;
			} else if (upPressed && player.hitbox.isOnGround()) {
				player.hitbox.gravity = -17000;
				timeSinceJump = currentTime;
			} // else if
			
			player.hitbox.setSpeedY((player.hitbox.gravity * delta) / 1000 + player.hitbox.getSpeedY());
			
			// dashing
			boolean hasDashed = false;
			if (player.hitbox.onGround) {
				player.setCanDash(true);
			} // if

			if (player.isDashing() && player.canDash()) {
				if ((leftPressed) && !hasDashed) {
					player.hitbox.setSpeedX(-1500);
					hasDashed = true;
					if ((System.currentTimeMillis() - lastDashTime > 350 && hasDashed) || (rightPressed && hasDashed || talkPressed || currentState >= 1)
							|| ((Math.abs(player.getHitBox().getX() - xBeforeDash) > 150) && hasDashed)) {
						player.hitbox.setSpeedX(0);
						player.setDashing(false);
						hasDashed = false;
						playSoundEffect(25);
						player.setCanDash(false);
					} // if
				} else if ((rightPressed) && !hasDashed) {
					player.hitbox.setSpeedX(1500);
					hasDashed = true;
					if (((Math.abs(player.getHitBox().getX() - xBeforeDash) > 150) && hasDashed)
							|| (leftPressed && hasDashed)
							|| (System.currentTimeMillis() - lastDashTime > 350 && hasDashed)) {
						player.hitbox.setSpeedX(0);
						player.setDashing(false);
						hasDashed = false;
						playSoundEffect(25);
						player.setCanDash(false);
					} // if
				} // else if
			} // if

			if (attackPressed) {
				tryAttack(currentTime);
			} // if
			
			if (currentTime - lastAttck > 35) {
				attack.hitbox.setX(-100);
				attack.hitbox.setY(-100);
			}
			
			if(currentState > 1) {
				trySpawnTumble(currentTime);
			}
			
			// pause
			try {
				Thread.sleep(2);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} // while

	} // gameLoop

	/*
	 * startGame input: none output: none purpose: start a fresh game, clear old
	 * data
	 */
	private void startGame() {
		// clear out any existing entities and initalize a new set
		tumbleweedEntities.clear();
		collectibleEntities.clear();
		interactiveEntities.clear();
		collectibleEntities3.clear();
		collectibleEntities4.clear();
		collectibleEntities5.clear();
		interactiveEntities1.clear();
		interactiveEntities2.clear();
		initEntities();

		currentState = prevState;
		changeStage();

		// blank out any keyboard settings that might exist
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		stage2Loaded = false;
		stage3Loaded = false;
		stage35Loaded = false;
		stage4Loaded = false;
		stage45Loaded = false;
		stage5Loaded = false;
		stage55Loaded = false;
		stage6Loaded = false;
		stage7Loaded = false;
		stage8Loaded = false;
		stage9Loaded = false;
		stage10Loaded = false;
		stage11Loaded = false;
		stage12Loaded = false;
		
		// reset all values in entities
		player.setNumCoin(0);
		player.setNumKeys(0);
	} // startGame

	/*** SOUND METHODS ***/
	public void playMusic(int i) {
		sound.setFile(i);
		sound.play();
		sound.loop();
	} // playMusic

	public void playSoundEffect(int i) {
		sound.setFile(i);
		sound.play();
	} // playSoundEffect

	public void stopMusic() {
		sound.stop();
	} // stopMusic

	/*
	 * inner class KeyInputHandler handles keyboard input from the user
	 */
	private class KeyInputHandler extends KeyAdapter {

		/*
		 * The following methods are required for any class that extends the abstract
		 * class KeyAdapter. They handle keyPressed, keyReleased and keyTyped events.
		 */
		public void keyPressed(KeyEvent e) {

			// respond to move left, right, up, dash or talk
			if (e.getKeyCode() == KeyEvent.VK_A) {
				leftPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_D) {
				rightPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				upPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_F) {
				talkPressed = true;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_G) {
				if (!delaySkip) {
					skipPressed = true;
				}
			} // if

			if (e.getKeyCode() == KeyEvent.VK_E) {
				player.setDashing(true);
				xBeforeDash = player.getHitBox().getX();
				if (!hasDashed) {
					lastDashTime = System.currentTimeMillis();
				}
			} // if

		} // keyPressed

		public void keyReleased(KeyEvent e) {
			// if waiting for keypress to start game, do nothing
			if (waitingForStartGame) {
				return;
			} // if

			// respond to move left, right, up or talk
			if (e.getKeyCode() == KeyEvent.VK_A) {
				leftPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_D) {
				rightPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				upPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_F) {
				talkPressed = false;
			} // if

			if (e.getKeyCode() == KeyEvent.VK_G) {
				skipPressed = false;
				delaySkip = false;
			} // if

		} // keyReleased

		public void keyTyped(KeyEvent e) {
			// if escape is pressed, end game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			} // if escape pressed
		} // keyTyped
	} // class KeyInputHandler

	private class MouseInput implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("mouseClicked");
		} // mouseClicked

		@Override
		public void mouseEntered(MouseEvent e) {
			System.out.println("mouseEvent");
		} // mouseEntered

		@Override
		public void mouseExited(MouseEvent e) {
			System.out.println("mouseExited");
		} // mouseExited

		@Override
		public void mousePressed(MouseEvent e) {
			int mouseX = e.getX();
			int mouseY = e.getY();

			if (waitingForStartGame) {
				if (mouseX >= 805 && mouseX <= 1115 && mouseY >= 550 && mouseY <= 722) {
					waitingForStartGame = false;
					startGame();
				} // inner if
			} else {
				attackPressed = true;
				playSoundEffect(23);
			} // else

		} // mousePressed

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("mouseReleased");
		} // mouseReleased

	} // MouseInput

	/**
	 * Main Program
	 */
	public static void main(String[] args) {
		// instantiate this object
		new Game();
	} // main
} // Game