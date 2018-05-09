package com.gmail.eksuzyan.pavel.education.market.config;

import com.gmail.eksuzyan.pavel.education.market.config.publisher.Publisher;

import java.util.Properties;

public interface Configuration extends Publisher {

    void addProperties(Properties props);

    void addProperty(Object key, Object value);

    Object getProperty(Object key, Object defaultValue);

    Properties getProperties();

    void clear();

}
