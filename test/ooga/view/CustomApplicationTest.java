package ooga.view;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import org.testfx.framework.junit5.ApplicationTest;

public class CustomApplicationTest extends ApplicationTest {

  protected void syncFXRun(Runnable r) throws InterruptedException {
    CountDownLatch cdl = new CountDownLatch(1);
    AtomicBoolean fail = new AtomicBoolean(false);
    Platform.runLater(
        () -> {
          try {
            r.run();
          } catch (Exception e) {
            e.printStackTrace();
          } catch (Error e) {
            e.printStackTrace();
            fail.set(true);
          }
          cdl.countDown();
        });
    cdl.await();
    if (fail.get()) {
      fail();
    }
  }
}
