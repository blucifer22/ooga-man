package ooga.model;

/**
 * This implementation of Pac-Man accommodates versus mode, allowing more than one player to play at
 * a time.
 *
 * @author George Hong
 */
public class Player {

  private final InputSource inputSource;
  private final int id;
  private int score;
  private int roundWins;

  public Player(int id, InputSource inputSource) {
    this(id, 0, 0, inputSource);
  }

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

  public int getId() {
    return id;
  }

  public InputSource getInputSource() {
    return inputSource;
  }
}
