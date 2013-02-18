package edu.kpi.fbp.network;

import java.io.File;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.utils.ComponentsObserver;

public class LocalConnect implements Connect{
	
	private ComponentsObserver obs;

	public LocalConnect() {
		obs = ComponentsObserver.create(new File("component/"));
	}

	@Override
	public ComponentDescriptor getComponentDescriptor(String componentName) {
		return ComponentDescriptor.buildDescriptor(Generator.class);
	}

	@Override
	public Object[] getComponentKeySet() {
		return obs.getAvailableComponentsSet().keySet().toArray();
	}

}
