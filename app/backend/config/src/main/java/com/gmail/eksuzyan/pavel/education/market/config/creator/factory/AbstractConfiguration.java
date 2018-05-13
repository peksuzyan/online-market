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

        initProps();
        addDefaults(defaultProps);

        LOG.info("Configuration initialized. ");
    }

    protected abstract void requestResources();

    protected abstract void releaseResources();

    @Override
    public void addDefaults(Properties props) {
        lock.writeLock().lock();
        try {
            this.defaultProps.putAll(props);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean subscribe(Subscriber subscriber) {
        lock.writeLock().lock();
        try {
            return subscribers.add(subscriber);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean unsubscribe(Subscriber subscriber) {
        lock.writeLock().lock();
        try {
            return subscribers.remove(subscriber);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void start() {
        lock.writeLock().lock();
        try {
            requestResources();
        } finally {
            lock.writeLock().unlock();
        }

        LOG.info("Configuration started. ");
    }

    public void stop() {
        lock.writeLock().lock();
        try {
            releaseResources();
            clear();
        } finally {
            lock.writeLock().unlock();
        }

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

        if (!Objects.equals(value, newValue)) {
            lock.readLock().lock();
            try {
                notifyListeners(String.valueOf(key));
            } finally {
                lock.readLock().unlock();
            }
        }
    }

    private void notifyListeners(String property) {
        subscribers.stream()
                .filter(subscriber -> subscriber.subscribed(property))
                .forEach(Subscriber::notifySubscriber);
    }

    @Override
    public Properties getProperties() {
        lock.readLock().lock();
        try {
            Properties props = new Properties();
            props.putAll(getPrintablePropsMap());
            return props;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Returns properties map of all non-system properties.
     * Runtime properties has more privilege than default ones.
     *
     * @return properties map of all non-system properties
     */
    private Map<Object, Object> getPrintablePropsMap() {
        Map<Object, Object> defaults = getNonSystemPropsMap(defaultProps);
        defaults.putAll(getNonSystemPropsMap(runtimeProps));
        return defaults;
    }

    /**
     * Returns properties map of the given properties except whose are't present among system properties.
     *
     * @param props properties
     * @return properties map without presented into system properties
     */
    private Map<Object, Object> getNonSystemPropsMap(Properties props) {
        return props.entrySet().stream()
                .filter(entry -> !systemProps.containsKey(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
