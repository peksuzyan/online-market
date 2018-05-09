package com.gmail.eksuzyan.pavel.education.market.config.creator.factory;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.creator.ConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.config.facade.ConfigurationSettingsFacade;

import java.util.Properties;

public abstract class SingletonConfigurationFactory implements ConfigurationFactory {

    private ConfigurationSettingsFacade instance;

    @Override
    public final Configuration getConfiguration(Properties props) {
        return getInstance(props);
    }

    @Override
    public final Settings getSettings(Properties props) {
        return getInstance(props);
    }

    private ConfigurationSettingsFacade getInstance(Properties props) {
        if (instance == null)
            synchronized (this) {
                if (instance == null)
                    instance = newSettingsConfigurationFacade(props);
            }

        return instance;
    }

    protected abstract ConfigurationSettingsFacade newSettingsConfigurationFacade(Properties props);

}
