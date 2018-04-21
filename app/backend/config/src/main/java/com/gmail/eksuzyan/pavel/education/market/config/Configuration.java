package com.gmail.eksuzyan.pavel.education.market.config;

import com.gmail.eksuzyan.pavel.education.market.config.wrappers.PropertiesWrapper;
import com.gmail.eksuzyan.pavel.education.market.config.wrappers.PropertyWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    private static Configuration instance;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final JAXBContext context;

    private static final String DEFAULT_CONFIG_FILE_NAME = "config.xml";

    private Configuration() {
        try {
            this.context = JAXBContext.newInstance(PropertyWrapper.class, PropertiesWrapper.class);
        } catch (JAXBException e) {
            LOG.error("Unable to initialize jaxb context. ", e);
            throw new IllegalStateException("Unable to initialize jaxb context. ", e);
        }
    }

    public static Configuration current() {
        if (instance == null)
            synchronized (Configuration.class) {
                if (instance == null)
                    return instance = new Configuration();
            }

        return instance;
    }

    public void write(Properties properties) throws IOException {
        try (OutputStream os = new FileOutputStream(DEFAULT_CONFIG_FILE_NAME)) {
            write(properties, os);
        }
    }

    public Properties read() throws IOException {
        try (InputStream is = new FileInputStream(DEFAULT_CONFIG_FILE_NAME)) {
            return read(is);
        }
    }

    void write(Properties properties, OutputStream outputStream) throws IOException {
        write(PropertiesWrapper.of(properties), outputStream);
    }

    Properties read(InputStream inputStream) throws IOException {
        return read0(inputStream).toProperties();
    }

    private void write(PropertiesWrapper properties, OutputStream outputStream) throws IOException {
        lock.writeLock().lock();
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(properties, outputStream);
        } catch (JAXBException e) {
            LOG.warn("Unable to write properties. ", e);
            throw new IOException("Unable to write properties. ", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private PropertiesWrapper read0(InputStream inputStream) throws IOException {
        lock.readLock().lock();
        try {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (PropertiesWrapper) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            LOG.warn("Unable to read properties. ", e);
            throw new IOException("Unable to read properties. ", e);
        } finally {
            lock.readLock().unlock();
        }
    }
}
