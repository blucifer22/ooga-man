package ooga.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.model.PacmanGameState;
import ooga.model.SpriteCoordinates;
import ooga.model.api.ObservableSprite;
import ooga.model.api.SpriteEvent.EventType;
import ooga.model.api.SpriteObserver;
import ooga.util.Vec2;
import ooga.view.theme.ConcreteThemeService;
import ooga.view.theme.ThemeService;
import ooga.view.views.GameView;

public class Controller {

  private final Stage primaryStage;

  public Controller(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.primaryStage.show();
    startGame();
  }

  public void startGame() {
    PacmanGameState pgs = new PacmanGameState();
    ThemeService ts = new ConcreteThemeService();
    GameView gv = new GameView(ts);
    pgs.addSpriteExistenceObserver(gv.getSpriteExistenceObserver());
    primaryStage.setScene(new Scene(gv.getRenderingNode(), 400.0, 400.0));
    gv.getSpriteExistenceObserver()
        .onSpriteCreation(
            new ObservableSprite() {

              @Override
              public boolean isVisible() {
                return true;
              }

              @Override
              public String getType() {
                return "pacman";
              }

              @Override
              public SpriteCoordinates getCenter() {
                return new SpriteCoordinates(new Vec2(1.5, 3.5));
              }

              @Override
              public Vec2 getDirection() {
                return new Vec2(1, 0);
              }

              @Override
              public void addObserver(SpriteObserver so, EventType... observedEvents) {}

              @Override
              public void removeObserver(SpriteObserver so) {}
            });

    gv.getSpriteExistenceObserver()
        .onSpriteCreation(
            new ObservableSprite() {

              @Override
              public boolean isVisible() {
                return true;
              }

              @Override
              public String getType() {
                return "ghost";
              }

              @Override
              public SpriteCoordinates getCenter() {
                return new SpriteCoordinates(new Vec2(1.5, 5.5));
              }

              @Override
              public Vec2 getDirection() {
                return new Vec2(1, 0);
              }

              @Override
              public void addObserver(SpriteObserver so, EventType... observedEvents) {}

              @Override
              public void removeObserver(SpriteObserver so) {}
            });
  }
}
