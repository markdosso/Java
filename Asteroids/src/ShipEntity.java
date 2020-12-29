/* ShipEntity.java
 * March 27, 2006
 * Represents player's ship
 */
public class ShipEntity extends Entity {

	protected double direction;
	private Game game; // the game in which the ship exists
	private double velocity;
	private double degree;
	private boolean upPressed;
	private boolean invincible;

	/*
	 * construct the player's ship input: game - the game in which the ship is
	 * being created ref - a string with the name of the image associated to the
	 * sprite for the ship x, y - initial location of ship
	 */
	public ShipEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY); // calls the constructor in Entity
		game = g;
	} // constructor

	/*
	 * move input: delta - time elapsed since last move (ms) purpose: move ship
	 */

	public void changeShipVelocity(double velo, double deg, boolean up) {
		
		velocity = velo;
		degree = deg;
		//System.out.println(degreeChange + " " + degree + " " + deg);
		upPressed = up;
		
	} // changeshipvelocity

	public void move(long delta) {
		double tempX = 0;
		double tempY = 0;
		degree = degree * Math.PI / 180;
		if (!upPressed) {
			
			dy /= 1.018;
			dx /= 1.018;
		} else {
			tempX = dx;
			tempY = dy;
			
			if (Math.abs(dy) < 200 ) {
				dy += Math.cos(degree) * velocity;
			}// if
			
			if (Math.abs(dx) < 200 ) {			
				dx += -(Math.sin(degree) * velocity);
			} // if 
			
			if (Math.abs(dy) > 200) {
				dy = tempY;
			} // if
				
			if (Math.abs(dx) > 200 ) {
				dx = tempX;
			} // if
			
				
		} // else
		// move the ship to the top, bottom, left, or right of the screen
		
		offScreen(this);
		
		super.move(delta); // calls the move method in Entity
	} // move

	public void setShipDegree(double deg) {
		super.setShipDegree(deg);
	}// setShipDegree
	
	public void setInvincible(boolean die) {
		invincible = die;
	} // setVerticalMovement

	public boolean getInvincible() {
		return invincible;
	} // getHorizontalMoveme

	/*
	 * collidedWith input: other - the entity with which the ship has collided
	 * purpose: notification that the player's ship has collided with something
	 */
	public void collidedWith(Entity other) {
		
		if (other instanceof AlienEntity && invincible == false|| other instanceof AsteroidEntity && invincible == false) {
			game.removeEntity(other);
			invincible = true;
			game.notifyDeath();
			x = 370;
			y = 530;
			dy = 0;
			dx = 0;
		} // if

	} // collidedWith

} // ShipEntity class