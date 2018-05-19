package com.gmail.eksuzyan.pavel.education.market.config.marshaller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Marshals and unmarshals configuration properties forward and backward.
 */
public interface Marshallizer {

    /**
     * Marshals a properties object into an output stream.
     *
     * @param properties   properties
     * @param outputStream outputStream
     * @throws NullPointerException     if properties or outputStream is null
     * @throws IllegalArgumentException if properties couldn't be marshalled into outputStream
     */
    void marshall(Properties properties, OutputStream outputStream);

    /**
     * Unmarshals a properties object from an input stream.
     *
     * @param inputStream inputStream
     * @return properties
     * @throws NullPointerException     if inputStream is null
     * @throws IllegalArgumentException if properties couldn't be unmarshalled from inputStream
     */
    Properties unmarshall(InputStream inputStream);

}
