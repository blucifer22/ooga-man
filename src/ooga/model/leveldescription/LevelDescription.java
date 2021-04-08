package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.model.PacmanGameState;

/**
 * LevelDescription is a class that serializes PacmanGameStates to JSON and can then convert it back
 * to a PacmanGameState with a call to toPacmanGameState.
 *
 * @author Marc Chmielewski
 */
public class LevelDescription extends JSONDescription {
  private final String levelName;
  private final GridDescription gridDescription;
  private final SpriteLayoutDescription spriteLayoutDescription;

  /**
   * The general constructor for LevelDescription. Takes in all of the information required to
   * create a PacmanGameState (a Pac-Man level), including a String levelName, a GridDescription,
   * and a SpriteLayoutDescription.
   *
   * @param levelName The name of the level
   * @param gridDescription The GridDescription for the Grid that this Level possesses
   * @param spriteLayoutDescription The SpriteLayout for this Level
   */
  @JsonCreator
  public LevelDescription(
      @JsonProperty("levelName") String levelName,
      @JsonProperty("gridDescription") GridDescription gridDescription,
      @JsonProperty("spriteLayoutDescription") SpriteLayoutDescription spriteLayoutDescription) {
    this.levelName = levelName;
    this.gridDescription = gridDescription;
    this.spriteLayoutDescription = spriteLayoutDescription;
  }

  /**
   * Gets this LevelDescription's GridDescription.
   *
   * @return This LevelDescription's GridDescription
   */
  @JsonGetter
  public GridDescription getGridDescription() {
    return gridDescription;
  }

  /**
   * Gets this LevelDescription's SpriteLayoutDescription.
   *
   * @return This LevelDescription's SpriteLayoutDescription
   */
  @JsonGetter
  public SpriteLayoutDescription getSpriteLayoutDescription() {
    return spriteLayoutDescription;
  }

  /**
   * Gets this LevelDescription's name.
   *
   * @return This LevelDescription's name.
   */
  @JsonGetter
  public String getLevelName() {
    return levelName;
  }

  /**
   * This method allows for this LevelDescription to be converted into a PacmanGameState by simple
   * instantiation.
   *
   * @return A PacmanGameState that possesses the properties of this LevelDescription.
   */
  public PacmanGameState toPacmanGameState() {
    PacmanGameState pgs = new PacmanGameState();
    pgs.loadGrid(this.gridDescription);
    pgs.loadSprites(this.spriteLayoutDescription.getSprites());
    return pgs;
  }
}
