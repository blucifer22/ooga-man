package ooga.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ooga.model.api.GameStateObservable;
import ooga.model.api.GameStateObserver;
import ooga.model.api.GridRebuildObservable;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.PowerupEventObserver;
import ooga.model.api.SpriteExistenceObservable;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.leveldescription.GridDescription;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.Sprite;
import ooga.util.Clock;

/**
 * This class contains all the state of a in-progress pacman game and serves as the top-level class
 * in the model.
 *
 * @author George Hong
 * @author Marc Chmielewski
 * @author Franklin Wei
 */
public class PacmanGameState
    implements SpriteExistenceObservable, GridRebuildObservable, MutableGameState,
    GameStateObservable {

  private final Set<SpriteExistenceObserver> spriteExistenceObservers;
  private final Set<GridRebuildObserver> gridRebuildObservers;
  private final Set<PowerupEventObserver> pacmanPowerupObservers;
  private final Set<GameStateObserver> pacmanGameStateObservers;

  private final List<Sprite> sprites;
  private final Set<Sprite> toDelete;
  private final Clock clock;
  private PacmanGrid grid;
  private Player pacmanPlayer;
  private Player ghostsPlayer;
  private int pacmanLivesRemaining;
  private int level;
  private boolean pacmanConsumed;

  public PacmanGameState() {
    spriteExistenceObservers = new HashSet<>();
    gridRebuildObservers = new HashSet<>();
    toDelete = new HashSet<>();
    pacmanGameStateObservers = new HashSet<>();
    sprites = new LinkedList<>();
    pacmanPowerupObservers = new HashSet<>();
    clock = new Clock();
  }

  public PacmanGameState(PacmanLevel level) {
    spriteExistenceObservers = new HashSet<>();
    gridRebuildObservers = new HashSet<>();
    pacmanGameStateObservers = new HashSet<>();
    toDelete = new HashSet<>();
    sprites = new LinkedList<>();

    for (Sprite sprite : level.getSprites()) {
      addSprite(sprite);
    }

    loadGrid(level.getGrid());

    pacmanPowerupObservers = new HashSet<>();
    clock = new Clock();
  }

  protected void loadNextLevel() {
    // TODO: Implement
  }

  protected void incrementLevel(){
    level++;
  }

  protected int getLevel() {
    return level;
  }

  protected boolean isPacmanConsumed() {
    return pacmanConsumed;
  }

  /**
   * Steps through a frame of the game and also checks for level progression/restart
   *
   * @param dt time-step, given by 1 / framerate
   */
  // advance game state by `dt' seconds
  public void step(double dt) {
    stepThroughSprites(dt);
    endLevel();
  }

  protected void stepThroughSprites(double dt) {
    clock.step(dt, this);
    toDelete.clear();
    for (Sprite sprite : getSprites()) {
      if (toDelete.contains(sprite)) {
        continue;
      }
      sprite.step(dt, this);
    }

    for (Sprite sprite : toDelete) {
      sprites.remove(sprite);
    }
  }

  public void addGameStateObserver(GameStateObserver observer) {
    pacmanGameStateObservers.add(observer);
  }

  public void notifyGameStateObservers() {
    for (GameStateObserver observer : pacmanGameStateObservers) {
      observer.onGameStateUpdate(this);
    }
  }

  protected ImmutablePlayer getGhostsPlayer() {
    return ghostsPlayer;
  }

  /**
   * Returns a list of players for this Pac-Man game mode
   *
   * @return list of players
   */
  @Override
  public List<ImmutablePlayer> getPlayers() {
    List<ImmutablePlayer> ret = new ArrayList<>();
    if (pacmanPlayer != null) {
      ret.add(pacmanPlayer);
    }
    if (ghostsPlayer != null) {
      ret.add(ghostsPlayer);
    }
    return ret;
  }

  @Override
  public int getPacmanLivesRemaining() {
    return pacmanLivesRemaining;
  }

  protected ImmutablePlayer getPacmanPlayer() {
    return pacmanPlayer;
  }

  /**
   * Sets up the Players associated with a PacMan game mode.  These players are responsible for
   * keeping score.
   *
   * @param pacmanPlayer Player controlling Pac-Man.  Null if single player during hunt mode.
   * @param ghostsPlayer Player controlling the ghosts.  Null if single player during classic mode.
   */
  public void setPlayers(Player pacmanPlayer, Player ghostsPlayer) {
    this.pacmanPlayer = pacmanPlayer;
    this.ghostsPlayer = ghostsPlayer;
  }

  /**
   * Returns the GameClock that keeps track of the elapsed time of the Pac-Man game
   *
   * @return game clock
   */
  public Clock getClock() {
    return clock;
  }

  /**
   * Increases the game score, corresponding to Pac-Man's consumption of game elements.  Only
   * Pac-Man and its player has an associated score.
   *
   * @param score
   */
  @Override
  public void incrementScore(int score) {
    //pacManScore += score;
    pacmanPlayer.setScore(pacmanPlayer.getScore() + score);
    notifyGameStateObservers();
  }

  /**
   * Returns Pac-Man's score from all levels
   *
   * @return Pac-Man's score
   */
  @Override
  public int getScore() {
    //return pacManScore;
    return pacmanPlayer.getScore();
  }

  @Override
  public void prepareRemove(Sprite sprite) {
    if (toDelete.contains(sprite)) {
      return;
    }
    toDelete.add(sprite);
    notifySpriteDestruction(sprite);
  }

  public void addSpriteExistenceObserver(SpriteExistenceObserver spriteExistenceObserver) {
    spriteExistenceObservers.add(spriteExistenceObserver);
  }

  public void loadGrid(GridDescription gridDescription) {
    grid = new PacmanGrid(gridDescription);
    notifyGridRebuildObservers();
  }

  public void loadGrid(PacmanGrid grid) {
    this.grid = grid;
    notifyGridRebuildObservers();
  }

  public void loadSprites(List<SpriteDescription> spriteDescriptions) {
    spriteDescriptions.forEach(spriteDescription -> addSprite(spriteDescription.toSprite()));
  }


  protected void endLevel() {
    // Next level, all consumables eaten
    if (getRemainingConsumablesCount() == 0) {
      // notifyGridRebuildObservers();
      // TODO: add some consumables and implement round progression logic
    }
  }

  private int getRemainingConsumablesCount() {
    int count = 0;
    for (Sprite sprite : getSprites()) {
      if (sprite.mustBeConsumed()) {
        count++;
      }
    }
    return count;
  }

  /**
   * Gets the list of other Sprites that resides in the same list as this sprite
   *
   * @param sprite
   * @return
   */
  @Override
  public List<Sprite> getCollidingWith(Sprite sprite) {
    TileCoordinates tc = sprite.getCoordinates().getTileCoordinates();
    List<Sprite> collidingSprites = new ArrayList<>();
    for (Sprite otherSprite : sprites) {
      if (!toDelete.contains(otherSprite) && sprite != otherSprite && otherSprite.getCoordinates()
          .getTileCoordinates().equals(tc)) {
        collidingSprites.add(otherSprite);
      }
    }
    return collidingSprites;
  }

  @Override
  public void addSprite(Sprite sprite) {
    sprites.add(sprite);
    notifySpriteCreation(sprite);
  }

  public Collection<Sprite> getSprites() {
    return sprites;
  }

  @Override
  public PacmanGrid getGrid() {
    return grid;
  }

  public void advanceLevel() {
  }

  protected void notifySpriteDestruction(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteDestruction(sprite);
    }
  }

  protected void notifySpriteCreation(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteCreation(sprite);
    }
  }

  protected void notifyGridRebuildObservers() {
    for (GridRebuildObserver observers : gridRebuildObservers) {
      observers.onGridRebuild(grid);
    }
  }

  @Override
  public void addGridRebuildObserver(GridRebuildObserver observer) {
    gridRebuildObservers.add(observer);
  }

  /**
   * Register Sprite to be notified when powerup effects begin and end, allowing the Sprites to
   * change their behavior as required.
   *
   * @param listener
   */
  @Override
  public void registerEventListener(Sprite listener) {
    pacmanPowerupObservers.add(listener);
  }

  /**
   * Can be used by Powerup Pills to introduce an effect
   *
   * @param type
   */
  @Override
  public void notifyPowerupListeners(PacmanPowerupEvent type) {
    for (PowerupEventObserver observer : pacmanPowerupObservers) {
      observer.respondToPowerEvent(type);
    }
  }

  /**
   * This method implements the ability for the PacmanGameState to handle action-key instigated
   * InputSource swaps.
   *
   * <p>This method scans the list of sprites to see if any are requesting swaps, and if so, will
   * move the target of the HumanInputManager to the next Sprite of the proper SwapClass in order in
   * the List. The Sprite that has just been relieved of the HumanInputManager will fall back onto
   * its defaultInputSource. This will implement the "cyclical" swapping behavior that is to be
   * expected in the adversarial Pac-Man game mode.
   */
  public void handleSwaps() {
    Sprite spriteToSwapOut = null;
    for (Sprite sprite : sprites) {
      if (sprite.needsSwap()) {
        spriteToSwapOut = sprite;
        break;
      }
    }
    if (spriteToSwapOut == null) {
      return;
    }

    List<Sprite> frontList = sprites.subList(0, sprites.indexOf(spriteToSwapOut));
    List<Sprite> backList = sprites.subList(sprites.indexOf(spriteToSwapOut), sprites.size());

    if (!attemptSwapExecution(spriteToSwapOut, backList)) {
      attemptSwapExecution(spriteToSwapOut, frontList);
    }
  }

  private boolean attemptSwapExecution(Sprite spriteToSwapOut, List<Sprite> spriteList) {
    for (Sprite sprite : spriteList) {
      if (spriteToSwapOut.equals(sprite)) {
        continue;
      }
      if (spriteToSwapOut.getSwapClass().equals(sprite.getSwapClass())) {
        sprite.setInputSource(spriteToSwapOut.getInputSource());
        spriteToSwapOut.setInputSource(spriteToSwapOut.getDefaultInputSource());
        return true;
      }
    }
    return false;
  }
}
