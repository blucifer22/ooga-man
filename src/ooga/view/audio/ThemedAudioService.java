package ooga.view.audio;

import java.util.HashMap;
import java.util.HashSet;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ooga.view.exceptions.ExceptionService;
import ooga.view.exceptions.UIServicedException;
import ooga.view.theme.api.ThemeService;

public class ThemedAudioService implements AudioService {

  private final ThemeService dataSource;
  private final ExceptionService exceptionService;
  private final HashMap<String, HashSet<MediaPlayer>> activeAudio;
  private final HashMap<String, HashSet<MediaPlayer>> reusablePlayers;
  private boolean disabled;

  public ThemedAudioService(ThemeService dataSource, ExceptionService exceptionService) {
    this.dataSource = dataSource;
    this.exceptionService = exceptionService;
    this.activeAudio = new HashMap<>();
    this.reusablePlayers = new HashMap<>();
    this.disabled = false;
  }

  @Override
  public void playOnce(String soundIdentifier) {
    MediaPlayer spaPlayer = getMediaPlayerForSound(soundIdentifier);

    if (spaPlayer != null) {
      spaPlayer.setOnEndOfMedia(() -> {
        activeAudio.get(soundIdentifier).remove(spaPlayer);
        reusablePlayers.get(soundIdentifier).add(spaPlayer);
      });
      spaPlayer.play();
    }
  }

  @Override
  public void playIndefinitely(String soundIdentifier) {
    MediaPlayer infPlayer = getMediaPlayerForSound(soundIdentifier);

    if (infPlayer != null) {
      infPlayer.setOnEndOfMedia(() -> {
        infPlayer.seek(Duration.ZERO);
        infPlayer.play();
      });
      infPlayer.play();
    }
  }

  @Override
  public void pause(String soundIdentifier) {
    if (activeAudio.containsKey(soundIdentifier)) {
      for (MediaPlayer player : activeAudio.get(soundIdentifier)) {
        player.pause();
      }
    }
  }

  @Override
  public void pauseAll() {
    for (HashSet<MediaPlayer> soundPlayers : activeAudio.values()) {
      for (MediaPlayer player : soundPlayers) {
        player.pause();
      }
    }
  }

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
        exceptionService.handleWarning(new UIServicedException("audioBadOS", System.getProperty(
            "os.name"), soundIdentifier, singlePlayAudio.getSource(), dataSource.getTheme()
            .getName()));
        disabled = true;
      }
    }
    return null;
  }
}
