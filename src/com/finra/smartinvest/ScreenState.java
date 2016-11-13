package com.finra.smartinvest;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;


public class ScreenState {
	
	// Global class variables
	private String id;
	public ArrayList<GraphicButton> buttons = new ArrayList<GraphicButton>();
	public ArrayList<TextBox> textboxes = new ArrayList<TextBox>();
	public ArrayList<UIGraphic> images = new ArrayList<UIGraphic>();
	
	public Color background = Color.WHITE; // default
	
	// Constructors
	
	public ScreenState(String id) {
		this.id = id;
	}
	
	// Setters and Adders
	
	public void setBackground(Color c) {
		this.background = c;
	}
	
	public void addButton(int x, int y, int w, int h, String path, String id) throws IOException {
		buttons.add(new GraphicButton(x, y, w, h, path, id));
	}
	
	public void addButton(int x, int y, int w, int h, String path, String id, String text, int size) throws IOException {
		buttons.add(new GraphicButton(x, y, w, h, path, id, text, size));
	}
	
	public void addTextBox(double screenWidth, int y, int size, String text, boolean centered) {
		textboxes.add(new TextBox((int)screenWidth, y, size, text, centered));
	}
	
	public void addImage(int x, int y, int w, int h, String imgPath) throws IOException {
		images.add(new UIGraphic(x, y, w, h, imgPath));
	}
	
	public void clearAll() {
		textboxes.clear();
		buttons.clear();
		images.clear();
	}
	
	public void clearTextBoxes() {
		textboxes.clear();
	}
	
	// Getters
	
	public String getIdentifier() {
		return id;
	}
	
	/*
	 * Fades elements if hovered
	 */
	public String checkIfMouseCollided(int x, int y) {
		for (GraphicButton b : buttons) {
			if (b.isCollided(x, y))
				return b.id;
		}
		return "";
	}
}
