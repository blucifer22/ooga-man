package ooga.model.api;

/**
 * Interface implemented by an object that can create/delete sprites.
 */
public interface SpriteExistenceObservable {

  /**
   * Add an observer.
   * @param spriteExistenceObserver Observer to add.
   */
  void addSpriteExistenceObserver(SpriteExistenceObserver spriteExistenceObserver);
}
