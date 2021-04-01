package ooga;

import java.util.Collection;

public class MarcUseCase {
  public static void main(String[] args) {
    PacmanGameState pgs = null; // In reality this would be instantiated to loaded game level

    Collection<Sprite> sprites = pgs.getSprites(); // Get the current sprites

    if(consumablesRemaining() == 0) {
      System.out.println("Pac-Man has eaten ALL THE THINGS!!!");
      // TODO: Handle loading the next level or ending the game if this is the last one.
    }
    else {
      System.out.println("Pac-Man still has to eat " + consumablesRemaining() + " things.");
    }
  }

  private int consumablesRemaining(Collection<Sprite> sprites) {
    int numConsumablesRemaining = 0;
    for (Sprite sprite : sprites) {
      if(sprite.mustBeConsumed()) {
        numConsumablesRemaining++;
      }
    }
    return numConsumablesRemaining;
  }
}