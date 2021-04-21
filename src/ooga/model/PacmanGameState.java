package ooga.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ooga.controller.HumanInputManager;
import ooga.controller.SpriteLinkageFactory;
import ooga.model.api.GameStateObservable;
import ooga.model.api.GameStateObserver;
import ooga.model.api.GridRebuildObservable;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.PowerupEventObserver;
import ooga.model.api.SpriteExistenceObservable;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.leveldescription.GridDescription;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.leveldescription.SpriteDescription;
import ooga.model.sprites.MoveableSprite;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.SwapClass;
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
    implements SpriteExistenceObservable,
        GridRebuildObservable,
        MutableGameState,
        GameStateObservable {

  public static final int STARTING_ROUND_NUMBER = 1;
  public static final int STARTING_LIVE_COUNT = 3;
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

  private HumanInputManager player1;
  private HumanInputManager player2;
  private String jsonFileName;

  private int pacmanLivesRemaining;
  protected boolean isPacmanDead;
  private int roundNumber;
  private boolean isGameOver;
  private boolean pacmanConsumed;

  public PacmanGameState() {
    spriteExistenceObservers = new HashSet<>();
    gridRebuildObservers = new HashSet<>();
    toDelete = new HashSet<>();
    pacmanGameStateObservers = new HashSet<>();
    sprites = new LinkedList<>();
    pacmanPowerupObservers = new HashSet<>();
    clock = new Clock();
    roundNumber = STARTING_ROUND_NUMBER;
    pacmanLivesRemaining = STARTING_LIVE_COUNT;
    isGameOver = false;
  }

  /**
   * This method is called to reset a level. Sprites that have been consumed (such as Dots or
   * Power-Pills) do not respawn. Other Sprites are reset to their initial positions. Finally, the
   * clock restarts.
   */
  public void resetLevel() {
    for (Sprite sprite : sprites) {
      sprite.reset();
    }
    clock.clear();
    clock.reset();
  }

  public void loadPacmanLevel(PacmanLevel level) {
    for (Sprite sprite : level.getSprites()) {
      addSprite(sprite);
    }
    loadGrid(level.getGrid());
  }

  /**
   * Initializes Pac-Man game state from a JSON file. Performs all of the AI/human input linkages
   * and sets up teleporters and other map elements.
   *
   * @param filepath
   * @param player1
   * @param player2
   * @throws IOException
   */
  public void initPacmanLevelFromJSON(
      String filepath, HumanInputManager player1, HumanInputManager player2) throws IOException {
    jsonFileName = filepath;
    this.player1 = player1;
    this.player2 = player2;
    PacmanLevel level = loadLevelFromJSON(filepath);

    for (Sprite sprite : level.getSprites()) {
      addSprite(sprite);
    }
    loadGrid(level.getGrid());

    SpriteLinkageFactory spriteLinkageFactory = new SpriteLinkageFactory(this, player1, player2);
    spriteLinkageFactory.linkSprites();
  }

  private PacmanLevel loadLevelFromJSON(String filepath) throws IOException {
    JSONDescriptionFactory jsonDescriptionFactory = new JSONDescriptionFactory();
    LevelDescription levelDescription =
        jsonDescriptionFactory.getLevelDescriptionFromJSON(filepath);
    return new PacmanLevel(levelDescription);
  }

  /**
   * This method adds the level defined by the JSON file and populates the game state with the
   * Sprites, level elements, and corresponding Sprite controllers.
   *
   * @throws IOException
   */
  protected void loadNextLevel() throws IOException {
    for (Sprite sprite : sprites) {
      notifySpriteDestruction(sprite);
    }
    sprites.clear();
    clock.reset();
    clock.clear();

    PacmanLevel level = loadLevelFromJSON(jsonFileName);
    for (Sprite sprite : level.getSprites()) {
      if (sprite.getSwapClass() == SwapClass.GHOST){
        // TODO: Change to not downcast -> Create a seperate lists of MoveableSprites
        MoveableSprite mover = (MoveableSprite) sprite;
        mover.setMovementSpeed(Math.min(mover.getMovementSpeed() + (0.5 * roundNumber), 6));
      }
      addSprite(sprite);
    }
    loadGrid(level.getGrid());
    SpriteLinkageFactory spriteLinkageFactory = new SpriteLinkageFactory(this, player1, player2);
    spriteLinkageFactory.linkSprites();
  }

  protected void incrementLevel() {
    roundNumber++;
  }

  protected int getRoundNumber() {
    return roundNumber;
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
    if (!isGameOver) {
      // Moves through Sprites, determines collisions
      stepThroughSprites(dt);
      // Check if Pac-Man is dead
      checkPacmanDead();
      // All Dots have been eaten
      checkProceedToNextLevel();
      handleSwaps();
    } else {
      System.out.println("GAME OVER!");
      // TODO: Implement game over score screen
    }
  }

  protected void checkPacmanDead() {
    if (isPacmanDead) {
      decrementPacmanLivesRemaining();
      if (pacmanLivesRemaining > 0) {
        resetLevel();
        isPacmanDead(false);
      }
      else {
        isGameOver = true;
      }
    }
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

  public void isPacmanDead(boolean isPacmanDead) {
    this.isPacmanDead = isPacmanDead;
  }

  protected ImmutablePlayer getPacmanPlayer() {
    return pacmanPlayer;
  }

  /**
   * Sets up the Players associated with a PacMan game mode. These players are responsible for
   * keeping score.
   *
   * @param pacmanPlayer Player controlling Pac-Man. Null if single player during hunt mode.
   * @param ghostsPlayer Player controlling the ghosts. Null if single player during classic mode.
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
   * Increases the game score, corresponding to Pac-Man's consumption of game elements. Only Pac-Man
   * and its player has an associated score.
   *
   * @param score
   */
  @Override
  public void incrementScore(int score) {
    // pacManScore += score;
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
    // return pacManScore;
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

  protected void checkProceedToNextLevel() {
    // Next level, all consumables eaten
    if (getRemainingConsumablesCount() == 0) {
      // TODO: add some consumables and implement round progression logic
      try {
        roundNumber++;
        System.out.println(roundNumber);
        loadNextLevel();
        // System.exit(0);
      } catch (IOException e) {

      }
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
      if (!toDelete.contains(otherSprite)
          && sprite != otherSprite
          && otherSprite.getCoordinates().getTileCoordinates().equals(tc)) {
        collidingSprites.add(otherSprite);
      }
    }
    return collidingSprites;
  }

  @Override
  public void addSprite(Sprite sprite) {
    sprites.add(sprite);
    registerEventListener(sprite);
    notifySpriteCreation(sprite);
  }

  public List<Sprite> getSprites() {
    return sprites;
  }

  @Override
  public PacmanGrid getGrid() {
    return grid;
  }

  public void advanceLevel() {}

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
      if (sprite.getInputSource() != null && sprite.needsSwap()) {
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

  public void decrementPacmanLivesRemaining() {
    pacmanLivesRemaining--;
  }

  private boolean attemptSwapExecution(Sprite spriteToSwapOut, List<Sprite> spriteList) {
    for (Sprite sprite : spriteList) {
      if (spriteToSwapOut.equals(sprite)) {
        continue;
      }
      if (spriteToSwapOut.getSwapClass().equals(sprite.getSwapClass())) {
        System.out.println("Swapping input from: " + spriteToSwapOut + " to " + sprite);
        sprite.setInputSource(spriteToSwapOut.getInputSource());
        spriteToSwapOut.setInputSource(spriteToSwapOut.getDefaultInputSource());
        return true;
      }
    }
    return false;
  }
}
