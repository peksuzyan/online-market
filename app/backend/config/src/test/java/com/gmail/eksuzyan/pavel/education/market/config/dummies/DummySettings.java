package com.gmail.eksuzyan.pavel.education.market.config.dummies;

import com.gmail.eksuzyan.pavel.education.market.config.Settings;

public class DummySettings implements Settings {

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
