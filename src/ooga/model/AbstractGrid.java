package ooga.model;

public interface AbstractGrid {
  public int getWidth();
  public int getHeight();
  public Tile getTile(TileCoordinates tileCoordinates);
}
