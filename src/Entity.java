/* Entity.java
 * An entity is any object that appears in the game.
 * It is responsible for resolving collisions and movement.
 */

import java.awt.*;

public abstract class Entity {

	// Java Note: the visibility modifier "protected"
	// allows the variable to be seen by this class,
	// any classes in the same package, and any subclasses
	// "private" - this class only
	// "public" - any class can see it

	protected Sprite sprite; // this entity's sprite
	protected Hitbox hitbox;
	
	/*
	 * Constructor input: reference to the image for this entity, initial x and y
	 * location to be drawn at
	 */
	public Entity(String r, int newX, int newY) {
		sprite = (SpriteStore.get()).getSprite(r);
		hitbox = new Hitbox(newX, newY, sprite.getWidth(), sprite.getHeight());
	} // constructor

	/*
	 * move input: delta - the amount of time passed in ms output: none purpose:
	 * after a certain amout of time has passed, update the location
	 */
	
	public void move(long delta) {
		// update location of entity based on move speeds
		hitbox.setX(hitbox.getX() + (delta * hitbox.getSpeedX()) / 1000);
		hitbox.setY(hitbox.getY() + (delta * hitbox.getSpeedY()) / 1000);
	} // move
	
	public Hitbox getHitBox() {
		return hitbox;
	}
	
	/*
	 * Draw this entity to the graphics object provided at (x,y)
	 */
	public void draw(Graphics g) {
		sprite.draw(g, hitbox.getX(), hitbox.getY());
	} // draw

	/*
	 * Do the logic associated with this entity. This method will be called
	 * periodically based on game events.
	 */
	public void doLogic() {
	}
	
	public abstract void collidedWith(Entity other);

} // Entity class