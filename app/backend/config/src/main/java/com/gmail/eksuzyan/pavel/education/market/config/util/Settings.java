package com.gmail.eksuzyan.pavel.education.market.config.util;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;

import java.util.Objects;
import java.util.Properties;

public class Settings {

    private static final String STORAGE_NAME = "com.gmail.eksuzyan.pavel.market.config.storage.name";
    private static final String STORAGE_ENCODING = "com.gmail.eksuzyan.pavel.market.config.storage.encoding";
    private static final String STORAGE_RELOAD_PERIOD = "com.gmail.eksuzyan.pavel.market.config.storage.reload.period";
    private static final String STORAGE_RELOAD_DELAY = "com.gmail.eksuzyan.pavel.market.config.storage.reload.delay";
    private static final String STORAGE_SAVE_ON_EXIT = "com.gmail.eksuzyan.pavel.market.config.storage.save.on.exit";

    private final Configuration configuration;

    public Settings(Configuration configuration) {
        this.configuration = Objects.requireNonNull(configuration);

        this.configuration.addDefaults(new Properties() {{
            put(STORAGE_NAME, "config.xml");
            put(STORAGE_RELOAD_PERIOD, 30);
            put(STORAGE_RELOAD_DELAY, 0);
        }});
    }

    public String getStorageName() {
        return configuration.getStringProperty(STORAGE_NAME);
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
