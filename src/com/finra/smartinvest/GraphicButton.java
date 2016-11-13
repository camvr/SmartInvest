package com.finra.smartinvest;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GraphicButton {
	public int x, y, w, h, textX, textY, size;
	public String imgPath, id, text;
	public Image img;
	
	
	public GraphicButton(int x, int y, int w, int h, String imgPath, String id) throws IOException {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.imgPath = imgPath;
		this.id = id;
		this.text = "";
		
		try {
			img = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			System.err.println("Failed to load image at: \"" + this.imgPath + "\"");
		}
	}
	
	public GraphicButton(int x, int y, int w, int h, String imgPath, String id, String text, int size) throws IOException {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.imgPath = imgPath;
		this.id = id;
		this.text = text;
		this.size = size;
		
		try {
			img = ImageIO.read(new File(imgPath));
			// scale image to size
		} catch (IOException e) {
			System.err.println("Failed to load image at: \"" + this.imgPath + "\"");
		}
	}
	
	public void setTextPosition(Graphics g) {
		g.setFont(new Font("", Font.PLAIN, this.size));
		this.textX = x + (w/2) - (g.getFontMetrics().stringWidth(this.text)/2);
		this.textY = y + (h/2);
	}
	
	/*
	 * Input the mouse x and y position and it will return if it is collided with
	 * this button.
	 */
	public boolean isCollided(int x, int y) {
		if (x >= this.x && x <= this.x + this.w && y >= this.y && y <= this.y + this.h) 
			return true;
		return false;
	}
}
