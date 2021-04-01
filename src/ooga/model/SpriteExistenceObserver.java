package ooga.model;

public interface SpriteExistenceObserver {

  void onSpriteCreation(SpriteObservable so);

  void onSpriteDestruction(SpriteObservable so);
}
