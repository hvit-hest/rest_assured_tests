import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TokenTest {

    private String urlToTest = "https://playground.learnqa.ru/ajax/api/longtime_job";
    private String errorMessageExpected = "No job linked to this token";
    private String errorAssertMessageIfFail = "Error message is not correct";
    private String statusNotReadyMessageExpected = "Job is NOT ready";
    private String statusAssertMessageIfFail = "Status is not correct";
    private String statusReadyMessageExpected = "Job is ready";
    private String resultAssertMessageIfFail = "Result is absent";

    @Test
    public void tokenTest() {

        SoftAssert softAssert = new SoftAssert();

        //Check 'error' without task. Just in case.
        JsonPath responseJsonWithoutTask = RestAssured
                .given()
                .queryParam("token", "Yo ho ho and a bottle of rum")
                .when()
                .get(urlToTest)
                .jsonPath();

        //print the json to study it
        responseJsonWithoutTask.prettyPrint();
        //assert since it has to be a test
        softAssert.assertEquals(responseJsonWithoutTask.get("error"),
                errorMessageExpected, errorAssertMessageIfFail);

        //create a task
        JsonPath responseJsonCreateTask = RestAssured.get(urlToTest).jsonPath();

        //print json response to study it
        responseJsonCreateTask.prettyPrint();

        int secondsToWait = responseJsonCreateTask.get("seconds");
        String token = responseJsonCreateTask.get("token");
        System.out.println(String.format("Seconds to wait '%s' and token to use '%s'", secondsToWait, token));

        //when task is not ready check the 'status' is correct
        JsonPath responseJsonTaskIsNotReady = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get(urlToTest)
                .jsonPath();

        //print the json to study it
        responseJsonTaskIsNotReady.prettyPrint();
        //assert since it has to be a test
        softAssert.assertEquals(responseJsonTaskIsNotReady.get("status"),
                statusNotReadyMessageExpected, statusAssertMessageIfFail);

        try {
            Thread.sleep(secondsToWait * 1000 + 500);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        JsonPath responseJsonTaskIsReady = RestAssured
                .given()
                .queryParam("token", token)
                .get(urlToTest)
                .andReturn()
                .jsonPath();

        //print the json to study it
        responseJsonTaskIsReady.prettyPrint();
        //asserts since it has to be a test
        softAssert.assertEquals(responseJsonTaskIsReady.getString("status"),
                statusReadyMessageExpected, statusAssertMessageIfFail);
        softAssert.assertNotNull(responseJsonTaskIsReady.getString("result"), resultAssertMessageIfFail);
        softAssert.assertNotEquals(responseJsonTaskIsReady.getString("result").trim(),
                "", resultAssertMessageIfFail);
        softAssert.assertAll();
    }
}