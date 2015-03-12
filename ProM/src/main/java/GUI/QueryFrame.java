package GUI;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import Controller.QueryFrameController;



/**
 * @author Brainrain
 */
public class QueryFrame extends JPanel {
	private QueryFrameController ctrl;
	
	public QueryFrame(UIPluginContext context, String ontology, String mapping) throws OWLOntologyCreationException, IOException, InvalidPredicateDeclarationException, InvalidMappingException {
		ctrl = new QueryFrameController(ontology, mapping);
		initComponents();
		System.out.println("yes");
		
		processButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				String sparql = textArea1.getText();
				String result = "";
				try {
					result = ctrl.process(sparql);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				textArea2.setText(result);
				
			}
		});
		
		
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		scrollPane1 = new JScrollPane();
		textArea1 = new JTextArea();
		processButton = new JButton();
		label3 = new JLabel();
		scrollPane2 = new JScrollPane();
		textArea2 = new JTextArea();
		buttonBar = new JPanel();
		processButton2 = new JButton();

		//======== this ========
		setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{

				//---- label1 ----
				label1.setText("Your SPARQL Query:");

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(textArea1);
				}

				//---- processButton ----
				processButton.setText("Process");

				//---- label3 ----
				label3.setText("Result:");

				//======== scrollPane2 ========
				{
					scrollPane2.setViewportView(textArea2);
				}

				GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
				contentPanel.setLayout(contentPanelLayout);
				contentPanelLayout.setHorizontalGroup(
					contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup()
							.addGap(26, 26, 26)
							.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
								.addComponent(scrollPane2, GroupLayout.Alignment.LEADING)
								.addComponent(scrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
								.addComponent(label1, GroupLayout.Alignment.LEADING)
								.addGroup(GroupLayout.Alignment.LEADING, contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
									.addComponent(processButton)
									.addGroup(contentPanelLayout.createSequentialGroup()
										.addComponent(label3)
										.addGap(347, 347, 347))))
							.addContainerGap(48, Short.MAX_VALUE))
				);
				contentPanelLayout.setVerticalGroup(
					contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(label1)
							.addGap(11, 11, 11)
							.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(processButton)
							.addGap(18, 18, 18)
							.addComponent(label3)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
							.addContainerGap())
				);
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new GridBagLayout());
				((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 0, 85, 0, 80};
				((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 0.0, 0.0, 0.0};

				//---- processButton2 ----
				processButton2.setText("Create Event Log");
				buttonBar.add(processButton2, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 5), 0, 0));
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
	private JScrollPane scrollPane1;
	private JTextArea textArea1;
	private JButton processButton;
	private JLabel label3;
	private JScrollPane scrollPane2;
	private JTextArea textArea2;
	private JPanel buttonBar;
	private JButton processButton2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
