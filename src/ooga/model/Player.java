package ooga.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

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

  /**
   * This is a reduced form constructor for Player that just takes in an id and an InputSource but
   * starts with a score of 0 and no round wins. This is intended to be used for the first round of
   * a Pac-Man Game.
   *
   * @param id identification number for this player.
   * @param inputSource Keybindings used by this player to control their Sprites.
   */
  @JsonCreator
  public Player(@JsonProperty("id") int id, @JsonProperty("inputSource") InputSource inputSource) {
    this(id, 0, 0, inputSource);
  }

  /**
   * Creates an instance of Player used to keep score for all variants of Pac-Man.
   *
   * @param id identification number for this player.
   * @param score total score attained by this player while playing Pac-Man
   * @param roundWins keeps track of the number of rounds won by this player, useful in best-of
   *     approaches to determining overall winner.
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

  @JsonGetter
  public int getID() {
    return id;
  }

  /**
   * Gets the input to control the Sprites. Used for swapping control of Ghosts and Pac-Man between
   * rounds.
   *
   * @return input source used by this Player, for example, (WASD) or (IJKL) for movement.
   */
  @JsonGetter
  public InputSource getInputSource() {
    return inputSource;
  }
}
