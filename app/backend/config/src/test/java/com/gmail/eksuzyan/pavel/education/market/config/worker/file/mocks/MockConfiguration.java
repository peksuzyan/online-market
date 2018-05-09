package com.gmail.eksuzyan.pavel.education.market.config.worker.file.mocks;

import com.gmail.eksuzyan.pavel.education.market.config.dummies.DummyConfiguration;

import java.util.Properties;

public class MockConfiguration extends DummyConfiguration {

    public Properties properties = new Properties();

    @Override
    public void addProperties(Properties props) {
        properties.putAll(props);
    }
}
