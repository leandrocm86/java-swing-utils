package lcm.java.swing;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Class for making a Toast with Swing.
 * A toast is a temporary fading message to be displayed.
 * It may also be called as a "snackbar".
 */
public class Toast extends JFrame {

	private static final long serialVersionUID = 1L;
	
    /** Maximum opacity */
	private final float MAX_OPACITY = 0.8f;

    /** Opacity level increments */
    private final float OPACITY_INCREMENT = 0.05f; 

    /** Refresh rate for the fading effect */
    private final int FADE_REFRESH_RATE = 20;

    /** Refresh rate for the fading effect. */
    private final int WINDOW_RADIUS = 15;

    /** Multiplier to affect the frame total size. */
    private final int CHARACTER_LENGTH_MULTIPLIER = 9;

    /** Defines the height for the Toast to be displayed. */
    private final int DISTANCE_FROM_PARENT_BOTTOM = 100;


    /**
     * Constructor for Toast.
     * 
     * @param owner - Parent's JFrame.
     * @param toastText - Text to be displayed.
     */
    public Toast(JFrame owner, String toastText) {
        setTitle("Transparent JFrame Demo");
        setLayout(new GridBagLayout());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setFocusableWindowState(false);

        setOpacity(0.4f);

        // setup the toast lable
        JLabel b1 = new JLabel(toastText);
        b1.setForeground(Color.WHITE);
        b1.setOpaque(false);
        add(b1);

        setSize(toastText.length() * CHARACTER_LENGTH_MULTIPLIER, 50);

        int x = (int) (owner.getLocation().getX() + (owner.getWidth() / 2));
        int y = (int) (owner.getLocation().getY() + owner.getHeight() - DISTANCE_FROM_PARENT_BOTTOM);
        setLocation(new Point(x, y));

        // configure frame
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), WINDOW_RADIUS, WINDOW_RADIUS));
        getContentPane().setBackground(new Color(0, 0, 0, 170));
    }

    /**
     * Method for manually calling the Toast to be visible.
     */
    public void fadeIn() {
    	setOpacity(0);
        setVisible(true);

        final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += OPACITY_INCREMENT;
                setOpacity(Math.min(opacity, MAX_OPACITY));
                if (opacity >= MAX_OPACITY) {
                    timer.stop();
                }
            }
        });

        timer.start();
    }
    /**
     * Method for manually calling the Toast to get invisible.
     */
    public void fadeOut() {
        final Timer timer = new Timer(FADE_REFRESH_RATE, null);
        timer.setRepeats(true);
        timer.addActionListener(new ActionListener() {
            private float opacity = MAX_OPACITY;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= OPACITY_INCREMENT;
                setOpacity(Math.max(opacity, 0));
                if (opacity <= 0) {
                    timer.stop();
                    setVisible(false);
                    dispose();
                }
            }
        });

        setOpacity(MAX_OPACITY);
        timer.start();
    }

    /**
     * Makes a toast and automatically displays it.
     * @param owner - Parent's JFrame.
     * @param toastText - Text to be displayed.
     * @param durationSec - Visibility duration.
     */
    public static void makeToast(final JFrame owner, final String toastText, final int durationSec) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast toastFrame = new Toast(owner, toastText);
                    toastFrame.fadeIn();
                    Thread.sleep(durationSec * 1000);
                    toastFrame.fadeOut();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }
    
    @Override
    public void setOpacity(float opacity) {
    	try {
    		super.setOpacity(opacity);
    	}
    	catch (UnsupportedOperationException e) {
        	// This didn't seem to work in X11/KDE.
        }
    }
}
