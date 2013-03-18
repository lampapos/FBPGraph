package edu.kpi.fbp.gui.networkConnection;

import java.io.File;
import java.net.URL;
import java.util.Map;

import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.params.ParametersStore;
import edu.kpi.fbp.utils.ComponentsObserver.ComponentClassDescriptor;

/**
 * Encapsulate all connections to server.
 * @author Cheshire
 */
public interface ServerConnection {
  /** @return all avaliabe components. */
  Map<String, ComponentClassDescriptor> getAvailableComponents();
  /**
   * Run network on server.
   * @param model network model.
   */
  void networkRun(NetworkModel model);
  /** Run parameterized network on server.
   * @param model network model
   * @param store parameters
   */
  void networkRun(NetworkModel model, ParametersStore store);
  /**
   * Create jar on server.
   * @param model network model
   * @param path path to place where save jar
   * @return url to jar
   */
  URL makeJar(NetworkModel model, File path);
}
