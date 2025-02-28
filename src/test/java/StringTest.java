import dataprovider.ProvideStringData;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


public class StringTest {
    String assertMessage = "The string is '%s'. Length of the string has to be more than 15 symbols";

    @Test(testName = "Short string test", dataProvider = "provideString", dataProviderClass = ProvideStringData.class)
    @Description("Test for string's length")
    public void stringTest(String stringToTest) {

        assertTrue(stringToTest != null && stringToTest != "" && stringToTest.length() > 15,
                String.format(assertMessage, stringToTest));
    }
}
