package com.finra.smartinvest;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UIGraphic {
	private String imgPath;
	public Image img;
	public int x, y, w, h;
	
	public UIGraphic(int x, int y, int w, int h, String imgPath) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.imgPath = imgPath;
		
		try {
			img = ImageIO.read(new File(imgPath));
			// scale image...
		} catch (IOException e) {
			System.err.println("Failed to load image at: \"" + this.imgPath + "\"");
		}
	}
}
