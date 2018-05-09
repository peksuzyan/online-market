package com.gmail.eksuzyan.pavel.education.market.config.marshaller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Marshals and unmarshals configuration util forward and backward.
 */
public interface Marshallizer {

    /**
     * Marshals a util object into an output stream.
     *
     * @param properties   util
     * @param outputStream outputStream
     * @throws NullPointerException     if util or outputStream is null
     * @throws IllegalArgumentException if util couldn't be marshalled into outputStream
     */
    void marshall(Properties properties, OutputStream outputStream);

    /**
     * Unmarshals a util object from an input stream.
     *
     * @param inputStream inputStream
     * @return util
     * @throws NullPointerException     if inputStream is null
     * @throws IllegalArgumentException if util couldn't be unmarshalled from inputStream
     */
    Properties unmarshall(InputStream inputStream);

}
