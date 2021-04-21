package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.controller.SpriteLinkageFactory;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.TeleporterOverlay;
import ooga.util.Vec2;
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
  public void resetTest() {
    spriteLinkageFactory.linkSprites();
    for (int k = 0; k < 90; k++) {
      pgs.step(1 / 60.);
    }
    pgs.resetLevel();
    Sprite blinkySprite = pgs.getSprites().get(0);
    assertEquals(new Vec2(8.5, 8.5), blinkySprite.getCoordinates().getPosition());
  }

  @Test
  public void testAllSpritesLoaded() {
    assertEquals(12, pgs.getSprites().size());
  }

  @Test
  public void testHumanInputLoaded() {
    boolean connectedToHumanInput = false;
    for (Sprite sprite : pgs.getSprites()) {
      if (sprite.getInputSource() == player1) {
        connectedToHumanInput = true;
      }
    }
    assertTrue(connectedToHumanInput);
  }

  @Test
  public void testInputLinkage() {
    spriteLinkageFactory.linkSprites();
    for (Sprite sprite : pgs.getSprites()) {
      if (!sprite.getInputString().equals("NONE")
          && !sprite.getInputString().contains("TELEPORTER")) {
        assertNotNull(sprite.getInputSource());
      }
    }
  }

  @Test
  public void testTeleporterLinkage() {
    spriteLinkageFactory.linkSprites();
    Map<String, List<Sprite>> teleporterOverlayMap = new HashMap<>();
    for (Sprite sprite : pgs.getSprites()) {
      if (sprite.getInputString().contains("TELEPORTER")) {
        TeleporterOverlay teleporterOverlay = (TeleporterOverlay) sprite;
        teleporterOverlayMap.putIfAbsent(teleporterOverlay.getInputString(), new ArrayList<>());
        teleporterOverlayMap
            .get(teleporterOverlay.getInputString())
            .addAll(teleporterOverlay.getConnectedTeleporters());
      }
    }

    for (String key : teleporterOverlayMap.keySet()) {
      assertEquals(teleporterOverlayMap.get(key).size(), 2);
    }
  }
}
