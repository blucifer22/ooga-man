package ooga.model;

public interface ObservableGrid {
  int getWidth();
  int getHeight();
  Tile getTile(TileCoordinates tileCoordinates);
}
