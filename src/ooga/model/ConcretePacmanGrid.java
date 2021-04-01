package ooga.model;

public class ConcretePacmanGrid extends PacmanGrid {

  @Override
  public Tile getTile(TileCoordinates tileCoordinates) {
    return new Tile();
  }
}
