package com.gmail.eksuzyan.pavel.education.market.model.entities;

/**
 * Common interface to access entity by primary key.
 *
 * @param <U> primary key type
 */
public interface Identifiable<U> {

    /**
     * Gets an entity primary key.
     *
     * @return primary key
     */
    U getPk();

    /**
     * Sets an entity primary key.
     *
     * @param pk primary key
     */
    void setPk(U pk);

}
