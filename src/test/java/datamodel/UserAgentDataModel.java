package datamodel;

import java.util.Map;

public class UserAgentDataModel {

    private String testUrl;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> expectedValues;

    public String getTestUrl() {
        return testUrl;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getExpectedValues() {
        return expectedValues;
    }
}
