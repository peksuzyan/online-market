package com.gmail.eksuzyan.pavel.education.market.config;

import com.gmail.eksuzyan.pavel.education.market.config.creator.ConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.config.creator.factory.SingletonConfigurationFactory;

public final class Configurations {

    private Configurations() {
    }

    public static ConfigurationFactory newSingleConfigurationFactory() {
        return new SingletonConfigurationFactory();
    }

}
