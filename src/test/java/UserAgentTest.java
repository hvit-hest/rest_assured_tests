import datamodel.UserAgentDataModel;
import dataprovider.UserAgentProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static lib.Assertions.mapContains;

public class UserAgentTest {

    private Response responseForCheck;

    @Test(dataProvider = "userAgentData", dataProviderClass = UserAgentProvider.class)
    public void serAgentTest(UserAgentDataModel testData) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri(testData.getTestUrl());
        spec.headers(testData.getHeaders());
        switch (testData.getMethod()) {
            case "get":
                //we have 'get' for the test only
                responseForCheck = spec.get();
                break;
            case "post":
                //we have 'get' for the test only but just in case
                responseForCheck = spec.post();
                break;
            default:
                throw new IllegalArgumentException(String.format("Method '%s' is not implemented yet", testData.getMethod()));
        }
        responseForCheck.prettyPrint();
        mapContains(responseForCheck.jsonPath().getMap("$"), testData.getExpectedValues(), false);
    }
}