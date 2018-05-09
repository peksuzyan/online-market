package com.gmail.eksuzyan.pavel.education.market.config.facade;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.subscriber.Subscriber;

import java.util.Objects;
import java.util.Properties;

public class ConfigurationSettingsFacade implements Configuration, Settings {

    private final Configuration configuration;

    public ConfigurationSettingsFacade(Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration);
    }

    @Override
    public boolean subscribe(Subscriber subscriber) {
        return configuration.subscribe(subscriber);
    }

    @Override
    public boolean unsubscribe(Subscriber subscriber) {
        return configuration.unsubscribe(subscriber);
    }

    @Override
    public void addProperties(Properties props) {
        configuration.addProperties(props);
    }

    @Override
    public void addProperty(Object key, Object value) {
        configuration.addProperty(key, value);
    }

    @Override
    public Object getProperty(Object key, Object defaultValue) {
        return configuration.getProperty(key, defaultValue);
    }

    @Override
    public Properties getProperties() {
        return configuration.getProperties();
    }

    @Override
    public void clear() {
        configuration.clear();
    }

    @Override
    public String getStorageName() {
        return getStringProperty(STORAGE_NAME, "config.xml");
    }

    @Override
    public String getStorageEncoding() {
        return getStringProperty(STORAGE_ENCODING, "UTF-8");
    }

    @Override
    public int getStorageReloadPeriod() {
        return getIntProperty(STORAGE_RELOAD_PERIOD, 30);
    }

    @Override
    public int getStorageReloadDelay() {
        return getIntProperty(STORAGE_RELOAD_DELAY, 0);
    }

    @Override
    public boolean getStorageSaveOnExit() {
        return getBooleanProperty(STORAGE_SAVE_ON_EXIT, false);
    }

    private boolean getBooleanProperty(String key, boolean defaultValue) {
        Object value = getProperty(key, defaultValue);
        return Boolean.parseBoolean((String) value);
    }

    private int getIntProperty(String key, int defaultValue) {
        Object value = getProperty(key, defaultValue);
        return (Integer) value;
    }

    private double getDoubleProperty(String key, double defaultValue) {
        Object value = getProperty(key, defaultValue);
        return Double.parseDouble((String) value);
    }

    private String getStringProperty(String key, String defaultValue) {
        Object value = getProperty(key, defaultValue);
        return String.valueOf(value);
    }
}
