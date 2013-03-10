package edu.kpi.fbp.network;

import edu.kpi.fbp.javafbp.ComponentDescriptor;

/**
 * Provide methods to get component descriptor from somewhere, specific to current realization.
 * @author Cheshire
 *
 */
public interface Connect {

  /**
   * @return array of unique component names
   */
  String[] getAvailableComponentsList();

  /**
   * @param componentName - unique component name
   * @return descriptor with all attributes of this component
   */
  ComponentDescriptor getComponentDescriptor(String componentName);

}
