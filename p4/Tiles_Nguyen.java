


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// Model
class Tile {
	private int row, col, shape;
	private String letter, shapeString;
	private Color shapeColor, letterColor;
	
	// Get functions
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	public int getShape() {
		return shape;
	}
	public String getLetter() {
		return letter;
	}
	public String shapeString() {
		return shapeString;
	}
	public Color getShapeColor() {
		return shapeColor;
	}
	public Color getLetterColor() {
		return letterColor;
	}
	
	// Set functions
	public void setRow(int row) {
		this.row = row;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public void setShape(int shape) {
		this.shape = shape;
		if (shape == 0) {
			shapeString = "Circle";
		}
		else {
			shapeString = "Square";
		}
	}
	public void setLetter(String letter) {
		// If string length is greater than one, only pick 0th index
		if (letter.length() != 1) {
			this.letter = Character.toString(letter.charAt(0));
		}
		else {
			this.letter = letter;
		}
	}
	public void setShapeColor(Color shapeColor) {
		this.shapeColor = shapeColor;
	}
	public void setLetterColor(Color letterColor) {
		this.letterColor = letterColor;
	}
	
	// Default constructor
	public Tile() {
		row = 0;
		col = 0;
		shape = 0;
		letter = " ";
		shapeString = null;
		shapeColor = Color.WHITE;
		letterColor = Color.WHITE;
	}
	
	// Non-default constructor
	public Tile(int row, int col, int shape,
				String letter, Color shapeColor, Color letterColor) {
		setRow(row);
		setCol(col);
		setShape(shape);
		setLetter(letter);
		setShapeColor(shapeColor);
		setLetterColor(letterColor);
	}
	
	// String format
	public String toString() {
		return String.format("Row: %d	Col: %d	 Shape: %s	Letter: %s",
							 row + 1, col + 1, shapeString, letter);
	}
}

// View
class TileFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TileRandomizer tr = new TileRandomizer();
	private TilePrinter tp = new TilePrinter();
	private TileDrawer td;
	private JPanel panTile;
	
	private static int frameSize = 600;
	
	public static int getFrameSize() {
		return frameSize;
	}
	
	public TileFrame(ArrayList<Tile> tiles) {
		setTheme();
		// Set the size and position of JFrame
		setBounds(100, 100, frameSize + 15, frameSize + 75);
		
		// Title of JFrame
		setTitle("Kevin's Shuffling Tiles");
		
		// Close operation
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Set frame to be border layout
		Container t = getContentPane();
		t.setLayout(new BorderLayout());
		
		// Create center panel for the tiles to be in
		panTile = new JPanel();
		t.add(panTile, BorderLayout.CENTER);
		
		// Create south panel for random button
		JPanel panButton = new JPanel();
		JButton randomButton = new JButton("Randomize!");
		randomButton.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tr.changeTiles(tiles);
					System.out.println();
					tp.printTiles(tiles);
					repaint();
				}
			}
		);
		panButton.add(randomButton);
		t.add(panButton, BorderLayout.SOUTH);
		
		td = new TileDrawer(tiles, panTile);
	}
	
	// Set JFrame theme
	public void setTheme() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		td.drawTiles();
	}
}

// Controller
class TileDrawer {
	private JPanel panTile;
	private ArrayList<Tile> tiles;
	private Font font;
	
	// Controller's constructor
	public TileDrawer(ArrayList<Tile> tiles, JPanel panTile) {
		this.tiles = tiles;
		this.panTile = panTile;
	}
	
	// Drawing the tiles on JFrame
	public void drawTiles() {
		Graphics g = panTile.getGraphics();
		
		// Font
		font = new Font("Arial", Font.BOLD, 30);
		
		int randomShape;
		
		// Tile properties and positions
		int tileX, tileY;
		int tileSize = (int)(TileFrame.getFrameSize() / 10);
		
		for (Tile t : tiles) {
			tileX = t.getCol() * tileSize;
			tileY = t.getRow() * tileSize;
			
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			
			randomShape = t.getShape();
			
			// Change to shape's color
			g.setColor(t.getShapeColor());
			
			// If randomShape is 0, draws oval
			if (randomShape == 0) {
				g.fillOval(tileX, tileY, tileSize, tileSize);
			}
			// Otherwise, it's a square
			else {
				g.fillRect(tileX, tileY, tileSize, tileSize);
			}
			
			// Change to letter's color
			g.setColor(t.getLetterColor());
			g.drawString(t.getLetter(), tileX + (tileSize / 2) - (fm.stringWidth(t.getLetter()) / 2),
						 tileY + (tileSize / 2) + (fm.getAscent() - fm.getDescent()) / 2);
		}
	}
}

// Manages building Tiles and changing Tiles
class TileRandomizer {
	private Random rnd = new Random();
	// Builds tiles
	public void buildTile(int row, int col, ArrayList<Tile> tiles) {
		Tile t;
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				// Create new Tile object
				t = new Tile(r, c, 0, " ", Color.WHITE, Color.WHITE);
				changeTile(t);
				tiles.add(t);
			}
		}
	}
	
	// Changes individual tile
	public void changeTile(Tile t) {
		// Random shape RGB
		int sr = rnd.nextInt(256);
		int sg = rnd.nextInt(256);
		int sb = rnd.nextInt(256);
		
		// Random letter RGB
		int lr = (sr + 128) % 256;
		int lg = (sg + 128) % 256;
		int lb = (sb + 128) % 256;
		
		// Random letter from A to Z
		int rndLetter = (int)'A' + rnd.nextInt((int)'Z' + 1 - (int)'A');
		
		// Determines whether it's circle or square
		int rndShape = rnd.nextInt(2);
		
		// Apply the new parameters to the tile
		t.setShape(rndShape);
		t.setLetter(Character.toString((char)rndLetter));
		t.setShapeColor(new Color(sr, sg, sb));
		t.setLetterColor(new Color (lr, lg, lb));
	}
	
	// Changes all tiles
	public void changeTiles(ArrayList<Tile> tiles) {
		for (Tile t : tiles) {
			changeTile(t);
		}
	}
}

// Print Tile properties 
class TilePrinter {
	public void printTiles(ArrayList<Tile> tiles) {
		for (Tile t : tiles) {
			System.out.println(t);
		}
	}
}

// Main method
public class Tiles_Nguyen {
	public static void main(String[] args) {
		TileRandomizer tr = new TileRandomizer();
		TilePrinter tp = new TilePrinter();

		ArrayList<Tile> tiles = new ArrayList<Tile>();
		
		tr.buildTile(10, 10, tiles);
		tp.printTiles(tiles);
		
		TileFrame tf = new TileFrame(tiles);
		tf.setVisible(true);
	}
}