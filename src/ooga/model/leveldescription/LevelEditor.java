package ooga.model.leveldescription;

import java.io.File;
import java.io.IOException;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.leveldescription.LevelBuilder.BuilderState;

/**
 * LevelEditor is an interface that defines the set of methods required to implement level-editing
 * behavior. Presently, this behavior is only implemented by the LevelBuilder, but this could be
 * trivially extended to other level-building entities.
 *
 * @author David Coffman
 */
public interface LevelEditor {

  /**
   * Retrieves the current state of the level builder.
   *
   * @return current state of the level builder.
   */
  BuilderState getBuilderState();

  /** Advance to the next state */
  void advanceState();

  /**
   * Retrieve sprite palette.
   * @return Palette of sprites.
   */
  Palette getPalette();

  /**
   * Writes a completed level to a JSON file
   *
   * @param file input file
   * @throws IOException on error.
   */
  void writeToJSON(File file) throws IOException;

  /**
   * Adds a selected Sprite (from the Palette) to the given location
   *
   * @param x x-coordinate of grid to add Sprite to
   * @param y y-coordinate of grid to add Sprite to
   */
  void addSprite(int x, int y);

  /**
   * Pokes a tile to toggle the tile properties to the next type
   *
   * @param x x-coordinate of grid to change tile type
   * @param y y-coordinate of grid to change tile type
   */
  void pokeTile(int x, int y);

  /**
   * Removes all Sprites occupying a given location
   *
   * @param x x-coordinate of grid to remove all Sprites from
   * @param y y-coordinate of grid to remove all Sprites from
   */
  void clearSpritesOnTile(int x, int y);

  /**
   * Generates the Grid, allowing for default values to be set. The Grid Builder assumes a default
   * open grid, with a frame preventing any Sprites from leaving
   *
   * @param height height of the grid
   * @param width width of the grid
   */
  void setGridSize(int width, int height);

  /**
   * This method attaches a new GridRebuildObserver to this LevelEditor, thus allowing the front-end
   * to be notified of any changes to the grid state.
   *
   *
   * @param observer The GridRebuildObserver to be added to this LevelEditor's set of observers.
   */
  void addGridRebuildObserver(GridRebuildObserver observer);


  /**
   * This method attaches a new SpriteExistenceObserver to this LevelEditor, thus allowing
   * the front-end to be notified of any changes to the Sprites held by this LevelEditor.
   *
   *
   * @param spriteExistenceObserver The SpriteExistenceObserver to be added to
   *                               this LevelEditor's set of observers.
   */
  void addSpriteExistenceObserver(SpriteExistenceObserver spriteExistenceObserver);
}
