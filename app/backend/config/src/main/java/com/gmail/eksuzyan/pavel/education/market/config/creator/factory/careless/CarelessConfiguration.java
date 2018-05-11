package com.gmail.eksuzyan.pavel.education.market.config.creator.factory.careless;

import com.gmail.eksuzyan.pavel.education.market.config.creator.factory.AbstractConfiguration;

import java.util.Properties;

public class CarelessConfiguration extends AbstractConfiguration {

    CarelessConfiguration(Properties defaultProps) {
        super(defaultProps);
    }

    @Override
    protected void requestResources() {
        /* NOP */
    }

    @Override
    protected void releaseResources() {
        /* NOP */
    }
}
