/* AlienEntity.java
 * March 27, 2006
 * Represents one of the aliens
 */
public class EnemyEntity extends Entity {

	private Game game; // the game in which the enemies exists
	
	private double moveSpeed = 5; // horizontal speed
	private int leftBound;
	private int rightBound;
	private boolean attacked;
	

	/*
	 * construct a new alien input: game - the game in which the alien is being
	 * created r - the image representing the alien x, y - initial location of alien
	 */
	public EnemyEntity(Game g, String r, int newX, int newY, int leftBound, int rightBound, String direction) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
		this.leftBound = leftBound;
		this.rightBound = rightBound;
		
		if (direction.equals("left")) {
			hitbox.setSpeedX(-moveSpeed); // start off moving left
		} else if (direction.equals("right")){
			hitbox.setSpeedX(moveSpeed); // start off moving left
		}
	} // constructor

	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move alien
	 */
	public void move(long delta) {
		// if we reach left side of screen and are moving left
		if ((hitbox.getSpeedX() < 0) && (hitbox.getX() <= leftBound)) {
			hitbox.setSpeedX(moveSpeed * 10);
			hitbox.setX(leftBound);
			System.out.println(hitbox.getSpeedX());
			System.out.println("left bound: " + hitbox.getX());
		} // if

		// if we reach right side of screen and are moving right
		if ((hitbox.getSpeedX() > 0) && (hitbox.getX() >= rightBound + hitbox.getWidth())) {
			hitbox.setSpeedX(-moveSpeed);
			hitbox.setX(rightBound - hitbox.getWidth());
			System.out.println(rightBound);
			System.out.println("right bound: " + hitbox.getX());
		} // if
		
		// proceed with normal move
		super.move(delta);
	} // move

	/*
	 * collidedWith input: other - the entity with which the alien has collided
	 * purpose: notification that the alien has collided with something
	 */
	public void collidedWith(Entity other) {
		if (other instanceof AttackEntity) {
			
			System.out.println("attacked");
			game.removeEntity(this);
			setAttacked(true);
			game.playSoundEffect(24);
			
		} // if
		
		if ((hitbox.getSpeedX() < 0)) {
			System.out.println(hitbox.getSpeedX());
			hitbox.setSpeedX(moveSpeed);
			System.out.println(hitbox.getSpeedX());
			System.out.println("collide left: " + hitbox.getX());
		} // if

		// if we reach right side of screen and are moving right
		if ((hitbox.getSpeedX() > 0) && (hitbox.getX() >= rightBound - hitbox.getWidth())) {
			hitbox.setSpeedX(-moveSpeed);
			System.out.println(rightBound);
		} // if
	} // collidedWith
	
	public boolean isAttacked() {
		return attacked;
	}

	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}

} // AlienEntity class
