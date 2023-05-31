package lcm.java.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Images {

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
     * Creates a tooltip (question mark) label with the given hint text.
     *
     * @param tipText the text to display in the tooltip
     * @param size the size of the tooltip
     * @return a JLabel with an attached mouse listener that displays the tooltip when clicked
     */
    public static JLabel createTooltipLabel(String tipText, int size) {
        ImageIcon questionIcon = (ImageIcon) UIManager.getIcon("OptionPane.questionIcon");
        questionIcon = new ImageIcon(questionIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
        JLabel questionLabel = new JLabel(questionIcon);
        questionLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        questionLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, tipText);
            }
        });
        return questionLabel;
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

}
