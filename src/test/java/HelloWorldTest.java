import io.qameta.allure.Description;
import org.testng.annotations.Test;

public class HelloWorldTest {

    private String helloString = "Hello from %s";
    private String incognitoName = "Alexander Sh";

    @Test(testName = "Hello word exercise")
    @Description("Just 'Hello word'")
    public void testHelloWorld() {
        System.out.println(String.format(helloString, incognitoName));
    }
}