package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.controller.SpriteLinkageFactory;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.powerups.GhostSlowdownPowerUp;
import ooga.model.powerups.PointBonusPowerUp;
import ooga.model.powerups.SpeedUpPowerUp;
import ooga.model.powerups.FrightenPowerUp;
import ooga.model.sprites.Dot;
import ooga.model.sprites.Ghost;
import ooga.model.sprites.Ghost.GhostBehavior;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PowerUpFactoryTests {

  private PacMan pacMan;
  private Ghost ghost;
  private Dot dot;
  private PacmanGameState pgs;
  private JSONDescriptionFactory jsonDescriptionFactory;
  private SpriteLinkageFactory spriteLinkageFactory;
  private HumanInputManager player1;
  private HumanInputManager player2;

  @BeforeEach
  public void setup() throws IOException {
    jsonDescriptionFactory = new JSONDescriptionFactory();
    player1 = new HumanInputManager(KeybindingType.PLAYER_1);
    player2 = new HumanInputManager(KeybindingType.PLAYER_2);
    pgs = new PacmanGameState();
    pgs.loadPacmanLevel(loadLevelFromJSON("data/levels/classic_pacman.json"));
    spriteLinkageFactory = new SpriteLinkageFactory(pgs, player1, player2);
    spriteLinkageFactory.linkSprites();
    pgs.setPlayers(new Player(1, player1), null);
  }

  private PacmanLevel loadLevelFromJSON(String filepath) throws IOException {
    LevelDescription levelDescription =
        jsonDescriptionFactory.getLevelDescriptionFromJSON(filepath);
    return new PacmanLevel(levelDescription);
  }

  @Test
  public void createSpeedUp(){
    for (Sprite sprite : pgs.getSprites()) {
      if (sprite.getSwapClass() == SwapClass.PACMAN){
        pacMan = (PacMan) sprite;
      }
    }

    SpeedUpPowerUp speedUp = new SpeedUpPowerUp();
    speedUp.executePowerUp(pgs);
    pgs.step(1.0/60);
    assertEquals(12.8, pacMan.getMovementSpeed());
  }

  @Test
  public void createGhostSlowdown(){
    for (Sprite sprite : pgs.getSprites()) {
      if (sprite.getSwapClass() == SwapClass.GHOST){
        ghost = (Ghost) sprite;
      }
    }

    GhostSlowdownPowerUp slowDown = new GhostSlowdownPowerUp();
    slowDown.executePowerUp(pgs);
    pgs.step(1.0/60);
    assertEquals(2.5, ghost.getMovementSpeed());
  }

  @Test
  public void createDoublePoints(){
    for (Sprite sprite : pgs.getSprites()) {
      if (sprite instanceof Dot){
        dot = (Dot) sprite;
      }
    }

    PointBonusPowerUp doublePoints = new PointBonusPowerUp();
    doublePoints.executePowerUp(pgs);
    pgs.step(1.0/60);
    assertEquals(20.0, dot.getScore());
  }

  @Test
  public void createFrightenedPowerUp(){
    for (Sprite sprite : pgs.getSprites()) {
      if (sprite.getSwapClass() == SwapClass.GHOST){
        ghost = (Ghost) sprite;
      }
    }

    ghost.unfreeze();
    ghost.onGameEvent(GameEvent.SPRITES_UNFROZEN);
    for (int i=0; i < 10000; i++){
      pgs.step(1.0/60);
    }

    FrightenPowerUp frightenPowerUp = new FrightenPowerUp();
    frightenPowerUp.executePowerUp(pgs);
    pgs.step(1.0/60);
    assertEquals(GhostBehavior.RUNAWAY, ghost.getGhostBehavior());
    for (int i=0; i < 10000; i++){
      pgs.step(1.0/60);
    }
  }
}
