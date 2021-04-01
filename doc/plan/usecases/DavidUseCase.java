/*
  The Controller class contains the startGame method, which links the observers as described in one of my use cases. The code responsible for that linkage is below.
*/

public class Controller {

  public void startGame() {
    // create the game state object (could also be supplied as a method param)
    PacmanGameState pgs = new PacmanGameState();
    // create the game view object (could also be supplied as a method param); implements SpriteExistenceObserver
    GameView gv = new GameView();
    // add the GameView as a SpriteExistenceObserver
    pgs.addExistenceObserver(gv);
    // start game through a call to PacmanGameState!
    // the GameView subsequently receives all notifications of sprites being created and destroyed; no controller intervention is required
  }
}
