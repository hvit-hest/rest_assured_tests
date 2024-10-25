package dataprovider;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;


public class ProvideStringData {

    @DataProvider(name = "provideString")
    public String[] stringProvider() {
        return new String[] { null, "", RandomStringUtils.randomAlphanumeric(1,15),
                RandomStringUtils.randomAlphanumeric(15,256), RandomStringUtils.randomAlphanumeric(256,10000)};
    }
}