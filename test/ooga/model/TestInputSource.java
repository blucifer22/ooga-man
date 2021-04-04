package ooga.model;

import java.util.ArrayList;
import java.util.List;
import ooga.util.Vec2;

public class TestInputSource implements InputSource {

  private int dex = 0;
  private List<Vec2> prepopulatedActions = new ArrayList<>();
  public TestInputSource() {
    for (int j = 0; j < 1000; j++){
      prepopulatedActions.add(new Vec2(-1, 0));
    }
  }

  @Override
  public Vec2 getRequestedDirection() {
    return prepopulatedActions.get(dex++);
  }

  @Override
  public boolean isActionPressed() {
    return false;
  }
}
