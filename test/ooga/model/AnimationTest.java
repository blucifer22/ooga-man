package ooga.model;

import ooga.model.sprites.animation.FreeRunningPeriodicAnimation;
import ooga.model.sprites.animation.ObservableAnimation;
import ooga.model.sprites.animation.PeriodicAnimation;
import ooga.model.sprites.animation.SpriteAnimation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnimationTest {
  ObservableAnimation animation;
  @BeforeEach
  void init() {
    animation = new FreeRunningPeriodicAnimation(List.of("pacman_closed", "pacman_halfopen", "pacman_open"),
                                                 PeriodicAnimation.FrameOrder.SAWTOOTH, 1.0);
  }

  @Test
  void testInitialState() {
    assertEquals(animation.getCurrentCostume(), "pacman_closed");
  }

  @Test
  void testAnimationCycle() {
    assertEquals(animation.getCurrentCostume(), "pacman_closed");
    animation.step(0.5);
    assertEquals(animation.getCurrentCostume(), "pacman_closed");
    animation.step(0.5);

    assertEquals(animation.getCurrentCostume(), "pacman_halfopen");
    animation.step(0.5);
    assertEquals(animation.getCurrentCostume(), "pacman_halfopen");
    animation.step(0.5);

    assertEquals(animation.getCurrentCostume(), "pacman_open");
    animation.step(0.5);
    assertEquals(animation.getCurrentCostume(), "pacman_open");
    animation.step(0.5);
  }

  @Test
  void testMultipleCycles() {
    for(int i = 0; i < 10; i++)
      testAnimationCycle();
  }

  @Test
  void testTriangleSequence() {
    animation = new FreeRunningPeriodicAnimation(List.of("pacman_closed", "pacman_halfopen", "pacman_open"),
                                                 PeriodicAnimation.FrameOrder.TRIANGLE, 1.0);

    for(int i = 0; i < 10; i++) {
      assertEquals(animation.getCurrentCostume(), "pacman_closed");
      animation.step(0.5);
      assertEquals(animation.getCurrentCostume(), "pacman_closed");
      animation.step(0.5);

      assertEquals(animation.getCurrentCostume(), "pacman_halfopen");
      animation.step(0.5);
      assertEquals(animation.getCurrentCostume(), "pacman_halfopen");
      animation.step(0.5);

      assertEquals(animation.getCurrentCostume(), "pacman_open");
      animation.step(0.5);
      assertEquals(animation.getCurrentCostume(), "pacman_open");
      animation.step(0.5);

      assertEquals(animation.getCurrentCostume(), "pacman_halfopen");
      animation.step(0.5);
      assertEquals(animation.getCurrentCostume(), "pacman_halfopen");
      animation.step(0.5);
    }
  }
}
