package com.gmail.eksuzyan.pavel.education.market.config.dummies;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.creator.ConfigurationFactory;

import java.util.Properties;

public class DummyConfigurationFactory implements ConfigurationFactory {

    @Override
    public Configuration getConfiguration(Properties props) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isOpen() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}
