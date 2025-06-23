package config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:${env}.properties",
        "classpath:default.properties"
})
public interface TestPropertiesConfig extends Config {
    @Key("baseUrl")
    //@DefaultValue("https://bonigarcia.dev/selenium-webdriver-java/")
    String getBaseUrl();

    @Key("username")
    String getUsername();

    @Key("password")
    String getPassword();
}
