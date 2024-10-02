import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class LongRedirectTest {
    private String urlToTest = "https://playground.learnqa.ru/api/long_redirect";
    private final String lastLocationHeaderExpected = "https://www.learnqa.ru/";
    private final String messageIfFail = "\n Location Header is not correct. \n Actual: %s. \n Expected: %s";

    @Test
    public void longRedirectTest() {
        Response response = null;
        int counter = 0;
        int responseCodeActual = -1;
        int redirectMax = 10;
        int responseCodeToWait = 200;
        String lastLocationHeaderActual = null;

        do {
            counter++;
            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(urlToTest)
                    .andReturn();

            responseCodeActual = response.getStatusCode();
            urlToTest = response.getHeader("location");
            if (urlToTest == null) break;
            lastLocationHeaderActual = urlToTest;
            System.out.println(lastLocationHeaderActual);

        }
        while (responseCodeActual != responseCodeToWait && counter < redirectMax);

        //assert since it has to be a test
        assertEquals(lastLocationHeaderActual, lastLocationHeaderExpected,
                String.format(messageIfFail, lastLocationHeaderActual, lastLocationHeaderExpected));
    }
}
