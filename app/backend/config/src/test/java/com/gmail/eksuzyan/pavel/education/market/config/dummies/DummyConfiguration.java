package com.gmail.eksuzyan.pavel.education.market.config.dummies;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.subscriber.Subscriber;

import java.util.Properties;

public class DummyConfiguration implements Configuration {

    @Override
    public void addProperties(Properties props) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addProperty(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getProperty(Object key, Object defaultValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean subscribe(Subscriber subscriber) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean unsubscribe(Subscriber subscriber) {
        throw new UnsupportedOperationException();
    }
}
