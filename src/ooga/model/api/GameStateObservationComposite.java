package ooga.model.api;

public interface GameStateObservationComposite {
  SpriteExistenceObserver spriteExistenceObserver();
  GridRebuildObserver gridRebuildObserver();
}
