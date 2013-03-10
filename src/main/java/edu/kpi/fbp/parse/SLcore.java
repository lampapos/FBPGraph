package edu.kpi.fbp.parse;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.kpi.fbp.primitives.Node;
import edu.kpi.fbp.utils.XmlIo;

/**
 * Save|Load core.
 *
 * @author cheshire
 *
 */
public class SLcore {
  /**
   * Set true to debag options.
   */
  boolean D = true;

  public ArrayList<Node> N;
  public ArrayList<Connection> C;

  public int max_id = 0;

  public void dump(final ArrayList<Node> nodes) {
    for (int i = 0; i < nodes.size(); i++) {
      System.out.println(nodes.get(i));
    }
  }

  public void dumpC(final ArrayList<Connection> nodes) {
    for (int i = 0; i < nodes.size(); i++) {
      System.out.println(nodes.get(i));
    }
  }

  public void load() {
    SLContainer res = null;

    // Создаю диалог для загрузки (стандартный класс)
    final FileDialog fd = new FileDialog(new Frame(), "Открыть", FileDialog.LOAD);
    // Задаю ему стартовую директорию
    fd.setDirectory("../res/");
    // Показываю диалог.
    fd.show();

    final String path = fd.getDirectory() + fd.getFile();
    if (D)
      System.out.println(path);

    if (path != null) {
      res = XmlIo.deserialize(new File(path), SLContainer.class);
    }

    N = res.data;
    C = res.con;
    max_id = res.max_id;
  }

  public void save(final ArrayList<Node> N, final ArrayList<Connection> C, final int max) {

    // Создаю диалог для загрузки (стандартный класс)
    final FileDialog fd = new FileDialog(new Frame(), "Cохранить", FileDialog.SAVE);
    // Задаю ему стартовую директорию
    fd.setDirectory("../res/");
    // Показываю диалог.
    fd.show();

    final String path = fd.getDirectory() + fd.getFile();
    if (path != null) {
      final String out_data = XmlIo.serialize(new SLContainer(N, C, max));
      try {
        final FileWriter wr = new FileWriter(new File(path));
        wr.write(out_data);
        wr.close();
      } catch (final IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    }
  }
}
