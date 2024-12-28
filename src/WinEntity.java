
public class WinEntity extends CollectibleEntity {

	public WinEntity(Game g, String r, int newX, int newY) {
		super(g, r, newX, newY);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void collidedWith(Entity other) {

		if (!collected) {
			game.playSoundEffect(29);
			game.notifyWin();
			game.removeEntity(this);

			this.collected = true;
		}

	} // collidedWith

} // WinEntity
