package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.controller.SpriteLinkageFactory;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.powerups.SpeedUpPowerUp;
import ooga.model.sprites.Dot;
import ooga.model.sprites.PacMan;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PowerUpFactoryTests {

  private SpriteLinkageFactory spriteLinkageFactory;
  private PacmanGameState pgs;
  private JSONDescriptionFactory jsonDescriptionFactory;
  private HumanInputManager player1;
  private HumanInputManager player2;

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

  private PacmanLevel loadLevelFromJSON(String filepath) throws IOException {
    LevelDescription levelDescription =
        jsonDescriptionFactory.getLevelDescriptionFromJSON(filepath);
    return new PacmanLevel(levelDescription);
  }

  @Test
  public void createSpeedUp(){
    PacMan pacMan = new PacMan(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(-1, 0), 5);
    Dot dot = new Dot(new SpriteCoordinates(new Vec2(2.5, 2.5)), new Vec2(-1, 0));
    pgs.addSprite(dot);
    pgs.addSprite(pacMan);
    SpeedUpPowerUp speedUp = new SpeedUpPowerUp();
    speedUp.executePowerUp(pgs);
    pgs.step(1.0/60);
    assertEquals(10, pacMan.getMovementSpeed());
  }
}
