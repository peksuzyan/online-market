package com.gmail.eksuzyan.pavel.education.market.config.creator;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.Settings;

import java.util.Properties;

public interface ConfigurationFactory {

    Configuration getConfiguration(Properties props);

    Settings getSettings(Properties props);

    default Configuration getConfiguration() {
        return getConfiguration(null);
    }

    default Settings getSettings() {
        return getSettings(null);
    }

}
