package com.gmail.eksuzyan.pavel.education.market.config.publisher;

import com.gmail.eksuzyan.pavel.education.market.config.subscriber.Subscriber;

/**
 * Provides a way to manage subscribers of the publisher.
 */
public interface Publisher {

    /**
     * Adds subscriber to the publisher.
     *
     * @param subscriber subscriber
     * @return added or not
     */
    boolean subscribe(Subscriber subscriber);

    /**
     * Removes subscriber from the publisher.
     *
     * @param subscriber subscriber
     * @return removed or not
     */
    boolean unsubscribe(Subscriber subscriber);

}
