import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class ParsingJsonTest {

    private final String urlToTest = "https://playground.learnqa.ru/api/get_json_homework";
    private final String textOfTheSecondMessageExpected = "And this is a second message";
    private final String messageIfFail = "\n Content is not correct. \n Actual: %s. \n Expected: %s";

    @Test
    public void parsingJsonTest() {
        JsonPath responseJson = RestAssured.get(urlToTest).jsonPath();
        //print the json response to study it just for fun though the fun is not so big
        responseJson.prettyPrint();
        //it was asked to print a text of the second message only
        String textOfTheSecondMessageActual = responseJson.get("messages[1].message");
        System.out.println(textOfTheSecondMessageActual);
        //assert since it has to be a test
        assertEquals(textOfTheSecondMessageActual, textOfTheSecondMessageExpected,
                String.format(messageIfFail, textOfTheSecondMessageActual, textOfTheSecondMessageExpected));
    }
}
