package ooga.view;

import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import org.testfx.framework.junit5.ApplicationTest;

public class CustomApplicationTest extends ApplicationTest {


  protected void syncFXRun(Runnable r) throws InterruptedException {
    CountDownLatch cdl = new CountDownLatch(1);
    Platform.runLater(() -> {
      try {
        r.run();
      } catch (Exception e) {
        e.printStackTrace();
      }
      cdl.countDown();
    });
    cdl.await();
  }
}
