package edu.kpi.fbp.gui.networkConnection;

import java.io.File;
import java.net.URL;
import java.util.Map;

import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.params.ParametersStore;
import edu.kpi.fbp.utils.ComponentsObserver;
import edu.kpi.fbp.utils.NetworkStarter;
import edu.kpi.fbp.utils.ComponentsObserver.ComponentClassDescriptor;

/**
 * Local realization of server connection.
 * @author Cheshire
 */
public class LocalConnection implements ServerConnection {
    /** Component observer utility - observes component library and gives map class. */
  private ComponentsObserver obs;
  /** Load components from file. */
  public LocalConnection() {
    File buf = new File("bin/component/");
    obs = ComponentsObserver.create(buf);
  }

  @Override
  public Map<String, ComponentClassDescriptor> getAvailableComponents() {
    return obs.getAvailableComponentsSet();
  }

  @Override
  public void networkRun(NetworkModel model) {
    try {
      NetworkStarter.startNetwork(model);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void networkRun(NetworkModel model, ParametersStore store) {
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
