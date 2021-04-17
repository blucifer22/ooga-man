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

  public PacmanLevel(LevelDescription levelDescription) {

    sprites = new ArrayList<>();

    List<SpriteDescription> spriteDescriptions =
        levelDescription.getSpriteLayoutDescription().getSprites();
    for(SpriteDescription spriteDescription : spriteDescriptions) {
      sprites.add(spriteDescription.toSprite());
    }

    grid = new PacmanGrid(levelDescription.getGridDescription());
  }

  public List<Sprite> getSprites() {
    return sprites;
  }

  public void setSprites(List<Sprite> sprites) {
    this.sprites = sprites;
  }

  public PacmanGrid getGrid() {
    return grid;
  }

  public void setGrid(PacmanGrid grid) {
    this.grid = grid;
  }
}
