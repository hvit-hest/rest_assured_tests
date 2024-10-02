import org.testng.annotations.Test;

public class HelloWorldTest {

    private String helloString = "Hello from %s";
    private String incognitoName = "Alexander Sh";

    @Test
    public void testHelloWorld() {
        System.out.println(String.format(helloString, incognitoName));
    }
}