package lcm.java.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import lcm.java.swing.CustomFont.Style;

/**
 * Class for creating text widgets (invisible frames that only display a text in system tray).
 * These widgets are able to be updated to display different texts and fonts.
 */
public class TextWidget {
	
	private SystemTrayFrame frame;
	private Color textColor = Color.WHITE;
	private CustomFont font = new CustomFont("Arial", Style.BOLD, 15);
	private CharSequence text;
	private CharSequence title;
	
	/**
	 * Widget's constructor.
	 * @param title - Tooltip to be used on the widget on mouse hover.
	 */
	public TextWidget(String title) {
		frame = new SystemTrayFrame(title);
		this.title = title;
	}
	
	/**
	 * Sets the text to be displayed on the system tray.
	 * @param text - Widget's text to show.
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Sets the widget's text color do be displayed.
	 * @param color - Color ({@link java.awt.Color}) to be shown on the widget's text.
	 */
	public void setTextColor(Color color) {
		this.textColor = color;
	}
	
	/**
	 * Sets the widget's text font do be displayed.
	 * @param font - Font to be used on the widget's text.
	 */
	public void setFont(CustomFont font) {
		this.font = font;
	}

	/**
	 * Changes the widget's title (tooltip on mouse hover).
	 * @param title - New title to be used (it's obligatory to define one on constructor).
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Updates the widget's rendering.
	 */
	public void refresh() {
		BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = bi.createGraphics();
	    g.setColor(this.textColor);
	    g.setFont(this.font);
	    g.drawString(this.text.toString(), 0, 15);
	    g.dispose();
	    frame.updateIcon(bi, title.toString());
	}
}

