package edu.kpi.fbp.gui.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
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

/** Main application window. */
public class MainWindow extends JFrame {

  /** GUI constants. */
  private static final int ROOT_FORM_BORDER = 5;
/** SerialVersionUID. */
  private static final long serialVersionUID = 1L;
  /** Global window container. */
  private final JPanel contentPane;
  /** Hierarchical structure of all existing node types. */
  private final JPanel panelComponentTree;
  /** Component palette. */
  private final ComponentTree classComponentTree;
  /** Panel for patchbay of nodes. */
  private final JPanel panelWorkField;
  /** Class for patchbay of nodes. */
  private final WorkField classWorkField;
  /** Display attributes of single node. */
  private final JPanel panelOption;
  /** Console for all text output. */
  private final JPanel panelConsole;
  /** Link to server. */
  private final ServerConnection serverConnection = new LocalConnection();
  /** Tabed pane consist of three pane : color palette, node attributes, node description. */
  private final JTabbedPane tabbedPane;
  /** {@link ColorTab}. */
  private final ColorTab colorTab;
  /** {@link AttributeTab}. */
  private final AttributeTab attributeTab;
  /** {@link DescriptionTab}. */
  private final DescriptionTab descriptionTab;
  /** {@link JMenuBar}. */
  private final JMenuBar menuBar;
  /** Edit scheme. */
  private final JMenu mnEdit;
  /** Save/load scheme. */
  private final JMenu mnFile;
  /** Run scheme. */
  private final JMenu mnRun;
  /** Run scheme. */
  private final JMenuItem mnRunNetwork;
  /** Run scheme with parameters. */
  private final JMenuItem mnRunNetworkParam;
  /** Save scheme. */
  private final JMenuItem mnFileOpen;
  /** Save scheme. */
  private final JMenuItem mnFileSave;
  /** Delete choosed cell and all links to it. */
  private final JMenuItem mnEditDelete;
  /** Clear all field. */
  private final JMenuItem mnEditDeleteAll;
  /** {@link SaveLoadCore}. */
  private final SaveLoadCore slCore = new SaveLoadCore();

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

    mnFileOpen = new JMenuItem("Open  (Ctrl+O)");
    mnFileOpen.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        slCore.load(classWorkField, classComponentTree);
      }
    });
    mnFile.add(mnFileOpen);

    mnFileSave = new JMenuItem("Save  (Ctrl+S)");
    mnFileSave.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
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

    mnRunNetwork = new JMenuItem("Run");
    mnRunNetwork.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        slCore.save(classWorkField.getNodes(), false);
        serverConnection.networkRun(slCore.getNetworkModel());
        slCore.clean();
      }
    });
    mnRun.add(mnRunNetwork);

    mnRunNetworkParam = new JMenuItem("Run with parameters  (Ctrl+P)");
    mnRunNetworkParam.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent e) {
        slCore.save(classWorkField.getNodes(), false);
        if (slCore.getParametersStore() != null) {
          serverConnection.networkRun(slCore.getNetworkModel(), slCore.getParametersStore());
        } else {
          System.out.println("Can't find file with parameters.");
        }
        slCore.clean();
      }
    });
    mnRun.add(mnRunNetworkParam);

    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(ROOT_FORM_BORDER, ROOT_FORM_BORDER, ROOT_FORM_BORDER, ROOT_FORM_BORDER));
    setContentPane(contentPane);
    contentPane.setLayout(new BorderLayout());

    // Component pallet
    panelComponentTree = new JPanel(new BorderLayout());
    panelComponentTree.add(new JScrollPane(classComponentTree.build()), BorderLayout.CENTER);

    // Working pane
    panelWorkField = new JPanel(new BorderLayout());
    panelWorkField.add(classWorkField.createMXGraph(), BorderLayout.CENTER);

    // Options pane
    panelOption = new JPanel(new BorderLayout());
    tabbedPane = new JTabbedPane(JTabbedPane.TOP);

    //// Color chooser
    tabbedPane.addTab("Color", colorTab.createPalette(classWorkField.getGraph()));
    //// Component description
    tabbedPane.addTab("Description", descriptionTab.getDescriptionPanel());
    //// Attribute panel
    tabbedPane.addTab("Parameters", attributeTab.getAttributePanel());
    panelOption.add(tabbedPane, BorderLayout.CENTER);

    panelConsole = new JPanel(new BorderLayout());
    panelConsole.add(new JTextArea(), BorderLayout.CENTER);

    // Level 0: work pane + options panel
    final JPanel lv0 = new JPanel(new BorderLayout());
    final JSplitPane lv0Split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelWorkField, panelOption);
    lv0.add(lv0Split, BorderLayout.CENTER);
    lv0Split.setResizeWeight(0.9);

    // Level 1: level 0 + console pane
    final JPanel lv1 = new JPanel(new BorderLayout());
    final JSplitPane lv1Split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lv0, panelConsole);
    lv1.add(lv1Split, BorderLayout.CENTER);
    lv1Split.setResizeWeight(0.75);

    // Level 2: level 1 + component pallete
    final JPanel lv2 = new JPanel(new BorderLayout());
    final JSplitPane lv2Split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelComponentTree, lv1);
    lv2.add(lv2Split, BorderLayout.CENTER);
    lv2Split.setResizeWeight(0.1);

    contentPane.add(lv2, BorderLayout.CENTER);
  }

  /**
   * Launch the application.
   * @param args dance, checkstyle, dance
   */
  public static void main(final String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          final MainWindow frame = new MainWindow();
          frame.setVisible(true);
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Place in input variables coordinates of WorkField for drag'n'drop.
   * @return int[4] = [x, y, width, heigth]
   */
  public int[] getWorkFieldCoordinates() {
    final int[] res = new int[4];
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
