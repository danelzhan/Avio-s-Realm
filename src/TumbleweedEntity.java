public class TumbleweedEntity extends Entity {

	private long moveSpeed = 75; // horizontal speed
	private Game game; // the game in which the alien exists
	private boolean attacked = false;
	
	private long lastFrameChange = 0;
	private long frameChangeInterval = 150;
	private int currentIndexOfFrame = 0;

	/*
	 * construct a new alien input: game - the game in which the alien is being
	 * created r - the image representing the alien x, y - initial location of alien
	 */
	public TumbleweedEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
		hitbox.setSpeedX(-moveSpeed); // start off moving left
	} // constructor
	
	public void	move(long delta) {	
		if(hitbox.getX() <= -50) {
			game.removeEntity(this);
		}
		hitbox.setX(hitbox.getX() + -1 * ((delta * hitbox.getSpeedX()) / 1000));
		tryToChangeFrame();
	} // enemyLogic

	/*
	 * collidedWith input: other - the entity with which the alien has collided
	 * purpose: notification that the alien has collided with something
	 */
	public void collidedWith(Entity other) {
		if (other instanceof AttackEntity) {
            System.out.println("attacked");
            game.removeEntity(this);
            setIsAttacked(true);
            game.playSoundEffect(24);
        } // if
	} // collidedWith
	
	public void tryToChangeFrame() {
		int lengthOfAnimation = 4;

		// check that we've waited long enough to change sprite
		if ((System.currentTimeMillis() - lastFrameChange) < frameChangeInterval) {
			return;
		} // if

		// otherwise change
		lastFrameChange = System.currentTimeMillis();
		
		currentIndexOfFrame++;
		if (currentIndexOfFrame > lengthOfAnimation - 1) {
			currentIndexOfFrame = 0;
		} // if
		sprite = (SpriteStore.get()).getSprite("sprites/tumble" + (currentIndexOfFrame + 1) + ".png");
		hitbox.setWidth(sprite.getWidth());
		hitbox.setHeight(sprite.getHeight());

	} // tryToChangeFrame

	public boolean isAttacked() {
		return attacked;
	} // isAttacked

	public void setIsAttacked(boolean attacked) {
		this.attacked = attacked;
	} // setIsAttacked
} // TumbleweedEntity
