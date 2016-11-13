package com.finra.smartinvest;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

enum State {
	MAIN,
	QUESTION,
	ANSWER,
	ABOUT
}

// Suppress warnings for now...
@SuppressWarnings("serial")

public class AwtQuizCanvas extends Canvas {
	private static String appTitle = "Smart Invest";
	private static State screenState = State.MAIN;
	
	private static JFrame mainFrame;
	private static AwtQuizCanvas quizCanvas;
	
	private static String[] currQA;
	
	// Define screen states
	private static ScreenState questionScreen;
	private static ScreenState answerScreen;
	private static ScreenState mainScreen;
	private static ScreenState aboutScreen;
	
	
	public static void main(String[] args) throws IOException {
		// Create an launch splash screen
		JWindow splashScreen = new JWindow();
		splashScreen.getContentPane().add(
				new JLabel("", new ImageIcon("Images/splashscreen.png"), SwingConstants.CENTER));
		splashScreen.setBounds(500, 150, 382, 441);
		splashScreen.setVisible(true);
		
		// Initialize screen states and UI
		mainScreen = new ScreenState("Main");
		questionScreen = new ScreenState("Question");
		answerScreen = new ScreenState("Answer");
		aboutScreen = new ScreenState("About");
		
		// Canvas and JFrame setup
		quizCanvas = new AwtQuizCanvas();
	    
		
		mainFrame = new JFrame();
		mainFrame.setTitle(appTitle);
		mainFrame.setSize(960,540);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initUI();
		
		splashScreen.setVisible(false);
		splashScreen.dispose();
		
		mainFrame.add(quizCanvas);
		mainFrame.setVisible(true);
	}
	
	// Defines the mouseListener
	public AwtQuizCanvas() {
		addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e){
            	switch(getScreenState().checkIfMouseCollided(e.getX(), e.getY())) {
            	case "YES":
            		if (currQA[2].equals("yes"))
            			answerScreen.addTextBox(mainFrame.getSize().getWidth(), 100, 20, "Yes, you are correct!\n" + currQA[1], true);
            		else
            			answerScreen.addTextBox(mainFrame.getSize().getWidth(), 100, 20, "Sorry, but you are incorrect.\n" + currQA[1], true);
            		screenState = State.ANSWER;
            		repaint();
            		break;
            	case "NO":
            		if (currQA[2].equals("no"))
            			answerScreen.addTextBox(mainFrame.getSize().getWidth(), 100, 20, "Yes, you are correct!\n" + currQA[1], true);
            		else
            			answerScreen.addTextBox(mainFrame.getSize().getWidth(), 100, 20, "Sorry, but you are incorrect.\n" + currQA[1], true);
            		screenState = State.ANSWER;
            		repaint();
            		break;
            	case "QUESTIONS":
            		screenState = State.QUESTION;
            		initQuestionAnswer();
            		repaint();
            		break;
            	case "ANSWER":
            		screenState = State.ANSWER;
            		repaint();
            		break;
            	case "ABOUT":
            		screenState = State.ABOUT;
            		repaint();
            		break;
            	case "RETURN":
            		screenState = State.MAIN;
            		repaint();
            		break;
            	case "QUIT":
            		System.exit(0);
            		break;
            	case "":
            		// No button clicked
            		break;
            	default:
            		System.out.println("Warning: Control case for this button is not defined.");
            		break;
            	}
            }

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		paintComponent(g);
	}
	
	public void paintComponent(Graphics g) {
		// Initialize the graphics object
		Graphics2D g2;
		g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
		// Draw elements from the screen state
		ScreenState screen = getScreenState();
		
		// Set background color
		quizCanvas.setBackground(screen.background);
		
		// Draw any graphics
		for (UIGraphic i : screen.images) {
			g2.drawImage(i.img, i.x, i.y, i.w, i.h, null);
		}
		
		// Draw button elements
		for (GraphicButton b : screen.buttons) {
			if (b.img != null) {
				// Draw button with image
				g.drawImage(b.img, b.x, b.y, b.w, b.h, null);
			} else {
				// Draw button with default grey box
				g.setColor(Color.DARK_GRAY);
				g2.drawRect(b.x, b.y, b.w, b.h);
			}
			
			if (b.text != "") {
				b.setTextPosition(g2);
				g2.drawString(b.text, b.textX, b.textY);
			}
		}
		
		// Draw TextBox elements
		for (TextBox t : screen.textboxes) {
			t.drawText(g2);
		}
	}
	
	public static void initUI() throws IOException {
		// Define state classes here and their elements
		// Note: Should really only use this for static states (i.e. Anything other than questions and answers)
		
		// Adding images
		mainScreen.addImage(0, 0, (int)mainFrame.getSize().getWidth(), (int)mainFrame.getSize().getHeight(), "Images/main.png");
		questionScreen.addImage(0, 0, (int)mainFrame.getSize().getWidth(), (int)mainFrame.getSize().getHeight(), "Images/background.png");
		answerScreen.addImage(0, 0, (int)mainFrame.getSize().getWidth(), (int)mainFrame.getSize().getHeight(), "Images/background.png");
		aboutScreen.addImage(0, 0, (int)mainFrame.getSize().getWidth(), (int)mainFrame.getSize().getHeight(), "Images/background.png");
		
		// Adding Buttons
		mainScreen.addButton((int)(mainFrame.getSize().getWidth()/4)-(180/2), (int)(5*mainFrame.getSize().getHeight()/8)-(50/2), 180, 50, "Images/questions.png", "QUESTIONS");
		mainScreen.addButton((int)(mainFrame.getSize().getWidth()/2)-(180/2), (int)(5*mainFrame.getSize().getHeight()/8)-(50/2), 180, 50, "Images/about.png", "ABOUT");
		mainScreen.addButton((int)(3*mainFrame.getSize().getWidth()/4)-(180/2), (int)(5*mainFrame.getSize().getHeight()/8)-(50/2), 180, 50, "Images/quit.png", "QUIT");
		questionScreen.addButton((int)(7*mainFrame.getSize().getWidth()/8)-(120/2), (int)(11*mainFrame.getSize().getHeight()/14)-(120/2), 120, 120, "Images/yes.png", "YES");
		questionScreen.addButton((int)(mainFrame.getSize().getWidth()/8)-(120/2), (int)(11*mainFrame.getSize().getHeight()/14)-(120/2), 120, 120, "Images/no.png", "NO");
		answerScreen.addButton((int)(7*mainFrame.getSize().getWidth()/8)-(80/2), (int)(11*mainFrame.getSize().getHeight()/14)-(80/2), 80, 80, "Images/next.png", "QUESTIONS");
		answerScreen.addButton((int)(mainFrame.getSize().getWidth()/8)-(80/2), (int)(11*mainFrame.getSize().getHeight()/14)-(80/2), 80, 80, "Images/return.png", "RETURN");
		aboutScreen.addButton((int)(mainFrame.getSize().getWidth()/8)-(80/2), (int)(11*mainFrame.getSize().getHeight()/14)-(80/2), 80, 80, "Images/return.png", "RETURN");
		
		// Adding text boxes
		aboutScreen.addTextBox(mainFrame.getSize().getWidth(), 50, 20, "About\n\n"
				+ "Smart Invest uses simulated world market data and their\n"
				+ "algorithms to help beginners make their first steps in the world of\n"
				+ "stock investment. Learn trading with our stock market simulator\n"
				+ "and use it as a guide for your future investments.\n"
				+ "Enhance your knowledge of the stock market and test yourself\n"
				+ "in different scenarios.\n\n"
				+ "Smart Invest will provide you with scenarios that you have to\n"
				+ "make wise choices and based on how well you do, the questions\n"
				+ "get more challenging.\n\nCameron Van Ravens - Lead Sofware Design"
				+ "\nErfan Jamalifar - Assistant Software Design\nAbsinthe Wu - Lead UI Designer and Layout", true);
	}
	
	// Dynamic initUI functions
	public static void initQuestionAnswer() {
		questionScreen.clearTextBoxes();
		answerScreen.clearTextBoxes();
		
		currQA = QuestionGenerator.generateQuestionAnswer();
		
		questionScreen.addTextBox(mainFrame.getSize().getWidth(), 100, 20, currQA[0], true);
	}
	
	// Get the current object which holds the current screen state
	public static ScreenState getScreenState() {
		switch(screenState) {
		case MAIN:
			return mainScreen;
		case QUESTION:
			return questionScreen;
		case ANSWER:
			return answerScreen;
		case ABOUT:
			return aboutScreen;
		default:
			return null;	// could not find defined screen state
		}
	}
}
