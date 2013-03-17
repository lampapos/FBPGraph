package edu.kpi.fbp.gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

import edu.kpi.fbp.gui.primitives.Node;
import edu.kpi.fbp.gui.primitives.Port;

/**
 * Color palette.
 * @author Cheshire
 *
 */
public class ColorTab {
  /** This node will change color. */
  Node node;
  
  /** @param node - node to change color. */
  public void setNode(Node node) {
    this.node = node;
  }

  /**
   * Create color palette.
   * @param graph - mxComponent.getGraph()
   * @return JPanel included color palette
   */
  public JPanel createPalette(final mxGraph graph) {
    
    JPanel res = new JPanel();
    res.setBackground(Color.white);
    res.setLayout(new MigLayout("", "[150px]", "[70px][70px][70px][70px][70px][70px]"));
    
    JPanel colorRed = new ColorImage("bin/img/colorRed.jpg");
    colorRed.setBackground(Color.white);
    colorRed.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        changeColor("red", graph);
      }
    });
    JPanel colorOrange = new ColorImage("bin/img/colorOrange.jpg");
    colorOrange.setBackground(Color.white);
    colorOrange.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        changeColor("orange", graph);
      }
    });
    JPanel colorGreen = new ColorImage("bin/img/colorGreen.jpg");
    colorGreen.setBackground(Color.white);
    colorGreen.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        changeColor("green", graph);
      }
    });
    JPanel colorBlue = new ColorImage("bin/img/colorBlue.jpg");
    colorBlue.setBackground(Color.white);
    colorBlue.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        changeColor("blue", graph);
      }
    });
    JPanel colorWhite = new ColorImage("bin/img/colorWhite.jpg");
    colorWhite.setBackground(Color.white);
    colorWhite.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        changeColor("white", graph);
      }
    });
    JPanel colorDefault = new ColorImage("bin/img/colorDefault.jpg");
    colorDefault.setBackground(Color.white);
    colorDefault.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        changeColor(null, graph);
      }
    });
    
    res.add(colorRed, "cell 0 0,grow");
    res.add(colorOrange, "cell 0 1,grow");
    res.add(colorGreen, "cell 0 2,grow");
    res.add(colorBlue, "cell 0 3,grow");
    res.add(colorWhite, "cell 0 4,grow");
    res.add(colorDefault, "cell 0 5,grow");
    
    return res;
  }
  
  private void changeColor(String color, mxGraph graph) {
    if (node != null) {
      ArrayList<Object> bufArray = new ArrayList<Object>();
      bufArray.add(node.getCell());
      for (Port port : node.getPorts()) {
        bufArray.add(port.getCell());
      }
      
      if (color != null) {
        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, color, bufArray.toArray());
      } else {
        graph.setCellStyle(null, bufArray.toArray());
      }
      node.setColor(color);
    }
  }
  
  /** Image that show node color. */
  class ColorImage extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** {@link ColorImage}. */
    private BufferedImage image;

      public ColorImage(String path) {
         try {
           image = ImageIO.read(new File(path));
         } catch (final IOException ex) {
           ex.printStackTrace();
         }
      }

      @Override
      public void paintComponent(final Graphics g) {
          super.paintComponent(g);
          g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters
      }
  }
}
