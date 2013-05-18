package edu.kpi.fbp.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
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
  private Node node;

  /** @param node - node to change color. */
  public void setNode(final Node node) {
    this.node = node;
  }

  /**
   * Create color palette.
   * @param graph - mxComponent.getGraph()
   * @return JPanel included color palette
   */
  public JPanel createPalette(final mxGraph graph) {

    final JPanel res = new JPanel();
    res.setMaximumSize(new Dimension(150, Integer.MAX_VALUE));
    res.setBackground(Color.white);
    res.setLayout(new MigLayout("", "[150px]", "[70px][70px][70px][70px][70px][70px]"));

    final JPanel colorRed = new ColorImage("/img/colorRed.jpg");
    colorRed.setBackground(Color.white);
    colorRed.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        changeColor("red", graph);
      }
    });
    final JPanel colorOrange = new ColorImage("/img/colorOrange.jpg");
    colorOrange.setBackground(Color.white);
    colorOrange.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        changeColor("orange", graph);
      }
    });
    final JPanel colorGreen = new ColorImage("/img/colorGreen.jpg");
    colorGreen.setBackground(Color.white);
    colorGreen.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        changeColor("green", graph);
      }
    });
    final JPanel colorBlue = new ColorImage("/img/colorBlue.jpg");
    colorBlue.setBackground(Color.white);
    colorBlue.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        changeColor("blue", graph);
      }
    });
    final JPanel colorWhite = new ColorImage("/img/colorWhite.jpg");
    colorWhite.setBackground(Color.white);
    colorWhite.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        changeColor("white", graph);
      }
    });
    final JPanel colorDefault = new ColorImage("/img/colorDefault.jpg");
    colorDefault.setBackground(Color.white);
    colorDefault.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
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

  private void changeColor(final String color, final mxGraph graph) {
    if (node != null) {
      final ArrayList<Object> bufArray = new ArrayList<Object>();
      bufArray.add(node.getCell());
      for (final Port port : node.getPorts()) {
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

      public ColorImage(final String path) {
         try {
           image = ImageIO.read(getClass().getResourceAsStream(path));
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
