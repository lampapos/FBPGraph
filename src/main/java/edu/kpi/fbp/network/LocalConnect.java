package edu.kpi.fbp.network;

import java.io.File;
import java.net.URL;

import com.jpmorrsn.fbp.engine.Component;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.params.ParametersStore;
import edu.kpi.fbp.utils.ComponentsObserver;
import edu.kpi.fbp.utils.ComponentsObserver.ComponentClassDescriptor;
import edu.kpi.fbp.utils.NetworkStarter;

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
  public Object[] getAvailableComponentsList() {
    return obs.getAvailableComponentsSet().keySet().toArray();
  }

  @Override
  public void networkRun(NetworkModel model) {
    // TODO Auto-generated method stub
  try {
    NetworkStarter.startNetwork(model);
  } catch (Exception e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
  }
  }

  @Override
  public void networkRun(NetworkModel model, ParametersStore store) {
    // TODO Auto-generated method stub
    try {
      NetworkStarter.startNetwork(model, store);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public URL makeJar(NetworkModel model, File path) {
    // TODO Auto-generated method stub
    return null;
  }

}
