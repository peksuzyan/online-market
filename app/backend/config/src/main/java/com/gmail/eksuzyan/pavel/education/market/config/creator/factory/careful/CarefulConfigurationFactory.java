package com.gmail.eksuzyan.pavel.education.market.config.creator.factory.careful;

import com.gmail.eksuzyan.pavel.education.market.config.creator.factory.SingletonConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.config.facade.ConfigurationSettingsFacade;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.JaxbMarshallizer;
import com.gmail.eksuzyan.pavel.education.market.config.storage.file.FileStorage;
import com.gmail.eksuzyan.pavel.education.market.config.worker.file.FileWorker;
import com.gmail.eksuzyan.pavel.education.market.config.worker.jmx.JmxWorker;

import java.util.Properties;

public final class CarefulConfigurationFactory extends SingletonConfigurationFactory {

    @Override
    protected ConfigurationSettingsFacade newSettingsConfigurationFacade(Properties props) {
        if (props == null)
            props = new Properties();

        CarefulConfiguration configuration = new CarefulConfiguration(props);

        ConfigurationSettingsFacade facade = new ConfigurationSettingsFacade(configuration);

        JaxbMarshallizer jaxbMarshallizer = new JaxbMarshallizer();
        FileStorage fileStorage = new FileStorage(facade);

        JmxWorker jmxWatchdog = new JmxWorker(facade, facade, jaxbMarshallizer);
        FileWorker fileWatchdog = new FileWorker(facade, facade, jaxbMarshallizer, fileStorage);

        configuration.addWorker(jmxWatchdog);
        configuration.addWorker(fileWatchdog);

        configuration.subscribe(jmxWatchdog);
        configuration.subscribe(fileWatchdog);

        return facade;
    }
}
