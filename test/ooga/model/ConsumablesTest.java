package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
  }

  @Test
  public void testCherryConsumption() {
    Cherry cherry = new Cherry(new SpriteCoordinates(new Vec2(0.5, 0.5)), new Vec2(-1, 0));
    pgs.addSprite(cherry);
    pgs.registerEventListener(cherry);
    pacMan.uponHitBy(cherry, pgs);
    cherry.uponHitBy(pacMan, pgs);
    assertEquals(50, pgs.getScore());

    pgs.notifyPowerupListeners(PacmanPowerupEvent.POINT_BONUS_ACTIVATED);
    pacMan.uponHitBy(cherry, pgs);
    cherry.uponHitBy(pacMan, pgs);
    assertEquals(150, pgs.getScore());
  }

  @Test
  public void testPowerPillConsumption() {
    // Assumes power pill does not increase points at the moment.
    PowerPill powerPill = new PowerPill(new SpriteCoordinates(new Vec2(0.5, 0.5)), new Vec2(-1, 0));
    pgs.addSprite(powerPill);
    pgs.registerEventListener(powerPill);
    pacMan.uponHitBy(powerPill, pgs);
    powerPill.uponHitBy(pacMan, pgs);
    assertEquals(0, pgs.getScore());

    pgs.notifyPowerupListeners(PacmanPowerupEvent.POINT_BONUS_ACTIVATED);
    pacMan.uponHitBy(powerPill, pgs);
    powerPill.uponHitBy(pacMan, pgs);
    assertEquals(0, pgs.getScore());
  }

  @Test
  public void testDotConsumption() {
    Dot dot = new Dot(new SpriteCoordinates(new Vec2(0.5, 0.5)), new Vec2(-1, 0));
    pgs.addSprite(dot);
    pgs.registerEventListener(dot);
    pacMan.uponHitBy(dot, pgs);
    dot.uponHitBy(pacMan, pgs);
    assertEquals(1, pgs.getScore());

    pgs.notifyPowerupListeners(PacmanPowerupEvent.POINT_BONUS_ACTIVATED);
    pacMan.uponHitBy(dot, pgs);
    dot.uponHitBy(pacMan, pgs);
    assertEquals(3, pgs.getScore());
  }
}
