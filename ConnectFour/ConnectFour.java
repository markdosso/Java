/*
 *  Author: Mark Dosso
 *  Date: Sept 2017
 */

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.beans.Expression;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConnectFour extends Canvas{

	public static boolean win = false;
	public static boolean playerWin;
	static int[][] connect = new int[6][7];
	public static boolean full = false;
	public static boolean player = true;
	private static BufferStrategy strategy; // take advantage of accelerated graphics
	private int[][] undoBoard = new int[6][7];
	private int counter = 0;
	
	public static void main(String[] args) {
		new ConnectFour();
	}
	
	public ConnectFour() {
		// create a frame to contain game
		JFrame container = new JFrame("Connect Four");
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("cool.png"));
		} catch (IOException e) {
		
		}
		container.setIconImage(img);
	
		// get hold the content of the frame
		JPanel panel = (JPanel) container.getContentPane();

		// set up the resolution of the game
		panel.setPreferredSize(new Dimension(700, 600));
		panel.setLayout(null);

		// set up canvas size (this) and add to frame
		setBounds(0, 0, 800, 700);
		panel.add(this);
		setBackground(new Color(42,42,42));
		// Tell AWT not to bother repainting canvas since that will
		// be done using graphics acceleration
		setIgnoreRepaint(true);

		// make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// if user closes window, shutdown game and jre
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			} // windowClosing
		});

		// add key listener to this canvas
		addKeyListener(new KeyInputHandler());
		
		// request focus so key events are handled by this canvas
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.black);
		for (int x = 0; x < 6; x++) {
			g.fillRect(100 + (x*100), 0, 10, 800);
		}
		
		for (int x = 0; x < 6; x++) {
			g.fillRect(0, 70 + (x*100), 800, 10);
		}
		
		Font currentFont = g.getFont();
		Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.1F);
		g.setFont(newFont);
		g.getFont().deriveFont(Font.BOLD); 
		g.setColor(Color.white);
		g.drawString("1", 40, 605);
		g.drawString("2", 150, 605);
		g.drawString("3", 250, 605);
		g.drawString("4", 350, 605);
		g.drawString("5", 450, 605);
		g.drawString("6", 550, 605);
		g.drawString("7", 650, 605);
		
		paintThis();
		strategy.show();
		requestFocus();
		gameLoop();
	}
	
	public static void gameLoop() {
		do {
			if (win) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} // try catch
				continue;
			} // if
			
		} while (true);
	} // gameLoop
		
	// places a marker at the specified position on the board
	public void placeMarker(int position) {
		for (int i = connect.length - 1; i >= 0; i--) {
			if (connect[i][position] == 0) {
				if (player == true) {
					connect[i][position] = 1;
					player = false;
					undoBoard[i][position] = ++counter;
					break;
				} else {
					connect[i][position] = 2;
					player = true;
					undoBoard[i][position] = ++counter;
					break;
				} // else
			} // if
		} // for
		checkWin();
		paintThis();
	} // placeMarker
	
	// checks to see if a player has one
	public static void checkWin() {
		int counter = 0;
		for (int x = 1; x < 3; x++) {	
			for (int i = 0; i< connect.length; i++) {
				counter = 0;
				for (int j = 0; j < connect[i].length; j++) {
					if (connect[i][j] == x) {
						counter++;
					} else {
						counter = 0;
					} // else
					if (counter == 4) {
						win = true;
						if (x == 1) {
							playerWin = true;
						} else { 
							playerWin = false;
						} // else
						return;
					} // if
				} // for
			} // for
			
			for (int i = 0; i< connect[0].length; i++) {
				counter = 0;
				for (int j = 0; j < connect.length; j++) {
					if (connect[j][i] == x) {
						counter++;
					} else { 
						counter = 0;
					} // else
					if (counter == 4) {
						win = true;
						if (x == 1) {
							playerWin = true;
						} else { 
							playerWin = false;
						} // else
						return;
					} // if
				} // for
			} // for
			
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 4; j++) {
					if (connect[i][j] == x
						&& connect[i+1][j+1] == x
						&& connect[i+2][j+2] == x
						&& connect[i+3][j+3] == x) {
						win = true;
						if (x == 1) {
							playerWin = true;
						} else {
							playerWin = false;
						} // else
						return;
					} // if
				} // for
			} // for
			
			
			for (int i = connect.length -1; i > 3; i--) {
				for (int j = 0; j < 4; j++) {
					if (connect[i][j] == x
						&& connect[i-1][j+1] == x
						&& connect[i-2][j+2] == x
						&& connect[i-3][j+3] == x) {
						win = true;
						//System.out.println("WINNNN");
						if (x == 1) {
							playerWin = true;
						} else {
							playerWin = false;
						} // else
						return;
					} // if
				} // for
			} // for
			
			
			for (int i = 0; i < 3; i++) {
				for (int j = connect[0].length - 1; j > 2; j--) {
					if (connect[i][j] == x
						&& connect[i+1][j-1] == x
						&& connect[i+2][j-2] == x
						&& connect[i+3][j-3] == x) {
						win = true;
						//System.out.println("WINNNN");
						if (x == 1) {
							playerWin = true;
						} else {
							playerWin = false;
						} // else
						return;
					} // if
				} // for
			} // for
		} // for
	} // checkWin
	
	public static void paintThis() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		for (int i = 0; i < connect.length; i++) {
			for (int j = 0; j < connect[i].length; j++) {
				if (connect[i][j] == 0) {
					g.setColor(Color.WHITE);
					g.fillOval((j * 100) + 45, (i * 100) + 20 , 21, 21);
				} else if (connect[i][j] == 1) {
					g.setColor(Color.black);
					g.fillOval((j * 100) + 45, (i * 100) + 20 , 21, 21);
					g.setColor(Color.red);
					g.fillOval((j * 100) + 45, (i * 100) + 20 , 20, 20);		
				} else {
					g.setColor(Color.black);
					g.fillOval((j * 100) + 45, (i * 100) + 20, 21, 21);
					g.setColor(Color.yellow);
					g.fillOval((j * 100) + 45, (i * 100) + 20, 20, 20);	
				} // else
			} // for
		} // for
		
		if (!full) {
			full = true;
			for (int i = 0; i < connect.length; i++) {
				if (full) {
					for (int j = 0; j < connect[i].length;j++) {
						if (connect[i][j] == 0) {
							full = false;
							break;
						} // if
					} // for
				} else {
					break;
				} // else
			} // for
		} // if
		
		if (win) {
			Font currentFont = g.getFont();
			Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.1F);
			g.setFont(newFont);
			g.getFont().deriveFont(Font.BOLD); 
			g.setColor(Color.black);
			if (!playerWin) {
				g.setColor(Color.magenta);
				g.drawString("Player Yellow WINS!!!", 250, 50);	
			} else {
				g.setColor(Color.magenta);
				g.drawString("Player Red WINS!!!", 250, 50);
			} // else
			
		} else if (full){
			Font currentFont = g.getFont();
			Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.1F);
			g.setFont(newFont);
			g.getFont().deriveFont(Font.BOLD); 
			g.setColor(Color.magenta);
			g.drawString("Tie Press \"Y\" to reset", 250, 50);	
		} // else if
		strategy.show();	
	} // paintThis
	
	public void reset() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		Font currentFont = g.getFont();
		Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.1F);
		g.setFont(newFont);
		g.getFont().deriveFont(Font.BOLD); 
		
		g.setColor(new Color(42,42,42));

		g.drawString("Player Yellow WINS!!!", 250, 50);	
		g.drawString("Player Red WINS!!!", 250, 50);
		g.drawString("Tie Press \"Y\" to reset", 250, 50);
		
		g.setColor(Color.black);
		for (int x = 0; x < 6; x++) {
			g.fillRect(100 + (x*100), 0, 10, 700);
		} // for
		//System.out.println("HIIIIIIIIIIIIIII");
		for (int x = 0; x < 6; x++) {
			g.fillRect(0, 70 + (x*100), 700, 10);
		} // for
		
		for (int i = 0; i < connect.length; i++) {
			for (int j = 0; j < connect[i].length; j++) {
				connect[i][j] = 0;
				undoBoard[i][j] = 0;
			} // for
		} // for
		counter = 0;
		win = false;
		full = false;
		paintThis();
	} // reset
	
	public void undo() {
		int temp = 0;
		int x = 0;
		int y = 0;
		for (int i = 0; i < undoBoard.length; i++) {
			for (int j = 0; j < undoBoard[i].length; j++) {
				if (undoBoard[i][j] > temp) {
					temp = undoBoard[i][j];
					x = j;
					y = i;
				} // if
			} // for
		} // for
		
		connect[y][x] = 0;
		undoBoard[y][x] = 0;
		if (player) {
			player = false;
		} else {
			player = true;
		} // else
		paintThis();
	} // undo
	
	private class KeyInputHandler extends KeyAdapter {

		public void keyReleased(KeyEvent e) {			
			if (e.getKeyCode() == 49 && !win) {
				placeMarker(0);
			} // if
			
			if (e.getKeyCode() == 82) {
				undo();
			} // if
			
			if (e.getKeyCode() == 50 && !win) {
				placeMarker(1);
			} // if
			
			if (e.getKeyCode() == 51 && !win) {
				placeMarker(2);
			} // if
			
			if (e.getKeyCode() == 52 && !win) {
				placeMarker(3);
			} // if
			if (e.getKeyCode() == 53 && !win) {
				placeMarker(4);
			} // if
			
			if (e.getKeyCode() == 54 && !win) {
				placeMarker(5);
			} // if
			
			if (e.getKeyCode() == 55 && !win) {
				placeMarker(6);
			} // if
			
			if (e.getKeyCode() == 89) {
				reset();
			} // if
			
			if (e.getKeyCode() == 78 && win) {
				System.exit(0);
			} // if
		} // keyReleased	
	} // keyInputHandler
} // ConnectFour
