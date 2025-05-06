import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DrawingCanvas {
	public static final int DEFAULT_REPAINT_DELAY = 100;
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;

	private Color bgColor = Color.white;
	private final BufferedImage onscreenImage, offscreenImage;
	private final Graphics2D onscreenGraphics, offscreenGraphics;

	private final JFrame frame;
	private final DrawingCanvasPanel panel;
	private Timer timer;
	private int repaintDelay = DEFAULT_REPAINT_DELAY;

	private boolean autoRedraw;
	
	public DrawingCanvas() {
		this(DEFAULT_WIDTH,DEFAULT_HEIGHT);
	}
	
	public DrawingCanvas(int w, int h) {
		// Create the JFrame
		frame = new JFrame("Drawing Canvas");
		
		// Create the images (double buffered)
		onscreenImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		offscreenImage = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		onscreenGraphics = onscreenImage.createGraphics();
		offscreenGraphics = offscreenImage.createGraphics();
		offscreenGraphics.setColor( Color.BLACK );

		// Enable antialiasing
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		offscreenGraphics.addRenderingHints(hints);
		
		// Create the panel to hold the image, and add to the frame
		panel = new DrawingCanvasPanel();
		panel.setPreferredSize( new Dimension(w,h) );
		frame.add(panel);

		// Setup key binding for save image
		int shortcut = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
		panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_S, shortcut), "save");
		panel.getActionMap().put("save", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser saveDialog = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"PNG Image", "png");
				saveDialog.setFileFilter(filter);
				saveDialog.setDialogTitle("Save image");
				int result = saveDialog.showSaveDialog(panel);
				if( result == JFileChooser.APPROVE_OPTION ) {
					try {
						save( saveDialog.getSelectedFile().getPath() );
					} catch( IOException ex ) {
						JOptionPane.showMessageDialog(panel, "Error saving file: " +
								saveDialog.getSelectedFile().getPath() + "\n" +
								ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		});

		// Set up the JFrame
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Set up the timer that will constantly redraw the screen
		autoRedraw = false;
		setAutoRedraw(true);
	}

	/**
	 * Enable/disable auto-redraw.  When enabled, the image is re-drawn
	 * automatically at regular intervals.  When disabled, you must call
	 * draw() to see changes.
	 *
	 * @param enable true to enable auto-redraw, false to disable
	 */
	public void setAutoRedraw(boolean enable) {
		if( autoRedraw == enable ) return;

		if(autoRedraw) {
			timer.stop();
		} else {
			timer = new Timer(repaintDelay, (e) -> this.draw());
			timer.start();
		}
		autoRedraw = enable;
	}

	/**
	 * Re-draw the image.  If auto-redraw is enabled, it is not necessary
	 * to call this method.  When auto-redraw is disabled, this must be called
	 * to make any changes visible.
	 */
	public void draw() {
		onscreenGraphics.drawImage(offscreenImage, 0, 0, bgColor, null);
		panel.repaint();
	}
	
	/**
	 * Sets the title for this window to the specified string.
	 * @param title the title to be displayed in the window's border.
	 */
	public void setTitle( String title ) {
		frame.setTitle( title );
	}
	
	/**
	 * Sets how often the window is redrawn when auto-redraw is enabled.
	 * 
	 * @param delay the delay in milliseconds
	 */
	public void setRedrawDelay(int delay ) {
		this.repaintDelay = delay;
		timer.setDelay(this.repaintDelay);
	}
	
	/**
	 * Returns the width of the inside of this DrawingCanvas.
	 * @return the width of the inside of this DrawingCanvas.
	 */
	public int getWidth() {
		return panel.getWidth();
	}
	
	/**
	 * Returns the height of the inside of this DrawingCanvas.
	 * @return the height of the inside of this DrawingCanvas.
	 */
	public int getHeight() { 
		return panel.getHeight();
	}
	
	private class DrawingCanvasPanel extends JComponent {
		@Override
		public void paintComponent( Graphics g ) {
			super.paintComponent(g);
			g.drawImage(onscreenImage, 0,0, this);
		}
	}
	
	/**
	 * Sets the background color for this window.
	 * @param c the background color.
	 */
	public void setBackground( Color c ) {
		if(c != null) bgColor = c;
	}
	
	/**
	 * Returns a Graphics2D object suitable for drawing onto the 
	 * window.
	 * 
	 * @return a Graphics2D object suitable for drawing onto this window.
	 */
	public Graphics2D getGraphics() { 
		return offscreenGraphics;
	}
	
	/**
	 * Sets the pixel at the given location to the given color.
	 * 
	 * @param x the x coordinate of the pixel (between 0 and the 
	 * width of the window minus one).
	 * @param y the y coordinate of the pixel (between 0 and the 
	 * height of the window minus one).
	 * @param c the color of the pixel.
	 */
	public void setPixel(int x, int y, Color c) {
		offscreenImage.setRGB(x, y, c.getRGB());
	}

	/**
	 * Sets the pixel at the given location to the current color of
	 * the Graphics2D object.
	 *
	 * @param x the x coordinate of the pixel (between 0 and the
	 * width of the window minus one).
	 * @param y the y coordinate of the pixel (between 0 and the
	 * height of the window minus one).
	 */
	public void setPixel( int x, int y ) {
		Color c = offscreenGraphics.getColor();
		setPixel(x, y, c);
	}
	
	/**
	 * Gets the color for the pixel at the given location.
	 * @param x the x coordinate of the pixel.
	 * @param y the y coordinate of the pixel.
	 * @return the color of the pixel.
	 */
	public Color getPixel( int x, int y ) {
		return new Color(offscreenImage.getRGB(x, y));
	}

	/**
	 * Writes the image to a file in PNG format.
	 *
	 * @param path the path to the destination file.  The file extension should be .png.
	 *             If the file exists, it will be overwritten.
	 */
	public void save( String path ) throws IOException {
		ImageIO.write(onscreenImage, "PNG", new File(path));
	}
}
