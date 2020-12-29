/* AlienEntity.java
 * March 27, 2006
 * Represents one of the aliens
 */
public class AlienEntity extends Entity {

	//private double moveSpeed = 75; // horizontal speed

	//private Game game; // the game in which the alien exists

	/*
	 * construct a new alien input: game - the game in which the alien is being
	 * created r - the image representing the alien x, y - initial location of
	 * alien
	 */
	public AlienEntity(Game g, String r, int newX, int newY, int size) {
		super(r, newX, newY); // calls the constructor in Entity
		//game = g;
		type = size;
		int degreeX = (int)(Math.random() * 360);
		int degreeY = (int)(Math.random() * 360);
		degreeX = (int) (degreeX * Math.PI / 180);
		degreeY = (int) (degreeY * Math.PI / 180);
		dy = Math.cos(degreeY) * ((Math.random() * 200) + 50);
		dx = -(Math.sin(degreeX) * ((Math.random() * 200) + 50)); 
	} // constructor

	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move alien
	 */
	public void move(long delta) {
		offScreen(this);
		int rand1 = (int) (Math.random() * 10000);
		
		if (rand1 >= 9950) {
			int degreeX = (int) ((Math.random() * 360) + 1);
			int degreeY = (int) ((Math.random() * 360) + 1);
			degreeX = (int) (degreeX * Math.PI / 180);
			degreeY = (int) (degreeY * Math.PI / 180);
			dy = Math.cos(degreeY) * ((Math.random() * 200) + 50);
			dx = -(Math.sin(degreeX) * ((Math.random() * 200) + 50)); 
		}
		
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
