package ooga.model.api;

public interface SpriteExistenceObserver {

  void onSpriteCreation(ObservableSprite so);

  void onSpriteDestruction(ObservableSprite so);
}
