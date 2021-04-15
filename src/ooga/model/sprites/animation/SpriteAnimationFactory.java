package ooga.model.sprites.animation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import ooga.model.sprites.animation.PeriodicAnimation.FrameOrder;

public class SpriteAnimationFactory {
  private String spriteName;

  public SpriteAnimationFactory(String spriteName) {
    this.spriteName = spriteName;
  }

  public ObservableAnimation createAnimation(String animationType) throws IllegalArgumentException {
    List<String> costumes =
    return new FreeRunningPeriodicAnimation();
  }

  public enum SpriteAnimationType {
    GHOST_UP(true, "up", 2),
    GHOST_DOWN(true, "down", 2),
    GHOST_LEFT(true, "left", 2),
    GHOST_RIGHT(true, "right", 2),
    GHOST_FRIGHTENED(false, "frightened", 2),
    GHOST_FRIGHTENED_END(false, "frightened_end", 2),
    GHOST_UP_EYES(false, "eyes_up", 1),
    GHOST_DOWN_EYES(false, "eyes_down",1),
    GHOST_LEFT_EYES(false, "eyes_left",1),
    GHOST_RIGHT_EYES(false, "eyes_right", 1),
    PACMAN_CHOMP(true, "chomp", 3, 1.0/6.0, FrameOrder.TRIANGLE);

    private static final double DEFAULT_FRAME_PERIOD = 1.0/6.0;
    private boolean spriteSpecific;
    private String costumeBaseName;
    private int frameCount;
    private double framePeriod;
    private FrameOrder frameOrder;

    SpriteAnimationType(boolean spriteSpecific, String costumeBaseName, int frameCount,
        double framePeriod, FrameOrder frameOrder) {
      this.spriteSpecific = spriteSpecific;
      this.costumeBaseName = costumeBaseName;
      this.frameCount = frameCount;
      this.framePeriod = framePeriod;
      this.frameOrder = frameOrder;
    }

    SpriteAnimationType(boolean spriteSpecific, String costumeBaseName, int frameCount) {
      this(spriteSpecific, costumeBaseName, frameCount, DEFAULT_FRAME_PERIOD, FrameOrder.SAWTOOTH);
    }

    public ObservableAnimation getAnimation(String spriteName) {
      List<String> animationCostumes = IntStream.range(1, frameCount + 1)
          .mapToObj(num -> ((spriteSpecific ? spriteName + "_" : "") + costumeBaseName + "_" + num))
          .collect(Collectors.toList());
      return new FreeRunningPeriodicAnimation(animationCostumes, )
    }
  }

}
