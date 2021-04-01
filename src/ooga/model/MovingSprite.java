package ooga.model;

/**
 * Mobile sprites can be in motion (although this does not need to always be true -- mobile sprites
 * can occasionally remain stationary)
 */
public abstract class MovingSprite extends Sprite {

  protected InputSource getInputSource() {
    return null;
  }

  public void setInputSource(InputSource s) {

  }
}