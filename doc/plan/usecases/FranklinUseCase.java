package ooga;

import ooga.model.*;

public class FranklinUseCase {
  public static void main(String args[]) {
    PacmanGameState pgs = null; // assume the game state is initialized to something

    // pretend that we're in PacmanGameState::step()
    boolean advanceLevel = true;

    for(Sprite s : pgs.getSprites()) {
      s.step(1/60.0);

      if(s.mustBeConsumed())
        advanceLevel = false;
    }

    if(advanceLevel) {
      pgs.advanceLevel();
    }
  }
}
