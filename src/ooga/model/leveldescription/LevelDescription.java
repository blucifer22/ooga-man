package ooga.model.leveldescription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ooga.model.PacmanGameState;
import ooga.model.PacmanGrid;

public class LevelDescription extends JSONDescription {
  private GridDescription gridDescription;
  private SpriteLayoutDescription spriteLayoutDescription;

  @JsonCreator
  public LevelDescription(
      @JsonProperty("gridDescription") GridDescription gridDescription,
      @JsonProperty("spriteLayoutDescription") SpriteLayoutDescription spriteLayoutDescription) {
    this.gridDescription = gridDescription;
    this.spriteLayoutDescription = spriteLayoutDescription;
  }

  public GridDescription getGridDescription() {
    return gridDescription;
  }

  public SpriteLayoutDescription getSpriteLayoutDescription() {
    return spriteLayoutDescription;
  }

  public PacmanGameState toPacmanGameState() {
    PacmanGameState pgs = new PacmanGameState();
    pgs.loadGrid(this.gridDescription);
    pgs.loadSprites(this.spriteLayoutDescription.getSprites());
    return pgs;
  }
}
