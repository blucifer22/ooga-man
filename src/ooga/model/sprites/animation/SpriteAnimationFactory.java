package ooga.model.sprites.animation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ooga.model.sprites.animation.PeriodicAnimation.FrameOrder;

/**
 * This class is a relatively simple factory that creates sprite animations for all of the non-still
 * types of sprite in the Pac-Man variations.
 *
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class SpriteAnimationFactory {
  private final String spriteName;

  public SpriteAnimationFactory(String spriteName) {
    this.spriteName = spriteName;
  }

  public ObservableAnimation createAnimation(SpriteAnimationType type)
      throws IllegalArgumentException {
    return type.getAnimation(spriteName);
  }

  public enum SpriteAnimationType {
    GHOST_UP(true, "up", 2),
    GHOST_DOWN(true, "down", 2),
    GHOST_LEFT(true, "left", 2),
    GHOST_RIGHT(true, "right", 2),
    GHOST_FRIGHTENED(false, "frightened", 2),
    GHOST_FRIGHTENED_END(false, "frightened_end", 4),
    GHOST_UP_EYES(false, "eyes_up", 1),
    GHOST_DOWN_EYES(false, "eyes_down", 1),
    GHOST_LEFT_EYES(false, "eyes_left", 1),
    GHOST_RIGHT_EYES(false, "eyes_right", 1),
    GHOST_UP_PLAYER(false, "ghost_player_up", 4),
    GHOST_DOWN_PLAYER(false, "ghost_player_down", 4),
    GHOST_LEFT_PLAYER(false, "ghost_player_left", 4),
    GHOST_RIGHT_PLAYER(false, "ghost_player_right", 4),
    PACMAN_CHOMP(true, "chomp", 3, 1.0 / 20.0, FrameOrder.TRIANGLE),
    PACMAN_STILL_HALFOPEN(true, "halfopen", 1),
    PACMAN_STILL_OPEN(true, "open", 1),
    PACMAN_DEATH(true, "death", 13, 1 / 9.0, true),
    POWER_PILL_BLINK(true, "blink", 2, 1.0 / 6.0, FrameOrder.SAWTOOTH),
    DOT_STILL(true, "still", 1),
    CHERRY_STILL(true, "still", 1),
    GAME_OVER_FLASH(false, "game_over", 2, 1.0 / 4.0, FrameOrder.SAWTOOTH),
    PACMAN_WIN_FLASH(false, "pacman_win", 2, 1.0 / 4.0, FrameOrder.SAWTOOTH),
    GHOST_WIN_FLASH(false, "ghosts_win", 2, 1.0 / 4.0, FrameOrder.SAWTOOTH),
    BLANK(false, "blank", 1);

    private static final double DEFAULT_FRAME_PERIOD = 1.0 / 8.0;
    private final boolean spriteSpecific, oneShot;
    private final String costumeBaseName;
    private final int frameCount;
    private final double framePeriod;
    private final FrameOrder frameOrder;

    SpriteAnimationType(
        boolean spriteSpecific,
        String costumeBaseName,
        int frameCount,
        double framePeriod,
        FrameOrder frameOrder,
        boolean oneShot) {
      this.spriteSpecific = spriteSpecific;
      this.costumeBaseName = costumeBaseName;
      this.frameCount = frameCount;
      this.framePeriod = framePeriod;
      this.frameOrder = frameOrder;
      this.oneShot = oneShot;
    }

    SpriteAnimationType(
        boolean spriteSpecific,
        String costumeBaseName,
        int frameCount,
        double framePeriod,
        FrameOrder frameOrder) {
      this(spriteSpecific, costumeBaseName, frameCount, framePeriod, frameOrder, false);
    }

    SpriteAnimationType(boolean spriteSpecific, String costumeBaseName, int frameCount) {
      this(
          spriteSpecific,
          costumeBaseName,
          frameCount,
          DEFAULT_FRAME_PERIOD,
          FrameOrder.SAWTOOTH,
          false);
    }

    SpriteAnimationType(
        boolean spriteSpecific,
        String costumeBaseName,
        int frameCount,
        double framePeriod,
        boolean oneShot) {
      this(spriteSpecific, costumeBaseName, frameCount, framePeriod, FrameOrder.SAWTOOTH, true);
    }

    public ObservableAnimation getAnimation(String spriteName) {
      List<String> costumes =
          IntStream.range(1, frameCount + 1)
              .mapToObj(
                  num -> ((spriteSpecific ? spriteName + "_" : "") + costumeBaseName + "_" + num))
              .collect(Collectors.toList());
      return frameCount > 1
          ? (oneShot
              ? new OneShotAnimation(costumes, framePeriod)
              : new FreeRunningPeriodicAnimation(costumes, frameOrder, framePeriod))
          : new StillAnimation(costumes.get(0));
    }
  }
}
