package ooga.usecase_george;

import ooga.model.SpriteCoordinates;
import ooga.model.TileCoordinates;

public class UsecaseGeorge {

  public static void main(String[] args) {
    // Assume this is in step of the BouncingBanana class

    ConcretePacmanGrid pacmanGrid = new ConcretePacmanGrid();
    // Bouncing powerup that moves around
    BouncingBanana bb = new BouncingBanana(new SpriteCoordinates());
    // Get current location of the bouncing powerup
    SpriteCoordinates current = bb.getCoordinates();

    // Gets tile reference of the banana
    TileCoordinates tc = current.getTileCoordinates();
    if (!pacmanGrid.getTile(tc).isOpen()) {
      // If in place the pacman can't move, apply bounce to have it return to a correct spot.
      bb.applyBounce();
    }
  }
}




