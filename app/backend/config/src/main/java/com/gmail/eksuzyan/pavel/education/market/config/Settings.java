package com.gmail.eksuzyan.pavel.education.market.config;

public interface Settings {

    String STORAGE_NAME = "com.gmail.eksuzyan.pavel.market.config.storage.name";
    String STORAGE_ENCODING = "com.gmail.eksuzyan.pavel.market.config.storage.encoding";
    String STORAGE_RELOAD_PERIOD = "com.gmail.eksuzyan.pavel.market.config.storage.reload.period";
    String STORAGE_RELOAD_DELAY = "com.gmail.eksuzyan.pavel.market.config.storage.reload.delay";
    String STORAGE_SAVE_ON_EXIT = "com.gmail.eksuzyan.pavel.market.config.storage.save.on.exit";

    String getStorageName();

    String getStorageEncoding();

    int getStorageReloadPeriod();

    int getStorageReloadDelay();

    boolean getStorageSaveOnExit();

}
