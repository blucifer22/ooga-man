package ooga.model.leveldescription;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ooga.model.PacmanLevel;
import ooga.model.api.GridRebuildObservable;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.SpriteExistenceObservable;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.Tile;
import ooga.model.grid.TileCoordinates;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;

/**
 * The StageBuilder is a stripped-down, non-steppable snapshot of the starting Pac-Man game state.
 * This allows for similar rendering, and ultimately serialization into a application-parsable
 * JSON file that describes a PacmanLevel.
 *
 * @author George Hong
 * @author Marc Chmielewski
 * @author Matthew Belissary
 */
public class LevelBuilder implements SpriteExistenceObservable, GridRebuildObservable, LevelEditor {

  private final Set<SpriteExistenceObserver> spriteExistenceObservers;
  private final Set<GridRebuildObserver> gridRebuildObservers;
  private final Set<Sprite> toDelete;
  private final PacmanLevel level;
  private final Palette palette;
  private int pacmanCount;
  private BuilderState currentState;

  /**
   * This constructor is the general constructor for this LevelBuilder. It takes no arguments, but
   * handles the instantiation of all of the containers within this LevelBuilder. It also instantiates
   * the currentState of this LevelBuilder to TILING, and the initial pacmanCount to 0.
   */
  public LevelBuilder() {
    spriteExistenceObservers = new HashSet<>();
    gridRebuildObservers = new HashSet<>();
    level = new PacmanLevel();
    toDelete = new HashSet<>();
    palette = new Palette();
    currentState = BuilderState.TILING;
    pacmanCount = 0;
  }

  /**
   * This method returns the current BuilderState of this LevelBuilder.
   *
   * Implemented as part of the LevelEditor interface.
   *
   * @return The current state of this LevelBuilder
   */
  @Override
  public BuilderState getBuilderState() {
    return currentState;
  }

  /**
   * This method advances the current BuilderState of this LevelBuilder from TILING to SPRITE_PLACEMENT
   * if it makes logical sense to do so. Else, it does nothing.
   *
   * Implemented as part of the LevelEditor interface.
   *
   */
  @Override
  public void advanceState() {
    if (currentState == BuilderState.TILING) {
      currentState = BuilderState.SPRITE_PLACEMENT;
    }
  }

  /**
   * Select is a modal method that changes the functionality of a click depending on the state of
   * the level builder.  To change what select does, advance to the next state.
   *
   * <p>If this LevelBuilder is currently in TILING mode, select calls pokeTile on the Tile at the
   * provided X/Y coordinates</p>
   *<p>If this LevelBuilder is currently in SPRITE_PLACEMENT mode, select calls addSprite and adds
   *  the currently selected Sprite on the palette to the Tile at the provided X/Y coordinates</p>
   *
   * @param x The X-coordinate of the selected Tile
   * @param y The Y-coordinate of the selected Tile
   */
  public void select(int x, int y) {
    switch (currentState) {
      case TILING -> pokeTile(x, y);
      case SPRITE_PLACEMENT -> addSprite(x, y);
    }
  }

  /**
   * This method returns this LevelBuilder's palette.
   *
   * Implemented as part of the LevelEditor interface.
   *
   * @return This LevelBuilder's palette.
   */
  @Override
  public Palette getPalette() {
    return palette;
  }

  /**
   * This method returns the PacmanLevel that is currently associated with this LevelBuilder.
   * This is currently just used for testing, but was made public to facilitate possible extensions
   * in dynamic serialization.
   *
   * @return The PacmanLevel that is currently associated with this LevelBuilder.
   */
  public PacmanLevel getLevel() {
    return level;
  }

  /**
   * This method writes the PacmanLevel, level, that is currently held by this LevelBuilder to JSON
   * by crafting a LevelDescription fom it, setting the gameMode to "CLASSIC" and then leveraging
   * the existing levelDescription.toJSON() method to facilitate the actual serialization.
   *
   * Implemented as part of the LevelEditor interface.
   *
   * @param file The input JSON file to write the serialization to.
   * @throws IOException When provided with an invalid JSON File. (Bad path, etc.)
   */
  @Override
  public void writeToJSON(File file) throws IOException {
    LevelDescription levelDescription = new LevelDescription(level);
    levelDescription.setGameMode("CLASSIC");
    levelDescription.toJSON(file.getPath());
  }

  /**
   * This method adds a Sprite to the grid Tile that has been clicked on. The type of Sprite is
   * determined by what type of Sprite is currently selected by the Palette. In the event that the
   * user is trying to place Pac-Man on the grid, this method performs a simple check to ensure that
   * there hasn't already been a Pac-Man placed on the grid, and throws an IllegalStateException if
   * that is not the case.
   *
   * Implemented as part of the LevelEditor interface.
   *
   * @param x x-coordinate of grid Tile to add Sprite to
   * @param y y-coordinate of grid Tile to add Sprite to
   */
  @Override
  public void addSprite(int x, int y) {
    double xCenter = x + 0.5;
    double yCenter = y + 0.5;
    Sprite sprite = palette.getSprite(xCenter, yCenter);
    if (sprite.getSwapClass() == SwapClass.PACMAN && pacmanCount == 1) {
      throw new IllegalStateException(
          "Pac-Man already placed on the board.  To reposition Pac-Man, remove previous instance");
    }
    level.getSprites().add(sprite);
    notifySpriteCreation(sprite);
    if (sprite.getSwapClass() == SwapClass.PACMAN) {
      pacmanCount++;
    }
  }

  /**
   * This method changes the state of the grid Tile that has been clicked by the user of the
   * program. The coordinates of this tile are passed in as parameters, which are then processed
   * by the method to determine the specific tile that should have its state advanced, and then
   * notifies the GridRebuildObservers to update this state visually.
   *
   * Implemented as part of the LevelEditor interface.
   *
   * @param x x-coordinate of grid to change tile type
   * @param y y-coordinate of grid to change tile type
   */
  @Override
  public void pokeTile(int x, int y) {
    Tile tile = level.getGrid().getTile(new TileCoordinates(x, y));
    level.getGrid()
        .setTile(tile.getCoordinates().getX(), tile.getCoordinates().getY(), updateTileState(tile));
    notifyGridRebuildObservers();
  }

  private Tile updateTileState(Tile tile) {
    List<String> tileOptions = List.of("tile", "tileclosed", "tilepermeable");
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
   * This method clears all of the Sprites that currently reside on a particular grid Tile. As with
   * other methods in this class, this Tile is determined from the provided coordinates.
   * This method also performs a check to verify that this LevelBuilder is in the SPRITE_PLACEMENT
   * state and throws an IllegalStateException if this condition is not upheld. As would be expected,
   * this method also maintains the count of Pac-Man instances and handles the notification of the
   * attached SpriteExistanceObservers.
   *
   * Implemented as part of the LevelEditor interface.
   *
   * @param x x-coordinate of grid to remove all Sprites from
   * @param y y-coordinate of grid to remove all Sprites from
   */
  @Override
  public void clearSpritesOnTile(int x, int y) {
    if (currentState != BuilderState.SPRITE_PLACEMENT) {
      throw new IllegalStateException("Changing sprite placement currently not allowed");
    }
    TileCoordinates tileToClear = new TileCoordinates(x, y);
    List<Sprite> sprites = level.getSprites();
    for (Sprite sprite : sprites) {
      TileCoordinates spriteTile = sprite.getCoordinates().getTileCoordinates();
      if (tileToClear.equals(spriteTile)) {
        toDelete.add(sprite);
      }
      if (sprite.getSwapClass() == SwapClass.PACMAN) {
        pacmanCount--;
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
  @Override
  public void setGridSize(int height, int width) {
    List<List<Tile>> tileList = new ArrayList<>();
    for (int y = 0; y < height; y++) {
      List<Tile> outputRow = new ArrayList<>();
      for (int x = 0; x < width; x++) {
        boolean borderTile = x == 0 || y == 0 || x == (width - 1) || y == (height -1);
        String tileName = borderTile ? "tileclosed" : "tile";
        outputRow.add(new Tile(new TileCoordinates(x, y), tileName, !borderTile, !borderTile));
      }
      tileList.add(outputRow);
    }
    PacmanGrid grid = new PacmanGrid(new GridDescription("", width, height, tileList));
    level.setGrid(grid);
    notifyGridRebuildObservers();
  }

  /**
   * This method attaches a new GridRebuildObserver to this LevelBuilder, thus allowing the front-end
   * to be notified of any changes to the grid state.
   *
   * Implemented as part of the GridRebuildObservable interface.
   *
   * @param observer The GridRebuildObserver to be added to this LevelBuilder's set of observers.
   */
  @Override
  public void addGridRebuildObserver(GridRebuildObserver observer) {
    gridRebuildObservers.add(observer);
  }

  /**
   * This method attaches a new SpriteExistenceObserver to this LevelBuilder, thus allowing
   * the front-end to be notified of any changes to the Sprites held by this LevelBuilder.
   *
   * Implemented as part of the SpriteExistenceObservable interface.
   *
   * @param spriteExistenceObserver The SpriteExistenceObserver to be added to
   *                               this LevelBuilder's set of observers.
   */
  @Override
  public void addSpriteExistenceObserver(SpriteExistenceObserver spriteExistenceObserver) {
    spriteExistenceObservers.add(spriteExistenceObserver);
  }

  /**
   * This method notifies each SpriteExistenceObserver in the event that a Sprite is destroyed. Thus,
   * ultimately, a call to this will remove the Sprite from the front-end one the notification has
   * been processed. This is the inverse of notifySpriteCreation.
   *
   * @param sprite The Sprite on which to act.
   */
  protected void notifySpriteDestruction(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteDestruction(sprite);
    }
  }

  /**
   * This method notifies each SpriteExistenceObserver in the event that a Sprite is created. Thus,
   * ultimately, a call to this will add the Sprite from to front-end one the notification has
   * been processed. This is the inverse of notifySpriteDestruction.
   *
   * @param sprite The Sprite on which to act.
   */
  protected void notifySpriteCreation(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteCreation(sprite);
    }
  }

  /**
   * This method notifies each GridRebuildObserver in the event that the grid is modified. Thus,
   * ultimately, a call to this will update the grid on the front-end one the notification has
   * been processed.
   *
   */
  protected void notifyGridRebuildObservers() {
    for (GridRebuildObserver observers : gridRebuildObservers) {
      observers.onGridRebuild(level.getGrid());
    }
  }

  /**
   * This enum enumerates the two possible BuilderStates that a given LevelBuilder can be in.
   *
   * TILING: The state in which TILIING modifications can be made to the PacmanLevel
   * in the LevelBuilder
   * SPRITE_PLACEMENT: The state in which SPRITE_PLACEMENT modifications can be made to the
   * PacmanLevel in the LevelBuilder
   */
  public enum BuilderState {
    /**
     * In tile modification.
     */
    TILING,
    /**
     * Placing sprites.
     */
    SPRITE_PLACEMENT,
  }
}
