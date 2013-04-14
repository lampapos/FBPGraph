package edu.kpi.fbp.gui.networkConnection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.params.ParametersStore;
import edu.kpi.fbp.utils.ComponentsObserver;
import edu.kpi.fbp.utils.ComponentsObserver.ComponentClassDescriptor;
import edu.kpi.fbp.utils.JarBuilder;
import edu.kpi.fbp.utils.NetworkStarter;

/**
 * The local implementation of server connection.
 * @author Cheshire
 */
public class LocalConnection implements ServerConnection {
    /** Component observer utility - observes component library and gives map class. */
  private final ComponentsObserver obs;
  /** Load components from file. */
  public LocalConnection() {
    final File buf = new File("components/");
    obs = ComponentsObserver.create(buf);
  }

  @Override
  public Map<String, ComponentClassDescriptor> getAvailableComponents() {
    return obs.getAvailableComponentsSet();
  }

  @Override
  public void networkRun(final NetworkModel model) {
    try {
      NetworkStarter.startNetwork(model, new File("components/"), new File("libs/"));
    } catch (final Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void networkRun(final NetworkModel model, final ParametersStore store) {
    try {
      NetworkStarter.startNetwork(model, store, new File("components/"), new File("libs/"));
    } catch (final Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public URL makeJar(final NetworkModel model, final File path) {

    new Thread(
        new Runnable() {
          @Override
          public void run() {
            try {
              JarBuilder.buildAndSaveJar(model, path);
              System.out.println("Buid finished.");
            } catch (final IOException e) {
              e.printStackTrace();
            }
          }
        }
    ).start();

    return null;
  }

}
