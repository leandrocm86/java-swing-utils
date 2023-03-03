package lcm.java.swing;

import java.awt.Window;
import java.awt.Dimension;
import java.awt.Toolkit;


/**
 * Helper class for dealing with screen/monitor properties.
 */
public class Screen {


    /**
     * Positions the given window component at the center of the screen.
	 * @param window - Window to be moved to the center of the screen.
     */
    public static void centralizeWindow(Window window) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(dim.width/2 - window.getSize().width/2, dim.height/2 - window.getSize().height/2);
	}
	
    /**
	 * Gets the total of pixels for the screen width.
	 * @return screen width in pixels.
	 */
	public static int getScreenWidth() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}
	
    /**
	 * Gets the total of pixels for the screen height.
	 * @return screen height in pixels.
	 */
	public static int getScreenHeight() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	/**
	 * Gets the number of pixels needed for the given percentage of screen width.
	 * @param percentage - Proportion of the screen to have the horizontal pixels counted.
	 * @return screen portion width in pixels, rounded for the given percentage.
	 */
	public static int getRelativeWidth(float percentage) {
		return Math.round(percentage * getScreenWidth() / 100);
	}
	
	/**
	 * Gets the number of pixels needed for the given percentage of screen height.
	 * @param percentage - Proportion of the screen to have the vertical pixels counted.
	 * @return screen portion height in pixels, rounded for the given percentage.
	 */
	public static int getRelativeHeight(float percentage) {
		return Math.round(percentage * getScreenHeight() / 100);
	}
    
}
