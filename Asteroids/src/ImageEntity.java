public class ImageEntity extends Entity {

	//private Game game; // the game in which the alien exists

	/*
	 * construct a new alien input: game - the game in which the alien is being
	 * created r - the image representing the alien x, y - initial location of
	 * alien
	 */
	public ImageEntity(Game g, String r, int newX, int newY, int size) {
		super(r, newX, newY); // calls the constructor in Entity
		//game = g;
		type = size;
	}//ImageEntity
	
	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move alien
	 */
	public void move(long delta) {
		offScreen(this);
		super.move(delta);
	} // move

	/*
	 * collidedWith input: other - the entity with which the alien has collided
	 * purpose: notification that the alien has collided with something
	 */
	public void collidedWith(Entity other) {
		// collisions with aliens are handled in ShotEntity and ShipEntity
	} // collidedWith

} // AlienEntity class
