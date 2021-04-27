package ooga.view.audio;

import java.util.HashMap;
import java.util.HashSet;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.UIServicedException;
import ooga.view.theme.api.ThemeService;

/**
 * Concrete implementation of {@link AudioService}. Uses a {@link ThemeService} to retrieve audio
 * files (and thus supports themed audio), then loads them into managed {@link MediaPlayer}s.
 * Calling classes interact with neither {@link Media} nor {@link MediaPlayer} instances directly.
 *
 * Automatically self-disabling on some Linux systems, where MPEG-3 audio codecs are often not
 * pre-installed. A message is piped to the {@link ExceptionService} provided in the constructor
 * in such cases.
 *
 * @author David Coffman
 */
public class ThemedAudioService implements AudioService {

  private final ThemeService dataSource;
  private final ExceptionService exceptionService;
  private final HashMap<String, HashSet<MediaPlayer>> activeAudio;
  private final HashMap<String, HashSet<MediaPlayer>> reusablePlayers;
  private boolean disabled;

  /**
   * Sole {@link ThemedAudioService} constructor.
   *
   * @param dataSource the {@link ThemeService} to query for {@link Media}
   * @param exceptionService the {@link ExceptionService} to which errors should be directed
   */
  public ThemedAudioService(ThemeService dataSource, ExceptionService exceptionService) {
    this.dataSource = dataSource;
    this.exceptionService = exceptionService;
    this.activeAudio = new HashMap<>();
    this.reusablePlayers = new HashMap<>();
    this.disabled = false;
  }

  /**
   * Plays a sound with the parameter identifier exactly one time.
   *
   * @param soundIdentifier the identifier of the sound to play
   */
  @Override
  public void playOnce(String soundIdentifier) {
    MediaPlayer spaPlayer = getMediaPlayerForSound(soundIdentifier);

    if (spaPlayer != null) {
      spaPlayer.setOnEndOfMedia(
          () -> {
            activeAudio.get(soundIdentifier).remove(spaPlayer);
            reusablePlayers.get(soundIdentifier).add(spaPlayer);
          });
      spaPlayer.play();
    }
  }

  /**
   * Plays a sound with the parameter identifier until stopped. The sound repeats automatically
   * when finished.
   *
   * @param soundIdentifier the identifier of the sound to play
   */
  @Override
  public void playIndefinitely(String soundIdentifier) {
    MediaPlayer infPlayer = getMediaPlayerForSound(soundIdentifier);

    if (infPlayer != null) {
      infPlayer.setOnEndOfMedia(
          () -> {
            infPlayer.seek(Duration.ZERO);
            infPlayer.play();
          });
      infPlayer.play();
    }
  }

  /**
   * Pauses all running sounds with the parameter identifier. Sounds playing indefinitely will
   * pause, but will resume playing indefinitely once resumed.
   *
   * @param soundIdentifier the identifier of the sound to stop
   */
  @Override
  public void pause(String soundIdentifier) {
    if (activeAudio.containsKey(soundIdentifier)) {
      for (MediaPlayer player : activeAudio.get(soundIdentifier)) {
        player.pause();
      }
    }
  }

  /**
   * Pauses all running sounds. Sounds playing indefinitely will pause, but will resume playing
   * indefinitely once resumed.
   */
  @Override
  public void pauseAll() {
    for (HashSet<MediaPlayer> soundPlayers : activeAudio.values()) {
      for (MediaPlayer player : soundPlayers) {
        player.pause();
      }
    }
  }

  /**
   * Stops all running sounds with the parameter identifier. Sounds playing indefinitely will
   * stop.
   *
   * @param soundIdentifier the identifier of the sound to stop
   */
  @Override
  public void stop(String soundIdentifier) {
    if (activeAudio.containsKey(soundIdentifier)) {
      for (MediaPlayer player : activeAudio.get(soundIdentifier)) {
        player.stop();
      }
      reusablePlayers.put(soundIdentifier, activeAudio.get(soundIdentifier));
      activeAudio.put(soundIdentifier, new HashSet<>());
    }
  }

  /**
   * Stops all running sounds. Sounds playing indefinitely will stop.
   */
  @Override
  public void stopAll() {
    for (String soundIdentifier : activeAudio.keySet()) {
      HashSet<MediaPlayer> soundPlayers = activeAudio.get(soundIdentifier);
      for (MediaPlayer player : soundPlayers) {
        player.pause();
      }
      reusablePlayers.put(soundIdentifier, activeAudio.get(soundIdentifier));
      activeAudio.put(soundIdentifier, new HashSet<>());
    }
  }

  // Constructs, or retrieves from a cache, a media player with audio matching the requested
  // identifier loaded and ready to play.
  private MediaPlayer getMediaPlayerForSound(String soundIdentifier) {
    if (disabled) {
      return null;
    }

    activeAudio.putIfAbsent(soundIdentifier, new HashSet<>());
    reusablePlayers.putIfAbsent(soundIdentifier, new HashSet<>());

    if (reusablePlayers.get(soundIdentifier).size() > 0) {
      for (MediaPlayer audioPlayer : reusablePlayers.get(soundIdentifier)) {
        reusablePlayers.get(soundIdentifier).remove(audioPlayer);
        activeAudio.get(soundIdentifier).add(audioPlayer);
        audioPlayer.seek(Duration.ZERO);
        return audioPlayer;
      }
    }

    Media singlePlayAudio = dataSource.getTheme().getSoundByIdentifier(soundIdentifier);

    if (singlePlayAudio != null) {
      try {
        MediaPlayer mediaPlayer = new MediaPlayer(singlePlayAudio);
        activeAudio.get(soundIdentifier).add(mediaPlayer);
        return mediaPlayer;
      } catch (Exception e) {
        // missing audio drivers on some operating systems lead to MediaPlayer instantiation failure
        exceptionService.handleWarning(
            new UIServicedException(
                "audioBadOS",
                System.getProperty("os.name"),
                soundIdentifier,
                singlePlayAudio.getSource(),
                dataSource.getTheme().getName()));
        disabled = true;
      }
    }
    return null;
  }
}
