package ooga.model;

import java.util.ArrayList;
import java.util.List;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.Sprite;

/**
 * PacmanLevel is a centralized data class that will contain all of the information required to
 * construct a Pac-Man level. Per now, this means the grid and sprites, but it may be extended if
 * alternative game modes require other entities.
 *
 * @author Marc Chmielewski
 */
public class PacmanLevel {

  private List<Sprite> sprites;
  private PacmanGrid grid;

  /**
   * The basic constructor for a PacmanLevel. Takes in a JSON-serializable levelDescription and and
   * uses it to construct a List of Sprites and a PacmanGrid.
   *
   * @param levelDescription The levelDescription containing the sprites and grid that define the
   *     level
   */
  public PacmanLevel(LevelDescription levelDescription) {

    sprites = new ArrayList<>();

    List<SpriteDescription> spriteDescriptions =
        levelDescription.getSpriteLayoutDescription().getSprites();
    for (SpriteDescription spriteDescription : spriteDescriptions) {
      sprites.add(spriteDescription.toSprite());
    }

    grid = new PacmanGrid(levelDescription.getGridDescription());
  }

  /**
   * This method exposes the List of sprites held by this PacmanLevel.
   *
   * @return The List of sprites held by this PacmanLevel.
   */
  public List<Sprite> getSprites() {
    return sprites;
  }

  /**
   * This method exposes the PacmanGrid held by this PacmanLevel.
   *
   * @return The PacmanGrid held by this PacmanLevel.
   */
  public PacmanGrid getGrid() {
    return grid;
  }
}