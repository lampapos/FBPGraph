package edu.kpi.fbp.parse;

public class Geometry {
  int x, y, id;
  // there is AWT Color class for colors. Maybe you should use it?
  String color;

  public Geometry(final int id, final String color, final int x, final int y) {
    this.id = id;
    this.color = color;
    this.x = x;
    this.y = y;
  }
}