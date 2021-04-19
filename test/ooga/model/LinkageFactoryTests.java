package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import javafx.stage.Stage;
import ooga.controller.HumanInputManager;
import ooga.controller.JSONController;
import ooga.controller.KeybindingType;
import ooga.controller.SpriteLinkageFactory;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.sprites.Sprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LinkageFactoryTests {

  private SpriteLinkageFactory spriteLinkageFactory;
  private PacmanGameState pgs;
  private JSONDescriptionFactory jsonDescriptionFactory;
  private HumanInputManager player1;
  private HumanInputManager player2;

  private PacmanLevel loadLevelFromJSON(String filepath) throws IOException {
    LevelDescription levelDescription =
        jsonDescriptionFactory.getLevelDescriptionFromJSON(filepath);
    return new PacmanLevel(levelDescription);
  }

  @BeforeEach
  public void setup() throws IOException {
    jsonDescriptionFactory = new JSONDescriptionFactory();
    player1 = new HumanInputManager(KeybindingType.PLAYER_1);
    player2 = new HumanInputManager(KeybindingType.PLAYER_2);
    pgs = new PacmanGameState();
    pgs.loadPacmanLevel(loadLevelFromJSON("data/levels/test_json_linkage_factory.json"));
    spriteLinkageFactory = new SpriteLinkageFactory(pgs, player1, player2);
    pgs.setPlayers(new Player(1, player1), null);
  }

  @Test
  public void testAllSpritesLoaded() {
    assertEquals(12, pgs.getSprites().size());
  }

  @Test
  public void testHumanInputLoaded() {
    boolean connectedToHumanInput = false;
    for (Sprite sprite : pgs.getSprites()){
      if (sprite.getInputSource() == player1){
        connectedToHumanInput = true;
      }
    }
    assertTrue(connectedToHumanInput);
  }

}
