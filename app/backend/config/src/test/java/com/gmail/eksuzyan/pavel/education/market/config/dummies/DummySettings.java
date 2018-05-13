package com.gmail.eksuzyan.pavel.education.market.config.dummies;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;

public class DummySettings extends Settings {

    public DummySettings(Configuration configuration) {
        super(configuration);
    }

    @Override
    public String getStorageName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getStorageEncoding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStorageReloadPeriod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getStorageReloadDelay() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getStorageSaveOnExit() {
        throw new UnsupportedOperationException();
    }
}
