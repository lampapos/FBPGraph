package edu.kpi.fbp.app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;

import edu.kpi.fbp.network.Connect;
import edu.kpi.fbp.network.LocalConnect;
import edu.kpi.fbp.panel.Components;
import edu.kpi.fbp.panel.NodeProperties;
import edu.kpi.fbp.panel.WorkField;
import edu.kpi.fbp.parse.Connection;
import edu.kpi.fbp.parse.SLcore;
import edu.kpi.fbp.primitives.Node;
import edu.kpi.fbp.primitives.Port;
import edu.kpi.fbp.primitives.imageArrow;

/**
 * Main applet which including all gui elements.
 * @author Cheshire
 *
 */
public class Gui extends JApplet {
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Set true to debag options.
	 */
  private final boolean debug = false;
  /**
   * Id of selected node.
   */
  private int cellI = -1;
  /**
   * Id of new elements would be maxId + 1.
   */
  public int maxId = 0;
  /**
   * Network interface.
   */
  public Connect connect = new LocalConnect();
  /**
   * Work filed.
   */
  public final WorkField workField = new WorkField();
  /**
   * Panel consist tree of all components.
   */
  private final JPanel elements = new JPanel();
  /**
   * ScrollPane for elements.
   */
  private JScrollPane jsp = new JScrollPane();
  /**
   * Auxiliary arrow.
   */
  private imageArrow jpArrow = new imageArrow();
  /**
   * mxGraph component created to adding cell on it.
   */
  public final mxGraphComponent work = workField.createMXGraph(workField.nodes);
  /**
   * Class which generated attribute panel.
   */
  private final NodeProperties prop = new NodeProperties(work, connect);
  /**
   * PancompTreeow component attributes.
   */
  private JPanel property = prop.generateJP(workField.nodes, cellI);
  /**
   * Tree with all components.
   */
  private final Components compTree = new Components(this);
  /**
   * Save/load network.
   */
  SLcore saveLoad = new SLcore();
  /**
   * Currently selected cell.
   */
  Object bufCell = null;

  /**
   * Delete one node from network and all connections to it.
   * @param pos - position in array of nodes.
   */
  public void deleteNode(final int pos) {
    final int bId = workField.nodes.get(pos).id;
    // Убираем ссылки на id данной вершины
    for (int i = 0; i < workField.con.size(); i++) {
      if ((workField.con.get(i).source == bId) || (workField.con.get(i).destination == bId)) {
        workField.con.remove(i);
      }
    }
    // Удаляем порты у вершины.
    workField.nodes.get(pos).deletePorts(work);
    // Добавляем вершину к массиву удаляемых.
    work.getGraph().removeCells(new Object[] {workField.nodes.get(pos).cell});
    // Убираем вершину из внутреннего представления.
    workField.nodes.remove(pos);

  }

  /**
   * Delete one node/connection.
   */
  void deleteCell() {
    final mxCell cell = (mxCell) bufCell;
    if (debug) {
      System.out.println("Source - " + cell.getSource());
    }
    final int pos = workField.getNode(cell);

    // Если выбрана вершина
    if (pos != -1) {
      deleteNode(pos);
    } else {
      // Если выбрано хоть что-то
      if (bufCell != null) {
        // Если это связь
        if (workField.isConnection(cell)) {

          for (int i = 0; i < workField.con.size(); i++) {
            if (cell.equals(workField.con.get(i).cell)) {
              workField.con.remove(i);
            }
          }

          work.getGraph().removeCells(new Object[] { bufCell });
        } else {
          saveLoad.dumpC(workField.con);
          System.out.println("port - " + cell);
          // Если это порт, то обрабатываем также как и удаление вершины но сначала необходимо найти эту вершину.
          // Находим порт
          Port buf = null;
          for (int i = 0; i < workField.nodes.size(); i++) {
            buf = workField.nodes.get(i).getInPort(cell);
            if (buf != null) {
              break;
            } else {
              buf = workField.nodes.get(i).getOutPort(cell);
              if (buf != null) {
                break;
              }
            }
          }
          // Находим родителя
          mxCell parent = null;
          for (int i = 0; i < workField.nodes.size(); i++) {
            if (workField.nodes.get(i).id == buf.parentId) {
              parent = workField.nodes.get(i).cell;
              break;
            }
          }
          // Находим позицию родителя и удаляем все вместе
          deleteNode(workField.getNode(parent));

        }

      }
    }
    saveLoad.dump(workField.nodes);
    bufCell = null;
    // Убираем панельку свойств для удаленных элементов
    repaintProperty(-1);
  }

  /**
   * Repaint NodeProperties.
   */
  void repaintProperty(final int buf) {
    remove(property);
    property = prop.generateJP(workField.nodes, buf);
    property.setBounds(745, 5, 150, 490);
    add(property);
    revalidate();
    repaint(745, 5, 150, 490);

    cellI = buf;
  }
  
  /**
   * Show/hide arrow image.
   * @param bool - show or hide image
   */
  public void showArrow(boolean bool) {
	  jpArrow.setVisible(bool);
	  revalidate();
	  repaint(161, 6, 18, 18);
  }

  /**
   * Обработчик открытия схемы.
   */
  void load() {
    // Загрузка схемы из файла
    saveLoad.load();
    workField.nodes = saveLoad.N;
    workField.con = saveLoad.C;
    // Очистка наборного поля
    work.getGraph().removeCells(work.getGraph().getChildVertices(work.getGraph().getDefaultParent()));
    // Отрисовываем вершины
    for (int i = 0; i < workField.nodes.size(); i++) {
      workField.nodes.get(i).draw(work.getGraph(), workField.nodes.get(i).x, workField.nodes.get(i).y);
    }
    // Отрисовываем связи
    for (int i = 0; i < workField.con.size(); i++) {
      workField.con.get(i).drawCon(work.getGraph(), workField.nodes);
    }
    // Запоминаем максимальный id
    maxId = saveLoad.max_id;

  }

  @Override
  public void init() {
    // Размер
    setSize(900, 520);
    // setLocation(100, 100);

    setLayout(null); //new BorderLayout());

    final JMenuBar menuBar = new JMenuBar();

    final JMenu fileMenu = new JMenu("File");
    final JMenu editMenu = new JMenu("Edit");

    final JMenuItem fileMenuOpen = new JMenuItem("Open");
    fileMenuOpen.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        load();
      }
    });

    final JMenuItem fileMenuSave = new JMenuItem("Save as");
    fileMenuSave.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        saveLoad.save(workField.nodes, workField.con, maxId);
      }
    });

    final JMenuItem editMenuClear = new JMenuItem("Clear all");
    editMenuClear.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().removeCells(work.getGraph().getChildVertices(work.getGraph().getDefaultParent()));
        workField.nodes.clear();
        workField.con.clear();
        maxId = 0;

        repaintProperty(-1);
      }
    });
    // Удаление определенных элементов
    final JMenuItem editMenuDel = new JMenuItem("Delete (Del)");
    editMenuDel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        deleteCell();
      }
    });

    final JMenuItem zoomIn = new JMenuItem("Zoom in (+)");
    zoomIn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        work.zoom(2);
      }
    });

    final JMenuItem zoomOut = new JMenuItem("Zoom out (-)");
    zoomOut.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        work.zoom(0.5);
      }
    });

    fileMenu.add(fileMenuOpen);
    fileMenu.add(fileMenuSave);
    editMenu.add(editMenuClear);
    editMenu.add(editMenuDel);
    editMenu.add(zoomIn);
    editMenu.add(zoomOut);

    menuBar.add(fileMenu);
    menuBar.add(editMenu);
    setJMenuBar(menuBar);

    work.setBounds(0, 0, 580, 490);

    // Прослушиваем мышку для mxGraph
    work.getGraphControl().addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(final MouseEvent e) {

        // Если нажата левая клавиша
        if (e.getButton() == MouseEvent.BUTTON1) {
          // Получить вершину по координатам
          bufCell = work.getCellAt(e.getX(), e.getY());
          // Получить ее обьект ( или -1 если мы нажали на пустое место )
          int buf = workField.getNode(bufCell);
          // =======
          if (debug) {
            System.out.println("buf cellI - " + buf);
          }
          // Отрисовать панельку свойств, если кликнули по тому же элементу не перерисовывать
          if (cellI != buf) {
            repaintProperty(buf);
          }
        } else {

          // При нажатии правой - сбросить выбор элемента
          if (e.getButton() == MouseEvent.BUTTON3) {
            compTree.choose = "";
          }
        }

      }
    });
    

    // Прослушиваем mxGraph на предмет соединения вершин и запоминаем
    work.getConnectionHandler().addListener(mxEvent.CONNECT, new mxIEventListener() {
      @Override
      public void invoke(final Object arg0, final mxEventObject arg1) {
        // Создаем обьект типа 'связь'
        final mxCell edge = (mxCell) arg1.getProperty("cell");
        // Добавляем связь к соедененным обьектам
        // от источника к приемнику
        if (edge.getTarget() != null) {
          Port in = null, out = null;
          in = workField.getPort(edge.getSource());
          out = workField.getPort(edge.getTarget());
          workField.con.add(new Connection(in.cellId, in.side, in.parentId, out.cellId, out.side, out.parentId, edge));
        } else {
          work.getGraph().removeCells(new Object[] {edge});
        }
        saveLoad.dumpC(workField.con);
      }
    });

    // Прослушиваем mxGraph на предмет перемещения вершины
    work.getGraph().addListener(mxEvent.CELLS_MOVED, new mxIEventListener() {
      @Override
      public void invoke(final Object arg0, final mxEventObject arg1) {

        final Object[] cells = (Object[]) arg1.getProperty("cells");

        if (workField.getNode(cells[0]) != -1) {
          final Node moved = workField.nodes.get(workField.getNode(cells[0]));
          final int oldX = moved.x, oldY = moved.y;

          // Сохраняем новое положение вершины
          moved.x += Double.parseDouble("" + arg1.getProperty("dx"));
          moved.y += Double.parseDouble("" + arg1.getProperty("dy"));

          // Смещаем порты относительно новой позиции вершины
          for (int i = 0; i < moved.in_connect.size(); i++) {
            work.getGraph().moveCells(new Object[] { moved.in_connect.get(i).cell }, moved.x - oldX, moved.y - oldY);

          }
          for (int i = 0; i < moved.out_connect.size(); i++) {
            work.getGraph().moveCells(new Object[] { moved.out_connect.get(i).cell }, moved.x - oldX, moved.y - oldY);

          }
        }
      }
    });

    // Ловим HotKey
    work.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(final KeyEvent e) {
        // Del - 127 || Backspace - 8
        // '-' - 45
        // '+' - 61
        // Ctrl + 'S' - 83
        // Ctrl + 'O' - 79
        if (e.getKeyCode() == 8 || e.getKeyCode() == 127) {
          deleteCell();
        }
        if (e.getKeyCode() == 61) {
          work.zoom(2);
        }
        if (e.getKeyCode() == 45) {
          work.zoom(0.5);
        }
        if (e.isControlDown() && (e.getKeyCode() == 83)) {
          saveLoad.save(workField.nodes, workField.con, maxId);
        }
        if (e.isControlDown() && (e.getKeyCode() == 79)) {
          load();
        }
      }
    });

    elements.setLayout(null);
    // final JScrollPane
    jsp = new JScrollPane(compTree.build());
    jsp.setBounds(0, 0, 150, 490);
    elements.add(jsp);
    
    jpArrow.setBounds(161, 6, 18, 18);
    jpArrow.setVisible(false);

    elements.setBounds(5, 5, 150, 490); //PreferredSize(new Dimension(150, 490));
    work.setBounds(160, 5, 580, 490);
    property.setBounds(745, 5, 150, 490); //PreferredSize(new Dimension(150, 490));

    add(jpArrow);
    
    add(elements); //, BorderLayout.WEST);
    add(work); //, BorderLayout.CENTER);
    add(property); //, BorderLayout.EAST);
    
  }

}
