package GUI;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
/*
 * Created by JFormDesigner on Thu May 21 08:19:25 CEST 2015
 */

import org.processmining.contexts.uitopia.UIPluginContext;



/**
 * @author Brainrain
 */
public class BootstrapFrame extends JPanel {
	
	String ontology;
	String mapping;
	String baseURI;
	String jdbcURL;
	String username;
	String password;
	String jdbcDriver;
	
	public String[] getConfiguration() {
		ontology = textField1.getText();
		mapping = textField2.getText();
		baseURI = textField3.getText();
		jdbcURL = textField4.getText();
		username = textField5.getText();
		password = textField7.getText();
		jdbcDriver = textField8.getText();
		
		String[] conf = {ontology, mapping, baseURI, jdbcURL, username, password, jdbcDriver};
		
		return conf;
	}
	
	
	public BootstrapFrame(final UIPluginContext context) {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		textField1 = new JTextField();
		textField2 = new JTextField();
		label2 = new JLabel();
		label3 = new JLabel();
		textField3 = new JTextField();
		label4 = new JLabel();
		label5 = new JLabel();
		textField4 = new JTextField();
		textField5 = new JTextField();
		textField7 = new JTextField();
		label7 = new JLabel();
		label8 = new JLabel();
		textField8 = new JTextField();
		textField6 = new JTextField();
		label6 = new JLabel();

		//======== this ========
		setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setMinimumSize(new Dimension(317, 299));
				contentPanel.setMaximumSize(new Dimension(32767, 42767));

				//---- label1 ----
				label1.setText("Ontology File");

				//---- label2 ----
				label2.setText("Mapping File");

				//---- label3 ----
				label3.setText("Base URI");

				//---- label4 ----
				label4.setText("Jcbc URL");

				//---- label5 ----
				label5.setText("Username");

				//---- label7 ----
				label7.setText("Password");

				//---- label8 ----
				label8.setText("Jdbc Driver Class");

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(
					contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup()
							.addGap(26, 26, 26)
							.addGroup(contentPanelLayout.createParallelGroup()
								.addComponent(label1)
								.addComponent(label2)
								.addComponent(label3)
								.addComponent(label5)
								.addComponent(label4)
								.addComponent(label7)
								.addComponent(label8))
							.addGap(52, 52, 52)
							.addGroup(contentPanelLayout.createParallelGroup()
								.addComponent(textField8, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField7, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField5, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
								.addComponent(textField4, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
								.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
									.addComponent(textField3)
									.addComponent(textField2)
									.addComponent(textField1, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)))
							.addContainerGap(134, Short.MAX_VALUE))
				);
				contentPanelLayout.setVerticalGroup(
					contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label1))
							.addGap(18, 18, 18)
							.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label2))
							.addGap(18, 18, 18)
							.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label3)
								.addComponent(textField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(20, 20, 20)
							.addGroup(contentPanelLayout.createParallelGroup()
								.addComponent(textField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label4))
							.addGap(18, 18, 18)
							.addGroup(contentPanelLayout.createParallelGroup()
								.addComponent(label5)
								.addComponent(textField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18, 18, 18)
							.addGroup(contentPanelLayout.createParallelGroup()
								.addComponent(textField7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label7))
							.addGap(18, 18, 18)
							.addGroup(contentPanelLayout.createParallelGroup()
								.addComponent(textField8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label8))
							.addGap(35, 35, 35))
				);
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);
		}
		add(dialogPane, BorderLayout.CENTER);

		//---- label6 ----
		label6.setText("Base URI");
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JTextField textField1;
	private JTextField textField2;
	private JLabel label2;
	private JLabel label3;
	private JTextField textField3;
	private JLabel label4;
	private JLabel label5;
	private JTextField textField4;
	private JTextField textField5;
	private JTextField textField7;
	private JLabel label7;
	private JLabel label8;
	private JTextField textField8;
	private JTextField textField6;
	private JLabel label6;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
