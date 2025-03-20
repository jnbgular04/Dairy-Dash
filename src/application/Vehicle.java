package application;

import javafx.scene.shape.Rectangle;

public class Vehicle extends Sprite {
	
	private int direction;

	// constructor
	public Vehicle(int x_pos, int y_pos) {
		super(x_pos, y_pos);
		this.imageSetUp();
	}
	
	// loading default image
	public void imageSetUp() {
		this.loadImage(getClass().getResource("cow_up.png").toExternalForm());
	}
	
//	public void move() {
//		// limiting movement within bounds
//		if (( (this.x + this.dx) > 0 ) && ((this.y + this.dy) > 0) && ((this.x + this.dx) < GameStage.WINDOW_WIDTH - 
//				(int) this.width) && ((this.y + this.dy) < GameStage.WINDOW_HEIGHT - (int) this.height )){
//			// checking if natural disaster is currently occurring
//			if (GameTimer.naturalDisasterOcc == false) {
//				this.x += this.dx;
//				this.y += this.dy;
//			}
//			else {
//				this.x += (this.dx)*4; // speed quadruples during a thunderstorm
//				this.y += (this.dy)*4;
//			}
//		}
//	}
	
	public void move() {
		if(( (this.x + this.dx) > 0 ) && ((this.y + this.dy) > 0) && ((this.x + this.dx) < GameStage.WINDOW_WIDTH - 
				(int) this.width) && ((this.y + this.dy) < GameStage.WINDOW_HEIGHT - (int) this.height )){
			this.x += this.dx;
			this.y += this.dy;
		}
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	// checking collisions
	public boolean collidesWith(Rectangle rectangle)	{
		Rectangle rectangle1 = this.getSpriteBounds();
		Rectangle rectangle2 = rectangle;

		return rectangle1.getBoundsInLocal().intersects(rectangle2.getBoundsInLocal());
	}
	
	public Rectangle getSpriteBounds() {
		return new Rectangle(this.x+10, this.y+10, this.width-10, this.height-10);
	}
}
