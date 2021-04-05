package ooga.model;

public interface ObservableGrid {
  int getWidth();
  int getHeight();
  TileObservable getTile(TileCoordinates tileCoordinates);
}
