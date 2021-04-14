package ooga.model.sprites;

import java.util.List;

/**
 * This class defines the abstract notion of a sprite "animation."
 *
 * Animations describe how sprites change appearance over time --
 * Pac-Man, for instance, is always performing the "chomp-chomp"
 * animation (except after being eaten by a ghost), while ghosts can
 * switch between different animations depending on their direction of
 * travel, and "frightened" status.
 *
 * Animations function by advancing through a series of predefined
 * "frames", which are simply strings that uniquely identify a
 * costume. The order in which frames are advanced through is defined
 * by the "frameOrder" property -- it can either be the "modulo-style"
 * wraparound order of 1-2-3-1-2-3, or the "bouncing" style of
 * 1-2-3-2-1.
 *
 *
 */
public class EventDrivenAnimation {
  private final boolean wrapAround;
  private final List<String> costumes;
  private int currentCostumeIndex, phase;
  private final int period, numCostumes;

  public EventDrivenAnimation(List<String> costumes, boolean wrapAround) {
    this.costumes = costumes;
    this.wrapAround = wrapAround;

    this.numCostumes = costumes.size();

    assert(numCostumes > 0);
    currentCostumeIndex = 0;

    period = wrapAround ? numCostumes : (numCostumes == 1 ? 1 : 2 * (numCostumes - 1));
    phase = 0;
  }

  public void advanceCostume() {
    phase = (phase + 1) % period;

    currentCostumeIndex = wrapAround ? phase : (phase < numCostumes ? phase : 2 * (numCostumes - 1) - phase);
  }

  public String getCurrentCostume() {
    return costumes.get(currentCostumeIndex);
  }
}
