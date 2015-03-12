package GUI;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import Controller.InputFrameController;
/*
 * Created by JFormDesigner on Wed Jan 21 10:19:45 CET 2015
 */
import Controller.QueryFrameController;



/**
 * @author Brainrain
 */
public class InputFrame extends JPanel {
	
	private InputFrameController ctrl;
	public String ontology;
	public String firstmapping;
	public String annotation;
	
	public InputFrame(final UIPluginContext context) {
		ctrl = new InputFrameController();
		initComponents();
		
		createButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				ontology = textField1.getText();
				firstmapping = textField2.getText();
				annotation = textField3.getText();
				
				try {
					ctrl.createSecondMapping(ontology, firstmapping, annotation);
					//InputFrame.this.dispose();
					//qFrame.setVisible(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				textArea1.setVisible(true);
			}
			
		});
		
		
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
		buttonBar = new JPanel();
		scrollPane1 = new JScrollPane();
		textArea1 = new JTextArea();
		createButton = new JButton();

		//======== this ========
		setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{

				//---- label1 ----
				label1.setText("Ontology:");

				//---- label2 ----
				label2.setText("First Mapping:");

				//---- label3 ----
				label3.setText("Annotation:");

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(
					contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup()
							.addGap(26, 26, 26)
							.addGroup(contentPanelLayout.createParallelGroup()
								.addComponent(label1)
								.addComponent(label2)
								.addComponent(label3))
							.addGap(52, 52, 52)
							.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(textField3)
								.addComponent(textField2)
								.addComponent(textField1, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
							.addContainerGap(89, Short.MAX_VALUE))
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
							.addContainerGap(34, Short.MAX_VALUE))
				);
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

				//======== scrollPane1 ========
				{

					//---- textArea1 ----
					textArea1.setVisible(false);
					textArea1.setText("Second mapping is successfully created. Click finish to go to the next step.");
					scrollPane1.setViewportView(textArea1);
				}
				buttonBar.add(scrollPane1, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

				//---- createButton ----
				createButton.setText("Create Second Mapping");
				buttonBar.add(createButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 5), 0, 0));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		add(dialogPane, BorderLayout.CENTER);
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
	private JPanel buttonBar;
	private JScrollPane scrollPane1;
	private JTextArea textArea1;
	private JButton createButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
