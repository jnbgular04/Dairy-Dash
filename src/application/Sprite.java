package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	protected Image image;
	protected int x, y, dx, dy;
	protected double width;
	protected double height;
	
	protected boolean visible;
	
	// constructor
	public Sprite(int x_pos, int y_pos) {
		this.x = x_pos;
		this.y = y_pos;
		this.visible = true;
	}
	
	// loading, setting the image
	protected void loadImage (String spriteImage) {
		try {
			Image img = new Image(spriteImage);
			this.image = img;
			this.setSize();
		} catch (Exception e) {}
	}
	
	// rendering image
	void render(GraphicsContext gc) {
		gc.drawImage(this.image, this.x, this.y);
	}
	
	private void setSize() {
		this.width = this.image.getWidth();
		this.height = this.image.getHeight();
	}
	
	Image getImage() {
		return this.image;
	}
	
	// GETTERS
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	public boolean isVisible() {
		if(visible) {
			return true;
		}
		return false;
	}
	
	// SETTERS
	public void setDX(int dx) {
		this.dx = dx;
	}
	
	public void setDY(int dy) {
		this.dy = dy;
	}
	
	public void setWidth(double value) {
		this.width = value;
	}
	
	public void setHeight(double value) {
		this.height = value;
	}
	
	public void setVisible(boolean value) {
		this.visible = value;
	}
}
