package ooga.view;

import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import org.testfx.framework.junit5.ApplicationTest;

public class CustomApplicationTest extends ApplicationTest {


  protected void syncFXRun(Runnable r) throws InterruptedException {
    CountDownLatch cdl = new CountDownLatch(1);
    Platform.runLater(() -> {
      r.run();
      cdl.countDown();
    });
    cdl.await();
  }
}
