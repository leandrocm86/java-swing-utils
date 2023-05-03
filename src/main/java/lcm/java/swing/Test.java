/* package lcm.java.swing;

import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class Test {
    public static void main(String[] args) {
        var components = new ArrayList<Component>();
        components.add(new JTextField("Teste 1"));
        components.add(new JTextField("Teste 2"));
        components.add(new JTextField("Teste 3"));
        components.add(null);
        components.add(new JTextField("Teste 4"));

        var panel = Layouts.verticalPane(components, 0.5f, 1, 1.5f, 2, 2.5f);
        new CustomFont("Arial", 15).apply(panel);
        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.setSize(600, 600);
        Screen.centralizeWindow(frame);
        frame.setState(Frame.NORMAL);
        frame.setVisible(true);
        frame.repaint();
        frame.toFront();
    }
} */
