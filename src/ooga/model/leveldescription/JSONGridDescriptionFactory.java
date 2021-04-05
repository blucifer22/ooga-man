package ooga.model.leveldescription;

import com.fasterxml.jackson.databind.ObjectMapper;
import ooga.model.leveldescription.GridDescription;

import java.io.File;
import java.io.IOException;

/**
 * JSONGridDescriptionFactory is a very basic "factory" class that allows for new GridDescriptions
 * to be easily deserialized from JSON and instantiated using Jackson.
 *
 * @author Marc Chmielewski
 */
public class JSONGridDescriptionFactory {

  ObjectMapper mapper;

  /**
   * A basic constructor for the JSONGridDescriptionFactory that instantiates the ObjectMapper.
   */
  public JSONGridDescriptionFactory() {
    mapper = new ObjectMapper();
  }

  /**
   * This method instantiates a new GridDescription from a serialized JSON file.
   *
   * @param filepath The filepath of the JSON that represents the GridDescription
   * @return A freshly-minted GridDescription that has been deserialized from the provided JSON.
   * @throws IOException If the filepath is invalid.
   */
  public GridDescription getGridDescriptionFromJSON(String filepath) throws IOException{
    return mapper.readValue(new File(filepath), GridDescription.class);
  }
}
