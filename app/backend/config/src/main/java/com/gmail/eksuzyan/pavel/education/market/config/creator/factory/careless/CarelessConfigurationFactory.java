package com.gmail.eksuzyan.pavel.education.market.config.creator.factory.careless;

import com.gmail.eksuzyan.pavel.education.market.config.creator.factory.SingletonConfigurationFactory;
import com.gmail.eksuzyan.pavel.education.market.config.facade.ConfigurationSettingsFacade;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.jaxb.JaxbMarshallizer;
import com.gmail.eksuzyan.pavel.education.market.config.storage.file.FileStorage;
import com.gmail.eksuzyan.pavel.education.market.config.worker.file.FileWorker;
import com.gmail.eksuzyan.pavel.education.market.config.worker.jmx.JmxWorker;

import java.util.Properties;

public final class CarelessConfigurationFactory extends SingletonConfigurationFactory {

    @Override
    protected ConfigurationSettingsFacade newSettingsConfigurationFacade(Properties props) {
        if (props == null)
            props = new Properties();

        CarelessConfiguration configuration = new CarelessConfiguration(props);

        ConfigurationSettingsFacade facade = new ConfigurationSettingsFacade(configuration);

        JaxbMarshallizer jaxbMarshallizer = new JaxbMarshallizer();
        FileStorage fileStorage = new FileStorage(facade);

        JmxWorker jmxWatchdog = new JmxWorker(facade, facade, jaxbMarshallizer);
        FileWorker fileWatchdog = new FileWorker(facade, facade, jaxbMarshallizer, fileStorage);

        configuration.subscribe(jmxWatchdog);
        configuration.subscribe(fileWatchdog);

        return facade;
    }
}
