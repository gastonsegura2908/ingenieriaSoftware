package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import graphics.Assets;
import graphics.Text;
import input.KeyBoard;
import input.MouseInput;
import input.Timer;
import main.Window;
import math.Vector2D;

public class Button {
	
	private BufferedImage mouseOutImg;
	private BufferedImage mouseInImg;
	protected boolean mouseIn;
	protected Rectangle boundingBox;
	protected Action action;
	private String text;
	private int X;
	protected boolean isSelected=false;
	
	public Button(
			BufferedImage mouseOutImg,
			BufferedImage mouseInImg,
			int x, int y,
			String text,
			Action action
			) {
		this.mouseInImg = mouseInImg;
		this.mouseOutImg = mouseOutImg;
		this.text = text;
		this.X=x;
		boundingBox = new Rectangle(X, y, mouseInImg.getWidth(), mouseInImg.getHeight());
		this.action = action;
	}
	
	public void update() {

		mouseIn = boundingBox.contains(MouseInput.X, MouseInput.Y);
		
		if(mouseIn && Window.mouseTimer.isTime() && MouseInput.MLB) {
			action.doAction(this);
			Window.mouseTimer.reset();
			return;
		}

		if (isSelected && Window.keyBoardTimer.isTime() && KeyBoard.ENTER){
			action.doAction(this);
			Window.keyBoardTimer.reset();
		}

	}
	public void updateTecla(){
		mouseIn=true;
	}

	public void doAction(){
		action.doAction(this);
		mouseIn=false;
	}
	
	public void draw(Graphics g) {
		
		if(boundingBox.contains(MouseInput.X, MouseInput.Y)) {
			g.drawImage(mouseInImg, boundingBox.x, boundingBox.y, null);
		}else {
			g.drawImage(mouseOutImg, boundingBox.x, boundingBox.y, null);
		}

		String buttonText = isSelected ? "[ " + text + " ]" : text;

		Text.drawText(
				g,
				buttonText,
				new Vector2D(
						boundingBox.getX() + boundingBox.getWidth() / 2,
						boundingBox.getY() + boundingBox.getHeight()),
				true,
				Color.BLACK,
				Assets.fontMed);
		
	}


	public void setText(String text) {
		this.text = text;
	}
	public void setX(int x){
		this.X=X+x;
	}
	public int getX(){
		return this.X;
	}
	public String getText(){
		return this.text;
	}

	public void setSelected(boolean isSelected){
		this.isSelected=isSelected;
	}
}
