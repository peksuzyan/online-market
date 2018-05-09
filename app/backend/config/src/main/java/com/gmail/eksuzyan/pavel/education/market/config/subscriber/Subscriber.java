package com.gmail.eksuzyan.pavel.education.market.config.subscriber;

/**
 * Provides an interface to notify subscribers when configuration has been changed.
 */
public interface Subscriber {

    /**
     * Notifies subscriber if configuration has been changed.
     */
    void notifySubscriber();

    /**
     * Checks whether this subscriber subscribed on the property or not.
     *
     * @param property property
     * @return subscribed or not
     */
    boolean subscribed(String property);

}
