import ooga.controller.SpriteObserver;
import ooga.model.Sprite;
import ooga.model.SpriteCoordinates;
import ooga.model.SpriteEvent;
import ooga.model.SpriteObservable;

/**
 * This use cases tests if pacman hits a cherry sprite using the current methods at the time of writing this code
 */
public class PacmanHitsCherry.java {

  // Users score
  private static int score;

  public static void main (String[] args) {

    // A cherry sprite is create to be a stationary sprite with the type "Cherry"
    SpriteObservable cherry = new Sprite() {
      @Override
      protected void step(double dt) {}

      @Override
      protected boolean isStationary() {
        return true;
      }

      @Override
      public String getType() {
        return "Cherry";
      }

      @Override
      public SpriteCoordinates getCenter() {
        return new SpriteCoordinates();
      }
    };

    // A pacman sprite is create to be a moveable sprite with the type "pacman"
    SpriteObservable pacman = new Sprite() {
      @Override
      protected boolean isStationary() {
        return false;
      }

      @Override
      public String getType() {
        return "pacman";
      }

      @Override
      protected void step(double dt) {

      }

      @Override
      public SpriteCoordinates getCenter() {
        return new SpriteCoordinates();
      }
    };

    /**
     * If the pacman sprite intersects with the cherry sprite, the score needs to increase by 50,
     * the cherry is then removed from the screen until the cherry is ready to respawn to be
     * collected again
     */

    if (pacman.getCenter().equals(cherry.getCenter())){
      score = score + 50;
      cherry.removeObserver(e -> {
        new SpriteObserver() {
          @Override
          public void onSpriteUpdate(SpriteEvent e) {
          }
        };
      });
      if (cherry.respawn()){
        cherry.addObserver();
      }
    }
  }
}