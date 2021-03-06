package com.gmail.eksuzyan.pavel.education.market.config.creator;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;

import java.util.Properties;

public interface ConfigurationFactory extends AutoCloseable {

    Configuration getConfiguration(Properties props);

    default Configuration getConfiguration() {
        return getConfiguration(null);
    }

    boolean isOpen();

}
