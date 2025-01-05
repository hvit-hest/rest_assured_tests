package tests;

import datamodel.User;
import datamodel.UserRegisterDataModel;
import dataprovider.DataProviders;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.testng.annotations.Test;

//@Feature("Delete tests")
public class UserDeleteTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test(testName = "Try to delete user", dataProvider = "provideUserDeleteRequestData",
            dataProviderClass = DataProviders.class)
    public void deleteUserTest(UserRegisterDataModel testData) {
        Allure.description(testData.getTestDescription());
        User user1;
        User user2;

        switch (testData.getTestDescription()) {
            case "User with id 2 can not be deleted":
                user1 = new User("2");
                apiCoreRequests.loginUser(user1);
                apiCoreRequests.deleteUser(user1);
                //USE TEST'S' JSON TO ASSERT
                Assertions.assertResponse(user1.getResponse("deleteResponse"), testData);
                //OR DIRECTLY. Also works just fine.
            /*Assertions.assertResponseCodeEquals(user1.getResponse("deleteResponse"), 400);
            Assertions.assertResponseTextEquals(user1.getResponse("deleteResponse"), "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");*/

                //check user1 data
                apiCoreRequests.getUser(user1);
                user1.getUserData().put("id", user1.getUserID());
                user1.getUserData().remove("password");
                Assertions.assertResponseCodeEquals(user1.getResponse("getResponse"), 200);
                Assertions.mapContains(user1.getResponse("getResponse"), "$",
                        user1.getUserData(), true);
                break;
            case "User delete himself and can not login":
                user1 = apiCoreRequests.createUser();
                user1.setUserID();
                apiCoreRequests.loginUser(user1);
                apiCoreRequests.deleteUser(user1);
                //USE TEST'S' JSON TO ASSERT
                Assertions.assertResponse(user1.getResponse("deleteResponse"), testData);
                //OR DIRECTLY. Also works just fine.
                /*Assertions.assertResponseCodeEquals(user1.getResponse("deleteResponse"), 200);
                Assertions.assertResponseTextEquals(user1.getResponse("deleteResponse"),"");*/
                //check that deleted user1 can not login
                apiCoreRequests.loginUser(user1);
                Assertions.assertResponseCodeEquals(user1.getResponse("loginResponse"), 400);
                Assertions.assertResponseTextEquals(user1
                        .getResponse("loginResponse"), "Invalid username/password supplied");
                break;
            case "User can not be deleted by another user":
                user1 = apiCoreRequests.createUser();
                user1.setUserID();
                user2 = apiCoreRequests.createUser();
                apiCoreRequests.loginUser(user2);
                apiCoreRequests.deleteUser1ByUser2(user1, user2);
                //USE TEST'S' JSON TO ASSERT
                //In reality Response Code is 200. And no any message. Bug or feature? Assume it's feature
                Assertions.assertResponse(user2.getResponse("deleteResponse"), testData);
                //OR DIRECTLY. Also works just fine.
                /*Assertions.assertResponseCodeEquals(user2.getResponse("deleteResponse"), 200);
                Assertions.assertResponseTextEquals(user2.getResponse("deleteResponse"),"");*/
                apiCoreRequests.loginUser(user1);
                Assertions.assertResponseCodeEquals(user1.getResponse("loginResponse"),200);
                Assertions.assertResponseTextEquals(user1
                        .getResponse("loginResponse"), String.format("{\"user_id\":%s}", user1.getUserID()));
                break;
            default:
                throw new IllegalArgumentException(String.format("Test '%s' is not developed yet", testData.getTestDescription()));
        }
    }
}