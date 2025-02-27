package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Test user API")
@Feature("Edit tests")
public class UserEditTest extends BaseTestCase {
    @Test
    @Description("Generate user, Login user, Edit user's name")
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<String, String>() {{
            put("email", userData.get("email"));
            put("password", userData.get("password"));
        }};

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<String, String>(){{
            put("firstName", newName);
        }};
        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();
        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", getHeader(responseGetAuth,"x-csrf-token" ))
                .cookie("auth_sid", getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.assertJsonByKeyNameAndValue(responseUserData, "firstName", newName);
    }
}