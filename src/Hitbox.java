import java.awt.Rectangle;

public class Hitbox {

	// Java Note: the visibility modifier "protected"
	// allows the variable to be seen by this class,
	// any classes in the same package, and any subclasses
	// "private" - this class only
	// "public" - any class can see it

	private double x; // current x location
	private double y; // current y location
	private int width; // width of hit box
	private int height; // height of hit box

	private double dx; // horizontal speed (px/s) + -> right
	private double dy; // vertical speed (px/s) + -> down

	protected boolean onGround = true;
	protected double gravity = 0;

	private Rectangle me = new Rectangle(); // bounding rectangle of
											// this entity
	private Rectangle him = new Rectangle(); // bounding rect. of other
												// entities

	/*
	 * Constructor input: reference to the image for this entity, initial x and y
	 * location to be drawn at
	 */
	public Hitbox(int startX, int startY, int width, int height) {
		this.x = startX;
		this.y = startY;
		this.width = width;
		this.height = height;
	} // constructor

	public boolean collided(int otherX, int otherY, int tileSize) {
		int leftOfMe = (int) x;
		int rightOfMe = leftOfMe + width;
		int topOfMe = (int) y;
		int bottomOfMe = (int) y + height;
		
		int leftOfOther = otherX * tileSize;
		int rightOfOther = leftOfOther + tileSize;
		int topOfOther = otherY * tileSize;
		int bottomOfOther = topOfOther + tileSize;
		
		boolean outsideVerticalEdges = leftOfMe > rightOfOther || rightOfMe < leftOfOther;
		boolean outsideHorizontalEdges = topOfMe > bottomOfOther || bottomOfMe < topOfOther;
		
		if (outsideHorizontalEdges || outsideVerticalEdges) {
			return false;
		}
		
		return true;
	} // collided

	public boolean tryMoveUp(int otherX, int otherY, int tileSize, long delta) {
		int leftOfMe = (int) x;
		int rightOfMe = leftOfMe + width;
		int topOfMe = (int) y;

		int leftOfOther = otherX * tileSize;
		int rightOfOther = leftOfOther + tileSize;
		int topOfOther = otherY * tileSize;
		int bottomOfOther = topOfOther + tileSize;

		if ((leftOfMe < rightOfOther && rightOfMe > leftOfOther)
				&& (topOfMe <= bottomOfOther && topOfMe > topOfOther)) {

			y = bottomOfOther + 1;
//			System.out.println(delta + ": " + otherX + ", " + otherY + " no go up");
			return false;
		} // if
		return true;
	} // tryUp

	public boolean tryMoveDown(int otherX, int otherY, int tileSize, long delta) {
		int leftOfMe = (int) x;
		int rightOfMe = leftOfMe + width;
		int bottomOfMe = (int) y + height;

		int leftOfOther = otherX * tileSize;
		int rightOfOther = leftOfOther + tileSize;
		int topOfOther = otherY * tileSize;
		int bottomOfOther = topOfOther + tileSize;

		if ((leftOfMe < rightOfOther && rightOfMe > leftOfOther)
				&& (bottomOfMe >= topOfOther && bottomOfMe < bottomOfOther)) {

			y = topOfOther - height;
			grounded();
//			System.out.println(delta + ": " + otherX + ", " + otherY + " no go down");
			return false;
		} // if

		onGround = false;
		return true;
	} // tryDown

	public boolean tryMoveRight(int otherX, int otherY, int tileSize, long delta) {
		int rightOfMe = (int) x + width;
		int topOfMe = (int) y;
		int bottomOfMe = topOfMe + height;

		int leftOfOther = otherX * tileSize;
		int rightOfOther = leftOfOther + tileSize;
		int topOfOther = otherY * tileSize;
		int bottomOfOther = topOfOther + tileSize;

		if ((topOfMe < bottomOfOther && bottomOfMe > topOfOther)
				&& (rightOfMe >= leftOfOther && rightOfMe < rightOfOther)) {

			x = leftOfOther - width;
//			System.out.println(delta + ": " + otherX + ", " + otherY + " no go right");
			return false;
		} // if
		return true;
	} // tryRight

	public boolean tryMoveLeft(int otherX, int otherY, int tileSize, long delta) {
		int leftOfMe = (int) x;
		int topOfMe = (int) y;
		int bottomOfMe = (int) y + height;

		int leftOfOther = otherX * tileSize;
		int rightOfOther = leftOfOther + tileSize;
		int topOfOther = otherY * tileSize;
		int bottomOfOther = topOfOther + tileSize;

		if ((topOfMe < bottomOfOther && bottomOfMe > topOfOther)
				&& (leftOfMe <= rightOfOther && leftOfMe > leftOfOther)) {

			x = rightOfOther;
//			System.out.println(delta + ": " + otherX + ", " + otherY + " no go left");
			return false;
		} // if
		return true;
	} // tryLeft

	protected void grounded() {
		onGround = true;
		gravity = 0;
	} // grounded

	public boolean isOnGround() {
		return onGround;
	} // isOnGround
	
	public void setOnGround(boolean b) {
		onGround = b;
	}
	
	/*
	 * move input: delta - the amount of time passed in ms output: none purpose:
	 * after a certain amout of time has passed, update the location
	 */

	// get and set velocities
	public void setSpeedX(double newDX) {
		dx = newDX;
	} // setHorizontalMovement

	public void setSpeedY(double newDY) {
		dy = newDY;
	} // setVerticalMovement

	public double getSpeedX() {
		return dx;
	} // getHorizontalMovement

	public double getSpeedY() {
		return dy;
	} // getVerticalMovement

	// get position
	public int getX() {
		return (int) x;
	} // getX

	public void setX(double newX) {
		this.x = newX;
	}

	public int getY() {
		return (int) y;
	} // getY

	public void setY(double newY) {
		y = newY;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	} // getY

	public int getHeight() {
		return height;
	} // getY

	public Rectangle getHitBox() {
		return me;
	}

	/*
	 * Do the logic associated with this entity. This method will be called
	 * periodically based on game events.
	 */
	public void doLogic() {
	}

	/*
	 * collidesWith input: the other entity to check collision against output: true
	 * if entities collide purpose: check if this entity collides with the other.
	 */
	public boolean collidesWith(Hitbox other) {
		me.setBounds((int) x, (int) y, width, height);
		him.setBounds(other.getX(), other.getY(), other.getWidth(), other.getHeight());
		return me.intersects(him);
	} // collidesWith

} // HitBox class
