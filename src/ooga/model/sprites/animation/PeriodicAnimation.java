package ooga.model.sprites.animation;

import java.util.List;

/**
 * Periodic animations function by advancing through a series of
 * predefined "frames", which are simply strings that uniquely
 * identify a costume. The order in which frames are advanced through
 * is defined by the "frameOrder" property -- it can either be the
 * "modulo-style" wraparound order of 1-2-3-1-2-3, or the "bouncing"
 * style of 1-2-3-2-1.
 *
 *
 */
public abstract class PeriodicAnimation extends SpriteAnimation {
    private final FrameOrder frameOrder;
  private final List<String> costumes;
  private final int period, numCostumes;
  private int phase;

  /**
   * Construct a periodic animation.
   * @param costumes List of costumes.
   * @param order Order in which to play costumes.
   */
  public PeriodicAnimation(List<String> costumes, FrameOrder order) {
    super(costumes.get(0));

    this.costumes = costumes;
    this.numCostumes = costumes.size();
    this.frameOrder = order;

    assert(numCostumes > 0);

    period = switch(frameOrder) {
      case SAWTOOTH -> numCostumes;
      case TRIANGLE -> (numCostumes == 1 ? 1 : 2 * (numCostumes - 1));
    };
    phase = 0;
  }

  /**
   * Update the phase of the animation.
   * @param newPhase New phase.
   */
  protected void setPhase(int newPhase) {
    phase = newPhase % period;

    int currentCostumeIndex = switch(frameOrder) {
      case SAWTOOTH -> phase;
      case TRIANGLE -> phase < numCostumes ? phase : 2 * (numCostumes - 1) - phase;
    };

    setCostume(costumes.get(currentCostumeIndex));
  }

  /**
   * Order in which to play frames.
   */
  public enum FrameOrder {
    /**
     * 1-2-3-2-1
     */
    SAWTOOTH,
    /**
     * 1-2-3-1-2-3
     */
    TRIANGLE
  }
}
