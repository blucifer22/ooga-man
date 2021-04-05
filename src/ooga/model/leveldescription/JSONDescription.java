package ooga.model.leveldescription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class JSONDescription {
    /**
     * Writes this GridDescription to a JSON file at the indicated filepath.
     *
     * @param filepath The filepath at which to write the JSON.
     * @throws IOException If the provided filepath is invalid.
     */
    public void toJSON(String filepath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(new File(filepath), this);
    }
}
