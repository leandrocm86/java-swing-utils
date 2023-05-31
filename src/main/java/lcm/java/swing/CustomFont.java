package lcm.java.swing;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JCheckBox;


/**
 * Class to handle fonts in Swing. It extends java.awt.Font (so it can be used transparently).
 * It has many public pre-defined options for fonts, and facilitates creating new ones.
 * It has methods to change fonts on Swing components, while dealing with other possible internal components inside them.
 */
public class CustomFont extends java.awt.Font {

	/**
	 * Enum representing font styles.
	 */
	public enum Style {
		/**
		 * Normal font style.
		 */
		PLAIN(0),

		/**
		 * Bold font style.
		 */
		BOLD(1),

		/**
		 * Italic font style.
		 */
		ITALIC(2);

		private int code;

		private Style(int code) {
			this.code = code;
		}

		private int getCode() {
			return code;
		}
	}

	/**
	 * Plain Arial 20. Default font for 1040p or lesser resolutions.
	 */
	public static final CustomFont ARIAL_20 = new CustomFont("Arial", Style.PLAIN, 20);

	/**
	 * Plain Arial 40. Default font for resolutions greater than 1040p.
	 */
	public static final CustomFont ARIAL_40 = new CustomFont("Arial", Style.PLAIN, 40);

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor receiving all possible parameters for a CustomFont.
	 * 
	 * @param name - Name of the Font (according to java.awt.Font).
	 * @param style - The desired style for the font.
	 * @param size - The desired size for the font.
	 */
	public CustomFont(String name, Style style, int size) {
		super(name, style.getCode(), size);
	}
	
	/**
	 * Overload of the complete constructor, considering PLAIN as default style.
	 * @param name - Name of the Font (according to java.awt.Font).
	 * @param size - The desired size for the font.
	 * @see #CustomFont(String, Style, int)
	 */
	public CustomFont(String name, int size) {
		super(name, Style.PLAIN.getCode(), size);
	}
	
	/**
     * Applies a font to all the given components.
     * If any of the components is a container, the font will also be applied to its inner components,
     * except inner components that already have a font applied (this can be changed setting the first parameter force = true).
     * 
     * @param force - Wether to apply the font on inner components of containers even if they already had a font applied.
     * @param components - Components to have the font applied on them.
	 */
	public void apply(boolean force, Component... components) {
		for (Component component : components) {
			component.setFont(this);
			if (component instanceof JCheckBox)
				SwingComponents.scaleCheckBoxIcon((JCheckBox) component);
		    if (component instanceof Container) {
		        for (Component child : SwingComponents.getAllChildren((Container) component)) {
		        	if (force || (child.getFont() == null || !(child.getFont() instanceof CustomFont)))
		        		this.apply(force, child);
		        }
		    }
		}
	}
	
	/**
	 * Overload of {@link #apply(boolean, Component...)} having force = false by default.
     * @param components - Components to have the font applied on them.
	 * @see #apply(boolean, Component...)
	 */
	public void apply(Component... components) {
		this.apply(false, components);
	}
	
}
