package edu.kpi.fbp.app;

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
import com.mxgraph.view.mxGraph;

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

public class Gui extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Set true to debag options;
	*/
  private final boolean D = false;

  private int cell_i = -1;

  public int max_id = 0;

  public Connect connect = new LocalConnect();

  public final WorkField W = new WorkField();

  private final JPanel elements = new JPanel();
  private JScrollPane jsp = new JScrollPane();
  private imageArrow jspArrow = new imageArrow();
  public final mxGraphComponent work = W.createMXGraph(W.nodes);
  private final NodeProperties prop = new NodeProperties(work, connect);
  private JPanel property = prop.generateJP(W.nodes, cell_i);

  private final Components C = new Components(this);
  

  SLcore SL = new SLcore();

  Object buf_cell = null;

  public void deleteNode(final int pos) {
    final int b_id = W.nodes.get(pos).id;
    // Убираем ссылки на id данной вершины
    for (int i = 0; i < W.con.size(); i++) {
      if ((W.con.get(i).source == b_id) || (W.con.get(i).destination == b_id)) {
        W.con.remove(i);
      }
    }
    // Удаляем порты у вершины.
    W.nodes.get(pos).deletePorts(work);
    // Добавляем вершину к массиву удаляемых.
    work.getGraph().removeCells(new Object[] { W.nodes.get(pos).cell });
    // Убираем вершину из внутреннего представления.
    W.nodes.remove(pos);

  }

  /**
   * Удаление определенной вершины.
   */
  void deleteCell() {
    final mxCell cell = (mxCell) buf_cell;
    if (D) {
      System.out.println("Source - " + cell.getSource());
    }
    final int pos = W.getNode(cell);

    // Если выбрана вершина
    if (pos != -1) {
      deleteNode(pos);
    } else {
      // Если выбрано хоть что-то
      if (buf_cell != null) {
        // Если это связь
        if (W.isConnection(cell)) {

          for (int i = 0; i < W.con.size(); i++) {
            if (cell.equals(W.con.get(i).cell)) {
              W.con.remove(i);
            }
          }

          work.getGraph().removeCells(new Object[] { buf_cell });
        } else {
          SL.dumpC(W.con);
          System.out.println("port - " + cell);
          // Если это порт, то обрабатываем также как и удаление вершины но сначала необходимо найти эту вершину.
          // Находим порт
          Port buf = null;
          for (int i = 0; i < W.nodes.size(); i++) {
            buf = W.nodes.get(i).getInPort(cell);
            if (buf != null) {
              break;
            } else {
              buf = W.nodes.get(i).getOutPort(cell);
              if (buf != null) {
                break;
              }
            }
          }
          // Находим родителя
          mxCell parent = null;
          for (int i = 0; i < W.nodes.size(); i++) {
            if (W.nodes.get(i).id == buf.parentId) {
              parent = W.nodes.get(i).cell;
              break;
            }
          }
          // Находим позицию родителя и удаляем все вместе
          deleteNode(W.getNode(parent));

        }

      }
    }
    SL.dump(W.nodes);
    buf_cell = null;
    // Убираем панельку свойств для удаленных элементов
    repaintProperty(-1);
  }

  /**
   * Перерисовать панельку свойств
   */
  void repaintProperty(final int buf) {
    remove(property);
    property = prop.generateJP(W.nodes, buf);
    property.setBounds(745, 5, 150, 490);
    add(property);
    revalidate();
    repaint(745, 5, 150, 490);

    cell_i = buf;
  }
  
  /**
   * Show/hide arrow image
   * @param b - show or hide image
   */
  public void showArrow(boolean b){
	  jspArrow.setVisible(b);
	  revalidate();
	  repaint(161, 6, 18, 18);
  }

  /**
   * Обработчик открытия схемы
   */
  void load() {
    // Загрузка схемы из файла
    SL.load();
    W.nodes = SL.N;
    W.con = SL.C;
    // Очистка наборного поля
    work.getGraph().removeCells(work.getGraph().getChildVertices(work.getGraph().getDefaultParent()));
    // Отрисовываем вершины
    for (int i = 0; i < W.nodes.size(); i++) {
      W.nodes.get(i).draw(work.getGraph(), W.nodes.get(i).x, W.nodes.get(i).y);
    }
    // Отрисовываем связи
    for (int i = 0; i < W.con.size(); i++) {
      W.con.get(i).drawCon(work.getGraph(), W.nodes);
    }
    // Запоминаем максимальный id
    max_id = SL.max_id;

  }

  @Override
  public void init() {
    // Размер
    setSize(900, 520);
    // setLocation(100, 100);

    setLayout(null);

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
        SL.save(W.nodes, W.con, max_id);
      }
    });

    final JMenuItem editMenuClear = new JMenuItem("Clear all");
    editMenuClear.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        work.getGraph().removeCells(work.getGraph().getChildVertices(work.getGraph().getDefaultParent()));
        W.nodes.clear();
        W.con.clear();
        max_id = 0;

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
          buf_cell = work.getCellAt(e.getX(), e.getY());
          // Получить ее обьект ( или -1 если мы нажали на пустое место )
          int buf = W.getNode(buf_cell);
          // =======
          if (D)
            System.out.println("buf cell_i - " + buf);
          // =======
          // Если кликнули по пустому полю и был выбран элемент - отрисовать его
          /* ==========================================================
          if ((buf == -1) && (!C.choose.equals(""))) {
            final mxGraph gf = work.getGraph();
            gf.getModel().beginUpdate();
            try {
              max_id++;
              final Node n = new Node(C.choose, max_id);
              n.draw(gf, e.getX(), e.getY());
              W.nodes.add(n);
            } finally {
              gf.getModel().endUpdate();
            }
            buf = W.nodes.size() - 1;

          }
          ========================================================== */
          
          // Отрисовать панельку свойств, если кликнули по тому же элементу не перерисовывать
          if (cell_i != buf) {
            repaintProperty(buf);
          }
        } else {

          // При нажатии правой - сбросить выбор элемента
          if (e.getButton() == MouseEvent.BUTTON3) {
            C.choose = "";
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
          in = W.getPort(edge.getSource());
          out = W.getPort(edge.getTarget());
          W.con.add(new Connection(in.cellId, in.side, in.parentId, out.cellId, out.side, out.parentId, edge));
        } else {
          work.getGraph().removeCells(new Object[] { edge });
        }
        SL.dumpC(W.con);
      }
    });

    // Прослушиваем mxGraph на предмет перемещения вершины
    work.getGraph().addListener(mxEvent.CELLS_MOVED, new mxIEventListener() {
      @Override
      public void invoke(final Object arg0, final mxEventObject arg1) {

        final Object[] cells = (Object[]) arg1.getProperty("cells");

        if (W.getNode(cells[0]) != -1) {
          final Node M = W.nodes.get(W.getNode(cells[0]));
          final int oldX = M.x, oldY = M.y;

          // Сохраняем новое положение вершины
          M.x += Double.parseDouble("" + arg1.getProperty("dx"));
          M.y += Double.parseDouble("" + arg1.getProperty("dy"));

          // Смещаем порты относительно новой позиции вершины
          for (int i = 0; i < M.in_connect.size(); i++) {
            work.getGraph().moveCells(new Object[] { M.in_connect.get(i).cell }, M.x - oldX, M.y - oldY);

          }
          for (int i = 0; i < M.out_connect.size(); i++) {
            work.getGraph().moveCells(new Object[] { M.out_connect.get(i).cell }, M.x - oldX, M.y - oldY);

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
          SL.save(W.nodes, W.con, max_id);
        }
        if (e.isControlDown() && (e.getKeyCode() == 79)) {
          load();
        }
      }
    });

    elements.setLayout(null);
    // final JScrollPane
    jsp = new JScrollPane(C.build());
    jsp.setBounds(0, 0, 150, 490);
    elements.add(jsp);
    
    jspArrow.setBounds(161, 6, 18, 18);
    jspArrow.setVisible(false);

    elements.setBounds(5, 5, 150, 490);
    work.setBounds(160, 5, 580, 490);
    property.setBounds(745, 5, 150, 490);

    add(jspArrow);
    
    add(elements);
    add(work);
    add(property);
    
  }

}
