package edu.kpi.fbp.network;

import java.io.File;
import java.net.URL;

import edu.kpi.fbp.javafbp.ComponentDescriptor;
import edu.kpi.fbp.model.NetworkModel;
import edu.kpi.fbp.params.ParametersStore;

/**
 * Provide methods to get component descriptor from somewhere, specific to current realization.
 * @author Cheshire
 *
 */
public interface Connect {

  /**
   * @return array of unique component names
   */
  Object[] getAvailableComponentsList();

  /**
   * @param componentName - unique component name
   * @return descriptor with all attributes of this component
   */
  ComponentDescriptor getComponentDescriptor(String componentName);

  void networkRun(NetworkModel model);
  
  void networkRun(NetworkModel model, ParametersStore store);
  
  URL makeJar(NetworkModel model, File path);
}
