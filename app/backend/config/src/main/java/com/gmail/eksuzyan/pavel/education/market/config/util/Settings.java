package com.gmail.eksuzyan.pavel.education.market.config.util;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;

import java.util.Objects;
import java.util.Properties;

public class Settings extends Properties {

    public static final String STORAGE_NAME = "com.gmail.eksuzyan.pavel.market.config.storage.name";
    public static final String STORAGE_ENCODING = "com.gmail.eksuzyan.pavel.market.config.storage.encoding";
    public static final String STORAGE_RELOAD_PERIOD = "com.gmail.eksuzyan.pavel.market.config.storage.reload.period";
    public static final String STORAGE_RELOAD_DELAY = "com.gmail.eksuzyan.pavel.market.config.storage.reload.delay";
    public static final String STORAGE_SAVE_ON_EXIT = "com.gmail.eksuzyan.pavel.market.config.storage.save.on.exit";

    private final Configuration configuration;

    public Settings(Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration);

        put(STORAGE_RELOAD_PERIOD, String.valueOf(30));
        put(STORAGE_RELOAD_DELAY, String.valueOf(0));

        this.configuration.addDefaults(this);
    }

    public String getStorageName() {
        return configuration.getStringProperty(STORAGE_NAME, "config.xml");
    }

    public String getStorageEncoding() {
        return configuration.getStringProperty(STORAGE_ENCODING, "UTF-8");
    }

    public int getStorageReloadPeriod() {
        return configuration.getIntProperty(STORAGE_RELOAD_PERIOD);
    }

    public int getStorageReloadDelay() {
        return configuration.getIntProperty(STORAGE_RELOAD_DELAY);
    }

    public boolean getStorageSaveOnExit() {
        return configuration.getBooleanProperty(STORAGE_SAVE_ON_EXIT, false);
    }
}
