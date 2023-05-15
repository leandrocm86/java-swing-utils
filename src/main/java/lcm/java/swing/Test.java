/*package lcm.java.swing;

import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Test {
    public static void main(String[] args) {
        ArrayList<Component> components = new ArrayList<Component>();
        components.add(new JTextField("Test 1"));
        components.add(new JTextField("Test 2"));
        components.add(new JTextField("Test 3"));
        components.add(null);
        components.add(new JTextField("Test 4"));

        JPanel panel = Layouts.verticalPane(components, 0.5f, 1, 1.5f, 2, 2.5f);
        new CustomFont("Arial", 15).apply(panel);
        JFrame frame = new JFrame();
        frame.setContentPane(panel);

        check(SwingComponents.filterChildren(frame, component -> component instanceof JTextField).size(), 4, "Wrong number of components");
        check(SwingComponents.filterChildren(panel, component -> !(component instanceof JTextField)).size(), 1, "Wrong number of components");

        frame.setSize(600, 600);
        Screen.centralizeWindow(frame);
        frame.setState(Frame.NORMAL);
        frame.setVisible(true);
        frame.repaint();
        frame.toFront();
    }

    private static void check(Object actual, Object expected, String errorMessage) {
        if (actual != expected) {
            throw new AssertionError(errorMessage + ": " + actual + " != " + expected);
        }
    }
}
*/