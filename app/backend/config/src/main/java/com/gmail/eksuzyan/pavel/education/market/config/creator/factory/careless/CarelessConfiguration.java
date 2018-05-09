package com.gmail.eksuzyan.pavel.education.market.config.creator.factory.careless;

import com.gmail.eksuzyan.pavel.education.market.config.creator.factory.AbstractConfiguration;

import java.util.Properties;

public class CarelessConfiguration extends AbstractConfiguration {

    public CarelessConfiguration(Properties defaultProps) {
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
