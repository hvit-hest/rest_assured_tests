package lib;

import datamodel.UserRegisterDataModel;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertEquals;


public class Assertions {

    public static void assertHeaderSSSByNameAndValue(Response response, Map<String, String> headersExpected) {
        Headers headersActual = response.getHeaders();
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(headersActual.size(), headersExpected.size(),
                String.format("Expected number of headers: '%s' Actual number: '%s'",
                        headersExpected.size(), headersActual.size()));

        headersExpected.entrySet().forEach(header -> {
            String headerNameExpected = header.getKey();
            String headerValueExpected = header.getValue();

            softAssert.assertTrue(headersActual.hasHeaderWithName(headerNameExpected),
                    String.format("Response doesn't have '%s' header", headerNameExpected));

            if (headersActual.hasHeaderWithName(headerNameExpected)) {
                String headerValueActual = headersActual.get(headerNameExpected).getValue();

                if (headerNameExpected.equals("Date") || headerNameExpected.equals("Expires")) {
                    headerValueActual = truncateSeconds(headersActual.get(headerNameExpected).getValue());
                    headerValueExpected = truncateSeconds(headerValueExpected);
                }

                softAssert.assertEquals(headerValueActual, headerValueExpected,
                        String.format("Header's '%s' value expected '%s' actual is '%s'",
                                headerNameExpected, headerValueExpected, headerValueActual));
            }
        });

        softAssert.assertAll();
    }

    public static void assertCookieSSSByNameAndValue(Response response, Map<String, String> cookiesExpected) {
        Map<String, String> cookiesActual = response.getCookies();
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(cookiesActual.size(), cookiesExpected.size(),
                String.format("Expected number of cookies: '%s' Actual number: '%s'",
                        cookiesExpected.size(), cookiesActual.size()));


        cookiesActual.entrySet().stream().forEach(cookie -> {
            String cookieNameExpected = cookie.getKey();
            String cookieValueExpected = cookie.getValue();

            softAssert.assertTrue(cookiesActual.containsKey(cookieNameExpected),
                    String.format("Response doesn't have '%s' cookie", cookieNameExpected));

            if (cookiesActual.containsKey(cookieNameExpected))
                softAssert.assertEquals(cookiesActual.get(cookieNameExpected), cookieValueExpected,
                        String.format("'%s' cookie has wrong value '%s'",
                                cookieNameExpected, cookiesActual.get(cookieNameExpected)));

        });
        softAssert.assertAll();
    }

    private static String truncateSeconds(String dateToTruncate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
        return ZonedDateTime.from(formatter.parse(dateToTruncate)).truncatedTo(ChronoUnit.MINUTES).toString();
    }

    //Simple, one cookie assertion -just template for future
    public static void assertCookieByNameAndValue(Response response, String cookieNameExpected, String cookieValueExpected) {

        Map<String, String> cookiesActual = response.getCookies();
        Assert.assertTrue(cookiesActual.containsKey(cookieNameExpected),
                String.format("Response doesn't have '%s' cookie", cookieNameExpected));
        assertEquals(cookieValueExpected, cookiesActual.get(cookieNameExpected),
                String.format("'%s' cookie has wrong value", cookieNameExpected));
    }

    //Simple, one header assertion  -just template for future
    public static void assertHeaderByNameAndValue(Response response, String headerNameExpected, String headerValueExpected) {
        Headers headersActual = response.getHeaders();

        Assert.assertTrue(headersActual.hasHeaderWithName(headerNameExpected),
                String.format("Response doesn't have '%s' header", headerNameExpected));

        assertEquals(headerValueExpected, headersActual.get(headerNameExpected).getValue(),
                String.format("Header value expected '%s' actual is '%s'",
                        headerValueExpected, headersActual.get(headerNameExpected).getValue()));
    }

    public static void mapContains(Response response, String whereToGetActualMap, Map<String, String> expectedMap, boolean compareSize) {
        String assertMessageIfFailToCompare = "Actual value '%s'does not equal expected value '%s' for key '%s'";
        String assertMessageIfKeyIsAbsent = "Key '%s' is not found";
        String assertMessageIfSizeIsDifferent = "Actual map size '%s' vs. expected map size '%s'";
        Map<String, String> actualMap = response.body().jsonPath().getMap(whereToGetActualMap);

        SoftAssert softAssert = new SoftAssert();

        if (compareSize) {
            softAssert.assertTrue(actualMap.size() == expectedMap.size(),
                    String.format(assertMessageIfSizeIsDifferent, actualMap.size(), expectedMap.size()));
        }

        expectedMap.entrySet().forEach(entry -> {
            String expectedKey = entry.getKey();
            String expectedValue = entry.getValue();

            softAssert.assertTrue(actualMap.containsKey(expectedKey),
                    String.format(assertMessageIfKeyIsAbsent, expectedKey));

            if (actualMap.containsKey(expectedKey))
                softAssert.assertEquals(actualMap.get(expectedKey), expectedValue,
                        String.format(assertMessageIfFailToCompare,
                                actualMap.get(expectedKey), expectedValue, expectedKey));

        });
        softAssert.assertAll();
    }

    public static void mapContains(Map<String, String> actualMap, Map<String, String> expectedMap, boolean compareSize) {
        String assertMessageIfFailToCompare = "Actual value '%s'does not equal expected value '%s' for key '%s'";
        String assertMessageIfKeyIsAbsent = "Key '%s' is not found";
        String assertMessageIfSizeIsDifferent = "Actual map size '%s' vs. expected map size '%s'";

        //add-on to JUnit
        SoftAssert softAssert = new SoftAssert();

        if (compareSize) {
            softAssert.assertTrue(actualMap.size() == expectedMap.size(),
                    String.format(assertMessageIfSizeIsDifferent, actualMap.size(), expectedMap.size()));
        }

        expectedMap.entrySet().forEach(entry -> {
            String expectedKey = entry.getKey();
            String expectedValue = entry.getValue();

            softAssert.assertTrue(actualMap.containsKey(expectedKey),
                    String.format(assertMessageIfKeyIsAbsent, expectedKey));

            if (actualMap.containsKey(expectedKey))
                softAssert.assertEquals(actualMap.get(expectedKey), expectedValue,
                        String.format(assertMessageIfFailToCompare,
                                actualMap.get(expectedKey), expectedValue, expectedKey));
        });
        softAssert.assertAll();
    }

    public static void assertJsonByName(Response response, String name, int expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        int value = response.jsonPath().getInt(name);
        assertEquals(value, expectedValue, "JSON value is not equal to expected value");
    }

    public static void assertJsonByName(Response response, String name, String expectedValue) {
        response.then().assertThat().body("$", hasKey(name));
        String value = response.jsonPath().getString(name);
        assertEquals(value, expectedValue, "JSON value is not equal to expected value");
    }

    public static void assertResponseTextEquals(Response response, String expectedAnswer) {
        assertEquals(response.asString(), expectedAnswer,
                String.format("Response is different. Expected '%s' vs. Actual '%s'", expectedAnswer,
                        response.asString()));
    }

    public static void assertResponseCodeEquals(Response response, int expectedStatusCode) {
        assertEquals(response.statusCode(), expectedStatusCode,
                String.format("Status Code is different. Expected '%s' vs. Actual '%s'", expectedStatusCode,
                        response.statusCode()));
    }

    public static void assertJsonHasField(Response response, String expectedFieldName) {
        response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasFields(Response response, String[] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            assertJsonHasField(response, expectedFieldName);
        }
    }

    public static void assertJsonHasNotField(Response response, String unexpectedFieldName) {
        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }

    public static void assertResponse(Response response, UserRegisterDataModel testData) {
        if (testData.getWhatToTest() != null && testData.getWhatToTest().length != 0) {
            SoftAssert softAssert = new SoftAssert();
            Arrays.stream(testData.getWhatToTest()).forEach(s -> {
                //WhatToTest - array from json
                switch (s) {
                    case "textMessageInBody":
             /* Try to use it one day
             response.then().assertThat().body(Matchers.hasXPath(String.format("//html/body[text()='%s']",testData.getExpectedValues().get(s))));
            response.then().assertThat().body(Matchers.hasXPath("//html/body", containsString(testData.getExpectedValues().get(s))));*/

                        String bodyText = response.getBody().htmlPath().getString("//html/body");
                        softAssert.assertEquals(!bodyText.equals("null") ? bodyText : null,
                                testData.getExpectedValues().get(s));
                        break;
                    case "responseCode":
                        softAssert.assertEquals(response.statusCode(),
                                Integer.parseInt(testData.getExpectedValues().get(s)));
                        break;
                    case "hasFieldNames":
                        String[] fieldsToCheck = testData.getExpectedValues().get(s)
                                .replaceAll("\\s*,\\s*", ",").split(",");
                        for (String expectedFieldName : fieldsToCheck) {
                            softAssert.assertTrue(response.jsonPath().getMap("$").containsKey(expectedFieldName),
                                    String.format("Field '%s' not found", expectedFieldName));
                        }
                        break;
                    case "numberOfFieldsTheSame":
                        int expectedNumberOfFields = testData.getExpectedValues().get("hasFieldNames")
                                .replaceAll("\\s*,\\s*", ",").split(",").length;
                        int actualNumberOfFields = response.jsonPath().getMap("$").size();
                        softAssert.assertEquals(actualNumberOfFields, expectedNumberOfFields,
                                String.format("Actual number of fields '%s' vs. expected '%s'", actualNumberOfFields, expectedNumberOfFields));
                        break;
                    case "username":
                        String userNameActual = response.jsonPath().get("username");
                        String userNameExpected = testData.getExpectedValues().get("username");
                        softAssert.assertEquals(userNameActual, userNameExpected,
                                String.format("Actual username '%s' vs. expected '%s'",
                                        userNameActual, userNameExpected));
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Test '%s' is not implemented yet", testData.getExpectedValues().get(s)));
                }
            });
            softAssert.assertAll();
        }
    }
}