package ooga.model.api;

import ooga.model.grid.TileCoordinates;

public interface ObservableGrid {
  int getWidth();

  int getHeight();

  ObservableTile getTile(TileCoordinates tileCoordinates);
}
