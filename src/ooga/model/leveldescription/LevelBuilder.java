package ooga.model.leveldescription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import ooga.model.PacmanGrid;
import ooga.model.PacmanLevel;
import ooga.model.Tile;
import ooga.model.TileCoordinates;
import ooga.model.api.GridRebuildObservable;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.SpriteExistenceObservable;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.sprites.Sprite;

/**
 * The StageBuilder is a stripped-down, non-steppable snapshot of the starting Pac-Man game state.
 * This allows for similar rendering.
 *
 * @author George Hong
 * @author Marc Chmielewski
 * @author Matthew Belissary
 */
public class LevelBuilder implements SpriteExistenceObservable, GridRebuildObservable {

  private final Set<SpriteExistenceObserver> spriteExistenceObservers;
  private final Set<GridRebuildObserver> gridRebuildObservers;
  private final Set<Sprite> toDelete;
  private String jsonFileName;
  private final PacmanLevel level;
  private Palette palette;

  public LevelBuilder() {
    spriteExistenceObservers = new HashSet<>();
    gridRebuildObservers = new HashSet<>();
    level = new PacmanLevel();
    toDelete = new HashSet<>();
    palette = new Palette();
  }

  public Palette getPalette() { return palette; }

  public PacmanLevel getLevel() { return level; }

  /**
   * Adds a selected Sprite (from the Palette) to the given location
   *
   * @param x x-coordinate of grid to add Sprite to
   * @param y y-coordinate of grid to add Sprite to
   */
  public void addSprite(int x, int y) {
    // TODO: Get currently active Sprite, feed x, y as inputs.  Load from properties files?
    // TODO: Pair Sprite descriptions to become a metadata + representation class?
    // list of (Sprite + InputSource) + (coordinate) ==> SpriteDescription ==> Sprite.  When ready, Sprite ==> SpriteDescription
    /*
    Option: Use property file of (className : inputSource)
     */
    double xCenter = x + 0.5;
    double yCenter = y + 0.5;
    Sprite sprite = palette.getSprite(xCenter, yCenter);
    level.getSprites().add(sprite);
    notifySpriteCreation(sprite);
  }

  /**
   * Adds a selected Tile (from the Palette) to the given location
   *
   * @param tile to cycle state
   */
  public void pokeTile(Tile tile) {
    // TODO: Get currently active Tile, feed x, y as inputs
    level.getGrid().setTile(tile.getCoordinates().getX(), tile.getCoordinates().getY(), updateTileState(tile));
    notifyGridRebuildObservers();
  }

  private Tile updateTileState(Tile tile) {
    List<String> tileOptions = List.of("tileclosed", "tile", "tilepermeable");
    Map<String, Boolean> pacmanTileMap = Map
        .of("tileclosed", false, "tile", true, "tilepermeable", false);
    Map<String, Boolean> ghostTileMap = Map
        .of("tileclosed", false, "tile", true, "tilepermeable", true);

    int currentTileTypeIndex = tileOptions.indexOf(tile.getType());
    String nextTileType = tileOptions.get((currentTileTypeIndex + 1) % tileOptions.size());

    tile.setIsOpenToPacman(pacmanTileMap.get(nextTileType));
    tile.setIsOpenToGhosts(ghostTileMap.get(nextTileType));
    tile.setType(nextTileType);

    return tile;
  }

  /**
   * Removes all Sprites occupying a given location
   *
   * @param x x-coordinate of grid to remove all Sprites from
   * @param y y-coordinate of grid to remove all Sprites from
   */
  public void clearSpritesOnTile(int x, int y) {
    TileCoordinates tileToClear = new TileCoordinates(x, y);
    List<Sprite> sprites = level.getSprites();
    for (Sprite sprite : sprites) {
      TileCoordinates spriteTile = sprite.getCoordinates().getTileCoordinates();
      if (tileToClear.equals(spriteTile)) {
        toDelete.add(sprite);
      }
    }
    for (Sprite sprite : toDelete) {
      sprites.remove(sprite);
      notifySpriteDestruction(sprite);
    }
    toDelete.clear();
  }

  /**
   * Generates the Grid, allowing for default values to be set.  The Grid Builder assumes a default
   * open grid, with a frame preventing any Sprites from leaving
   *
   * @param height height of the grid
   * @param width  width of the grid
   */
  public void setGridSize(int height, int width) {
    PacmanGrid grid = new PacmanGrid(height, width);
    List<List<Tile>> tileList = new ArrayList<>();
    for (int y = 0; y < height; y++) {
      List<Tile> outputRow = new ArrayList<>();
      for (int x = 0; x < width; x++) {
        boolean notBorderTile = x != 0 && y != 0 && x != width - 1 && y != height - 1;
        boolean openTile = notBorderTile;
        String tileName = notBorderTile ? "tile" : "tileclosed";
        outputRow.add(new Tile(new TileCoordinates(x, y), tileName, openTile, openTile));
      }
      tileList.add(outputRow);
    }
    level.setGrid(grid);
  }


  @Override
  public void addGridRebuildObserver(GridRebuildObserver observer) {
    gridRebuildObservers.add(observer);
  }

  @Override
  public void addSpriteExistenceObserver(SpriteExistenceObserver spriteExistenceObserver) {
    spriteExistenceObservers.add(spriteExistenceObserver);
  }

  protected void notifySpriteDestruction(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteDestruction(sprite);
    }
  }

  protected void notifySpriteCreation(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteCreation(sprite);
    }
  }

  protected void notifyGridRebuildObservers() {
    for (GridRebuildObserver observers : gridRebuildObservers) {
      observers.onGridRebuild(level.getGrid());
    }
  }
}
