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

  /**
   * This constructor instantiates a new SpriteAnimationFactory with the spriteName passed in as the
   * spriteName.
   *
   * @param spriteName The spriteName with which to construct this SpriteAnimationFactory
   */
  public SpriteAnimationFactory(String spriteName) {
    this.spriteName = spriteName;
  }

  /**
   * This method creates as new ObservableAnimation for the provided SpriteAnimationType, leveraging
   * the parameters provided within the SpriteAnimationType enum to define its specific behavior.
   *
   * @param type The SpriteAnimationType for which to create the animation.
   * @return An ObservableAnimation of the specified type.
   * @throws IllegalArgumentException in the event that an illegal SpriteAnimationType has been
   *     provided.
   */
  public ObservableAnimation createAnimation(SpriteAnimationType type)
      throws IllegalArgumentException {
    return type.getAnimation(spriteName);
  }

  /**
   * This enum defines a number of SpriteAnimationTypes, and constructs them with the provided
   * behaviors.
   */
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
    PACMAN_DEATH(true, "death", 13, 1 / 9.0),
    POWER_PILL_BLINK(true, "blink", 2, 1.0 / 6.0, FrameOrder.SAWTOOTH),
    DOT_STILL(true, "still", 1),
    CHERRY_STILL(true, "still", 1),
    GAME_OVER_FLASH(false, "game_over", 2, 1.0 / 4.0, FrameOrder.SAWTOOTH),
    PACMAN_WIN_FLASH(false, "pacman_win", 2, 1.0 / 4.0, FrameOrder.SAWTOOTH),
    GHOST_WIN_FLASH(false, "ghosts_win", 2, 1.0 / 4.0, FrameOrder.SAWTOOTH),
    READY(false, "ready", 1),
    BLANK(false, "blank", 1);

    private static final double DEFAULT_FRAME_PERIOD = 1.0 / 8.0;
    private final boolean spriteSpecific, oneShot;
    private final String costumeBaseName;
    private final int frameCount;
    private final double framePeriod;
    private final FrameOrder frameOrder;

    /**
     * This constructor creates a new SpriteAnimationType from the most general set of parameters.
     *
     * @param spriteSpecific A boolean indicating if the animation is sprite-specific. For example
     *     Pac-Man's chomp vs a generic flash.
     * @param costumeBaseName The base name of the costume for this animation type.
     * @param frameCount The number of frames in the animation.
     * @param framePeriod How long each frame is displayed for. Determines animation speed. Smaller
     *     number == higher speed.
     * @param frameOrder A FrameOrder that specifies how the animation is to be repeated.
     * @param oneShot A boolean indicating if the animation is a single run animation. Only used for
     *     Pac-Man death
     */
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

    /**
     * This constructor creates a new, non-oneshot SpriteAnimationType from a curtailed set of
     * parameters.
     *
     * @param spriteSpecific A boolean indicating if the animation is sprite-specific. For example
     *     Pac-Man's chomp vs a generic flash.
     * @param costumeBaseName The base name of the costume for this animation type.
     * @param frameCount The number of frames in the animation.
     * @param framePeriod How long each frame is displayed for. Determines animation speed. Smaller
     *     number == higher speed.
     * @param frameOrder A FrameOrder that specifies how the animation is to be repeated.
     */
    SpriteAnimationType(
        boolean spriteSpecific,
        String costumeBaseName,
        int frameCount,
        double framePeriod,
        FrameOrder frameOrder) {
      this(spriteSpecific, costumeBaseName, frameCount, framePeriod, frameOrder, false);
    }

    /**
     * This constructor creates a new SAWTOOTH SpriteAnimationType from a relatively minimal set of
     * parameters.
     *
     * @param spriteSpecific A boolean indicating if the animation is sprite-specific. For example
     *     Pac-Man's chomp vs a generic flash.
     * @param costumeBaseName The base name of the costume for this animation type.
     * @param frameCount The number of frames in the animation.
     */
    SpriteAnimationType(boolean spriteSpecific, String costumeBaseName, int frameCount) {
      this(
          spriteSpecific,
          costumeBaseName,
          frameCount,
          DEFAULT_FRAME_PERIOD,
          FrameOrder.SAWTOOTH,
          false);
    }

    /**
     * This constructor creates a new oneshot, SAWTOOTH SpriteAnimationType from a curtailed set of
     * parameters.
     *
     * @param spriteSpecific A boolean indicating if the animation is sprite-specific. For example
     *     Pac-Man's chomp vs a generic flash.
     * @param costumeBaseName The base name of the costume for this animation type.
     * @param frameCount The number of frames in the animation.
     * @param framePeriod How long each frame is displayed for. Determines animation speed. Smaller
     *     number == higher speed.
     */
    SpriteAnimationType(
        boolean spriteSpecific, String costumeBaseName, int frameCount, double framePeriod) {
      this(spriteSpecific, costumeBaseName, frameCount, framePeriod, FrameOrder.SAWTOOTH, true);
    }

    /**
     * This method gets a new ObservableAnimation given a spriteName.
     * The implementation of this method leverages the Java Stream->Map->Collect pattern to
     * consolidate this down to a single line.
     *
     * @param spriteName The spriteName for which to construct the ObservableAnimation.
     * @return An ObservableAnimation for the provided spriteName.
     */
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
