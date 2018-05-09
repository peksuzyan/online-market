package com.gmail.eksuzyan.pavel.education.market.config.worker.jmx.mbean;

import com.gmail.eksuzyan.pavel.education.market.config.Configuration;
import com.gmail.eksuzyan.pavel.education.market.config.marshaller.Marshallizer;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Properties;

public class JmxProperties implements JmxPropertiesMBean {

    private final Configuration configuration;

    private final Marshallizer marshallizer;

    private final Charset charset;

    public JmxProperties(Configuration configuration, Marshallizer marshallizer, Charset charset) {
        this.configuration = Objects.requireNonNull(configuration);
        this.marshallizer = Objects.requireNonNull(marshallizer);
        this.charset = Objects.requireNonNull(charset);
    }

    @Override
    public String getProperty(String key) {
        return String.valueOf(configuration.getProperty(key, ""));
    }

    @Override
    public String getProperties() {
        Properties props = configuration.getProperties();
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            marshallizer.marshall(props, os);
            return new String(os.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to return runtime util. ", e);
        }
    }

    @Override
    public void addProperty(String key, String value) {
        configuration.addProperty(key, value);
    }

    @Override
    public void addProperties(String props) {
        try (InputStream is = IOUtils.toInputStream(props, charset)) {
            Properties properties = marshallizer.unmarshall(is);
            configuration.addProperties(properties);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to update runtime util. ", e);
        }
    }

    @Override
    public void clear() {
        configuration.clear();
    }
}
