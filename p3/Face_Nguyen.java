import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

// Model

class Face {
	// Face parameters
	private int x, y, width, height, faceStatus;
	private String faceStatusString;
	
	// Eye parameters
	private int eyeWidth, eyeHeight, eyeLX, eyeRX, eyeY;
	
	// Mouth parameters
	private int mouthX, mouthY, mouthW, mouthH;
	
	// Face get functions
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int getFaceStatus() {
		return faceStatus;
	}
	public String getFaceStatusString() {
		return faceStatusString;
	}
	
	// Eye get functions
	public int getEyeWidth() {
		return eyeWidth;
	}
	public int getEyeHeight() {
		return eyeHeight;
	}
	public int getEyeY() {
		return eyeY;
	}
	public int getLEyeX() {
		return eyeLX;
	}
	public int getREyeX() {
		return eyeRX;
	}
	
	// Mouth get functions
	public int getMouthX() {
		return mouthX;
	}
	public int getMouthY() {
		return mouthY;
	}
	public int getMouthWidth() {
		return mouthW;
	}
	public int getMouthHeight() {
		return mouthH;
	}
	
	// Face set functions
	public void setX(int x) {
		if (x < 0) {
			this.x = 0;
		}
		else {
			this.x = x;
		}
	}
	public void setY(int y) {
		if (y < 0) {
			this.y = 0;
		}
		else {
			this.y = y;
		}
	}
	public void setWidth(int w) {
		if (w < 0) {
			width = 0;
		}
		else {
			width = w;
		}
	}
	public void setHeight(int h) {
		if (h < 0) {
			height = 0;
		}
		else {
			height = h;
		}
	}
	public void setFaceStatus(int m) {
		// Causes arc of the mouth to be a smile
		if (m == 0) {
			faceStatus = 180;
			faceStatusString = "Sad";
		}
		else if (m == 1) {
			faceStatus = 180;
			faceStatusString = "Neutral";
		}
		else {
			faceStatus = -180;
			faceStatusString = "Happy";
		}
	}
	
	// Eye set functions
	public void setLEyeX(int x, int w) {
		eyeLX = x + (int)(w / 3 - w / 10 / 2);
	}
	public void setREyeX(int x, int w) {
		eyeRX = x + (int)(2 * w / 3 - w / 10 / 2);
	}
	public void setEyeY(int y, int h) {
		eyeY = y + (int)(h / 3);
	}
	public void setEyeWidth(int w) {
		eyeWidth = (int)(w / 10);
	}
	public void setEyeHeight(int h) {
		eyeHeight = (int)(h / 10);
	}
	
	// Mouth set functions
	public void setMouthX(int x, int w) {
		mouthX = x + (int)(w / 4);
	}
	public void setMouthY(int y, int h) {
		mouthY = y + (int)(2 * h / 3);
	}
	public void setMouthWidth(int w) {
		mouthW = (int)(getWidth() / 2);
	}
	public void setMouthHeight(int h, int m) {
		if (m == 1) {
			mouthH = 0;
		}
		else {
			mouthH = (int)(h / 5);
		}
	}
	

	// Default constructor
	public Face() {
		x = 0;
		y = 0;
		width = 0;
		height = 0;
		faceStatus = 0;
		faceStatusString = null;
	}
	
	
	// Non-default constructor
	public Face(int x, int y, int w, int h, int m) {
		// Face
		setX(x);
		setY(y);
		setWidth(w);
		setHeight(h);
		setFaceStatus(m);
		
		// Eyes
		setEyeY(y, h);
		setLEyeX(x, w);
		setREyeX(x, w);
		setEyeWidth(w);
		setEyeHeight(h);
		
		// Mouth
		setMouthX(x, w);
		setMouthY(y, h);
		setMouthWidth(w);
		setMouthHeight(h, m);
	}
	
	// Prompt format
	// List all the faces in prompt
	public String toString() {
		return String.format("%s Face:\tx = %d\ty = %d\tw = %d\th = %d",
							 getFaceStatusString(), getX(), getY(), getWidth(), getHeight());
	}
}

// View
class FaceFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DrawFacePanelController dfpc;
	
	// Creates JFrame for drawing faces on it
	public FaceFrame(ArrayList<Face> faces) {
		// Set the size and position of JFrame
		setBounds(100, 100, 500, 500);
		
		// Title of JFrame
		setTitle("Kevin's Miscellaneous Faces");
		
		// Close operation
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Set frame to be a border layout
		Container f = getContentPane();
		f.setLayout(new BorderLayout());
		
		// Create center JPanel
		JPanel panFace = new JPanel();
		f.add(panFace, BorderLayout.CENTER);
		dfpc = new DrawFacePanelController(faces, panFace);
	}
	
	// Allows the faces to be painted on JFrame
	public void paint(Graphics g) {
		super.paint(g);
		dfpc.drawFaces();
	}
}

// Controller
class DrawFacePanelController {
	private JPanel panFace;
	private ArrayList<Face> faces;
	
	public DrawFacePanelController(ArrayList<Face> faces, JPanel pan) {
		this.faces = faces;
		panFace = pan;
	}
	
	// Provides information on face properties and position to JFrame graphics
	public void drawFaces() {
		Graphics g = panFace.getGraphics();
		
		// Draw the following circles to create a face
		for (Face f : faces) {
			// Face outline
			g.drawOval(f.getX(), f.getY(), f.getWidth(), f.getHeight());
			
			// Left eye
			g.drawOval(f.getLEyeX(), f.getEyeY(), f.getEyeWidth(), f.getEyeHeight());
			
			// Right eye
			g.drawOval(f.getREyeX(), f.getEyeY(), f.getEyeWidth(), f.getEyeHeight());
			
			// Mouth
			g.drawArc(f.getMouthX(), f.getMouthY(), f.getMouthWidth(), f.getMouthHeight(), 0, f.getFaceStatus());
		}
	}
}

public class Face_Nguyen {
	// Print method
	public static void printFaces(ArrayList<Face> faces) {
		for (Face f : faces) {
			System.out.println(f);
		}
	}
	
	// Main method
	public static void main(String[] args) {
		Random rnd = new Random();
		int sizeScale = 1;
		int xRandom = 500 * sizeScale + 1;
		int yRandom = 500 * sizeScale + 1;
		int wLRange = 50 * sizeScale;
		int wHRange = 100 * sizeScale + 1;
		int hLRange = 50 * sizeScale;
		int hHRange = 100 * sizeScale + 1;
		
		// Generate three to ten faces
		int faceCount = 3 + rnd.nextInt(10 + 1 - 3);
		
		Face f;
		ArrayList<Face> faces = new ArrayList<Face>();
		
		for (int i = 0; i < faceCount; i++) {
			f = new Face(rnd.nextInt(xRandom), rnd.nextInt(yRandom),
						 wLRange + rnd.nextInt(wHRange - wLRange), hLRange + rnd.nextInt(hHRange - hLRange),
						 rnd.nextInt(3));
			faces.add(f);
		}
		
		printFaces(faces);
		
		// Create new FaceFrame object
		FaceFrame ff = new FaceFrame(faces);
		ff.setVisible(true);
	}
}