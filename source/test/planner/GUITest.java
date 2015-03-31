package planner;
import com.windowtester.runtime.swing.locator.JTextComponentLocator;
import javax.swing.JTextField;
import com.windowtester.runtime.swing.SwingWidgetLocator;
import javax.swing.JPanel;
import com.windowtester.runtime.swing.UITestCaseSwing;
import com.windowtester.runtime.IUIContext;

public class GUITest extends UITestCaseSwing {

	/**
	 * Create an Instance
	 */
	public GUITest() {
		super(planner.UserInterface.class);
	}


	// Test if gui add feedback is correct
	public void testGUI() throws Exception {
		IUIContext ui = getUI();
		
		ui.click(new JTextComponentLocator(JTextField.class,
				new SwingWidgetLocator(JPanel.class)));
		
		ui.enterText("add good food at 7 8 2008\n");
		
		ui.assertThat(new JTextComponentLocator(JTextField.class,
                new SwingWidgetLocator(JPanel.class)).hasText("Task added successfully"));

	}

}