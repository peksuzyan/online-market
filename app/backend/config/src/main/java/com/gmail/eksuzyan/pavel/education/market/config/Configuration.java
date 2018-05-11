package com.gmail.eksuzyan.pavel.education.market.config;

import com.gmail.eksuzyan.pavel.education.market.config.publisher.Publisher;

import java.util.Properties;

@SuppressWarnings("unused")
public interface Configuration extends Publisher {

    void addDefaults(Properties props);

    void addProperties(Properties props);

    void addProperty(Object key, Object value);

    Object getProperty(Object key, Object defaultValue);

    Properties getProperties();

    void clear();

    default boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, false);
    }

    default boolean getBooleanProperty(String key, boolean defaultValue) {
        Object value = getProperty(key, defaultValue);
        return Boolean.parseBoolean((String) value);
    }

    default int getIntProperty(String key) {
        return getIntProperty(key, 0);
    }

    default int getIntProperty(String key, int defaultValue) {
        Object value = getProperty(key, defaultValue);
        return (Integer) value;
    }

    default double getDoubleProperty(String key) {
        return getDoubleProperty(key, 0.0);
    }

    default double getDoubleProperty(String key, double defaultValue) {
        Object value = getProperty(key, defaultValue);
        return Double.parseDouble((String) value);
    }

    default String getStringProperty(String key) {
        return getStringProperty(key, null);
    }

    default String getStringProperty(String key, String defaultValue) {
        Object value = getProperty(key, defaultValue);
        return String.valueOf(value);
    }
}
