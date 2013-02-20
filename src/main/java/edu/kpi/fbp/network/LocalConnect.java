package edu.kpi.fbp.network;

import java.io.File;

import com.jpmorrsn.fbp.engine.Component;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.utils.ComponentsObserver;
import edu.kpi.fbp.utils.ComponentsObserver.ComponentClassDescriptor;

public class LocalConnect implements Connect{
	
	private ComponentsObserver obs;

	public LocalConnect() {
		obs = ComponentsObserver.create(new File("component/"));
	}

	@Override
	public ComponentDescriptor getComponentDescriptor(String componentName) {
		
		ComponentClassDescriptor ccd = obs.getAvailableComponentsSet().get(componentName);
		
		final Class<? extends Component> clazz = ccd.getComponentClass();
		ComponentDescriptor desc = ComponentDescriptor.buildDescriptor(clazz);
		return desc;
	}

	@Override
	public Object[] getComponentKeySet() {
		return obs.getAvailableComponentsSet().keySet().toArray();
	}

}
