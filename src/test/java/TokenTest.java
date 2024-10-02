import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TokenTest {

    private String urlToTest = "https://playground.learnqa.ru/ajax/api/longtime_job";
    private String errorMessageExpected = "No job linked to this token";
    private String errorAssertMessageIfFail = "Error message is not correct";



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
        softAssert.assertEquals(responseJsonWithoutTask.get("error"), errorMessageExpected, errorAssertMessageIfFail);

        softAssert.assertAll();
    }



}
