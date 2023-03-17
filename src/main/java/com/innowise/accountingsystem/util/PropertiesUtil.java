package com.innowise.accountingsystem.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesUtil {

    private static final Properties PROPERTIES;
    private static final String PROPERTIES_FILE_NAME = "application.properties";

    static {
        PROPERTIES = new Properties();
        loadProperties();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            //log.error("error loading properties", e.getMessage()
            throw new RuntimeException(e);
        }
    }

}
