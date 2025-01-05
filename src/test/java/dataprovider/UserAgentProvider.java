package dataprovider;

import com.google.gson.reflect.TypeToken;
import datamodel.UserAgentDataModel;
import org.testng.annotations.DataProvider;
import java.lang.reflect.Type;
import java.util.List;

import static lib.DataReaders.readJson;

public class UserAgentProvider {

    private static final String jsonArrayFile = "UserAgentData.json";
    private final Type type = new TypeToken<List<UserAgentDataModel>>() {
    }.getType();

    @DataProvider(name = "userAgentData")
    public Object[] getData() {
        return readJson(jsonArrayFile, type).toArray();
    }
}