package com.finra.smartinvest;

import java.awt.Font;
import java.awt.Graphics;

public class TextBox {
	public int x, y, size, w, screenWidth;
	private String text;
	private boolean centered;
	
	public TextBox(int screenWidth, int y, int size, String text, boolean centered) {
		this.screenWidth = screenWidth;
		this.y = y;
		this.size = size;
		this.text = text;
		this.centered = centered;
	}
	
	public void setWidth(Graphics g) {
		String[] lines = this.text.split("\n");
		int maxSize = 0;
		for (String line : lines) {
			if (g.getFontMetrics().stringWidth(line) > maxSize)
				maxSize = g.getFontMetrics().stringWidth(line);
		}
		this.w = maxSize;
		
		this.x = (screenWidth/2) - (this.w/2);
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public void drawText(Graphics g) {
		g.setFont(new Font("MyriadPro", Font.PLAIN, this.size));
		setWidth(g);
		
		int y_offset = 0;
		// Centers text to largest line if centered = true
		String[] lines = this.text.split("\n");
		
		// Draw the lines
		for (int i = 0; i < lines.length; i++) {
			int x_offset = 0;
			if (centered) {
				x_offset = Math.round((this.w - g.getFontMetrics().stringWidth(lines[i])) / 2);
			}
			g.drawString(lines[i], this.x + x_offset, this.y + y_offset);
			y_offset += (this.size + 2);
		}
	}
}
