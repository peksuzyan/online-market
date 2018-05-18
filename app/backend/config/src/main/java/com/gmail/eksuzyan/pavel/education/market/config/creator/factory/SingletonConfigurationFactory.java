package com.gmail.eksuzyan.pavel.education.market.config.creator.factory;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.creator.ConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.JaxbMarshallizer;
import com.gmail.eksuzyan.pavel.education.market.config.storage.file.FileStorage;
import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.worker.file.FileWorker;
import com.gmail.eksuzyan.pavel.education.market.config.worker.jmx.JmxWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public final class SingletonConfigurationFactory implements ConfigurationFactory {

    private static final Logger LOG = LoggerFactory.getLogger(SingletonConfigurationFactory.class);

    private boolean closed = false;

    private Configuration configuration;

    private JmxWorker jmxWorker;
    private FileWorker fileWorker;

    @Override
    public synchronized Configuration getConfiguration(Properties props) {
        if (closed)
            throw new IllegalStateException("ConfigurationFactory has been already closed. ");

        if (configuration == null)
            configuration = newConfiguration(props);

        return configuration;
    }

    private Configuration newConfiguration(Properties props) {
        if (props == null)
            props = new Properties();

        CarelessConfiguration configuration = new CarelessConfiguration(props);

        Settings settings = new Settings(configuration);

        JaxbMarshallizer jaxbMarshallizer = new JaxbMarshallizer();
        FileStorage fileStorage = new FileStorage(settings);

        jmxWorker = new JmxWorker(configuration, settings, jaxbMarshallizer);
        fileWorker = new FileWorker(configuration, settings, jaxbMarshallizer, fileStorage);

        configuration.subscribe(jmxWorker);
        configuration.subscribe(fileWorker);

        jmxWorker.start();
        fileWorker.start();

        return configuration;
    }

    @Override
    public synchronized boolean isOpen() {
        return !closed;
    }

    @Override
    public synchronized void close() {
        if (closed)
            throw new IllegalStateException("ConfigurationFactory has been already closed. ");

        closed = true;

        if (configuration != null) {
            configuration.clear();

            configuration.unsubscribe(jmxWorker);
            configuration.unsubscribe(fileWorker);

            jmxWorker.stop();
            fileWorker.stop();
        }

        LOG.info("ConfigurationFactory closed. ");
    }

}
