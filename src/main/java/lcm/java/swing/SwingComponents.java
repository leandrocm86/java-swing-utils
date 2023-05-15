package lcm.java.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Helper class for building and manipulating various complex Swing components.
 */
public class SwingComponents {
	
    /**
     * The default font to be used on the swing components.
     * This font may not be automatically applied on all components, so it is public and available to be used manually if necessary.
     * It takes into consideration the current display resolution to decide its size.
     * Notice it is not final, so it can be changed if needed.
     */
	public static CustomFont DEFAULT_FONT = Screen.getScreenHeight() > 1440 ? CustomFont.ARIAL_40 : CustomFont.ARIAL_20;
	
	static {
		javax.swing.UIManager.put("OptionPane.messageFont", DEFAULT_FONT);
		javax.swing.UIManager.put("OptionPane.buttonFont", DEFAULT_FONT);
		
//		UIManager.put("Tree.closedIcon", new ImageIcon(SwingUtils.class.getResource("mais.jpg")));
//		UIManager.put("Tree.openIcon", new ImageIcon(SwingUtils.class.getResource("menos.png")));
//		UIManager.put("Tree.leafIcon", new ImageIcon(SwingUtils.class.getResource("dot.png")));
	}
	
    /**
	 * Sets the background color for the given panel and all its inner panels.
     * 
     * @param panel - The Jpanel to have its background changed.
     * @param color - The color to display.
	 */
	public static void setBackgroundColor(JPanel panel, Color color) {
		panel.setBackground(color);
		for (Component component : panel.getComponents()) {
			if (component instanceof JPanel)
				setBackgroundColor((JPanel) component, color);
			else
				component.setBackground(color);
		}
	}
	
	/**
     * Creates a JScrollPane that wraps the given content component, and configures its
     * vertical and horizontal scrollbars with the given size. The scrollbars are set to
     * appear only when needed.
     *
     * @param content The component to wrap in the scroll pane.
     * @param scrollSize The size of the scrollbars, in pixels.
     * @return The newly created JScrollPane.
     */
	public static JScrollPane createScrollPane(Component content, int scrollSize) {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(content);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(scrollSize, 0));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, scrollSize));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return scrollPane;
	}
	
    /**
	 * Creates a button with a given image.
     * 
     * @param imagePath - The path for the image's file.
     * @return JButton with an icon.
	 */
	public static JButton createImageButton(String imagePath) {
		JButton button = new JButton();
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setIcon(new StretchIcon(imagePath, false));
		return button;
	}
	
    /**
	 * Scales a checkbox size to be appropriate to its font size.
     * @param checkbox - JCheckBox element to have its size scaled.
	 */
	public static void scaleCheckBoxIcon(JCheckBox checkbox){
	    boolean previousState = checkbox.isSelected();
	    checkbox.setSelected(false);
	    FontMetrics boxFontMetrics =  checkbox.getFontMetrics(checkbox.getFont());
	    Icon boxIcon = UIManager.getIcon("CheckBox.icon");
	    BufferedImage boxImage = new BufferedImage(
	        boxIcon.getIconWidth(), boxIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB
	    );
	    Graphics graphics = boxImage.createGraphics();
	    try{
	        boxIcon.paintIcon(checkbox, graphics, 0, 0);
	    }finally{
	        graphics.dispose();
	    }
	    ImageIcon newBoxImage = new ImageIcon(boxImage);
	    Image finalBoxImage = newBoxImage.getImage().getScaledInstance(
	        boxFontMetrics.getHeight(), boxFontMetrics.getHeight(), Image.SCALE_SMOOTH
	    );
	    checkbox.setIcon(new ImageIcon(finalBoxImage));

	    checkbox.setSelected(true);
	    Icon checkedBoxIcon = UIManager.getIcon("CheckBox.icon");
	    BufferedImage checkedBoxImage = new BufferedImage(
	        checkedBoxIcon.getIconWidth(),  checkedBoxIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB
	    );
	    Graphics checkedGraphics = checkedBoxImage.createGraphics();
	    try{
	        checkedBoxIcon.paintIcon(checkbox, checkedGraphics, 0, 0);
	    }finally{
	        checkedGraphics.dispose();
	    }
	    ImageIcon newCheckedBoxImage = new ImageIcon(checkedBoxImage);
	    Image finalCheckedBoxImage = newCheckedBoxImage.getImage().getScaledInstance(
	        boxFontMetrics.getHeight(), boxFontMetrics.getHeight(), Image.SCALE_SMOOTH
	    );
	    checkbox.setSelectedIcon(new ImageIcon(finalCheckedBoxImage));
	    checkbox.setSelected(false);
	    checkbox.setSelected(previousState);
	}
	
    /**
	 * Updates the rendering of a component. If it's a container, all inner components will also be updated.
     * @param component - Component to be updated. 
	 */
	public static void refresh(Component component) {
    	if (component instanceof Container) {
	    	for (Component c : ((Container)component).getComponents()) {
	    		refresh(c);
	    	}
    	}
    	component.revalidate();
    	component.repaint();
    }

    /**
     * An <CODE>Icon</CODE> that scales its image to fill the component area,
     * excluding any border or insets, optionally maintaining the image's aspect
     * ratio by padding and centering the scaled image horizontally or vertically.
     * <P>
     * The class is a drop-in replacement for <CODE>ImageIcon</CODE>, except that
     * the no-argument constructor is not supported.
     * <P>
     * As the size of the Icon is determined by the size of the component in which
     * it is displayed, <CODE>StretchIcon</CODE> must only be used in conjunction
     * with a component and layout that does not depend on the size of the
     * component's Icon.
     * 
     * @version 1.0 03/27/12
     * @author Darryl
     * Updated: 28/02/23 by leandrocm86
     */
    private static class StretchIcon extends ImageIcon {
        private static final long serialVersionUID = 1L;
        
        /**
         * Determines whether the aspect ratio of the image is maintained.
         * Set to <code>false</code> to allow th image to distort to fill the component.
         */
        protected boolean proportionate = true;


        /**
         * Creates a <CODE>StretchIcon</CODE> from the specified file with the specified behavior.
         * 
         * @param filename a String specifying a filename or path
         * @param proportionate <code>true</code> to retain the image's aspect ratio,
         *        <code>false</code> to allow distortion of the image to fill the
         *        component.
         *
         * @see ImageIcon#ImageIcon(java.lang.String)
         */
        public StretchIcon(String filename, boolean proportionate) {
            super(filename);
            this.proportionate = proportionate;
        }

        /**
         * Paints the icon.  The image is reduced or magnified to fit the component to which
         * it is painted.
         * <P>
         * If the proportion has not been specified, or has been specified as <code>true</code>,
         * the aspect ratio of the image will be preserved by padding and centering the image
         * horizontally or vertically.  Otherwise the image may be distorted to fill the
         * component it is painted to.
         * <P>
         * If this icon has no image observer,this method uses the <code>c</code> component
         * as the observer.
         *
         * @param c the component to which the Icon is painted.  This is used as the
         *          observer if this icon has no image observer
         * @param g the graphics context
         * @param x not used.
         * @param y not used.
         *
         * @see ImageIcon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
         */
        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Image image = getImage();
            if (image == null) {
            return;
            }
            Insets insets = ((Container) c).getInsets();
            x = insets.left;
            y = insets.top;

            int w = c.getWidth() - x - insets.right;
            int h = c.getHeight() - y - insets.bottom;

            if (proportionate) {
            int iw = image.getWidth(c);
            int ih = image.getHeight(c);

            if (iw * h < ih * w) {
                iw = (h * iw) / ih;
                x += (w - iw) / 2;
                w = iw;
            } else {
                ih = (w * ih) / iw;
                y += (h - ih) / 2;
                h = ih;
            }
            }

            ImageObserver io = getImageObserver();
            g.drawImage(image, x, y, w, h, io == null ? c : io);
        }

        /**
         * Overridden to return 0.  The size of this Icon is determined by
         * the size of the component.
         * 
         * @return 0
         */
        @Override
        public int getIconWidth() {
            return 0;
        }

        /**
         * Overridden to return 0.  The size of this Icon is determined by
         * the size of the component.
         *
         * @return 0
         */
        @Override
        public int getIconHeight() {
            return 0;
        }
    }

    /**
     * Creates a tooltip (question mark) label with the given hint text.
     *
     * @param tipText the text to display in the tooltip
     * @return a JLabel with an attached mouse listener that displays the tooltip when clicked
     */
    public static JLabel createTooltipLabel(String tipText) {
        JLabel questionLabel = new JLabel(UIManager.getIcon("OptionPane.questionIcon"));
        questionLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        questionLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, tipText);
            }
        });
        return questionLabel;
    }

    /**
     * Attaches a key listener to the given input text field that triggers the given
     * action when the specified key code is pressed.
     *
     * @param inputText the text field to attach the key listener to
     * @param keyCode   the key code that triggers the action
     * @param action    the action to execute when the key is pressed
     */
    public static void addKeyPressedListener(JTextField inputText, int keyCode, Runnable action) {
		inputText.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == keyCode)
					action.run();
			}
		});
    }

    /**
     * Adds a listener to the specified JTextField that will run the given action 
     * when the enter key is pressed.
     * 
     * @param inputText The JTextField to add the listener to.
     * @param action The action to run when the enter key is pressed.
     */
    public static void addEnterPressedListener(JTextField inputText, Runnable action) {
        addKeyPressedListener(inputText, KeyEvent.VK_ENTER, action);
    }

    /**
     * Retrieves an image from a resource with the specified path (may be relative inside the JAR).
     *
     * @param  resource  the path to the image resource
     * @return           the image retrieved from the resource
     * @throws IllegalArgumentException if there is an error while trying to get the image from the resource
     */
    public static Image getImageFromResource(String resource) {
        try {
            return ImageIO.read(SwingComponents.class.getResourceAsStream(resource));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while trying to get image from resource " + resource, e);
        }
    }

    /**
     * Returns an Image object that can then be drawn to the screen. 
     * The image is specified by a filesystem's path and is loaded using the 
     * default toolkit. 
     *
     * @param  path  a string that represents the path to the image file
     * @return       the Image object representing the image at the specified path
     */
    public static Image getImageFromPath(String path) {
        return Toolkit.getDefaultToolkit().getImage(path);
    }

    /**
     * Restricts the input of a given JTextField to only allow characters that match the given regex pattern. 
     *
     * @param input The JTextField to restrict input on.
     * @param regex The regex pattern to match against the input.
     */
    public void restrictInput(JTextField input, String regex) {
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text.matches(regex)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    /**
     * Filters the children of the given parent container based on the provided filter
     * and returns a list of all components that pass the filter.
     *
     * @param parent the container whose children are to be filtered
     * @param filter the filter function to apply to each child component
     * @return a list of all children components that pass the filter
     */
    public static List<Component> filterChildren(Container parent, Function<Component, Boolean> filter) {
        ArrayList<Component> result = new ArrayList<>();
        for (Component child : parent.getComponents()) {
            if (filter.apply(child)) {
                result.add(child);
            }
            if (child instanceof Container) {
                result.addAll(filterChildren((Container) child, filter));
            }
        }
        return result;
    }
}
