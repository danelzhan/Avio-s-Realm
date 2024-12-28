
public class KeyEntity extends CollectibleEntity {

	public KeyEntity(Game g, String r, int newX, int newY) {
		super(g, r, newX, newY);
		
	} // Constructor
	
	@Override
	public void collidedWith(Entity other) {
		
		if(!collected) {
			game.playSoundEffect(29);
			
			
			game.player.addKey();
			game.removeEntity(this);
			
			this.collected = true;
		}
	} // collidedWith

} // KeyEntity
