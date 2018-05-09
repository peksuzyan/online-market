package com.gmail.eksuzyan.pavel.education.market.config.worker;

/**
 * Provides an interface to manage worker's resources.
 */
public interface Worker {

    /**
     * Starts worker's resources. Operation is thread-safe.
     *
     * @throws IllegalStateException if worker isn't ready
     */
    void start();

    /**
     * Stops worker's resources. Operation is thread-safe.
     *
     * @throws IllegalStateException if worker isn't running
     */
    void stop();

    /**
     * Worker state.
     */
    enum State {

        /**
         * Worker is running.
         */
        RUNNING,

        /**
         * Worker stopped.
         */
        STOPPED
    }
}
 