package com.gmail.eksuzyan.pavel.education.market.config.worker.jmx.mbean;

public interface JmxPropertiesMBean {

    String getProperty(String key);

    String getProperties();

    void addProperty(String key, String value);

    void addProperties(String props);

    void clear();
}
