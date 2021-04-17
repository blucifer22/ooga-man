package ooga.model;

/**
 * This implementation of Pac-Man accommodates versus mode, allowing more than one player to play at
 * a time.
 *
 * @author George Hong
 */
public class Player implements ImmutablePlayer {

  private final InputSource inputSource;
  private final int id;
  private int score;
  private int roundWins;

  public Player(int id, InputSource inputSource) {
    this(id, 0, 0, inputSource);
  }

  /**
   * Creates an instance of Player used to keep score for all variants of Pac-Man.
   *
   * @param id          identification number for this player.
   * @param score       total score attained by this player while playing Pac-Man
   * @param roundWins   keeps track of the number of rounds won by this player, useful in best-of
   *                    approaches to determining overall winner.
   * @param inputSource Keybindings used by this player to control their Sprites.
   */
  public Player(int id, int score, int roundWins, InputSource inputSource) {
    this.id = id;
    this.score = score;
    this.roundWins = roundWins;
    this.inputSource = inputSource;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int getRoundWins() {
    return roundWins;
  }

  public void setRoundWins(int roundWins) {
    this.roundWins = roundWins;
  }

  public int getID() {
    return id;
  }

  /**
   * Gets the input to control the Sprites.  Used for swapping control of Ghosts and Pac-Man between
   * rounds.
   *
   * @return input source used by this Player, for example, (WASD) or (IJKL) for movement.
   */
  public InputSource getInputSource() {
    return inputSource;
  }
}
