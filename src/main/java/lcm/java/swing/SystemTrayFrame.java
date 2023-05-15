package lcm.java.swing;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

/** 
 * A specialization of JFrame to be accessible from the system tray.
 */
public class SystemTrayFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private TrayIcon trayIcon;
    private SystemTray tray;
    private boolean restoreOption;
    
    /**
     * Overload for the complete constructor {@link #SystemTrayFrame(String, String, boolean)}, assuming restoreOption = FALSE.
     * @param name - Name of the frame.
     * @see #SystemTrayFrame(String, String, boolean)
     */
    public SystemTrayFrame(String name) {
    	this(name, null);
    }
    
    /**
     * Overload for the complete constructor {@link #SystemTrayFrame(String, String, boolean)}, assuming restoreOption = FALSE.
     * @param name - Name of the frame.
     * @param image - The image to be used as an icon.
     * @see #SystemTrayFrame(String, String, boolean)
     */
    public SystemTrayFrame(String name, Image image) {
    	this(name, image, false);
    }
    
    /** 
     * Constructor for the SystemTrayFrame.
     * 
     * @param name - Name of the frame.
     * @param image - The image to be used as an icon.
     * @param restoreOption - Wether the frame should have an option to be restored (also by double-clicking its icon).
     */
    public SystemTrayFrame(String name, Image image, boolean restoreOption) {
        super(name);
        this.restoreOption = restoreOption;
        
        if (SystemTray.isSupported()) {
            this.tray = SystemTray.getSystemTray();
            this.setTrayIcon(image, name);
        } else {
            throw new IllegalStateException("Swing error: SystemTray is not supported.");
        }
        
        super.addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
            	tray.remove(trayIcon);
            	if (e.getNewState() == MAXIMIZED_BOTH || e.getNewState() == NORMAL) {
                  setVisible(true);
            	}
            	else {
                    addIconToTray();
                    setVisible(false);
                    // if (e.getNewState() == ICONIFIED)
                    // 	Events.notify(new Evento(EVENT_WINDOW_MINIMIZED, this));
                }
            }
        });
        super.setIconImage(image); // Window icon
    }

    private void addIconToTray() {
        try {
            this.tray.add(this.trayIcon);
        } catch (AWTException ex) {
            throw new IllegalStateException("Swing error: it wasn't possible to add icon to the system tray.", ex);
        }
    }
    
    /**
     * Sets the icon to be displayed on the system tray.
     * 
     * @param image - Image to be rendered.
     * @param title - Title (tooltip) for the image.
     */
    public void setTrayIcon(Image image, String title) {
    	this.tray.remove(this.trayIcon);
    	
    	ActionListener exitListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exiting....
            }
        };
        PopupMenu popup = new PopupMenu();
        MenuItem defaultItem = new MenuItem("Exit");
        defaultItem.setFont(SwingComponents.DEFAULT_FONT);
        defaultItem.addActionListener(exitListener);
        popup.add(defaultItem);
        
        if (this.restoreOption) {
        	ActionListener restoreListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	restore();
                }
            };
            MenuItem retoreOption = new MenuItem("Restore");
            retoreOption.setFont(SwingComponents.DEFAULT_FONT);
            retoreOption.addActionListener(restoreListener);
            popup.add(retoreOption);
        }
        
        this.trayIcon = new TrayIcon(image, title, popup);
        this.trayIcon.setImageAutoSize(true);
        this.trayIcon.setToolTip(title);
        
        // Making a double-click listener for restoring the window.
        if (this.restoreOption) {
        	this.trayIcon.addMouseListener(new MouseListener() {
                final int INTERVALO = 200; // Maximum time interval (ms) to be considered between clicks.
	            long lastClick = 0;
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    if (System.currentTimeMillis() - lastClick < INTERVALO)
                        restore();
                    lastClick = System.currentTimeMillis();
                }
                @Override
                public void mouseEntered(MouseEvent arg0) {}
                @Override
                public void mouseExited(MouseEvent arg0) {}
                @Override
                public void mousePressed(MouseEvent arg0) {}
                @Override
                public void mouseReleased(MouseEvent arg0) {}
			});
        }
        
        addIconToTray();
    }
    

    /**
     * Adss a mouse listener to the icon on system tray.
     * @param listener - The mouse listener to be added on system tray.
     */
    public void addListener(MouseListener listener) {
    	this.trayIcon.addMouseListener(listener);
    }
    
    /**
     * Restores the frame window.
     */
    public void restore() {
    	setState(NORMAL);
    	setVisible(true);
    	toFront();
    	SwingComponents.refresh(this);
    }
    
    /**
     * Minimizes the frame window.
     */
    public void minimizeToTray() {
    	setState(ICONIFIED);
    	setVisible(false);
    }
    
    /**
     * Displays a desktop notification message.
     * 
     * @param title - Message's title.
     * @param msg - Messages's content.
     * @param type - Type of the message: {@link java.awt.TrayIcon.MessageType}
     */
    public void displayMessage(String title, String msg, MessageType type) {
    	this.trayIcon.displayMessage(title, msg, type);
    }
    
    /**
     * Overload of {@link #displayMessage(String, String, MessageType)} assuming INFO as default message type.
     * @param title - Message's title.
     * @param msg - Messages's content.
     */
    public void displayMessage(String title, String msg) {
    	displayMessage(title, msg, MessageType.INFO);
    }
    
    /**
     * Adds a listener for when a notification is clicked.
     * @param listener - An ActionListener to be executed when a message is clicked.
     */
    public void addMessageListener(ActionListener listener) {
    	this.trayIcon.addActionListener(listener);
    }
    
}
