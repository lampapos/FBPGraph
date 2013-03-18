package edu.kpi.fbp.gui.app;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import edu.kpi.fbp.gui.file.SaveLoadCore;
import edu.kpi.fbp.gui.networkConnection.LocalConnection;
import edu.kpi.fbp.gui.networkConnection.ServerConnection;
import edu.kpi.fbp.gui.panels.AttributeTab;
import edu.kpi.fbp.gui.panels.ColorTab;
import edu.kpi.fbp.gui.panels.ComponentTree;
import edu.kpi.fbp.gui.panels.DescriptionTab;
import edu.kpi.fbp.gui.panels.WorkField;
import edu.kpi.fbp.utils.ComponentsObserver.ComponentClassDescriptor;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;

/** Main application window. */
public class MainWindow extends JFrame {

  /** SerialVersionUID. */
  private static final long serialVersionUID = 1L;
  /** Global window container. */
  private JPanel contentPane;
  /** Hierarchical structure of all existing node types. */
  private JPanel panelComponentTree;
  /** Component palette. */
  private ComponentTree classComponentTree;
  /** Panel for patchbay of nodes. */
  private JPanel panelWorkField;
  /** Class for patchbay of nodes. */
  private WorkField classWorkField;
  /** Display attributes of single node. */
  private JPanel panelOption;
  /** Console for all text output. */
  private JPanel panelConsole;
  /** Link to server. */
  private ServerConnection serverConnection = new LocalConnection();
  /** Tabed pane consist of three pane : color palette, node attributes, node description. */
  private JTabbedPane tabbedPane;
  /** {@link ColorTab}. */
  private ColorTab colorTab;
  /** {@link AttributeTab}. */
  private AttributeTab attributeTab;
  /** {@link DescriptionTab}. */
  private DescriptionTab descriptionTab;
  /** {@link JMenuBar}. */
  private JMenuBar menuBar;
  /** Edit scheme. */
  private JMenu mnEdit;
  /** Save/load scheme. */
  private JMenu mnFile;
  /** Run scheme. */
  private JMenu mnRun;
  /** Run scheme. */
  private JMenuItem mnRunNetwork;
  /** Save scheme. */
  private JMenuItem mnFileSave;
  /** Delete choosed cell and all links to it. */
  private JMenuItem mnEditDelete;
  /** Clear all field. */
  private JMenuItem mnEditDeleteAll;
  /** {@link SaveLoadCore}. */
  private SaveLoadCore slCore = new SaveLoadCore();
  

  /**
   * Launch the application.
   * @param args dance, checkstyle, dance
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          MainWindow frame = new MainWindow();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the frame.
   */
  public MainWindow() {

    classComponentTree = new ComponentTree(this);
    colorTab = new ColorTab();
    descriptionTab = new DescriptionTab();
    attributeTab = new AttributeTab();
    classWorkField = new WorkField(colorTab, descriptionTab, attributeTab);
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 900, 500);
    
    menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    
    mnFile = new JMenu("File");
    menuBar.add(mnFile);
    
    mnFileSave = new JMenuItem("Save  (Ctrl+S)");
    mnFileSave.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        slCore.save(classWorkField.getNodes(), true);
      }
    });
    mnFile.add(mnFileSave);
    
    mnEdit = new JMenu("Edit");
    menuBar.add(mnEdit);
    
    mnEditDelete = new JMenuItem("Delete      (Del)");
    mnEditDelete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        classWorkField.deleteCell();
      }
    });
    mnEdit.add(mnEditDelete);
    
    mnEditDeleteAll = new JMenuItem("Delete all (Ctrl+Del)");
    mnEditDeleteAll.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        classWorkField.deleteAll();
      }
    });
    mnEdit.add(mnEditDeleteAll);
    
    mnRun = new JMenu("Run");
    menuBar.add(mnRun);
    
    mnRunNetwork = new JMenuItem("Run  (Ctrl+R)");
    mnRunNetwork.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        slCore.save(classWorkField.getNodes(), false);
        serverConnection.networkRun(slCore.getNetworkModel());
      }
    });
    mnRun.add(mnRunNetwork);
    
    contentPane = new JPanel();
    contentPane.setBackground(Color.WHITE);
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(new MigLayout("", "[::160px,grow][grow][::150px,grow]", "[159.00,grow][::100px,grow]"));
    
    panelComponentTree = new JPanel();
    panelComponentTree.setLayout(new BoxLayout(panelComponentTree, BoxLayout.X_AXIS));
    panelComponentTree.add(new JScrollPane(classComponentTree.build()));
    contentPane.add(panelComponentTree, "cell 0 0 1 2,grow");
    
    panelWorkField = new JPanel();
    panelWorkField.add(classWorkField.createMXGraph());
    contentPane.add(panelWorkField, "cell 1 0,grow");
    panelWorkField.setLayout(new BoxLayout(panelWorkField, BoxLayout.X_AXIS));
    
    panelOption = new JPanel();
    panelOption.setBackground(Color.white);
    contentPane.add(panelOption, "cell 2 0,grow");
    panelOption.setLayout(new BoxLayout(panelOption, BoxLayout.X_AXIS));
    
    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    //Color chooser
    //tabbedPane.addTab("Color", colorTab.createPalette(classWorkField.getGraph()));
    //Component description
    tabbedPane.addTab("Description", descriptionTab.getDescriptionPanel());
    //Attribute panel
    tabbedPane.addTab("Parameters", attributeTab.getAttributePanel());
    panelOption.add(tabbedPane);
    
    panelConsole = new JPanel();
    contentPane.add(panelConsole, "cell 1 1 2 1,grow");
  }
  
  /** 
   * Place in input variables coordinates of WorkField for drag'n'drop.
   * @return int[4] = [x, y, width, heigth]
   */
  public int[] getWorkFieldCoordinates() {
    int[] res = new int[4];
    res[0] = panelWorkField.getLocation().x;
    res[1] = panelWorkField.getLocation().y;
    res[2] = classWorkField.getGraphComponentSize().width;
    res[3] = classWorkField.getGraphComponentSize().height;
    
    return res;
  }
  
  /** @return classWorkField */
  public WorkField getClassWorkField() {
    return classWorkField;
  }
  
  /** @return available node types */
  public Map<String, ComponentClassDescriptor> getAvailableComponents() {
    return serverConnection.getAvailableComponents();
  }
  
  /**
   * Test method used to calculate new node position.
   * @return JPanel size
   */
  public Dimension getPanelComponentTreeSize() {
    return panelComponentTree.getSize();
  }

}