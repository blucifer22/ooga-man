package ooga.model;

import java.util.ArrayList;
import java.util.List;
import ooga.model.sprites.Sprite;
import ooga.util.Vec2;

/**
 * Test input source that plays a set number of predetermined actions.
 */
public class SeededTestInputSource implements InputSource {

  private final List<Vec2> prepopulatedActions = new ArrayList<>();
  private int dex = 0;

  public void addActions(List<Vec2> actions) {
    prepopulatedActions.addAll(actions);
  }

  @Override
  public Vec2 getRequestedDirection(double dt) {
    return prepopulatedActions.get(dex++);
  }

  @Override
  public boolean isActionPressed() {
    return false;
  }

  @Override
  public void addTarget(Sprite target) {

  }
}
