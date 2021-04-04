package ooga.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class GridDescriptionFactory {
  ObjectMapper mapper;

  public GridDescriptionFactory() {
    mapper = new ObjectMapper();
  }

  public GridDescription getGridDescriptionFromJSON(String filepath) throws IOException{
    return mapper.readValue(new File(filepath), GridDescription.class);
  }
}
