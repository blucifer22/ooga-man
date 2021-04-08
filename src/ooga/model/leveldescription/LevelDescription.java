package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.model.PacmanGameState;

public class LevelDescription extends JSONDescription {
  private final String levelName;
  private final GridDescription gridDescription;
  private final SpriteLayoutDescription spriteLayoutDescription;

  @JsonCreator
  public LevelDescription(
      @JsonProperty("levelName") String levelName,
      @JsonProperty("gridDescription") GridDescription gridDescription,
      @JsonProperty("spriteLayoutDescription") SpriteLayoutDescription spriteLayoutDescription) {
    this.levelName = levelName;
    this.gridDescription = gridDescription;
    this.spriteLayoutDescription = spriteLayoutDescription;
  }

  @JsonGetter
  public GridDescription getGridDescription() {
    return gridDescription;
  }

  @JsonGetter
  public SpriteLayoutDescription getSpriteLayoutDescription() {
    return spriteLayoutDescription;
  }

  @JsonGetter
  public String getLevelName() {
    return levelName;
  }

  public PacmanGameState toPacmanGameState() {
    PacmanGameState pgs = new PacmanGameState();
    pgs.loadGrid(this.gridDescription);
    pgs.loadSprites(this.spriteLayoutDescription.getSprites());
    return pgs;
  }
}
