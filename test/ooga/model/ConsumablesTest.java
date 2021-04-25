package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ooga.controller.HumanInputManager;
import ooga.controller.KeybindingType;
import ooga.model.sprites.Cherry;
import ooga.model.sprites.Dot;
import ooga.model.sprites.PacMan;
import ooga.model.sprites.PowerPill;
import ooga.util.Vec2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConsumablesTest {

  private PacMan pacMan;
  private PacmanGameState pgs;

  @BeforeEach
  public void setUp() {
    pacMan = new PacMan(new SpriteCoordinates(new Vec2(0.5, 0.5)), new Vec2(-1, 0), 5);
    pgs = new PacmanGameState();
    pgs.addSprite(pacMan);
    pgs.registerEventListener(pacMan);
    pgs.setPlayers(new Player(1, new HumanInputManager(KeybindingType.PLAYER_1)), null);
  }

  @Test
  public void testCherryConsumption() {
    Cherry cherry1 = new Cherry(new SpriteCoordinates(new Vec2(0.5, 0.5)), new Vec2(-1, 0));
    Cherry cherry2 = new Cherry(new SpriteCoordinates(new Vec2(1.5, 1.5)), new Vec2(-1, 0));
    pgs.addSprite(cherry1);
    pgs.addSprite(cherry2);
    pgs.registerEventListener(cherry1);
    pgs.registerEventListener(cherry2);

    pacMan.uponHitBy(cherry1, pgs);
    cherry1.uponHitBy(pacMan, pgs);
    assertEquals(100, pgs.getScore());

    pgs.broadcastEvent(GameEvent.POINT_BONUS_ACTIVATED);
    pacMan.uponHitBy(cherry2, pgs);
    cherry2.uponHitBy(pacMan, pgs);
    assertEquals(300, pgs.getScore());
  }

  @Test
  public void testPowerPillConsumption() {
    PowerPill powerPill = new PowerPill(new SpriteCoordinates(new Vec2(0.5, 0.5)), new Vec2(-1, 0));
    pgs.addSprite(powerPill);
    pgs.registerEventListener(powerPill);
    pacMan.uponHitBy(powerPill, pgs);
    powerPill.uponHitBy(pacMan, pgs);
    assertEquals(50, pgs.getScore());

    pgs.broadcastEvent(GameEvent.POINT_BONUS_ACTIVATED);
    pacMan.uponHitBy(powerPill, pgs);
    powerPill.uponHitBy(pacMan, pgs);
    assertEquals(100, pgs.getScore());
  }

  @Test
  public void testDotConsumption() {
    Dot dot = new Dot(new SpriteCoordinates(new Vec2(0.5, 0.5)), new Vec2(-1, 0));
    pgs.addSprite(dot);
    pgs.registerEventListener(dot);
    pacMan.uponHitBy(dot, pgs);
    dot.uponHitBy(pacMan, pgs);
    assertEquals(10, pgs.getScore());

    pgs.broadcastEvent(GameEvent.POINT_BONUS_ACTIVATED);
    pacMan.uponHitBy(dot, pgs);
    dot.uponHitBy(pacMan, pgs);
    assertEquals(30, pgs.getScore());
  }
}
