package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertTrue;

//@Epic("Authorization cases")
//@Feature("Authorization")
public class UserAuthTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String baseUriAuth = "https://playground.learnqa.ru/api/user/auth";
    private String cookie;
    private String header;
    private int userIdOnAuth;

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

    @Test(testName = "Test positive auth user")
//    @Description("This test successfully authorizes an user by email and password")
    public void testAuthUser() {
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(baseUriAuth, header, cookie);
        assertTrue (userIdOnAuth != 0, "user_id is 0. User is not authorized");
        Assertions.assertJsonByName(responseCheckAuth, "user_id", userIdOnAuth);
    }

    @Test(testName = "Test negative auth user", dataProvider = "cookiesOrHeadersOnly")
//    @Description("This test checks authorization status w/o sending cookie or token")
    public void testNegativeAuthUser(String condition) {
/*        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");
        if (condition.equals("cookie")) {
            spec.cookie("auth_sid", cookie);
        } else if (condition.equals("headers")) {
            spec.header("x-csrf-token", header);
        } else {
            throw new IllegalArgumentException("Condition value is known: " + condition);
        }
        Response responseForCheck = spec.get().andReturn();*/

        Response responseForCheck;

        switch (condition) {
            case "cookie":
                responseForCheck = apiCoreRequests.makeGetRequestWithCookie(baseUriAuth, cookie);
                break;
            case "headers":
                responseForCheck = apiCoreRequests.makeGetRequestWithToken(baseUriAuth, header);
                break;
            default:
                throw new IllegalArgumentException("Condition value is known: " + condition);
        }
        Assertions.assertJsonByName(responseForCheck, "user_id", 0);
    }

    @DataProvider(name = "cookiesOrHeadersOnly")
    public String[] getLoginRequestType() {
        return new String[]{"cookie", "headers"};
    }
}