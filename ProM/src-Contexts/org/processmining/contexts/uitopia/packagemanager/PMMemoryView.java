package org.processmining.contexts.uitopia.packagemanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.deckfour.uitopia.ui.components.ImageLozengeButton;
import org.deckfour.uitopia.ui.util.ImageLoader;
import org.processmining.framework.util.OsUtil;

import com.fluxicon.slickerbox.components.RoundedPanel;

public class PMMemoryView extends RoundedPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2483996283422006055L;

	/*
	 * Get whether we are running in 64 bit mode, this allows allocating more
	 * than 1300 MB of memory.
	 */
	private static boolean is64bit = OsUtil.is64Bit();
	/*
	 * Get the available memory.
	 */
	private static long mem = OsUtil.getPhysicalMemory() / (1024 * 1024);

	/*
	 * Start with using 1 GB of memory.
	 */
	private static MemoryOption selectedMem = MemoryOption.XMX1G;
	private static MemoryOption oldSelectedMem = MemoryOption.XMX1G;

	private enum MemoryOption implements ActionListener {
		XMX1G("1G", true), // 1 GB
		XMX1300M("1300M", true), // 1300 MB, about the limit in 32 bit mode
		XMX2G("2G", is64bit && mem >= 2 * 1024), // 2 GB
		XMX3G("3G", is64bit && mem >= 3 * 1024), // 3 GB
		XMX4G("4G", is64bit && mem >= 4 * 1024), // 4 GB
		XMX6G("6G", is64bit && mem >= 6 * 1024), // 6 GB
		XMX8G("8G", is64bit && mem >= 8 * 1024), // 8 GB
		XMX12G("12G", is64bit && mem >= 12 * 1024), // 12 GB
		XMX16G("16G", is64bit && mem >= 16 * 1024); // 16 GB

		/*
		 * Text to be added to "-Xmx"
		 */
		private String size;
		/*
		 * Whether this memory option is available on this computer.
		 */
		private boolean isAvailable;
		/*
		 * Button to show when this option is selected.
		 */
		private ImageLozengeButton selected;
		/*
		 * Button to show when this option is not selected.
		 */
		private ImageLozengeButton notSelected;

		/*
		 * Creates a memory option.
		 */
		private MemoryOption(String size, boolean isAllowed) {
			this.size = size;
			this.isAvailable = isAllowed;
		}

		/*
		 * Returns the string to add to "-Xmx".
		 */
		public String toString() {
			return size;
		}

		/*
		 * Returns the string to put on the buttons.
		 */
		public String toDisplay() {
			return size.replace("G", " GB").replace("M", " MB");
		}

		/*
		 * Returns whether this option is available.
		 */
		public boolean isAvailable() {
			return isAvailable;
		}

		/*
		 * Returns the current shown button.
		 */
		public JButton getButton(boolean isSelected) {
			return isSelected ? selected : notSelected;
		}

		/*
		 * Initialize this memory option. Creates the buttons, sets the tool
		 * tip, and the action listener.
		 */
		public void init(ActionListener actionListener) {
			selected = new ImageLozengeButton(ImageLoader.load("remove_30x30_black.png"), toDisplay());
			selected.setEnabled(false);
			notSelected = new ImageLozengeButton(ImageLoader.load("action_30x30_black.png"), toDisplay());
			notSelected.setToolTipText("Use " + toDisplay() + " of memory when running ProM");
			notSelected.addActionListener(actionListener);
		}

		/*
		 * Act on a button being selected.
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		public void actionPerformed(ActionEvent e) {
			/*
			 * Check whether my button was selected.
			 */
			if (e.getSource() == notSelected) {
				/*
				 * Yes, it was. Set my memory size.
				 */
				selectedMem = this;
			}
		}
	};

	/*
	 * Creates the memory view panel.
	 */
	public PMMemoryView() {
		super(20, 5, 0);
		setBackground(new Color(160, 160, 160));
		setLayout(new BorderLayout());
		try {
			/*
			 * Try to read the current memory option from file, and set the
			 * default accordingly.
			 */
			FileReader reader = new FileReader("ProM641.l4j.ini");
			char[] a = new char[10];
			reader.read(a);
			reader.close();
			String b = String.valueOf(a);
			for (MemoryOption size : MemoryOption.values()) {
				if (b.startsWith("-Xmx" + size)) {
					/*
					 * Found a match. Set this option as default.
					 */
					selectedMem = size;
					continue;
				}
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		setupUI();
		update();
	}

	/*
	 * Sets up the buttons.
	 */
	private void setupUI() {
		/*
		 * Initialize all buttons.
		 */
		for (MemoryOption size : MemoryOption.values()) {
			size.init(this);
		}
	}

	/*
	 * A button was pressed. Check every option, and then update.
	 */
	public void actionPerformed(ActionEvent e) {
		for (MemoryOption size : MemoryOption.values()) {
			/*
			 * Check whether the button belongs to this option.
			 */
			size.actionPerformed(e);
		}
		update();
	}

	/*
	 * Update the memory view.
	 */
	private void update() {
		updateFiles();
		/*
		 * Create new buttonPanel.
		 */
		JPanel buttonPanel = new RoundedPanel(20, 5, 0);
		buttonPanel.setBackground(new Color(80, 80, 80));
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		for (MemoryOption size : MemoryOption.values()) {
			/*
			 * Only add button if available.
			 */
			if (size.isAvailable()) {
				/*
				 * Use selected button if selected, not-selected button otherwise.
				 */
				buttonPanel.add(size.getButton(selectedMem == size));
			}
		}
		/*
		 * Refresh with new button panel.
		 */
		removeAll();
		add(new JLabel("Select the amount of memory ProM may use:"), BorderLayout.NORTH);
		add(buttonPanel, BorderLayout.CENTER);
		revalidate();
	}

	/*
	 * Updates the necessary files with the new memory option.
	 */
	private void updateFiles() {
		if (oldSelectedMem == selectedMem) {
			return;
		}
		if (OsUtil.isRunningWindows()) {
			/*
			 * Windows. Need to update ini file and bat file.
			 */
			if (!updateIniFile() || !updateBatFile()) {
				/*
				 * Something failed. Possibly, the PM was not run in Administrator mode.
				 */
				JOptionPane
						.showMessageDialog(
								null,
								"Unable to set memory limit (-Xmx"
										+ selectedMem
										+ ") in ProM.l4j.ini and/or ProM641.bat file.\nPlease run the Package Manager as administrator, or set the memory limit manually.");
				selectedMem = oldSelectedMem;
				return;
			}
		} else if (OsUtil.isRunningLinux() || OsUtil.isRunningUnix()) {
			/*
			 * Linux or UNIX. Need to update sh file (and init file for reading default option next time).
			 */
			if (!updateIniFile() || !updateShFile()) {
				JOptionPane.showMessageDialog(null, "Unable to set memory limit (-Xmx" + selectedMem
						+ ") in ProM.l4j.ini and/or ProM641.sh file.\nPlease set the memory limit manually.");
				selectedMem = oldSelectedMem;
				return;
			}
		}
		/*
		 * No MAC support (yet), sorry.
		 */
		oldSelectedMem = selectedMem;
	}

	private boolean updateIniFile() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("ProM.l4j.ini", "UTF-8");
			writer.println("-Xmx" + selectedMem + " -XX:MaxPermSize=256m");
			writer.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	private boolean updateBatFile() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("ProM.bat", "UTF-8");
			writer.println("@GOTO start");
			writer.println("");
			writer.println(":add");
			writer.println(" @set X=%X%;%1");
			writer.println(" @GOTO :eof");
			writer.println("");
			writer.println(":start");
			writer.println("@set X=.\\dist\\ProM-Framework.jar");
			writer.println("@set X=%X%;.\\dist\\ProM-Contexts.jar");
			writer.println("@set X=%X%;.\\dist\\ProM-Models.jar");
			writer.println("@set X=%X%;.\\dist\\ProM-Plugins.jar");
			writer.println("");
			writer.println("@for /R . %%I IN (\"\\lib\\*.jar\") DO @call :add .\\lib\\%%~nI.jar");
			writer.println("");
			writer.println("@java -Xmx"
					+ selectedMem
					+ " -XX:MaxPermSize=256m -classpath \"%X%\" -XX:+UseCompressedOops -Djava.library.path=.//lib -Djava.util.Arrays.useLegacyMergeSort=true org.processmining.contexts.uitopia.UI");
			writer.println("");
			writer.println("set X=");
			writer.println("");
			writer.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	private boolean updateShFile() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("ProM.sh", "UTF-8");
			writer.println("#!/bin/sh");
			writer.println("CP=./ProM.jar");
			writer.println("MEM=" + selectedMem);
			writer.println("add() {");
			writer.println("\tCP=${CP}:$1");
			writer.println("}");
			writer.println("for lib in ./lib/*.jar");
			writer.println("do");
			writer.println("\tadd $lib");
			writer.println("done");
			writer.println("java -classpath ${CP} -Djava.library.path=./lib -da -Xmx${MEM} -XX:MaxPermSize=256m -XX:+UseCompressedOops -Djava.util.Arrays.useLegacyMergeSort=true org.processmining.contexts.uitopia.UI");
			writer.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

}
