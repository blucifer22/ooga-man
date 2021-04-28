package ooga.model.api;

/**
 * Composite observable interface. Streamlines the delivery of dependencies to the model.
 *
 * @author David Coffman
 * @author Marc Chmielewski
 */
public interface GameStateObservationComposite {

  /**
   * Returns this composite's {@link SpriteExistenceObserver}.
   *
   * @return this composite's {@link SpriteExistenceObserver}.
   */
  SpriteExistenceObserver spriteExistenceObserver();

  /**
   * Returns this composite's {@link GridRebuildObserver}.
   *
   * @return this composite's {@link GridRebuildObserver}.
   */
  GridRebuildObserver gridRebuildObserver();

  /**
   * Returns this composite's {@link AudioObserver}.
   *
   * @return this composite's {@link AudioObserver}.
   */
  AudioObserver audioObserver();

  /**
   * Returns this composite's {@link GameStateObserver}.
   *
   * @return this composite's {@link GameStateObserver}.
   */
  GameStateObserver gameStateObserver();
}
