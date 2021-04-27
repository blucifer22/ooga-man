package ooga.model.api;

/**
 * Interface to be implemented by objects which should respond
 * sprite creation/deletion.
 */
public interface SpriteExistenceObserver {

  /**
   * Called on creation of a sprite.
   * @param so Sprite that was created.
   */
  void onSpriteCreation(ObservableSprite so);

  /**
   * Called upon deletion of a sprite.
   *
   * @param so Destroyed sprite.
   */
  void onSpriteDestruction(ObservableSprite so);
}
