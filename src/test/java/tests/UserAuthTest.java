package tests;

import datamodel.User;
import datamodel.UserRegisterDataModel;
import dataprovider.DataProviders;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

//@Epic("Authorization cases")
//@Feature("Authorization")
public class UserAuthTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String baseUriAuth = "https://playground.learnqa.ru/api/user/auth";
    private String cookie;
    private String header;
    private int userIdOnAuth;
    User user2 = new User("2");

    //@BeforeMethod
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        cookie = getCookie(responseGetAuth, "auth_sid");
        header = getHeader(responseGetAuth, "x-csrf-token");
        userIdOnAuth = getIntFromJson(responseGetAuth, "user_id");
    }

    @Test(testName = "Positive. Auth user")
//    @Description("This test successfully authorizes a user by email and password")
    public void testAuthUser() {
        apiCoreRequests.loginUser(user2);
        apiCoreRequests.getUser(user2);

        int expectedUserId = Integer.parseInt(user2.getUserID());
        int actualUserId = user2.getLoginResponse().jsonPath().getInt("user_id");

        user2.getResponse("getResponse");
        Response responseCheckAuth = user2.getResponse("getResponse");
        assertTrue(actualUserId != 0, "user_id is 0. User is not authorized");
        assertTrue(actualUserId == expectedUserId, "Expected and Actual user_id is not the same");
        Assertions.assertJsonByKeyNameAndValue(user2.getResponse("getResponse"), "id", expectedUserId);
    }

    @Test(testName = "Negative. Auth user", dataProvider = "cookiesOrHeadersOnly")
//    @Description("This test checks authorization status w/o sending cookie or token")
    public void testNegativeAuthUser(String condition) {
        apiCoreRequests.loginUser(user2);
        Response loginResponse = user2.getLoginResponse();
        cookie = getCookie(loginResponse, "auth_sid");
        header = getHeader(loginResponse, "x-csrf-token");

        Response responseForCheck;

        switch (condition) {
            case "cookie only":
                responseForCheck = apiCoreRequests.makeGetRequestWithCookie(baseUriAuth, cookie);
                break;
            case "headers only":
                responseForCheck = apiCoreRequests.makeGetRequestWithToken(baseUriAuth, header);
                break;
            default:
                throw new IllegalArgumentException("Condition value is known: " + condition);
        }
        Assertions.assertJsonByKeyNameAndValue(responseForCheck, "user_id", 0);
    }

    @Test(testName = "Positive. Auth user. JSON", dataProvider = "provideUserLoginPositiveData", dataProviderClass = DataProviders.class)
//    @Description("This test successfully authorizes a user by email and password")
    public void testAuthUserAlt(UserRegisterDataModel testData) {

        User user = apiCoreRequests.createUser();
        Map<String, String> userData = user.getUserData();
        user.setUserID();

        testData.setUserData(userData);
        Response response = apiCoreRequests.requestGenerator(testData, testData.getRequestDescription());
        Assertions.assertResponse(response, testData);
    }

    @DataProvider(name = "cookiesOrHeadersOnly")
    public String[] getLoginRequestType() {
        return new String[]{"cookie only", "headers only"};
    }
}