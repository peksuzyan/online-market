package com.gmail.eksuzyan.pavel.education.market.config.creator.factory;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.creator.ConfigurationFactory;

import java.util.Properties;

public abstract class SingletonConfigurationFactory implements ConfigurationFactory {

    private Configuration instance;

    @Override
    public final Configuration getConfiguration(Properties props) {
        return getInstance(props);
    }

    private Configuration getInstance(Properties props) {
        if (instance == null)
            synchronized (this) {
                if (instance == null)
                    instance = newConfiguration(props);
            }

        return instance;
    }

    protected abstract Configuration newConfiguration(Properties props);

}
