package com.gmail.eksuzyan.pavel.education.market.config.creator.factory.careful;

import com.gmail.eksuzyan.pavel.education.market.config.creator.factory.AbstractConfiguration;
import com.gmail.eksuzyan.pavel.education.market.config.manager.Manager;
import com.gmail.eksuzyan.pavel.education.market.config.worker.Worker;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class CarefulConfiguration extends AbstractConfiguration implements Manager {

    private final Set<Worker> workers = new HashSet<>();

    CarefulConfiguration(Properties defaultProps) {
        super(defaultProps);
    }

    @Override
    public boolean addWorker(Worker worker) {
        return workers.add(worker);
    }

    @Override
    public boolean removeWorker(Worker worker) {
        return workers.remove(worker);
    }

    @Override
    protected void requestResources() {
        workers.forEach(Worker::start);
    }

    @Override
    protected void releaseResources() {
        workers.forEach(Worker::stop);
    }
}
