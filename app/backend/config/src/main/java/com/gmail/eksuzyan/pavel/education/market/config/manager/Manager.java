package com.gmail.eksuzyan.pavel.education.market.config.manager;

import com.gmail.eksuzyan.pavel.education.market.config.worker.Worker;

/**
 * Provides a way to manage workers of the manager.
 */
public interface Manager {

    /**
     * Adds worker to the manager.
     *
     * @param worker worker
     * @return added or not
     */
    boolean addWorker(Worker worker);

    /**
     * Removes worker fro, the manager.
     *
     * @param worker worker
     * @return removed or not
     */
    boolean removeWorker(Worker worker);

}
