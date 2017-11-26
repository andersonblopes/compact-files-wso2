package com.lopes.sample;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class UppercaseConvertMediator extends AbstractMediator {

	private String name;

	public boolean mediate(MessageContext context) {
		convertToUppercase(context);
		return true;
	}

	private void convertToUppercase(MessageContext context) {
		String city = (String) context.getProperty("CITY");

		String converted = city.toUpperCase();

		context.setProperty("CONVERTED", getName() + " is lives at " + converted);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
