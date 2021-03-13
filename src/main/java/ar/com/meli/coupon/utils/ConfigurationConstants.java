package ar.com.meli.coupon.utils;

public class ConfigurationConstants {
    private ConfigurationConstants() {}

    public static final String TEST_PROPERTIES_URL = "/application-test.properties";

    public static String testCoverage(){
        return TEST_PROPERTIES_URL;
    }
}
