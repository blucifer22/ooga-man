package ooga.model.api;

/**
 * Composite observable interface.
 *
 * @author David Coffman
 * @author Marc Chmielewski
 */
public interface GameStateObservationComposite {
  SpriteExistenceObserver spriteExistenceObserver();

  GridRebuildObserver gridRebuildObserver();

  AudioObserver audioObserver();

  GameStateObserver gameStateObserver();
}
