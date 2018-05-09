package com.gmail.eksuzyan.pavel.education.market.config;

import com.gmail.eksuzyan.pavel.education.market.config.creator.ConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.config.creator.factory.careful.CarefulConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.config.creator.factory.careless.CarelessConfigurationFactory;

public final class Configurations {

    private Configurations() {
    }

    public static ConfigurationFactory newCarefulConfigurationFactory() {
        return new CarefulConfigurationFactory();
    }

    public static ConfigurationFactory newCarelessConfigurationFactory() {
        return new CarelessConfigurationFactory();
    }
}
