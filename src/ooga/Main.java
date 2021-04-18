package ooga;

import javafx.application.Application;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.controller.DemoController;
import ooga.controller.JSONController;

/** Feel free to completely change this code or delete it entirely. */
public class Main extends Application {

  /**
   * The main entry point for all JavaFX applications. The start method is called after the init
   * method has returned, and after the system is ready for the application to begin running.
   *
   * <p>NOTE: This method is called on the JavaFX Application Thread.
   *
   * @param primaryStage the primary stage for this application, onto which the application scene
   *     can be set. Applications may create other stages, if needed, but they will not be primary
   *     stages.
   */
  @Override
  public void start(Stage primaryStage) {
    //new DemoController(primaryStage);
    //new Controller(primaryStage);
    new JSONController(primaryStage);
  }
}
