/* ShotEntity.java
 * March 27, 2006
 * Represents player's ship
 */
public class ShotEntity extends Entity {
	private int counter = 0;
	private boolean used = false; // true if shot hits something
	private Game game; // the game in which the ship exists
	private boolean shipShot;
	/*
	 * construct the shot input: game - the game in which the shot is being
	 * created ref - a string with the name of the image associated to the
	 * sprite for the shot x, y - initial location of shot
	 */
	public ShotEntity(Game g, String r, int newX, int newY, int speed, double angle, boolean shot) {
		super(r, newX, newY); // calls the constructor in Entity
		shipShot = shot;
		game = g;

		angle = angle * Math.PI / 180;
		dy = Math.cos(angle) * speed;
		dx = -(Math.sin(angle) * speed);
	} // constructor

	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move shot
	 */
	public void move(long delta) {
		counter++;
		offScreen(this);
		
		super.move(delta); // calls the move method in Entity

		// if shot moves off top of screen, remove it from entity list
		if (counter >= 70) {
			game.removeEntity(this);
		} // if

	} // move

	/*
	 * collidedWith input: other - the entity with which the shot has collided
	 * purpose: notification that the shot has collided with something
	 */
	public void collidedWith(Entity other) {
		// prevents double kills
		if (used) {
			return;
		} // if

		// if it has hit an alien, kill it!
		if (other instanceof AlienEntity || other instanceof AsteroidEntity) {
			// remove affect entities from the Entity list
			game.removeEntity(this);
			
			// notify the game that the alien is dead
			game.notifyAlienKilled(other);
			game.removeEntity(other);

			
			used = true;
		}
		if (other instanceof ShipEntity && shipShot == false) {
			game.removeEntity(this);
			invincible = true;
			game.notifyDeath();
			x = 370;
			y = 530;
			dy = 0;
			dx = 0;
		} // else

	} // collidedWith

} // ShipEntity class