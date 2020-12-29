
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

	protected double x; // current x location
	protected double y; // current y location
	protected Sprite sprite; // this entity's sprite
	protected double dx; // horizontal speed (px/s) + -> right
	protected double dy; // vertical speed (px/s) + -> down
	protected int type;
	protected boolean invincible;
	// protected double degree;

	private Rectangle me = new Rectangle(); // bounding rectangle of
											// this entity
	private Rectangle him = new Rectangle(); // bounding rect. of other
												// entities

	/*
	 * Constructor input: reference to the image for this entity, initial x and
	 * y location to be drawn at
	 */
	public Entity(String r, int newX, int newY) {
		x = newX;
		y = newY;
		sprite = (SpriteStore.get()).getSprite(r);
	} // constructor

	public void setShipDegree(double deg) {
		sprite = (SpriteStore.get()).getSprite("sprites/" + (int) deg + ".png");
		// System.out.println(deg);
	}// setShipDegree

	/*
	 * move input: delta - the amount of time passed in ms output: none purpose:
	 * after a certain amout of time has passed, update the location
	 */
	public void move(long delta) {
		// update location of entity based on move speeds
		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	} // move

	// get and set velocities
	public void setHorizontalMovement(double newDX) {
		dx = newDX;
	} // setHorizontalMovement

	public void setVerticalMovement(double newDY) {
		dy = newDY;
	} // setVerticalMovement

	public double getHorizontalMovement() {
		return dx;
	} // getHorizontalMovement

	public double getVerticalMovement() {
		return dy;
	} // getVerticalMovement
	
	public void setInvincible(boolean die) {
		invincible = die;
	} // setVerticalMovement

	public boolean getInvincible() {
		return invincible;
	} // getHorizontalMovement

	// get position
	public int getX() {
		return (int) x;
	} // getX

	public int getY() {
		return (int) y;
	} // getY
	
	public void setX(int newX){
		x = newX;
	}//setX
	
	public void setY(int newY){
		y = newY;
	}//setY

	/*
	 * Draw this entity to the graphics object provided at (x,y)
	 */
	public void draw(Graphics g) {
		sprite.draw(g, (int) x, (int) y);
	} // draw

	

	/*
	 * collidesWith input: the other entity to check collision against output:
	 * true if entities collide purpose: check if this entity collides with the
	 * other.
	 */
	public boolean collidesWith(Entity other) {
		me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
		him.setBounds(other.getX(), other.getY(), other.sprite.getWidth(), other.sprite.getHeight());
		return me.intersects(him);
	} // collidesWith

	public void offScreen(Entity object) {
		if(!(object instanceof ShotEntity)){
			char side = ' ';
			if (x > 810) {
				side = 'l';
			} else if (x < -10) {
				side = 'r';
			} else if (y > 610) {
				side = 'b';
			} else if (y < -10) {
				side = 't';
			} // else if
			
			if (side == 'l') {
				x = -10;
			} else if (side == 'r') {
				x = 810;
			} else if (side == 'b') {
				y = -10;
			} else if (side == 't') {
				y = 610;
			} // else if
		}
		
	}

	/*
	 * collidedWith input: the entity with which this has collided purpose:
	 * notification that this entity collided with another Note: abstract
	 * methods must be implemented by any class that extends this class
	 */
	public abstract void collidedWith(Entity other);

	public void changeShipVelocity(double velo, double deg, boolean up) {
	}

} // Entity class
