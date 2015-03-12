package org.processmining.newpackage.confs;

import org.processmining.newpackage.models.YourFirstInput;
import org.processmining.newpackage.models.YourSecondInput;

public class YourConfiguration {

	private boolean yourBoolean;
	private int yourInteger;
	private String yourString;
	
	public YourConfiguration(String input1, String input2) {
		setYourBoolean(input1.equals(input2));
		setYourInteger(input1.toString().length() - input2.toString().length());
		setYourString(input1.toString() + input2.toString());
	}

	//public YourConfiguration(String input1, String input2) {
		// TODO Auto-generated constructor stub
	//}

	public void setYourBoolean(boolean yourBoolean) {
		this.yourBoolean = yourBoolean;
	}

	public boolean isYourBoolean() {
		return yourBoolean;
	}

	public void setYourInteger(int yourInteger) {
		this.yourInteger = yourInteger;
	}

	public int getYourInteger() {
		return yourInteger;
	}

	public void setYourString(String yourString) {
		this.yourString = yourString;
	}

	public String getYourString() {
		return yourString;
	}
}
