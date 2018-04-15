package com.gmail.eksuzyan.pavel.education.market.config;

import com.gmail.eksuzyan.pavel.education.market.config.mapper.MarketProperty;
import com.gmail.eksuzyan.pavel.education.market.config.mapper.MarketProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("WeakerAccess")
public final class Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    private static final String DEFAULT_CONFIG_FILE_NAME = "config.xml";
    private static final ConcurrentMap<String, Configuration> CONTEXTS = new ConcurrentHashMap<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final JAXBContext context;
    private final File configFile;

    private Configuration(File configFile) {
        this.configFile = configFile;
        try {
            this.context = JAXBContext.newInstance(MarketProperty.class, MarketProperties.class);
        } catch (JAXBException e) {
            LOG.error("Unable to initialize jaxb context. ", e);
            throw new IllegalStateException("Unable to initialize jaxb context. ", e);
        }
    }

    public static Configuration of(String configFileName) {
        if (configFileName == null)
            configFileName = DEFAULT_CONFIG_FILE_NAME;

        return CONTEXTS.computeIfAbsent(
                configFileName, fileName -> new Configuration(new File(fileName)));
    }

    public static Configuration current() {
        return Configuration.of(DEFAULT_CONFIG_FILE_NAME);
    }

    public Map<String, String> read() throws IOException {
        return read0().toMap();
    }

    private MarketProperties read0() throws IOException {
        lock.readLock().lock();
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (MarketProperties) unmarshaller.unmarshal(configFile);
        } catch (JAXBException e) {
            LOG.warn("Unable to read properties from file.", e);
            throw new IOException("Unable to read properties from file.", e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(Map<String, String> properties) throws IOException {
        write(MarketProperties.of(properties));
    }

    private void write(MarketProperties marketProperties) throws IOException {
        lock.writeLock().lock();
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(marketProperties, configFile);
        } catch (JAXBException e) {
            LOG.warn("Unable to save properties to file.", e);
            throw new IOException("Unable to save properties to file.", e);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
