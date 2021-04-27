package ooga.model;

import static ooga.model.audio.AudioManager.NORMAL_AMBIENCE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ooga.controller.HumanInputManager;
import ooga.controller.SpriteLinkageFactory;
import ooga.model.api.AudioObserver;
import ooga.model.api.GameEventObserver;
import ooga.model.api.GameStateObservable;
import ooga.model.api.GameStateObserver;
import ooga.model.api.GridRebuildObservable;
import ooga.model.api.GridRebuildObserver;
import ooga.model.api.ImmutablePlayer;
import ooga.model.api.SpriteExistenceObservable;
import ooga.model.api.SpriteExistenceObserver;
import ooga.model.audio.AudioManager;
import ooga.model.grid.PacmanGrid;
import ooga.model.grid.SpriteCoordinates;
import ooga.model.grid.TileCoordinates;
import ooga.model.leveldescription.GridDescription;
import ooga.model.leveldescription.JSONDescriptionFactory;
import ooga.model.leveldescription.LevelDescription;
import ooga.model.sprites.Sprite;
import ooga.model.sprites.status.GameOver;
import ooga.model.sprites.status.GhostWin;
import ooga.model.sprites.status.PacmanWin;
import ooga.util.Clock;
import ooga.util.Vec2;

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

  /**
   * Iniital round number.
   */
  public static final int STARTING_ROUND_NUMBER = 1;
  /**
   * Initial life count.
   */
  public static final int STARTING_LIVE_COUNT = 3;
  private final Set<SpriteExistenceObserver> spriteExistenceObservers;
  private final Set<GridRebuildObserver> gridRebuildObservers;
  private final Set<GameEventObserver> pacmanPowerupObservers;
  private final Set<GameStateObserver> pacmanGameStateObservers;

  private final List<Sprite> sprites;
  private final Clock clock;
  private final AudioManager audioManager;
  private final Set<Sprite> toDelete;
  private PacmanGrid grid;
  private Player pacmanPlayer;
  private Player ghostsPlayer;
  private HumanInputManager player1;
  private HumanInputManager player2;
  private String jsonFileName;
  private int pacmanLivesRemaining;
  private boolean isPacmanDead;
  private int roundNumber;
  private boolean isGameOver;

  /**
   * The general-purpose constructor for PacmanGameState. This constructor instantiates all of the
   * Collections and other data-management variables, instantiates the game clock, and attaches the
   * audioManager.
   */
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

    audioManager = new AudioManager();
    registerEventListener(audioManager);
  }

  /**
   * Sets the number of lives that Pac-Man has.
   *
   * @param lives The new number of lives for Pac-Man.
   */
  protected void setLives(int lives) {
    pacmanLivesRemaining = lives;
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
    getAudioManager().reset();
    getAudioManager().setAmbience(NORMAL_AMBIENCE);
  }

  /**
   * Fetches whether or not Pac-Man is currently dead.
   *
   * @return Whether or not Pac-Man is currently dead.
   */
  protected boolean isPacmanDead() {
    return isPacmanDead;
  }

  /**
   * This method loads a PacmanLevel into this PacmanGameState. In the process it calls addSprite on
   * each sprite in the PacmanLevel and loads in the grid.
   *
   * @param level The PacmanLevel to load into this PacmanGameState.
   */
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
   * @param filepath The filepath of the JSON that contains the serialized PacmanLevel to load in.
   * @param player1 A HumanInputManager for Player 1
   * @param player2 A HumanInputManager for Player 2
   * @throws IOException In the event than an invalid JSON filepath is provided.
   */
  public void initPacmanLevelFromJSON(
      String filepath, HumanInputManager player1, HumanInputManager player2) throws IOException {
    jsonFileName = filepath;
    this.player1 = player1;
    this.player2 = player2;
    setupSprites(filepath, player1, player2);
    setPlayers(new Player(1, player1), null);
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
   * @throws IOException In the event than an invalid JSON filepath is provided.
   */
  protected void loadNextLevel() throws IOException {
    for (Sprite sprite : sprites) {
      notifySpriteDestruction(sprite);
    }
    sprites.clear();
    clock.reset();
    clock.clear();
    audioManager.reset();

    setupSprites(jsonFileName, player1, player2);
  }

  private void setupSprites(
      String jsonFileName, HumanInputManager player1, HumanInputManager player2)
      throws IOException {
    PacmanLevel level = loadLevelFromJSON(jsonFileName);
    for (Sprite sprite : level.getSprites()) {
      sprite.uponNewLevel(roundNumber, this);
      addSprite(sprite);
    }
    loadGrid(level.getGrid());
    SpriteLinkageFactory spriteLinkageFactory = new SpriteLinkageFactory(this, player1, player2);
    spriteLinkageFactory.linkSprites();
  }

  /**
   * Steps through a frame of the game and also checks for level progression/restart
   *
   * @param dt time-step, given by 1 / framerate
   */
  public void step(double dt) {
    stepThroughSprites(dt);
    if (!isGameOver) {
      checkProceedToNextLevel();
      checkPacmanDead();
      handleSwaps();
      notifyGameStateObservers();
    } else {
    }
  }

  /**
   * This method checks to see if Pac-Man is dead, and if so, decrements the number of lives
   * remaining and handles the implications of this decrement. If Pac-Man still has lives remaining,
   * the level is reset and play resumes as normal. If not, then the grid is wiped and a GameOver
   * Sprite is spawned to indicate that the game has concluded.
   */
  protected void checkPacmanDead() {
    if (isPacmanDead) {
      decrementPacmanLivesRemaining();
      if (pacmanLivesRemaining > 0) {
        resetLevel();
        isPacmanDead(false);
      } else {
        gameOverCleanup();
        addSprite(
            new GameOver(
                new SpriteCoordinates(
                    new Vec2(getGrid().getWidth() / 2.0, getGrid().getHeight() / 2.0)),
                new Vec2(1, 0)));
      }
    }
  }

  /**
   * This method handles the cleanup of Sprites in the event of a game over by notifying each
   * SpriteExistenceObserver that each Sprite has been destroyed. It also toggles the isGameOver
   * boolean to true and stops the ambience.
   */
  protected void gameOverCleanup() {
    isGameOver = true;
    toDelete.addAll(sprites);
    sprites.forEach(this::notifySpriteDestruction);
    sprites.clear();
    audioManager.stopAmbience();
  }

  /**
   * This method steps through each of the sprites on the interval of one tick of dt. In doing this,
   * the method will advance the game clock, check to see if Sprites need to be deleted, and if so,
   * remove them.
   *
   * @param dt The time interval over which to step through the sprites.
   */
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

  /**
   * This method attaches a new GameStateObserver to the PacmanGameState.
   *
   * @param observer The GameStateObserver to attach to the PacmanGameState.
   */
  public void addGameStateObserver(GameStateObserver observer) {
    pacmanGameStateObservers.add(observer);
  }

  /**
   * This method notifies each GameStateObserver in the set of pacmanGameStateObservers by calling
   * the onGameStateUpdate method of each of them and passing in this PacmanGameState.
   */
  public void notifyGameStateObservers() {
    for (GameStateObserver observer : pacmanGameStateObservers) {
      observer.onGameStateUpdate(this);
    }
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

  /**
   * Fetches the number of lives remaining for Pac-Man.
   *
   * @return The number of lives remaining for Pac-Man.
   */
  @Override
  public int getPacmanLivesRemaining() {
    return pacmanLivesRemaining;
  }

  /**
   * Fetches the current round number.
   *
   * @return The current round number.
   */
  @Override
  public int getRoundNumber() {
    return roundNumber;
  }

  /**
   * Sets the current "death status" of Pac-Man to the passed in parameter.
   *
   * @param isPacmanDead Sets the current "death status" of Pac-Man to the passed in parameter.
   */
  public void isPacmanDead(boolean isPacmanDead) {
    this.isPacmanDead = isPacmanDead;
  }

  /**
   * Sets up the Players associated with a PacMan game mode. These players are responsible for
   * keeping score.
   *
   * @param pacmanPlayer Player controlling Pac-Man. Null if single player during hunt mode.
   * @param ghostsPlayer Player controlling the ghosts. Null if single player during classic mode.
   */
  protected void setPlayers(Player pacmanPlayer, Player ghostsPlayer) {
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
   * @param score The new score for the game.
   */
  @Override
  public void incrementScore(int score) {
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
    return pacmanPlayer.getScore();
  }

  /**
   * This method prepares the passed in Sprite for removal by adding it to the toDelete set and
   * notifying its observers that it is being destroyed.
   *
   * @param sprite The Sprite to prepare for removal.
   */
  @Override
  public void prepareRemove(Sprite sprite) {
    if (toDelete.contains(sprite)) {
      return;
    }
    toDelete.add(sprite);
    notifySpriteDestruction(sprite);
  }

  /**
   * Attaches a SpriteExistenceObserver to this PacmanGameState.
   *
   * @param spriteExistenceObserver The SpriteExistenceObserver to attach to this PacmanGameState.
   */
  public void addSpriteExistenceObserver(SpriteExistenceObserver spriteExistenceObserver) {
    spriteExistenceObservers.add(spriteExistenceObserver);
  }

  /**
   * This method loads in a new PacmanGrid to this PacmanGameState by taking in a GridDescription,
   * constructing a PacmanGrid from it, and then notifying the GridRebuildObservers.
   *
   * @param gridDescription The gridDescription from which to construct the new PacmanGrid.
   */
  public void loadGrid(GridDescription gridDescription) {
    grid = new PacmanGrid(gridDescription);
    notifyGridRebuildObservers();
  }

  /**
   * This method loads in a new PacmanGrid through direct assignment to the grid variable of
   * PacmanGameState and then notifies the GridRebuildObservers.
   *
   * @param grid The PacmanGrid to set the PacmanGameState grid to.
   */
  public void loadGrid(PacmanGrid grid) {
    this.grid = grid;
    notifyGridRebuildObservers();
  }

  /**
   * This method checks to see if it is time to proceed to the next level, that is, the number of
   * consumables is zero, and if so handles the increment of the round number, ambiance management,
   * and the loading of the next level.
   */
  protected void checkProceedToNextLevel() {
    if (getRemainingConsumablesCount() == 0) {
      try {
        roundNumber++;
        getAudioManager().stopAmbience();
        loadNextLevel();
      } catch (IOException e) {

      }
    }
  }

  /**
   * Gets the number of remaining consumables in the current PacmanGameState.
   *
   * @return The number of remaining consumables in this PacmanGameState.
   */
  protected int getRemainingConsumablesCount() {
    int count = 0;
    for (Sprite sprite : getSprites()) {
      if (sprite.mustBeConsumed()) {
        count++;
      }
    }
    return count;
  }

  /**
   * Gets the list of other Sprites that resides in the same list as this sprite.
   *
   * @param sprite The Sprite in question.
   * @return The list of other Sprites that resides in the same list as this sprite
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

  /**
   * This method adds a new Sprite to the PacmanGameState, registers the EventListeners, and then
   * notifies any observers.
   *
   * @param sprite The Sprite that is being added.
   */
  @Override
  public void addSprite(Sprite sprite) {
    sprites.add(sprite);
    registerEventListener(sprite);
    notifySpriteCreation(sprite);
  }

  /**
   * Returns the list of Sprites currently in this PacmanGameState.
   *
   * @return The List of Sprites currently in this PacmanGameState.
   */
  public List<Sprite> getSprites() {
    return sprites;
  }

  /**
   * Returns the PacmanGrid of this PacmanGameState.
   *
   * @return The PacmanGrid of this PacmanGameState.
   */
  @Override
  public PacmanGrid getGrid() {
    return grid;
  }

  /**
   * This method notifies each SpriteExistenceObserver in the event that a Sprite is destroyed.
   * Thus, ultimately, a call to this will remove the Sprite from the front-end one the notification
   * has been processed. This is the inverse of notifySpriteCreation.
   *
   * @param sprite The Sprite on which to act.
   */
  protected void notifySpriteDestruction(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteDestruction(sprite);
    }
  }

  /**
   * This method notifies each SpriteExistenceObserver in the event that a Sprite is created. Thus,
   * ultimately, a call to this will add the Sprite from to front-end one the notification has been
   * processed. This is the inverse of notifySpriteDestruction.
   *
   * @param sprite The Sprite on which to act.
   */
  protected void notifySpriteCreation(Sprite sprite) {
    for (SpriteExistenceObserver observer : spriteExistenceObservers) {
      observer.onSpriteCreation(sprite);
    }
  }

  /**
   * This method notifies each GridRebuildObserver in the event that the grid is modified. Thus,
   * ultimately, a call to this will update the grid on the front-end one the notification has been
   * processed.
   */
  protected void notifyGridRebuildObservers() {
    for (GridRebuildObserver observers : gridRebuildObservers) {
      observers.onGridRebuild(grid);
    }
  }

  /**
   * This method attaches a new GridRebuildObserver to this PacmanGameState, thus allowing the
   * front-end to be notified of any changes to the grid state.
   *
   * <p>Implemented as part of the GridRebuildObservable interface.
   *
   * @param observer The GridRebuildObserver to be added to this PacmanGameState's set of observers.
   */
  @Override
  public void addGridRebuildObserver(GridRebuildObserver observer) {
    gridRebuildObservers.add(observer);
  }

  /**
   * Register Sprite to be notified when powerup effects begin and end, allowing the Sprites to
   * change their behavior as required.
   *
   * @param listener Listener to add.
   */
  @Override
  public void registerEventListener(GameEventObserver listener) {
    pacmanPowerupObservers.add(listener);
  }

  /**
   * Broadcasts a GameEvent to all GameEventObservers. An example of this is the notification of
   * GameEventObservers after Pac-Man eats a PowerPill.
   *
   * @param type The specific GameEvent that is being broadcast.
   */
  @Override
  public void broadcastEvent(GameEvent type) {
    for (GameEventObserver observer : pacmanPowerupObservers) {
      observer.onGameEvent(type);
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

  /** This method decrements the number of lives the Pac-Man has remaining. */
  public void decrementPacmanLivesRemaining() {
    pacmanLivesRemaining--;
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

  /**
   * This method attaches an AudioObserver to this PacmanGameState. This is accomplished by querying
   * this PacmanGameState's AudioManager and attaching the observer directly to it.
   *
   * @param obs The AudioObserver to attach to this PacmanGameState.
   */
  public void addAudioObserver(AudioObserver obs) {
    getAudioManager().addObserver(obs);
  }

  /**
   * Returns this PacmanGameState's AudioManager.
   *
   * @return This PacmanGameState's AudioManager.
   */
  public AudioManager getAudioManager() {
    return audioManager;
  }

  /**
   * This method sets the state of gameOver to that of the passed in state.
   *
   * @param gameOver The new state of gameOver.
   */
  protected void setGameOver(boolean gameOver) {
    isGameOver = gameOver;
  }

  /**
   * Gets the Set of Sprites that are marked for deletion.
   *
   * @return The Set of Sprites that are marked for deletion.
   */
  protected Set<Sprite> getToDelete() {
    return toDelete;
  }

  /**
   * This method is called in the event of a Pac-Man Win, and will set gameOver to true, delete all
   * Sprites currently on screen, notify their observers and then spawn a PacmanWin Sprite in the
   * center of the screen.
   */
  protected void showPacmanWin() {
    setGameOver(true);
    getToDelete().addAll(getSprites());
    getSprites().forEach(this::notifySpriteDestruction);
    addSprite(
        new PacmanWin(
            new SpriteCoordinates(
                new Vec2(getGrid().getWidth() / 2.0, getGrid().getHeight() / 2.0)),
            new Vec2(1, 0)));
  }

  /**
   * This method is called in the event of a Ghost Win, and will set gameOver to true, delete all
   * Sprites currently on screen, notify their observers and then spawn a GhostWin Sprite in the
   * center of the screen.
   */
  protected void showGhostWin() {
    setGameOver(true);
    getToDelete().addAll(getSprites());
    getSprites().forEach(this::notifySpriteDestruction);
    addSprite(
        new GhostWin(
            new SpriteCoordinates(
                new Vec2(getGrid().getWidth() / 2.0, getGrid().getHeight() / 2.0)),
            new Vec2(1, 0)));
  }
}
