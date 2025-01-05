package lib;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class DataReaders {

    public static List<?> readJson (String jsonFile, Type type) {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            //InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream (jsonFile);
            InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().
                    getResourceAsStream(jsonFile);
            reader = new JsonReader(new InputStreamReader(resourceAsStream));
        } catch (NullPointerException ioe) {
            System.err.println("Unable to read json file " + jsonFile);
        }
        return gson.fromJson(reader, type);
    }
}
