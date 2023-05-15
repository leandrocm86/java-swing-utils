package lcm.java.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

import lcm.java.swing.RelativeLayout.Axis;

/**
 *  Helper class to create layouts in Swing. It abstracts and makes easier the use of loyout classes such as GridLayout, GridBagLayout and RelativeLayout.
 *  The options of layout given are always one dimensional. We can either dispose elements in a row (horizontally) or in a column (vertically).
 *  Also, the inserted components will always be stretched to fill the chosen dimension, but the stretching on the other dimension is optional.
 * 	To stretch the elements in both dimensions (filling their whole space), it's necessary to call the "full" methods (fullHorizontalPane and fullVerticalPane).
 *  If no arguments are given beside the components, the space available for each component will be the same (GridLayout or GridBagLayout will be used).
 *  To use RelativeLayout, it's necessary to give each component's relative proportions after the list of components, in the same order as the list. 
 */
public class Layouts {

    /**
	 * Creates a JPanel with horizontal layout containing the given Components, disposed with the given relative proportions.
	 * The vertical space of this panel is not necessarily filled by the components (contrary to {@link #fullHorizontalPane(List, float...)}).
	 * The list's order defines the elements from left to right, with null components replaced by empty spaces.
	 *
	 * @param proportions An array of floats representing the proportions of each component's width 
	 *                     in the layout. Must have the same length as the number of components.
	 * @param components  An array of JComponents to be added to the panel in order.
	 * @return            A JPanel containing the given components horizontally disposed with the given proportions.
	 */
	public static JPanel horizontalPane(List<Component> components, float... proportions) {
		return createRelativeLayoutPanel(new RelativeLayout(Axis.HORIZONTAL, false), components, proportions);
	}

	/**
	 * Creates a JPanel with horizontal layout containing the given Components, disposed with the given relative proportions.
	 * All the vertical space of this panel will be filled by the components (contrary to {@link #horizontalPane(List, float...)}).
	 * The list's order defines the elements from left to right, with null components replaced by empty spaces.
	 *
	 * @param proportions An array of floats representing the proportions of each component's width 
	 *                     in the layout. Must have the same length as the number of components.
	 * @param components  An array of JComponents to be added to the panel in order.
	 * @return            A JPanel containing the given components horizontally disposed with the given proportions.
	 */
	public static JPanel fullHorizontalPane(List<Component> components, float... proportions) {
		return createRelativeLayoutPanel(new RelativeLayout(Axis.HORIZONTAL, true), components, proportions);
	}

	/**
	 * Creates a JPanel with vertical layout containing the given Components, disposed with the given relative proportions.
	 * The horizontal space of this panel is not necessarily filled by the components (contrary to {@link #fullVerticalPane(List, float...)}).
	 * The list's order defines the elements from top to bottom, with null components replaced by empty spaces.
	 *
	 * @param proportions An array of floats representing the proportions of each component's height 
	 *                     in the layout. Must have the same length as the number of components.
	 * @param components  An array of JComponents to be added to the panel in order.
	 * @return            A JPanel containing the given components vertically disposed with the given proportions.
	 */
	public static JPanel verticalPane(List<Component> components, float... proportions) {
		return createRelativeLayoutPanel(new RelativeLayout(Axis.VERTICAL, false), components, proportions);
	}

	/**
	 * Creates a JPanel with vertical layout containing the given Components, disposed with the given relative proportions.
	 * All the horizontal space of this panel will be filled by the components (contrary to {@link #verticalPane(List, float...)}).
	 * The list's order defines the elements from top to bottom, with null components replaced by empty spaces.
	 *
	 * @param proportions An array of floats representing the proportions of each component's height 
	 *                     in the layout. Must have the same length as the number of components.
	 * @param components  An array of JComponents to be added to the panel in order.
	 * @return            A JPanel containing the given components vertically disposed with the given proportions.
	 */
	public static JPanel fullVerticalPane(List<Component> components, float... proportions) {
		return createRelativeLayoutPanel(new RelativeLayout(Axis.VERTICAL, true), components, proportions);
	}

	private static JPanel createRelativeLayoutPanel(RelativeLayout layout, List<Component> components, float... proportions) {
		if (components.size() != proportions.length)
			throw new IllegalArgumentException("Different number of components and proportions for RelativeLayout!");

		JPanel panel = new JPanel(layout);
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			if (component == null)
				component = layout.getAxis() == Axis.HORIZONTAL ? Box.createHorizontalGlue() : Box.createVerticalGlue();
			panel.add(component, proportions[i]);
		}

		return panel;
	}

	/**
	 * Returns a JPanel with the given list of components laid out horizontally using a GridBagLayout with no fill.
	 * Each component is given the same space. If the list contains a null component, it is replaced by an empty space.
	 * The vertical space of this panel is not necessarily filled by the components (contrary to {@link #fullHorizontalPane(List)}).
	 * 
	 * @param components a List of Component objects to be added to the panel
	 * @return a JPanel with the components laid out horizontally
	 */
	public static JPanel horizontalPane(List<Component> components) {
		return createGridBagPaneWithNoFillPanel(components, true);
	}

	/**
	 * Returns a JPanel with the given list of components laid out vertically using a GridBagLayout with no fill.
	 * Each component is given the same space. If the list contains a null component, it is replaced by an empty space.
	 * The horizontal space of this panel is not necessarily filled by the components (contrary to {@link #fullVerticalPane(List)}).
	 * 
	 * @param components a List of Component objects to be added to the panel
	 * @return a JPanel with the components laid out vertically
	 */
	public static JPanel verticalPane(List<Component> components) {
		return createGridBagPaneWithNoFillPanel(components, false);
	}

	private static JPanel createGridBagPaneWithNoFillPanel(List<Component> components, boolean horizontal) {
		JPanel panel = new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = horizontal ? GridBagConstraints.HORIZONTAL : GridBagConstraints.VERTICAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
        gbc.weighty = 1.0;


		for (int i = 0; i < components.size(); i++) {
			gbc.gridx = horizontal ? i : 0;
			gbc.gridy = horizontal ? 0 : i;
            Component component = components.get(i) != null ? components.get(i) : (horizontal ? Box.createHorizontalGlue() : Box.createVerticalGlue());
			panel.add(component, gbc);
		}

		return panel;
	}

	/**
	 * Creates a JPanel with a GridLayout that arranges the given components horizontally.
	 * Each component is given the same space. If the list contains a null component, it is replaced by an empty space.
	 * All the vertical space of this panel will be filled by the components (contrary to {@link #horizontalPane(List)}).
	 *
	 * @param components A list of components to be added to the JPanel.
	 * @return A JPanel object with a GridLayout, containing the given components.
	 */
	public static JPanel fullHorizontalPane(List<Component> components) {
		return createGridLayoutPanel(components, true);
	}

	/**
	 * Creates a JPanel with a GridLayout that arranges the given components vertically.
	 * Each component is given the same space. If the list contains a null component, it is replaced by an empty space.
	 * All the horizontal space of this panel will be filled by the components (contrary to {@link #verticalPane(List)}).
	 *
	 * @param components A list of components to be added to the JPanel.
	 * @return A JPanel object with a GridLayout, containing the given components.
	 */
	public static JPanel fullVerticalPane(List<Component> components) {
		return createGridLayoutPanel(components, false);
	}

	private static JPanel createGridLayoutPanel(List<Component> components, boolean horizontal) {
		JPanel panel = new JPanel(new GridLayout(horizontal ? 1 : components.size(), horizontal ? components.size() : 1));
		for (Component component : components) {
            Component componentToAdd = component != null ? component : horizontal ? Box.createHorizontalGlue() : Box.createVerticalGlue();
			panel.add(componentToAdd);
        }
		return panel;
	}

}