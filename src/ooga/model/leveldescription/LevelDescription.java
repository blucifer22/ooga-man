package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.stream.Collectors;
import ooga.model.PacmanLevel;

/**
 * LevelDescription is a class that serializes PacmanGameStates to JSON and can then convert it back
 * to a PacmanGameState with a call to toPacmanGameState.
 *
 * @author Marc Chmielewski
 */
public class LevelDescription extends JSONDescription {
  private final GridDescription gridDescription;
  private final SpriteLayoutDescription spriteLayoutDescription;
  private String gameMode;

  /**
   * The general constructor for LevelDescription. Takes in all of the information required to
   * create a PacmanGameState (a Pac-Man level), including a String gameMode, a GridDescription, and
   * a SpriteLayoutDescription.
   *
   * @param gameMode The name of the level
   * @param gridDescription The GridDescription for the Grid that this Level possesses
   * @param spriteLayoutDescription The SpriteLayout for this Level
   */
  @JsonCreator
  public LevelDescription(
      @JsonProperty("gameMode") String gameMode,
      @JsonProperty("gridDescription") GridDescription gridDescription,
      @JsonProperty("spriteLayoutDescription") SpriteLayoutDescription spriteLayoutDescription) {
    this.gameMode = gameMode;
    this.gridDescription = gridDescription;
    this.spriteLayoutDescription = spriteLayoutDescription;
  }

  /**
   * A special-case constructor for LevelDescription that takes in a PacmanLevel and extracts the
   * relevant information therein to construct a new LevelDescription. Used to facilitate the JSON
   * saving behavior of the LevelBuilder. Leverages the Java stream-map-collect pattern to keep this
   * down to a one-liner. (technically :) )
   *
   * @param pacmanLevel The PacmanLevel from which to construct the LevelDescription.
   */
  public LevelDescription(PacmanLevel pacmanLevel) {
    this(
        "",
        new GridDescription(pacmanLevel.getGrid()),
        new SpriteLayoutDescription(
            pacmanLevel.getSprites().stream()
                .map(SpriteDescription::new)
                .collect(Collectors.toList())));
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
  public String getGameMode() {
    return gameMode;
  }

  /**
   * This method sets the gameMode of the LevelDescription to a new value.
   *
   * @param gameMode The new value for gameMode.
   */
  public void setGameMode(String gameMode) {
    this.gameMode = gameMode;
  }

  /**
   * This method allows for this LevelDescription to be converted into a PacmanGameState by simple
   * instantiation.
   *
   * @return A PacmanGameState that possesses the properties of this LevelDescription.
   */
  public PacmanLevel toPacmanLevel() {
    return new PacmanLevel(this);
  }
}
