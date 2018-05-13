package com.gmail.eksuzyan.pavel.education.market.config.subscriber.restartable;

import com.gmail.eksuzyan.pavel.education.market.config.subscriber.Subscriber;

import java.util.Collection;

import static java.util.Collections.unmodifiableCollection;

/**
 * Basic implementation of subscriber with an opportunity to restart itself.
 */
public abstract class RestartableSubscriber implements Subscriber {

    /**
     * Subscriptions.
     */
    private final Collection<String> subscriptions;

    /**
     * Single constructor.
     *
     * @param subscriptions subscriptions
     * @throws NullPointerException if arg is null
     */
    public RestartableSubscriber(Collection<String> subscriptions) {
        this.subscriptions = unmodifiableCollection(subscriptions);
    }

    /**
     * Notifies subscriber if configuration has been changed.
     */
    @Override
    public void notifySubscriber() {
        restart();
    }

    /**
     * Checks whether this subscriber subscribed on the property or not.
     *
     * @param property property
     * @return subscribed or not
     */
    @Override
    public boolean subscribed(String property) {
        return subscriptions.contains(property);
    }

    /**
     * Restarts itself.
     *
     * @throws IllegalStateException if subscriber isn't ready to restart
     */
    protected abstract void restart();
}
