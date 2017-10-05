

// Kevin Nguyen
// Dr. Klump
// Object Oriented Programming
// HW 5
// March 27, 2017

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// Model class
class Die {
	private int x, y, value;
	private Color dieColor, dotColor;
	
	// Default constructor
	public Die() {
		x = 0;
		y = 0;
		value = 1;
		dieColor = Color.WHITE;
		dotColor = Color.BLACK;
	}
	
	// Non-default constructor
	public Die(int x, int y, int value, Color dieColor) {
		setX(x);
		setY(y);
		setValue(value);
		setDieColor(dieColor);
	}
	
	// String format
	public String toString() {
		return String.format("Value = %d, x = %d, y = %d", value, x, y);
	}
	
	// Get functions
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getValue() {
		return value;
	}
	public Color getDieColor() {
		return dieColor;
	}
	public Color getDotColor() {
		return dotColor;
	}
	
	// Set functions
	public void setX(int x) {
		if (x < 0) {
			this.x = 0;
		}
		else if (x >= DiceFrame.getFrameSize() - DiceDrawer.getDieSize()) {
			this.x = DiceFrame.getFrameSize() - DiceDrawer.getDieSize();
		}
		else {
			this.x = x;
		}
	}
	public void setY(int y) {
		if (y < 0) {
			this.y = 0;
		}
		else if (y >= DiceFrame.getFrameSize() - DiceDrawer.getDieSize()) {
			this.y = DiceFrame.getFrameSize() - DiceDrawer.getDieSize();
		}
		else {
			this.y = y;
		}
	}
	public void setValue(int value) {
		if (value < 1) {
			this.value = 1;
		}
		else if (value > 6) {
			this.value = 6;
		}
		else {
			this.value = value;
		}
	}
	public void setDieColor(Color dieColor) {
		this.dieColor = dieColor;
		if (this.dieColor == Color.WHITE) {
			dotColor = Color.BLACK;
		}
		else {
			dotColor = Color.WHITE;
		}
	}
}

// Dice controller
class DiceManager {
	ArrayList<String> rollRecord;
	private Die firstDie, secondDie;
	private Die[] dice;
	private int rollWidth, rollHeight, rollSum;
	private static int rollCount, rollTotal;
	private static int rollAverage;
	private Random rnd;
	
	// Default constructor
	public DiceManager() {
		rnd = new Random();
		firstDie = new Die();
		secondDie = new Die();
		rollCount = 0;
	}
	
	// Non-default constructor
	public DiceManager(int rollWidth, int rollHeight, ArrayList<String> rollRecord) {
		this();
		setRollWidth(rollWidth);
		setRollHeight(rollHeight);
		this.rollRecord = rollRecord;
	}
	
	// String format
	public String toString() {
		return String.format("Roll %d: Die 1: %s	Die 2: %s" +
							 "	Roll Total = %d, Roll Average = %d",
							 getRollCount(), firstDie.toString(), secondDie.toString(),
							 getRollTotal(), getRollAverage());
	}
	
	// Get functions
	public Die getFirstDie() {
		return firstDie;
	}
	public Die getSecondDie() {
		return secondDie;
	}
	public Die[] getDice() {
		dice = new Die[2];
		dice[0] = firstDie;
		dice[1] = secondDie;
		return dice;
	}
	public int getRollWidth() {
		return rollWidth;
	}
	public int getRollHeight() {
		return rollHeight;
	}
	public static int getRollCount() {
		return rollCount;
	}
	public int getRollSum() {
		rollSum = firstDie.getValue() + secondDie.getValue();
		return rollSum;
	}
	public static int getRollTotal() {
		return rollTotal;
	}
	public static int getRollAverage() {
		rollAverage = (int)Math.round((double)getRollTotal() / getRollCount());
		return rollAverage;
	}
	
	// Set functions
	public void setRollWidth(int rollWidth) {
		if (rollWidth < 100) {
			this.rollWidth = 100;
		}
		else if (rollWidth > DiceFrame.getFrameSize() - DiceDrawer.getDieSize()) {
			this.rollWidth = DiceFrame.getFrameSize() - DiceDrawer.getDieSize();
		}
		else {
			this.rollWidth = rollWidth;
		}
	}
	public void setRollHeight(int rollHeight) {
		if (rollHeight < 100) {
			this.rollHeight = 100;
		}
		else if (rollHeight > DiceFrame.getFrameSize() - DiceDrawer.getDieSize()) {
			this.rollHeight = DiceFrame.getFrameSize() - DiceDrawer.getDieSize();
		}
		else {
			this.rollHeight = rollHeight;
		}
	}
	public void setRollCount(int rc) {
		if (rc < 0) {
			rollCount = 0;
		}
		else {
			rollCount = rc;
		}
	}
	public void setRollTotal(int rt) {
		if (rt < 0) {
			rollTotal = 0;
		}
		else {
			rollTotal = rt;
		}
	}
	public void setRollAverage(int rollAvg) {
		if (rollAvg < 0) {
			rollAverage = 0;
		}
		else {
			rollAverage = rollAvg;
		}
	}
	
	// Roll single die
	public void rollDie(Die die) {
		die.setX(rnd.nextInt(rollWidth));
		die.setY(rnd.nextInt(rollHeight));
		die.setValue(1 + rnd.nextInt(6));
	}
	
	// Roll both dice
	public void rollDice() {
		rollDie(firstDie);
		rollDie(secondDie);
		setRollCount(getRollCount() + 1);
		
		if (checkBounds() == false) {
			rollDice();
		}

		setRollTotal(getRollTotal() + getRollSum());
		
		// Print dice properties to prompt
		System.out.println(this);
		
		// Record roll to rollRecord
		rollRecord.add(this.toString());
	}
	
	// Maintaining and enforcing boundaries
	public boolean checkBounds() {
		// Left side of firstDie
		int firstDieLeft = firstDie.getX();
		// Right side of firstDie
		int firstDieRight = firstDie.getX() + DiceDrawer.getDieSize();
		// Top side of firstDie
		int firstDieTop = firstDie.getY();
		// Bottom side of firstDie
		int firstDieBottom = firstDie.getY() + DiceDrawer.getDieSize();
		
		// Left side of secondDie
		int secondDieLeft = secondDie.getX();
		// Right side of secondDie
		int secondDieRight = secondDie.getX() + DiceDrawer.getDieSize();
		// Top side of secondDie
		int secondDieTop = secondDie.getY();
		// Bottom side of secondDie
		int secondDieBottom = secondDie.getY() + DiceDrawer.getDieSize();
		
		// If dice overlap, return false
		if ((firstDieRight >= secondDieLeft) && (firstDieLeft <= secondDieRight) &&
			(firstDieBottom >= secondDieTop) && (firstDieTop <= secondDieBottom)) {
			return false;
		}
		
		// If dice overlaps top right info, return false
		if ((secondDieRight > 3 * DiceFrame.getFrameSize() / 4) &&
			(secondDieTop < 50)) {
			return false;
		}
		if ((firstDieRight > 3 * DiceFrame.getFrameSize() / 4) &&
			(firstDieTop < 50)) {
			return false;
		}
		
		// Return true if none of the above
		else {
			return true;
		}
	}
	
	// Change colors to respective colors
	public void changeToRed() {
		firstDie.setDieColor(Color.RED);
		secondDie.setDieColor(Color.RED);
	}
	public void changeToGreen() {
		firstDie.setDieColor(Color.GREEN);
		secondDie.setDieColor(Color.GREEN);
	}
	public void changeToBlue() {
		firstDie.setDieColor(Color.BLUE);
		secondDie.setDieColor(Color.BLUE);
	}
	public void changeToWhite() {
		firstDie.setDieColor(Color.WHITE);
		secondDie.setDieColor(Color.WHITE);
	}
	public void changeToBlack() {
		firstDie.setDieColor(Color.BLACK);
		secondDie.setDieColor(Color.BLACK);
	}
	public void changeToPink() {
		firstDie.setDieColor(Color.PINK);
		secondDie.setDieColor(Color.PINK);
	}
	public void changeToViolet() {
		firstDie.setDieColor(new Color(255, 0, 255));
		secondDie.setDieColor(new Color(255, 0, 255));
	}
}

// View class
class DiceFrame extends JFrame implements ActionListener, KeyListener {
	private ArrayList<String> rollRecord;
	private DiceManager dman;
	private DiceDrawer drawer;
	private JPanel panDice, panRoll;
	private Font font;
	private Timer autorollTimer;
	private DiceIO dio;
	private JMenuBar menuBar = new JMenuBar();
	private static int frameSize = 500;
	private static int mediumSize = 75;
	
	// Default constructor
	public DiceFrame() {
		setTheme();
		setupMenu();
		rollRecord = new ArrayList<String>();
		dman = new DiceManager(frameSize - mediumSize,
							   frameSize - mediumSize,
							   rollRecord);
		dio = new DiceIO();
		font = new Font("Segoe UI", Font.PLAIN, 12);
		drawer = new DiceDrawer(font, mediumSize);
		
		// Specify JFrame's size and location
		setBounds(100, 100, frameSize + 50, frameSize + 100);
		setTitle("Kevin's Rolling Die");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Set frame to BorderLayout
		Container d = getContentPane();
		d.setLayout(new BorderLayout());
		
		// Panel for dice
		panDice = new JPanel();
		d.add(panDice, BorderLayout.CENTER);
		
		// Panel for roll button
		panRoll = new JPanel();
		JButton roll = new JButton("Roll");
		roll.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dman.rollDice();
					repaint();
					requestFocusInWindow();
				}
			}
		);
		panRoll.add(roll);
		d.add(panRoll, BorderLayout.SOUTH);
		
		// Create Timer object
		autorollTimer = new Timer(3000, this);
		
		// Make DiceFrame the KeyListener
		addKeyListener(this);
		setFocusable(true);
		dman.rollDice();
	}
	
	// Get functions
	public static int getFrameSize() {
		return frameSize;
	}
	public static int getMediumSize() {
		return mediumSize;
	}
	
	// Set functions
	public void setFrameSize(int fs) {
		if (fs < 0) {
			frameSize = 0;
		}
		else {
			frameSize = fs;
		}
	}
	
	public void setTheme() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Menu bar
	public void setupMenu() {
		fileMenu();
		colorMenu();
		sizeMenu();
		autorollMenu();
		setJMenuBar(menuBar);
	}
	public void fileMenu() {
		JMenu file = new JMenu("File");
		// Roll dice
		JMenuItem roll = new JMenuItem("Roll (SPACE)");
		file.add(roll);
		roll.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Tell DiceManager to roll dice and force repaint
					dman.rollDice();
					rollRecord.add(dman.toString());
					repaint();
				}
			}
		);
		// Save the rolls to file
		JMenuItem saveRolls = new JMenuItem("Save Rolls");
		file.add(saveRolls);
		saveRolls.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser jfc = new JFileChooser();
					if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						if (dio.saveDiceToFile(rollRecord, jfc.getSelectedFile()) == true) {
							JOptionPane.showMessageDialog(null, "Dice successfully saved.");
						}
						else {
							JOptionPane.showMessageDialog(null, "Dice could not be saved.");
						}
					}
				}
			}
		);
		// Reset roll history
		JMenuItem reset = new JMenuItem("Reset");
		file.add(reset);
		reset.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dman.setRollTotal(0);
					dman.setRollAverage(0);
					dman.setRollCount(0);
					System.out.println();
					
					// Clears rollRecord ArrayList
					rollRecord.clear();
					
					repaint();
				}
			}
		);
		// Exit program
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		exit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);
		menuBar.add(file);
	}
	public void colorMenu() {
		JMenu color = new JMenu("Color");
		JMenuItem red = new JMenuItem("Red (R)");
		color.add(red);
		red.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dman.changeToRed();
					repaint();
				}
			}
		);
		
		JMenuItem green = new JMenuItem("Green (G)");
		color.add(green);
		green.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dman.changeToGreen();
					repaint();
				}
			}
		);
		
		JMenuItem blue = new JMenuItem("Blue (B)");
		color.add(blue);
		blue.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dman.changeToBlue();
					repaint();
				}
			}
		);
		
		JMenuItem white = new JMenuItem("White (W)");
		color.add(white);
		white.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dman.changeToWhite();
					repaint();
				}
			}
		);
		
		JMenuItem black = new JMenuItem("Black (L)");
		color.add(black);
		black.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dman.changeToBlack();
					repaint();
				}
			}
		);
		
		JMenuItem pink = new JMenuItem("Pink (P)");
		color.add(pink);
		pink.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dman.changeToPink();
					repaint();
				}
			}
		);
		
		JMenuItem violet = new JMenuItem("Violet (V)");
		color.add(violet);
		violet.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dman.changeToViolet();
					repaint();
				}
			}
		);
		menuBar.add(color);
	}
	public void sizeMenu() {
		// Set size to small
		JMenu size = new JMenu("Size");
		JMenuItem small = new JMenuItem("Small (S)");
		size.add(small);
		small.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DiceDrawer.setDieSize(2 * getMediumSize() / 3);
					repaint();
				}
			}
		);
		// Set size to medium
		JMenuItem medium = new JMenuItem("Medium (M)");
		size.add(medium);
		medium.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DiceDrawer.setDieSize(getMediumSize());
					repaint();
				}
			}
		);
		// Set size to large
		JMenuItem large = new JMenuItem("Large (L)");
		size.add(large);
		large.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DiceDrawer.setDieSize(4 * getMediumSize() / 3);
					repaint();
				}
			}
		);
		menuBar.add(size);
	}
	public void autorollMenu() {
		JMenu autoroll = new JMenu("Autoroll");
		// Start autoroll
		JMenuItem start = new JMenuItem("Start");
		autoroll.add(start);
		start.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					autorollTimer.start();
				}
			}
		);
		// End autoroll
		JMenuItem stop = new JMenuItem("Stop");
		autoroll.add(stop);
		stop.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					autorollTimer.stop();
				}
			}
		);
		menuBar.add(autoroll);
	}
	
	// Key functions
	public void keyPressed(KeyEvent e) {
		// When user presses SPACE, roll the die
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			dman.rollDice();
		}
		// When user presses R, change color to red
		else if (e.getKeyCode() == KeyEvent.VK_R) {
			dman.changeToRed();
		}
		// When user presses G, change color to green
		else if (e.getKeyCode() == KeyEvent.VK_G) {
			dman.changeToGreen();
		}
		// When user presses B, change color to blue
		else if (e.getKeyCode() == KeyEvent.VK_B) {
			dman.changeToBlue();
		}
		// When user presses W, change color to white
		else if (e.getKeyCode() == KeyEvent.VK_W) {
			dman.changeToWhite();
		}
		// When user presses L, change color to black
		else if (e.getKeyCode() == KeyEvent.VK_L) {
			dman.changeToBlack();
		}
		// When user presses P, change color to pink
		else if (e.getKeyCode() == KeyEvent.VK_P) {
			dman.changeToPink();
		}
		// When user presses V, change color to purple
		else if (e.getKeyCode() == KeyEvent.VK_V) {
			dman.changeToViolet();
		}
		// When user presses S, change size to small
		else if (e.getKeyCode() == KeyEvent.VK_S) {
			DiceDrawer.setDieSize(2 * getMediumSize() / 3);
		}
		// When user presses M, change size to medium
		else if (e.getKeyCode() == KeyEvent.VK_M) {
			DiceDrawer.setDieSize(getMediumSize());
		}
		// When user presses L, change size to large
		else if (e.getKeyCode() == KeyEvent.VK_L) {
			DiceDrawer.setDieSize(4 * getMediumSize() / 3);
		}
		repaint();
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {
		
	}
	
	// Roll dice automatically once timer starts
	public void actionPerformed(ActionEvent e) {
		dman.rollDice();
		repaint();
	}
	
	// Override paint function
	public void paint(Graphics g) {
		super.paint(g);
		drawer.drawDice(dman.getDice(), panDice);
	}
}

// Controller class for drawing
class DiceDrawer {
	private Font font;
	private static int dieSize;
	
	// Non-default constructor
	public DiceDrawer(Font f, int ds) {
		font = f;
		dieSize = ds;
	}
	
	// Get and set dieSize
	public static int getDieSize() {
		return dieSize;
	}
	public static void setDieSize(int ds) {
		if (dieSize < 0) {
			dieSize = 0;
		}
		else {
			dieSize = ds;
		}
	}
	
	// Draw the dice to the panel
	public void drawDice(Die[] dice, JPanel panel) {
		Graphics g = panel.getGraphics();
		g.setFont(font);
		
		int dotSize = getDieSize() / 6;
		int dieValue, leftX, centerX, rightX, topY, centerY, bottomY;
		
		for (Die d : dice) {
			g.setColor(Color.BLACK);
			g.drawString("Total roll value: " + DiceManager.getRollTotal(),
						 3 * DiceFrame.getFrameSize() / 4, 20);
			g.drawString("Average roll: " + DiceManager.getRollAverage(),
						 3 * DiceFrame.getFrameSize() / 4, 40);
			
			// Set color to current die color
			g.setColor(d.getDieColor());
			g.fillRect(d.getX(), d.getY(), getDieSize(), getDieSize());
			
			// Draw the dots
			leftX = d.getX() + (getDieSize() / 6) - (dotSize / 2);
			centerX = d.getX() + (getDieSize() / 2) - (dotSize / 2);
			rightX = d.getX() + (5 * getDieSize() / 6) - (dotSize / 2);
			
			topY = d.getY() + (getDieSize() / 6) - (dotSize / 2);
			centerY = d.getY() + (getDieSize() / 2) - (dotSize / 2);
			bottomY = d.getY() + (5 * getDieSize() / 6) - (dotSize / 2);
			
			g.setColor(d.getDotColor());
			dieValue = d.getValue();
			switch(dieValue) {
			case 1:
				// Center dot
				g.fillOval(centerX, centerY, dotSize, dotSize);
				continue;
			case 2:
				// Top left dot
				g.fillOval(leftX, topY, dotSize, dotSize);
				// Bottom right dot
				g.fillOval(rightX, bottomY, dotSize, dotSize);
				continue;
			case 3:
				// Top left dot
				g.fillOval(leftX, topY, dotSize, dotSize);
				// Center dot
				g.fillOval(centerX, centerY, dotSize, dotSize);
				// Bottom right dot
				g.fillOval(rightX, bottomY, dotSize, dotSize);
				continue;
			case 4:
				// Top left dot
				g.fillOval(leftX, topY, dotSize, dotSize);
				// Top right dot
				g.fillOval(rightX, topY, dotSize, dotSize);
				// Bottom left dot
				g.fillOval(leftX, bottomY, dotSize, dotSize);
				// Bottom right dot
				g.fillOval(rightX, bottomY, dotSize, dotSize);
				continue;
			case 5:
				// Top left dot
				g.fillOval(leftX, topY, dotSize, dotSize);
				// Top right dot
				g.fillOval(rightX, topY, dotSize, dotSize);
				// Center dot
				g.fillOval(centerX, centerY, dotSize, dotSize);
				// Bottom left dot
				g.fillOval(leftX, bottomY, dotSize, dotSize);
				// Bottom right dot
				g.fillOval(rightX, bottomY, dotSize, dotSize);
				continue;
			case 6:
				// Top left dot
				g.fillOval(leftX, topY, dotSize, dotSize);
				// Top right dot
				g.fillOval(rightX, topY, dotSize, dotSize);
				// Center left dot
				g.fillOval(leftX, centerY, dotSize, dotSize);
				// Center right dot
				g.fillOval(rightX, centerY, dotSize, dotSize);
				// Bottom left dot
				g.fillOval(leftX, bottomY, dotSize, dotSize);
				// Bottom right dot
				g.fillOval(rightX, bottomY, dotSize, dotSize);
				continue;
			}
		}
	}
}

// Write recorded rolls to a save file
class DiceIO {
	public boolean saveDiceToFile(ArrayList<String> rollRecord, File f) {
		try {
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			
			// For each r in rollRecord, write it to a save file
			for (String r : rollRecord) {
				pw.println(r);
			}
			pw.close();
			return true;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}

// Main class
public class DieRoll_Nguyen {
	public static void main(String[] args) {
		DiceFrame df = new DiceFrame();
		df.setVisible(true);
	}
}