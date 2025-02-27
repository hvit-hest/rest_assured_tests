package tests;

import datamodel.UserRegisterDataModel;
import dataprovider.DataProviders;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Feature("Create user cases")
public class UserRegisterTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test(testName = "Negative. Create user with existing email")
    @Description("Attempt to create user with existing email")
    public void   testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<String, String>() {{
            put("email", email);
        }};

        userData = DataGenerator.getRegistrationData(userData);
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth,
                String.format("Users with email '%s' already exists", email));
    }

    @Test(testName = "Positive. Create user successfully")
    @Description("Attempt to create user with correct data provided")
    public void testCreateUserSuccessfully() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test(testName = "Negative. Try to create user.", dataProvider = "provideUserRegisterNegativeData", dataProviderClass = DataProviders.class)
    public void  userRegisterNegativeTest(UserRegisterDataModel testData) {
        Allure.description(testData.getTestDescription());
        Response response = apiCoreRequests.requestGenerator(testData, testData.getRequestDescription());
        Assertions.assertResponse(response, testData);
    }

    @Test(testName = "Positive. Try to create user.", dataProvider = "provideUserRegisterPositiveData", dataProviderClass = DataProviders.class)
    public void  userRegisterPositiveTest(UserRegisterDataModel testData) {
        Allure.description(testData.getTestDescription());
        Response response = apiCoreRequests.requestGenerator(testData, testData.getRequestDescription());
        Assertions.assertResponse(response, testData);
    }
}