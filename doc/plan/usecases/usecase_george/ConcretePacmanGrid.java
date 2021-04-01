package ooga.usecase_george;

import ooga.model.PacmanGrid;
import ooga.model.Tile;
import ooga.model.TileCoordinates;

public class ConcretePacmanGrid extends PacmanGrid {

  @Override
  public Tile getTile(TileCoordinates tileCoordinates) {
    return new Tile();
  }
}
