import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RedirectTest {

    private final String urlToTest = "https://playground.learnqa.ru/api/long_redirect";
    private final String firstLocationHeaderExpected = "https://playground.learnqa.ru/";
    private final String messageIfFail = "\n URL for redirection is not correct. \n Actual: %s. \n Expected: %s";

    @Test
    public void redirectTest() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get(urlToTest);

        //print the  response to study it just for fun (not big though one)
        response.print();
        //it was asked to print a text of the second message only
        String firstLocationHeaderActual = response.getHeader("location");
        System.out.println(firstLocationHeaderActual);
        //assert since it has to be a test
        assertEquals(firstLocationHeaderActual, firstLocationHeaderExpected,
                String.format(messageIfFail, firstLocationHeaderActual, firstLocationHeaderExpected));
    }
}