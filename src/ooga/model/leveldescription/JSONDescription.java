package ooga.model.leveldescription;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Superclass of objects that can be serialized to JSON files.
 *
 * This superclass contains one method -- toJSON(), which serializes
 * an object to a specified JSON file.
 *
 * It is intentionally made abstract to prevent initializing a bare
 * JSONDescription, which would be useless and confusing.
 *
 * @author Franklin Wei
 */
public abstract class JSONDescription {
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
