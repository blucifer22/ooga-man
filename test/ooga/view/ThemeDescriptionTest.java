package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import ooga.view.theme.serialized.CostumeDescription;
import ooga.view.theme.serialized.ThemeDescription;
import org.junit.jupiter.api.Test;

public class ThemeDescriptionTest {
  @Test
  public void testCostumeDescription() throws IOException {
    CostumeDescription desc = new CostumeDescription("TRANSPARENT", false, 1.2, true, true);
    desc.toJSON("data/test.json");
    CostumeDescription cd2 = (new ObjectMapper()).readValue(new File("data/test.json"),
        CostumeDescription.class);

    assertEquals(desc.getFill(), cd2.getFill());
    assertEquals(desc.getScale(), cd2.getScale());
    assertEquals(desc.isBottomHeavy(), cd2.isBottomHeavy());
    assertEquals(desc.isImage(), cd2.isImage());
  }

  @Test
  public void testThemeDescription() throws IOException {
    ThemeDescription td = new ThemeDescription();
    td.toJSON("data/test.json");
    ThemeDescription td2 = (new ObjectMapper()).readValue(new File("data/test.json"),
        ThemeDescription.class);
  }
}
