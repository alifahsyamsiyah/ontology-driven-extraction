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
		// Generated using JFormDesigner Evaluation license - lili lala
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		scrollPane1 = new JScrollPane();
		textArea1 = new JTextArea();
		processButton = new JButton();
		label3 = new JLabel();
		scrollPane2 = new JScrollPane();
		textArea2 = new JTextArea();

		//======== this ========

		// JFormDesigner evaluation mark
		setBorder(new javax.swing.border.CompoundBorder(
			new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
				"JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
				java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});

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
								.addComponent(scrollPane1, GroupLayout.Alignment.LEADING)
								.addGroup(contentPanelLayout.createSequentialGroup()
									.addComponent(processButton)
									.addGap(138, 138, 138))
								.addGroup(GroupLayout.Alignment.LEADING, contentPanelLayout.createSequentialGroup()
									.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(label1, GroupLayout.Alignment.LEADING)
										.addComponent(label3, GroupLayout.Alignment.LEADING))
									.addGap(264, 264, 264))
								.addComponent(scrollPane2, GroupLayout.Alignment.LEADING))
							.addContainerGap(45, Short.MAX_VALUE))
				);
				contentPanelLayout.setVerticalGroup(
					contentPanelLayout.createParallelGroup()
						.addGroup(contentPanelLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(label1)
							.addGap(11, 11, 11)
							.addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(processButton)
							.addGap(20, 20, 20)
							.addComponent(label3)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(95, Short.MAX_VALUE))
				);
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);
		}
		add(dialogPane, BorderLayout.CENTER);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - lili lala
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JScrollPane scrollPane1;
	private JTextArea textArea1;
	private JButton processButton;
	private JLabel label3;
	private JScrollPane scrollPane2;
	private JTextArea textArea2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
