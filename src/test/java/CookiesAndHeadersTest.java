import datamodel.CookiesAndHeadersDataModel;
import dataprovider.CookiesHeadersProvider;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static lib.Assertions.assertCookieSSSByNameAndValue;
import static lib.Assertions.assertHeaderSSSByNameAndValue;

public class CookiesAndHeadersTest {
    private Response responseToTest;

    @Test(testName = "Cookies and Headers", dataProvider = "provideCookiesAndHeadersData", dataProviderClass = CookiesHeadersProvider.class)
    @Description("Check cookies and headers expected")
    public void cookiesAndHeadersTest(CookiesAndHeadersDataModel testData) {
        RequestSpecification spec = RestAssured.given();
        spec.baseUri(testData.getTestUrl());
        switch (testData.getMethod()) {
            case "get":
                responseToTest = spec.get();
                break;
            case "post":
                responseToTest = spec.post();
                break;
            default:
                throw new IllegalArgumentException(String.format("Method '%s' is not implemented yet", testData.getMethod()));
        }

        switch (testData.getWhatToTest()) {
            case "cookies":
                assertCookieSSSByNameAndValue(responseToTest, testData.getCookies());
                break;
            case "headers":
                assertHeaderSSSByNameAndValue(responseToTest, testData.getHeaders());
                break;
            default:
                throw new IllegalArgumentException(String.format("'%s' is not implemented yet", testData.getWhatToTest()));
        }
    }
}