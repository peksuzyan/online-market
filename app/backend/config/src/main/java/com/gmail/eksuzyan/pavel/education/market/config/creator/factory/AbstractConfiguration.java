package com.gmail.eksuzyan.pavel.education.market.config.creator.factory;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.subscriber.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public abstract class AbstractConfiguration implements Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractConfiguration.class);

    private final Properties systemProps = new Properties();
    private final Properties defaultProps = new Properties();
    private final Properties runtimeProps = new Properties();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Set<Subscriber> subscribers = new HashSet<>();

    public AbstractConfiguration(Properties defaultProps) {
        requireNonNull(defaultProps);

        this.defaultProps.putAll(defaultProps);

        LOG.info("Configuration initialized. ");
    }

    protected abstract void requestResources();

    protected abstract void releaseResources();

    @Override
    public boolean subscribe(Subscriber subscriber) {
        return subscribers.add(subscriber);
    }

    @Override
    public boolean unsubscribe(Subscriber subscriber) {
        return subscribers.remove(subscriber);
    }

    public void start() {
        initProps();

        requestResources();

        LOG.info("Configuration started. ");
    }

    public void stop() {
        releaseResources();

        clear();

        LOG.info("Configuration stopped. ");
    }

    private void initProps() {
        systemProps.putAll(System.getProperties());
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            runtimeProps.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Object getProperty(Object key, Object defaultValue) {
        lock.readLock().lock();
        try {
            if (runtimeProps.containsKey(key))
                return runtimeProps.get(key);

            if (defaultProps.containsKey(key))
                return defaultProps.get(key);

            return systemProps.getOrDefault(key, defaultValue);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void addProperties(Properties props) {
        for (Map.Entry<Object, Object> property : props.entrySet())
            addProperty(property.getKey(), property.getValue());
    }

    @Override
    public void addProperty(Object key, Object newValue) {
        Object value;

        lock.writeLock().lock();
        try {
            value = runtimeProps.put(key, newValue);
        } finally {
            lock.writeLock().unlock();
        }

        if (!Objects.equals(value, newValue))
            notifyListeners(String.valueOf(key));
    }

    private void notifyListeners(String property) {
        subscribers.stream()
                .filter(subscriber -> subscriber.subscribed(property))
                .forEach(Subscriber::notifySubscriber);
    }

    @Override
    public Properties getProperties() {
        Properties props = new Properties();
        props.putAll(getPrintableMap());
        return props;
    }

    private Map<Object, Object> getPrintableMap() {
        lock.readLock().lock();
        try {
            return runtimeProps.entrySet().stream()
                    .filter(entry -> !defaultProps.containsKey(entry.getKey()))
                    .filter(entry -> !systemProps.containsKey(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } finally {
            lock.readLock().unlock();
        }
    }

}
