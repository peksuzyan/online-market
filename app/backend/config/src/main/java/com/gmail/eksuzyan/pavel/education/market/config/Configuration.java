package com.gmail.eksuzyan.pavel.education.market.config;

import com.gmail.eksuzyan.pavel.education.market.config.publisher.Publisher;

import java.util.Properties;

/**
 * Determines which ways are possible to manipulate properties.
 */
@SuppressWarnings("unused")
public interface Configuration extends Publisher {

    /**
     * Adds default properties.
     *
     * @param props default props
     */
    void addDefaults(Properties props);

    /**
     * Adds actual properties.
     *
     * @param props actual props
     */
    void addProperties(Properties props);

    /**
     * Adds a property with specified key and value.
     *
     * @param key   key
     * @param value value
     */
    void addProperty(Object key, Object value);

    /**
     * Gets a value by specified key or specified default value if the key isn't present.
     *
     * @param key          key
     * @param defaultValue default value
     * @return value if key is present or default value otherwise
     */
    Object getProperty(Object key, Object defaultValue);

    /**
     * Gets actual properties.
     *
     * @return actual props
     */
    Properties getProperties();

    /**
     * Clears actual properties.
     */
    void clear();

    /**
     * Gets boolean value by specified key.
     *
     * @param key key
     * @return boolean value
     */
    default boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, false);
    }

    /**
     * Gets boolean value by specified key or default value if key isn't present.
     *
     * @param key          key
     * @param defaultValue default value
     * @return value if key is present or default value otherwise
     */
    default boolean getBooleanProperty(String key, boolean defaultValue) {
        Object value = getProperty(key, null);
        return value != null
                ? Boolean.valueOf((String) value)
                : defaultValue;
    }

    /**
     * Gets int value by specified key.
     *
     * @param key key
     * @return int value
     */
    default int getIntProperty(String key) {
        return getIntProperty(key, 0);
    }

    /**
     * Gets int value by specified key or default value if key isn't present.
     *
     * @param key          key
     * @param defaultValue default value
     * @return value if key is present or default value otherwise
     */
    default int getIntProperty(String key, int defaultValue) {
        Object value = getProperty(key, null);
        return value != null
                ? Integer.parseInt((String) value)
                : defaultValue;
    }

    /**
     * Gets double value by specified key.
     *
     * @param key key
     * @return double value
     */
    default double getDoubleProperty(String key) {
        return getDoubleProperty(key, 0.0);
    }

    /**
     * Gets double value by specified key or default value if key isn't present.
     *
     * @param key          key
     * @param defaultValue default value
     * @return value if key is present or default value otherwise
     */
    default double getDoubleProperty(String key, double defaultValue) {
        Object value = getProperty(key, null);
        return value != null
                ? Double.parseDouble((String) value)
                : defaultValue;
    }

    /**
     * Gets string value by specified key.
     *
     * @param key key
     * @return string value
     */
    default String getStringProperty(String key) {
        return getStringProperty(key, null);
    }

    /**
     * Gets string value by specified key or default value if key isn't present.
     *
     * @param key          key
     * @param defaultValue default value
     * @return value if key is present or default value otherwise
     */
    default String getStringProperty(String key, String defaultValue) {
        Object value = getProperty(key, null);
        return value != null
                ? String.valueOf(value)
                : defaultValue;
    }
}
