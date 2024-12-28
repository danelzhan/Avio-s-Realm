
/* ShipEntity.java
 * March 27, 2006
 * Represents player's ship
 */
public class PlayerEntity extends Entity {

	private Game game; // the game in which the ship exists
	private int numCoins = 0;

	private int availableDashes = 0;
	private boolean dashing = false;
	private boolean canDash = true;

	private boolean attacking = false;
	private long lastFrameChange = 0;
	private long frameInterval = 150;
	private int currentIndexOfFrame = 0;
	private int numKeys = 0;

	private int previousXSpeed;

	/*
	 * construct the player's ship input: game - the game in which the ship is being
	 * created ref - a string with the name of the image associated to the sprite
	 * for the ship x, y - initial location of ship
	 */
	public PlayerEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
	} // constructor

	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move ship
	 */
	public void move(long delta) {
		// stop at left side of screen
		if ((hitbox.getSpeedX() < 0) && (hitbox.getX() < 0)) {
			return;
		} // if
			// stop at right side of screen
		if ((hitbox.getSpeedX() > 0) && (hitbox.getX() > game.windowWidth - hitbox.getWidth())) {
			return;
		} // if

		super.move(delta); // calls the move method in Entity

		if (hitbox.getSpeedX() != 0) {
			previousXSpeed = (int) hitbox.getSpeedX();
		} // if

		// animations
		if (getDirection().equals("right") && !hitbox.isOnGround()) {
			tryToChangeFrame("jumpR");
		} else if (getDirection().equals("left") && !hitbox.isOnGround()) {
			tryToChangeFrame("jumpL");
		} else if (hitbox.getSpeedX() > 0 && hitbox.isOnGround()) {
			tryToChangeFrame("moveR");
		} else if (hitbox.getSpeedX() < 0 && hitbox.isOnGround()) {
			tryToChangeFrame("moveL");
		} else if (hitbox.isOnGround()) {
			if (getDirection().equals("right")) {
				tryToChangeFrame("idle1R");
			} else if (getDirection().equals("left")) {
				tryToChangeFrame("idle1L");
			} // else if
		} // else if

	} // move

	public void tryToChangeFrame(String action) {
		int lengthOfAnimation = 0;

		// check that we've waited long enough to change sprite
		if ((System.currentTimeMillis() - lastFrameChange) < frameInterval) {
			return;
		} // if

		// otherwise change
		lastFrameChange = System.currentTimeMillis();

		switch (action) {
		case "moveR":
		case "moveL":
			lengthOfAnimation = 8;
			break;
		case "jumpR":
		case "jumpL":
			lengthOfAnimation = 11;
			break;
		case "idle1R":
		case "idle1L":
			lengthOfAnimation = 5;
			break;
		case "dieR":
		case "dieL":
			lengthOfAnimation = 7;
			break;
		} // switch

		if (action.contains("jump") && currentIndexOfFrame == lengthOfAnimation) {
			// do nothing
		} else if (currentIndexOfFrame < lengthOfAnimation - 1) {
			currentIndexOfFrame++;
		} else {
			currentIndexOfFrame = 0;
		}

		int oldHeight = hitbox.getHeight();
		int oldWidth = hitbox.getWidth();

		sprite = (SpriteStore.get()).getSprite("sprites/fox/" + action + currentIndexOfFrame + ".png");

		int heightDiff = sprite.getHeight() - oldHeight;
		int widthDiff = sprite.getWidth() - oldWidth;

		// correct for different sprite sizes
		if (previousXSpeed > 0) {
			hitbox.setX(hitbox.getX() - widthDiff);
		}

		hitbox.setY(hitbox.getY() + heightDiff);

		hitbox.setWidth(sprite.getWidth());
		hitbox.setHeight(sprite.getHeight());

	} // tryToChangeFrame

	public String getDirection() {
		return previousXSpeed < 0 ? "left" : "right";
	}

	/*
	 * collidedWith input: other - the entity with which the ship has collided
	 * purpose: notification that the player's ship has collided with something
	 */
	public void collidedWith(Entity other) {
		if (other instanceof TumbleweedEntity) {
			game.notifyDeath();
		} // else if
	} // collidedWith

	public boolean isAttacking() {
		return attacking;
	} // isAttacking

	public void setIsAttacking(boolean attack) {
		this.attacking = attack;
	} // setIsAttacking

	public int getNumKeys() {
		return numKeys;
	} // getNumKeys

	public void setNumKeys(int i) {
		numKeys = i;
	} // setNumKeys

	public void addKey() {
		numKeys++;
	} // addKey

	public int getNumCoins() {
		return numCoins;
	} // getNumCoins

	public void setNumCoin(int coins) {
		numCoins = coins;
	} // setNumCoin

	public void addCoin() {
		numCoins++;
	} // addCoin

	public boolean isDashing() {
		return dashing;
	} // isDashing

	public int getAvailableDashes() {
		return availableDashes;
	} // getAvailableDashes

	public void setDashing(boolean b) {
		this.dashing = b;
	} // setDashing

	public boolean canDash() {
		return canDash;
	} // canDash

	public void setCanDash(boolean b) {
		this.canDash = b;
	} // setCanDash

} // ShipEntity class