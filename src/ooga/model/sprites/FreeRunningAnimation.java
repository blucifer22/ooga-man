package ooga.model.sprites;

import java.util.List;

public class FreeRunningAnimation extends EventDrivenAnimation {
  private double frameRate;

  public FreeRunningAnimation(
          List<String> costumes, boolean wrapAround, double frameRate) {
    super(costumes, wrapAround);

    this.frameRate = frameRate;
  }


}
