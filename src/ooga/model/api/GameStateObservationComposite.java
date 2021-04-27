package ooga.model.api;

/**
 * Composite observable interface to be implemented by objects which must observe
 * a game state.
 *
 * @author David Coffman
 * @author Marc Chmielewski
 */
public interface GameStateObservationComposite {
  /**
   * Get the sprite existence observer
   * @return The sprite existence component of this composite.
   */
  SpriteExistenceObserver spriteExistenceObserver();

  /**
   * Get the grid rebuild observer of this composite.
   * @return The rebuild observer of this composite.
   */
  GridRebuildObserver gridRebuildObserver();

  /**
   * Get the audio observer of this composite.
   * @return Audio observer.
   */
  AudioObserver audioObserver();

  /**
   * Get the game state observer of this composite.
   *
   * @return Game state observer.
   */
  GameStateObserver gameStateObserver();
}
