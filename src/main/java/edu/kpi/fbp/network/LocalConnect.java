package edu.kpi.fbp.network;

import java.io.File;

import com.jpmorrsn.fbp.engine.Component;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.utils.ComponentsObserver;
import edu.kpi.fbp.utils.ComponentsObserver.ComponentClassDescriptor;

public class LocalConnect implements Connect{

  private final ComponentsObserver obs;

  public LocalConnect() {
    obs = ComponentsObserver.create(new File("component/"));
  }

  @Override
  public ComponentDescriptor getComponentDescriptor(final String componentName) {

    final ComponentClassDescriptor ccd = obs.getAvailableComponentsSet().get(componentName);

    final Class<? extends Component> clazz = ccd.getComponentClass();
    final ComponentDescriptor desc = ComponentDescriptor.buildDescriptor(clazz);
    return desc;
  }

  @Override
  public String[] getAvailableComponentsList() {
    return (String[]) obs.getAvailableComponentsSet().keySet().toArray();
  }

}
