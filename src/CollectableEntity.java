public class CollectableEntity extends Entity {

	protected boolean collected = false;
	Game game;
	
	public CollectableEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY);
		this.game = g;

	} // Constructor

	public boolean isCollected() {
		return collected;
	} // isCollected
	
	@Override
	public void collidedWith(Entity other) {
		
		game.playSoundEffect(29);
		game.player.addCoin();
		game.removeEntity(this);
		
		this.collected = true;

		
	} // collidedWith
	
} // CollectableEntity