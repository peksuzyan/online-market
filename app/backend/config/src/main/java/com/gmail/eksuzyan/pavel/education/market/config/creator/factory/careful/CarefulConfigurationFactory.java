package com.gmail.eksuzyan.pavel.education.market.config.creator.factory.careful;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.creator.factory.SingletonConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.JaxbMarshallizer;
import com.gmail.eksuzyan.pavel.education.market.config.storage.file.FileStorage;
import com.gmail.eksuzyan.pavel.education.market.config.util.Settings;
import com.gmail.eksuzyan.pavel.education.market.config.worker.file.FileWorker;
import com.gmail.eksuzyan.pavel.education.market.config.worker.jmx.JmxWorker;

import java.util.Properties;

public final class CarefulConfigurationFactory extends SingletonConfigurationFactory {

    @Override
    protected Configuration newConfiguration(Properties props) {
        if (props == null)
            props = new Properties();

        CarefulConfiguration configuration = new CarefulConfiguration(props);

        Settings settings = new Settings(configuration);

        JaxbMarshallizer jaxbMarshallizer = new JaxbMarshallizer();
        FileStorage fileStorage = new FileStorage(settings);

        JmxWorker jmxWatchdog = new JmxWorker(configuration, settings, jaxbMarshallizer);
        FileWorker fileWatchdog = new FileWorker(configuration, settings, jaxbMarshallizer, fileStorage);

        configuration.subscribe(jmxWatchdog);
        configuration.subscribe(fileWatchdog);

        configuration.addWorker(jmxWatchdog);
        configuration.addWorker(fileWatchdog);

        return configuration;
    }
}
